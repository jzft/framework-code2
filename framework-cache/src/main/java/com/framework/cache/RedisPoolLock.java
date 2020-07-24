package com.framework.cache;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

public class RedisPoolLock<T> implements ConnectionPool <T> {

	
	private Integer maxActive = 100;
	private Integer maxWait = 30;
	
	public final String LOCKSTR =  "POOL_EXEC:";
	@Override
	public void init(Integer maxActive,Integer maxWait) {
		this.maxActive = maxActive;
		this.maxWait = maxWait;
		
	}



	@Override
	public void release(T connection) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T getResource(String key) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
