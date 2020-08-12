package com.framework.shard.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.framework.shard.DynamicDataSource;
import com.framework.shard.ShardDataSource;
import com.framework.shard.ShardDbException;
import com.framework.shard.TranHolder;
import com.framework.shard.annotation.Shard;
import com.framework.shard.annotation.SplitShard;


/**
 * 数据源指向处理切片；
 * 如果同一个方法内调用同一个类里面的其它方法，该类不会执行第二次，除非特殊处理，如：
 * 在其它方法里面同时声明注解@Transactional(TxType.REQUIRES_NEW)或者@Transactional(TxType.NOT_SUPPORTED)，同时在该类中同时注入该类去调用该方法。
 * 
 * @author lyq
 *
 */
@Aspect
@Component
@Order(0)//必须在事务注解切片生效前执行。
public class TransAop {
	

	@Autowired
	DynamicDataSource datasource;
	
	//通过BeanPostProcessor指定。
//	@Pointcut(value="execution(* replace..*(..))")
//	private void shardTranc(){}
	@Pointcut(value = "@annotation(com.framework.shard.annotation.SplitShard)")
    public void splitShardPointcut(){}

    @Pointcut(value = "@annotation(com.framework.shard.annotation.Shard)")
    public void shardPointcut(){}
//	@Around("shardTranc()")
//	@Around(value="execution(* replace..*(..))")
    
    @Around("@annotation(shadeAnnotation)")
    public Object doShardAround(ProceedingJoinPoint  joinPoint,Shard shadeAnnotation) throws Throwable {
		Stack<String> stack = TranHolder.shadeStack.get();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
	    Method method = methodSignature.getMethod();
//	    System.out.println("method:"+method.getName());
	    boolean isPush = false;
	    try{
	    	SplitShard splitShadeAnnotation = method.getDeclaredAnnotation(SplitShard.class);
		    if(shadeAnnotation==null){
		    	//未使用注解的默认第一个注解
//		    	pushStack(stack, datasource.getDefaultTargetDataSource().getKey());
//		    	isPush = true;
		    }else if(splitShadeAnnotation!=null){
		    	throw new ShardDbException("@Shade和@SplitShade不能同时使用");
		    }else{
		    	String shade = datasource.getDefaultTargetDataSource().getKey();
	    		shade = ((Shard)shadeAnnotation).value();
    			shade = StringUtils.isEmpty(shade)?datasource.getDefaultTargetDataSource().getKey():shade;
    			stack = pushStack(stack, shade);
		    	isPush = true;
//		    	 System.out.println("shade:"+shade);
		    }
		   return joinPoint.proceed();
	    }finally{
	    	if(isPush){
	    		//事务会在动态数据源通过peek()方法拿出来使用，这里不管失败与否都必须将使用完的事务删除。
	    		stack.pop();
	    	}
	    	if(stack!=null&&stack.empty()){
	    		TranHolder.shadeStack.remove();//所有事物都已经执行完毕，销毁线程里面的对战容器
	    	}
	    }
	 }
    
