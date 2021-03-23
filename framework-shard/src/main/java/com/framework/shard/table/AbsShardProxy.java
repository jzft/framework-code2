package com.framework.shard.table;

import java.lang.reflect.Method;
import java.util.Stack;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.framework.shard.KeyValue;
import com.framework.shard.ShardParamUtil;
import com.framework.shard.TranHolder;
import com.framework.shard.table.parse.AbsSqlParser;
import com.framework.shard.table.policy.SplitTablePolicy;

public abstract class AbsShardProxy {

	private SplitTablePolicy splitTablePolicy;
	protected String [] sqlMethod;
	
	protected Object[] getArgs(Method method,Object []args){//连接池及statement相关参数方法，参数重构
		if(StringUtils.isNotEmpty(ShardParamUtil.shardTables)&&ArrayUtils.isNotEmpty(args)&&ArrayUtils.contains(sqlMethod, method.getName())){//判断是传入sql方法
			Object hitValue = null;
			Stack<KeyValue<String,Object>> stack = TranHolder.shardStack.get();
			if(stack!=null&&!stack.empty()){
				hitValue = stack.peek().getValue();
				
			}
			String []tables = StringUtils.split(ShardParamUtil.shardTables,",");
			
			for(int i=0;i<args.length;i++){//查找sql并处理
				Object arg = args[i];
				if(arg instanceof String){//数据源连接规则，第一个String类型的参数一定是sql
					String logicSql = (String)arg;
					String []tableExpressions = new String[tables.length];
					String hitValueStr = (String)hitValue;
					for(Integer j =0 ;j<tables.length;j++){
						String table = tables[j];
						String tableExpression = splitTablePolicy.getShardTableExpression(hitValueStr, table);
						tableExpressions[j]=tableExpression;
					}
					
					AbsSqlParser parser = SqlParserFactory.getParser(logicSql);
					args[i] = parser.genSql(logicSql,tableExpressions);
					break;//只需要处理第一个sql类型的sql语句
				}
			}
		}
		
		return args;
	}

	public void setSplitTablePolicy(SplitTablePolicy splitTablePolicy) {
		this.splitTablePolicy = splitTablePolicy;
	}
	
	
}
