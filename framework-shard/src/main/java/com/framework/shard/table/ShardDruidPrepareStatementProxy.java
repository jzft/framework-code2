package com.framework.shard.table;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;


public class ShardDruidPrepareStatementProxy  extends ShardStatementProxy {
	PreparedStatementProxy statement ;
	public ShardDruidPrepareStatementProxy (PreparedStatementProxy statement){
		super(statement);
		this.statement = statement;
	}


	
	 /** 
     * 绑定并获取代理对象 
     * @param conn 转换器接口 
     * @return Connection代理对象 
     * @author lyq 
     * @date 2021-03-16 
     */  
	@Override
    public PreparedStatementProxy bind(){ 
            Class [] clzz = {PreparedStatementProxy.class};  
            Object obj = Proxy.newProxyInstance(  
            this.statement.getClass().getClassLoader(),clzz , this);
            return (PreparedStatementProxy)obj;  
    }
}