	@Around("@annotation(splitShadeAnnotation)")
	public Object doSplitShardAroud(ProceedingJoinPoint  joinPoint,SplitShard splitShadeAnnotation) throws Throwable {
		Stack<String> stack = TranHolder.shadeStack.get();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
	    Method method = methodSignature.getMethod();
//	    System.out.println("method:"+method.getName());
	    boolean isPush = false;
	    try{
	    	 Shard shadeAnnotation = method.getDeclaredAnnotation(Shard.class);
		    if(splitShadeAnnotation==null){
		    	//未使用注解的默认第一个注解
//		    	pushStack(stack, datasource.getDefaultTargetDataSource().getKey());
//		    	isPush = true;
		    }else if(shadeAnnotation!=null){
		    	throw new ShardDbException("@Shade和@SplitShade不能同时使用");
		    }else {
		    	if(datasource==null||MapUtils.isEmpty(datasource.getTargetDataSources())){
		    		throw new ShardDbException("属性文件shards-db.properties未设置数据源，或者spring容器不存在AbsShardDbConfig对象。");
		    	}
		    	String shade = datasource.getDefaultTargetDataSource().getKey();
		    	if(joinPoint.getArgs()==null||joinPoint.getArgs().length==0){
		    		throw new ShardDbException("使用@SplitShade 必须匹配分库参数。");
		    	}
		    	
	    	   Object splitParam = joinPoint.getArgs()[splitShadeAnnotation.paramPosition()];
	    	   if(splitParam==null){
	    		  throw new ShardDbException(String.format("属性：%s 不能为空",method.getParameters()[splitShadeAnnotation.paramPosition()].getName()));
	    	   }
	    	   if(StringUtils.isEmpty(splitShadeAnnotation.paramPropertyName())){
	    		   shade = getShortTableName(method.getParameters()[splitShadeAnnotation.paramPosition()].getName(), splitParam);
	    	   }else{
	    		   try{
	    			   shade = getShortTableName(splitShadeAnnotation.paramPropertyName(),BeanUtils.getProperty(splitParam, splitShadeAnnotation.paramPropertyName()));
	    		   }catch(Exception e){
	    			   if(e instanceof ShardDbException){
	    				   throw e;
	    			   }else{
	    			   throw new ShardDbException("参数位置设置有误，请查看是否有该参数或者是否有getter方法",e);
	    			   }
	    		   }
	    	   }
	    	   stack = pushStack(stack, shade);
		       isPush = true;
		    }
		   return joinPoint.proceed();
	    }finally{
	    	if(isPush){
	    		//事务会在动态数据源通过peek()方法拿出来使用，这里不管失败与否都必须将使用完的事务删除。
	    		stack.pop();
	    	}
	    	if(stack!=null&&stack.empty()){
	    		TranHolder.shadeStack.remove();//所有事物都已经执行完毕，销毁线程里面的对战容器
	    	}
	    }
	 }
	
	@Deprecated
	public Object doAround(ProceedingJoinPoint  joinPoint,SplitShard splitShard) throws Throwable {
		Stack<String> stack = TranHolder.shadeStack.get();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
	    Method method = methodSignature.getMethod();
//	    System.out.println("method:"+method.getName());
	    Shard shadeAnnotation = method.getDeclaredAnnotation(Shard.class);
	    SplitShard splitShadeAnnotation = method.getDeclaredAnnotation(SplitShard.class);
	    boolean isPush = false;
	    try{
	    	
		    if(shadeAnnotation==null&&splitShadeAnnotation==null){
		    	//未使用注解的默认第一个注解
//		    	pushStack(stack, datasource.getDefaultTargetDataSource().getKey());
//		    	isPush = true;
		    }else if(shadeAnnotation!=null){
		    	String shade = datasource.getDefaultTargetDataSource().getKey();
	    		shade = ((Shard)shadeAnnotation).value();
    			shade = StringUtils.isEmpty(shade)?datasource.getDefaultTargetDataSource().getKey():shade;
    			stack = pushStack(stack, shade);
		    	isPush = true;
//		    	 System.out.println("shade:"+shade);
		    }else if(splitShadeAnnotation!=null){
		    	if(datasource==null||MapUtils.isEmpty(datasource.getTargetDataSources())){
		    		throw new ShardDbException("属性文件shards-db.properties未设置数据源，或者spring容器不存在AbsShardDbConfig对象。");
		    	}
		    	String shade = datasource.getDefaultTargetDataSource().getKey();
		    	if(joinPoint.getArgs()==null||joinPoint.getArgs().length==0){
		    		throw new ShardDbException("使用@SplitShade 必须匹配分库参数。");
		    	}
		    	
	    	   Object splitParam = joinPoint.getArgs()[splitShadeAnnotation.paramPosition()];
	    	   if(splitParam==null){
	    		  throw new ShardDbException(String.format("属性：%s 不能为空",method.getParameters()[splitShadeAnnotation.paramPosition()].getName()));
	    	   }
	    	   if(StringUtils.isEmpty(splitShadeAnnotation.paramPropertyName())){
	    		   shade = getShortTableName(method.getParameters()[splitShadeAnnotation.paramPosition()].getName(), splitParam);
	    	   }else{
	    		   try{
	    			   shade = getShortTableName(splitShadeAnnotation.paramPropertyName(),BeanUtils.getProperty(splitParam, splitShadeAnnotation.paramPropertyName()));
	    		   }catch(Exception e){
	    			   if(e instanceof ShardDbException){
	    				   throw e;
	    			   }else{
	    			   throw new ShardDbException("参数位置设置有误，请查看是否有该参数或者是否有getter方法",e);
	    			   }
	    		   }
	    	   }
	    	   stack = pushStack(stack, shade);
		       isPush = true;
		    }else{
		    	throw new ShardDbException("@Shade和@SplitShade不能同时使用");
		    }
		   return joinPoint.proceed();
	    }finally{
	    	if(isPush){
	    		//事务会在动态数据源通过peek()方法拿出来使用，这里不管失败与否都必须将使用完的事务删除。
	    		stack.pop();
	    	}
	    	if(stack!=null&&stack.empty()){
	    		TranHolder.shadeStack.remove();//所有事物都已经执行完毕，销毁线程里面的对战容器
	    	}
	    }
	 }

