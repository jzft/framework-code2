package com.framework.security;


/**
 * 
 * @ClassName:     ZkPromulgator.java
 * @Description:   zookeeper 发布公告接口
 * @author         Administrator
 * @version        V1.0  
 * @Date           2017年1月5日 下午5:06:53
 */
public interface ZkPromulgator<T> {
	/**
	 * 
	 * @Title:        publish 
	 * @Description:  发布公告方法 
	 * @param:        @param msg
	 * @param:        @throws Exception    
	 * @return:       void    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午5:07:16
	 */
	public void publish(T msg) throws Exception;
	
	public boolean isZkEnabled();
}