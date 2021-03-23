package com.framework.shard.table;

import java.lang.reflect.Proxy;
import java.sql.Connection;

import com.alibaba.druid.proxy.jdbc.StatementProxy;

public class ShardDruidStatementProxy   extends ShardStatementProxy   {

	StatementProxy statement;
	public ShardDruidStatementProxy (StatementProxy statement){
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
    public StatementProxy bind(){ 
            Class [] clzz = {StatementProxy.class};  
            Object obj = Proxy.newProxyInstance(  
            this.statement.getClass().getClassLoader(),clzz , this);
            return (StatementProxy)obj;  
    }

}
