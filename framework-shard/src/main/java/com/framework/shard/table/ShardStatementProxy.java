package com.framework.shard.table;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;


public class ShardStatementProxy  extends AbsShardProxy implements InvocationHandler {
	Statement statement ;
	public ShardStatementProxy (Statement statement){
		this.statement = statement;
		super.sqlMethod = new String[]{
			"executeLargeUpdate","execute","executeUpdate","addBatch","executeQuery"
		};
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		 return method.invoke(statement, this.getArgs(method, args));
	}
	
	 /** 
     * 绑定并获取代理对象 
     * @param conn 转换器接口 
     * @return Connection代理对象 
     * @author lyq 
     * @date 2021-03-16 
     */  
    public Statement bind(){ 
    		this.statement = statement;
            Class [] clzz = {Statement.class};  
            Object obj = Proxy.newProxyInstance(  
            this.statement.getClass().getClassLoader(),clzz , this);
            return (Statement)obj;  
    }
}
