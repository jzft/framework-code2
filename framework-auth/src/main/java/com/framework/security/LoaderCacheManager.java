package com.framework.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.SimpleSession;


/**
 * @ClassName:     LoaderCacheManager.java
 * @Description:   自定义cacheManager 
 * @author         Administrator
 * @version        V1.0  
 * @Date           2017年1月5日 下午4:19:10 
 */
public interface LoaderCacheManager extends CacheManager {
	/**
	 * 
	 * @Title:        setCache 
	 * @Description:  将name设置到缓存中 
	 * @param:        @param name
	 * @param:        @param cache    
	 * @return:       void    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:23:54
	 */
	public <T> void setCache(String name,Cache<String,T>cache);
	/**
	 * 
	 * @Title:        getAuthenticationInfoCache 
	 * @Description:  获取身份验证信息缓存 
	 * @param:        @return    
	 * @return:       Cache<String,AuthenticationInfo>    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:30:44
	 */
	public Cache<String, AuthenticationInfo> getAuthenticationInfoCache() ;
	
	/**
	 * 
	 * @Title:        getAuthorizationInfoCache 
	 * @Description:  获取授权信息缓存 
	 * @param:        @return    
	 * @return:       Cache<String,AuthorizationInfo>    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:31:57
	 */
	public Cache<String, AuthorizationInfo> getAuthorizationInfoCache();
	
	/**
	 * 
	 * @Title:        getSessionCache 
	 * @Description:  获取会话缓存 
	 * @param:        @return    
	 * @return:       Cache<String,SimpleSession>    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:33:00
	 */
	public Cache<String, SimpleSession> getSessionCache();
	
	/**
	 * 
	 * @Title:        setSessionCache 
	 * @Description:  将会话的值设置到缓存中
	 * @param:        @param sessionCache    
	 * @return:       void    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:33:39
	 */
	public void setSessionCache(Cache<String, SimpleSession> sessionCache);
	
	/**
	 * 
	 * @Title:        setAuthenticationInfoCache 
	 * @Description:  将身份验证信息设置到缓存中
	 * @param:        @param authenticationInfoCache    
	 * @return:       void    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:34:45
	 */
	public void setAuthenticationInfoCache(
			Cache<String, AuthenticationInfo> authenticationInfoCache) ;

	/**
	 * 
	 * @Title:        setAuthorizationInfoCache 
	 * @Description:  将授权信息设置到缓存中
	 * @param:        @param authorizationInfoCache    
	 * @return:       void    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:35:27
	 */
	public void setAuthorizationInfoCache(
			Cache<String, AuthorizationInfo> authorizationInfoCache) ;
}