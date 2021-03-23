package com.framework.shard;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import com.framework.shard.table.ShardConnectionProxy;
import com.framework.shard.table.ShardDruidConnectionProxy;
import com.framework.shard.table.parse.SqlParserException;
import com.framework.shard.table.policy.SplitTablePolicy;

/**
 * 继承DruidDataSource，自定义数据源，增加切片属性
 * @author lyq
 * @date 2020年8月14日 下午7:43:06
 */
@SuppressWarnings("serial")
public class ShardDataSource extends DruidDataSource {
	private String scope;//数据库分库字段映射范围
	private String key;//数据源target值。
	private String splitTablePolicyClass ;//分表策略实现类
	private SplitTablePolicy splitTablePolicy = null ;
	
	@Override
	public Connection createPhysicalConnection(String url, Properties info) throws SQLException {
		 Connection connection = super.createPhysicalConnection(url,info);
//		 return connection;
		 if(connection instanceof ConnectionProxy){
			 ShardDruidConnectionProxy proxy = new ShardDruidConnectionProxy((ConnectionProxy)connection);
			 proxy.setSplitTablePolicy(splitTablePolicy);
			 return proxy.bind();
		 }else{
		 	 ShardConnectionProxy proxy = new ShardConnectionProxy(connection);
		 	 proxy.setSplitTablePolicy(splitTablePolicy);
			 return proxy.bind();
		 }
		 
	}
	
	 
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSplitTablePolicyClass() {
		return splitTablePolicyClass;
	}

	public void setSplitTablePolicyClass(String splitTablePolicyClass) {
		this.splitTablePolicyClass = splitTablePolicyClass;
		try {
			this.splitTablePolicy = (SplitTablePolicy)Class.forName(splitTablePolicyClass).newInstance();
		} catch (Exception e) {
			throw new SqlParserException(e);
		} 
	}
}
