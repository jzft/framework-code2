package com.framework.security;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.session.mgt.SimpleSession;
/**
 * @ClassName:     RedisCacheManager.java
 * @Description:   实现自定义CacheManager 
 * @author         lenovo
 * @version        V1.0  
 * @Date           2017年1月5日 下午4:44:39 
 */
public class RedisCacheManager implements LoaderCacheManager {
		
	
	 Cache<String,SimpleSession > sessionCache = null;
	 Cache<String, AuthenticationInfo> authenticationInfoCache =null;
	 Cache<String, AuthorizationInfo> authorizationInfoCache =null;
	    // fast lookup by name map
	    private final ConcurrentMap<String, Cache<String,Object>> caches = new ConcurrentHashMap<String, Cache<String,Object>>();
	    /**
	     * The Redis key prefix for caches 
	     */
	    private String keyPrefix = "redis";
	 
	    /**
	     * 
	     * @Title:        getKeyPrefix 
	     * @Description:  Returns the Redis session keys 
	     * @param:        @return   The prefix 
	     * @return:       String    
	     * @throws 
	     * @author        join
	     * @Date          2017年1月5日 下午4:45:26
	     */
	    public String getKeyPrefix() {
	        return keyPrefix;
	    }
	 
	    /**
	     * 
	     * @Title:        setKeyPrefix 
	     * @Description:  Sets the Redis sessions key 
	     * @param:        @param keyPrefix The prefix   
	     * @return:       void    
	     * @throws 
	     * @author        join
	     * @Date          2017年1月5日 下午4:46:06
	     */
	    public void setKeyPrefix(String keyPrefix) {
	        this.keyPrefix = keyPrefix;
	    }
	    
	    /**
	     * 
	     * @Title:        getCache 
	     * @Description: 获取名称为XXX的RedisCache实例 
	     * @param:        @param name
	     * @param:        @return
	     * @param:        @throws CacheException        
	     * @throws 
	     * @author        join
	     * @Date          2017年1月5日 下午4:46:38
	     */
	    @Override
	    public Cache getCache(String name) throws CacheException {
//	        logger.debug("获取名称为: " + name + " 的RedisCache实例");
	        Cache<String,Object> c = caches.get(name);
	 
	        if (c == null) {
	            // create a new cache instance
	            c = new RedisCache(name);
	            // add it to the cache collection
	            caches.put(name, c);
	        } 
	        return c;
	    }
	    @SuppressWarnings("rawtypes")
		@Override
	    public <T> void setCache(String name,Cache<String,T>cache){
	    	Cache c = caches.get(name);
	    	if(c==null){
	    		 caches.put(name, (Cache<String,Object>)cache);
	    	}else{
	    		caches.remove(name);
	    		caches.put(name,  (Cache<String,Object>)cache);
	    	}
	    }

		public Cache<String, SimpleSession> getSessionCache() {
			return sessionCache;
		}

		public void setSessionCache(Cache<String, SimpleSession> sessionCache) {
			this.sessionCache = sessionCache;
		}

		public Cache<String, AuthenticationInfo> getAuthenticationInfoCache() {
			return authenticationInfoCache;
		}

		public void setAuthenticationInfoCache(
				Cache<String, AuthenticationInfo> authenticationInfoCache) {
			this.authenticationInfoCache = authenticationInfoCache;
		}

		public Cache<String, AuthorizationInfo> getAuthorizationInfoCache() {
			return authorizationInfoCache;
		}

		public void setAuthorizationInfoCache(
				Cache<String, AuthorizationInfo> authorizationInfoCache) {
			this.authorizationInfoCache = authorizationInfoCache;
		}
	    
	    
	 
}