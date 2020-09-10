package com.framework.shard;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 继承DruidDataSource，自定义数据源，增加切片属性
 * @author lyq
 * @date 2020年8月14日 下午7:43:06
 */
@SuppressWarnings("serial")
public class ShardDataSource extends DruidDataSource {
	private String scope;//数据库分库字段映射范围
	private String key;//数据源target值。
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
}
