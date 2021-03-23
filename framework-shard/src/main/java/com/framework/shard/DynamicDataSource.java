package com.framework.shard;


import java.util.Map;
import java.util.Stack;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.framework.shard.TranHolder;


/**
 * 使用动态数据源实现分库选择数据源
 * 
 * @author lyq
 * @date 2020年8月14日 下午7:39:26
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
	
	private Map<String,ShardDataSource> targetDataSources;
	private ShardDataSource defaultTargetDataSource;
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
	 */
	protected Object determineCurrentLookupKey() {
		Stack<KeyValue<String,Object>> stack = TranHolder.shardStack.get();
		if(stack!=null&&!stack.empty()){
			return stack.peek().getKey();
		}else{
			return defaultTargetDataSource.getKey();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		super.setTargetDataSources(targetDataSources);
		this.targetDataSources = (Map)targetDataSources;
	}
	public Map<String, ShardDataSource> getTargetDataSources() {
		return targetDataSources;
	}
	
	@Override
	public void setDefaultTargetDataSource(Object targetDataSource){
		this.defaultTargetDataSource = (ShardDataSource) targetDataSource;
		super.setDefaultTargetDataSource(targetDataSource);
	}

	public ShardDataSource getDefaultTargetDataSource() {
		return defaultTargetDataSource;
	}
}
