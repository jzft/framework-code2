package com.framework.security;


/**
 *@Description: redis存储特殊对象存储代理
 *@author Administrator
 *
 * @param <T>
 */
public interface RedisObjProxy <T>  {

	/**
	 *@Description: 获取代理对象
	 * @return
	 */
	public T getObj();
}