package com.framework.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import com.framework.cache.RedisHelper;


/**
 * @ClassName:     RedisCache.java
 * @Description:   shiro缓存接口实现类 
 * @author         lenovo
 * @version        V1.0  
 * @Date           2017年1月5日 下午4:38:30 
 */
public class RedisCache<K,T> implements Cache<K,T> {

	private int expireSeconds = 24*2*60*60;//默认时间从配置文件　（System.getProterty）里面获取
	private  String SECURITY_PREFIX = "_CACHE:";
	Class tclass= null;
	
//	public RedisCache(){}
	
	public <V extends T> RedisCache(String prefix,Class<V> tclass ){
		this(prefix);
		this.tclass=tclass;
	}
	public RedisCache(String prefix){
		SECURITY_PREFIX = "SHIRO_"+prefix+SECURITY_PREFIX;
		
	}
	
	public <V extends T> RedisCache(String prefix,Long timeout,Class<V> tclass){
		this(prefix);
		this.expireSeconds = timeout.intValue()/1000;
		this.tclass=tclass;
	}
	
	public RedisCache(String prefix,Long timeout){
		this(prefix);
		this.expireSeconds = timeout.intValue()/1000;
	}
	

	/** 
	 * @Title:        get 
	 * @Description:  获取键 
	 * @param:        @param key
	 * @param:        @return
	 * @param:        @throws CacheException        
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:40:18 
	 */
	@Override
	public T get(K key) throws CacheException {
		// TODO 自动生成的方法存根
		if(this.tclass==null){
			T aa = null;
			try{
			aa = (T)RedisHelper.getObj(genkey(key.toString()));
			}catch(Exception e){
				e.printStackTrace();
			}
			return aa;
		}
		return (T)RedisHelper.get(genkey(key.toString()),this.tclass);
		
	}
	/**
	 * 
	 * @Title:        put 
	 * @Description:  存键值 
	 * @param:        @param key
	 * @param:        @param value
	 * @param:        @return
	 * @param:        @throws CacheException        
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:40:41
	 */
	@Override
	public T put(K key, T value) throws CacheException {
		if(tclass==null){
			RedisHelper.setObj(genkey(key.toString()), value,expireSeconds);
			//RedisHelper.set(genkey(key.toString()), value,expireSeconds);
		}else{
			RedisHelper.set(genkey(key.toString()), value,expireSeconds);
		}
		return value;
	}
	/**
	 * 
	 * @Title:        remove 
	 * @Description:  删除一个缓存 
	 * @param:        @param key
	 * @param:        @return
	 * @param:        @throws CacheException        
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:41:03
	 */
	@Override
	public T  remove(K key) throws CacheException {
		if(tclass==null){
		RedisHelper.delObj(genkey(key.toString()));
		}else{
			RedisHelper.del(genkey(key.toString()));
		}
		return this.get(key);
	}
	
	
	@Override
	public void clear() throws CacheException {
	}
	/**
	 * 
	 */
	@Override
	public int size() {
		int len =  RedisHelper.keys(SECURITY_PREFIX+"*").size();
		return len;
	}
	@Override
	public Set<K> keys() {
		return (Set<K>) RedisHelper.keys(SECURITY_PREFIX+"*");
	}
	/**
	 * 
	 * @Title:        values 
	 * @Description:  获取所有缓存 
	 * @param:        @return        
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:43:13
	 */
	@Override
	public Collection<T> values() {
		return (Collection<T>)Collections.unmodifiableList(RedisHelper.values(SECURITY_PREFIX+"*"));
	}
	/**
	 * 
	 * @Title:        genkey 
	 * @Description:  生成主键 
	 * @param:        @param key
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:43:31
	 */
	public String genkey(String key){
		return SECURITY_PREFIX+key;
	}
	
	
	
}