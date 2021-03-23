package com.framework.shard.table;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;

public class ShardConnectionProxy  extends AbsShardProxy implements InvocationHandler  {

	/**
	 * 传入sql的方法
	 */
	
	Connection conn = null;
	
	public ShardConnectionProxy(Connection conn){
		this.conn = conn;
		super.sqlMethod = new String[]{
				"prepareStatement","prepareCall","nativeSQL"
		};
		
	}
	@Override
	public Object invoke(Object proxy, Method method, Object...args) throws Throwable {
		// TODO Auto-generated method stub
//		args = ;
		Object statement = method.invoke(conn, this.getArgs(method, args));
		if(statement instanceof PreparedStatementProxy){
			return new ShardDruidPrepareStatementProxy((PreparedStatementProxy)statement).bind();
		}else if(statement instanceof StatementProxy){
			return new ShardDruidStatementProxy((StatementProxy)statement).bind();
		}else if(statement instanceof PreparedStatement){
			return new ShardPrepareStatementProxy((PreparedStatement)statement).bind();
		}else if(statement instanceof Statement){
			return new ShardStatementProxy((Statement)statement).bind();
		}
		return statement;
	}
	 /** 
     * 绑定并获取代理对象 
     * @param conn 转换器接口 
     * @return Connection代理对象 
     * @author lyq 
     * @date 2021-03-16 
     */  
    public Connection bind(){ 
//    		this.conn = conn;
            
            Class [] clzz = {Connection.class};  
            Object obj = Proxy.newProxyInstance(  
            this.conn.getClass().getClassLoader(),clzz , this);  
            return (Connection)obj;  
    } 

}
