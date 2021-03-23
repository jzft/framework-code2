package com.framework.shard.table;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;

public class ShardDruidConnectionProxy extends ShardConnectionProxy implements InvocationHandler  {

	/**
	 * 传入sql的方法
	 */
	
	ConnectionProxy conn = null;
	
	public ShardDruidConnectionProxy(ConnectionProxy conn){
		super(conn);
		this.conn = conn;
		super.sqlMethod = new String[]{
				"prepareStatement","prepareCall","nativeSQL"
		};
	}
//	@Override
//	public Object invoke(Object proxy, Method method, Object...args) throws Throwable {
//		// TODO Auto-generated method stub
////		args = ;
//		Object statement = method.invoke(conn, this.getArgs(method, args));
//		if(statement instanceof StatementProxy){
//			return new DruidStatementProxy((StatementProxy)statement).bind();
//		}else if(statement instanceof PreparedStatement){
//			return new ShardPrepareStatementProxy((PreparedStatement)statement).bind();
//		}else if(statement instanceof Statement){
//			return new ShardStatementProxy((Statement)statement).bind();
//		}	
//		return statement;
//	}
	 /** 
     * 绑定并获取代理对象 
     * @param conn 转换器接口 
     * @return Connection代理对象 
     * @author lyq 
     * @date 2021-03-16 
     */  
    public ConnectionProxy bind(){ 
//    		this.conn = conn;
            
            Class [] clzz = {ConnectionProxy.class};  
            Object obj = Proxy.newProxyInstance(  
            this.conn.getClass().getClassLoader(),clzz , this);  
            return (ConnectionProxy)obj;  
    } 

}
