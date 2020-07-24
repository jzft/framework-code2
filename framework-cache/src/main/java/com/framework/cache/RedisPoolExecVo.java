package com.framework.cache;

public class RedisPoolExecVo <T> {

	private T t;
	private Integer status = 0;
	public RedisPoolExecVo(){
	}
	public RedisPoolExecVo(T t,Integer status){
		this.t = t;
		this.status = status;
	}
	
	public T getResult(){
		return t;
	}
}