	private Stack pushStack(Stack<String> stack, String shade) {
//		System.out.println("============="+shade);
		if(stack==null){
			stack = new Stack<String>();
			TranHolder.shadeStack.set(stack);
		}
		stack.push(shade);
		return stack;
	}
	private String getShortTableName(String paramName, Object splitParam) {
			String shortTableName = null;
		//不包含对象属性
		   if(splitParam instanceof String || splitParam instanceof Integer){
			   String splitParamStr = StringUtils.lowerCase(splitParam+"");
			   if(!splitParamStr.matches("^[a-z0-9]*$")){
				   throw new ShardDbException(String.format("分库参数 %s 必须是整型或者字符[a-zA-Z]，%s 不正确",paramName,splitParam));
			   }
			   for(Entry<String, ShardDataSource >shard:this.datasource.getTargetDataSources().entrySet()){
				   String scope = shard.getValue().getScope();
				   String db = shard.getKey();
				   if(scope.indexOf("-")!=-1){
					   //scope带‘-’为分表字段开头值范围分表
					   String start = StringUtils.substringBefore(scope,"-") ;
					   String end = StringUtils.substringAfter(scope,"-") ;
					   if(StringUtils.compare(splitParamStr, start)>=0
							   &&StringUtils.compare(splitParamStr, end)<=0){
						   shortTableName = db;
						   return shortTableName;
					   }
				   }else if(StringUtils.isAlpha(scope)&&StringUtils.startsWithIgnoreCase(splitParamStr, scope)){
					   //scope字母开头分库
					   if(!StringUtils.isAlpha(splitParamStr)){
						   throw new ShardDbException(String.format("分库参数：%s 必须是字母，当前值为：%s",paramName,splitParamStr));
					   }
					   shortTableName = db;
					   return shortTableName;
				   }else if(StringUtils.isNumeric(scope)){
					  //scope如果是数字，取余数
					   if(!StringUtils.isNumeric(splitParamStr)){
						   throw new ShardDbException(String.format("分库参数：%s 必须是数字，当前值为：%s",paramName,splitParamStr));
					   }
					  if(NumberUtils.toInt(scope)==NumberUtils.toInt(splitParamStr)%this.datasource.getTargetDataSources().size()){
						  shortTableName = db;
						   return shortTableName;
					  }
//					  throw new ShardDbException(String.format("分库参数：%s 的值： %s不能映射数据源",paramName,splitParamStr));
				   }else{
					   throw new ShardDbException(String.format("请核对数据 源： %s 的scope值是否正确",db,scope));
				   }
			   }
			  
			   throw new ShardDbException(String.format("分库参数：%s = %s 未能命中数据库，请常看配置",paramName,splitParamStr));
		   }else{
			   throw new ShardDbException(String.format("分库参数：%s 必须是整型或者字符[a-zA-Z]",paramName));
		   }
	}
	
public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//	System.out.println(PropertyUtils.getProperty(new A(), "bbb.cc"));
	System.out.println(StringUtils.compare("abc", "bc"));
}	
		
}
