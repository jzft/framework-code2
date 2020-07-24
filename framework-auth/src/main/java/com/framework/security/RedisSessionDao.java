package com.framework.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.cache.RedisHelper;

/**
 * @ClassName:     RedisSessionDao.java
 * @Description:   session设置
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月5日 下午4:48:29 
 */
public class RedisSessionDao extends EnterpriseCacheSessionDAO {
	private static final Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);
	LoaderCacheManager cacheManager ;
	/**
	 * 单独为session设置过期时间
	 */
	private long session_timeout = 24*2*60*60;//从配置里面拿,System.getProperty(key);
	
//	private String key_prefix = "session";
	public RedisSessionDao(LoaderCacheManager cacheManager){
		cache = new RedisCache<String,Session>(super.getActiveSessionsCacheName(),session_timeout*1000L);
		this.setActiveSessionsCache(cache);
		this.setCacheManager(cacheManager);
		cacheManager.setSessionCache(cache);
		
	}
	/**
	 * 
	 * @Description:  session timeout To calculate 
	 * @param:        @param cacheManager
	 * @param:        @param timeout       
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:50:55
	 */
	public RedisSessionDao(LoaderCacheManager cacheManager,Integer timeout){
		this.session_timeout = timeout;
		cache = new RedisCache<String,Session>(super.getActiveSessionsCacheName(),session_timeout*1000L);
		this.setActiveSessionsCache(cache);
		this.setCacheManager(cacheManager);
		cacheManager.setSessionCache(cache);
		
	}
	
	private RedisCache cache = null;
	 
	@Override
	public void update(Session session) throws UnknownSessionException {
		 if(session == null || session.getId() == null){
	            logger.error("session or session id is null");
	            return;
	        }
		session.setTimeout(session_timeout*1000);
		cache.put(session.getId().toString(), (session));
	}
	
	@Override
	public void delete(Session session) {
		 if(session == null || session.getId() == null){
	            logger.error("session or session id is null");
	            return;
	        }
		cache.remove(session.getId().toString());
	}

	@Override
	public Collection<Session> getActiveSessions() {
		 Set<Session> sessions = new HashSet<Session>();
	        Set<String> keys = cache.keys();
	        if(keys != null && keys.size()>0){
	            for(String key:keys){
	                Session s = (Session)RedisHelper.getObj(key);
	                sessions.add(s);
	            }
	        }
	        return sessions;
	}
	/**
	 * 
	 * @Title:        doCreate 
	 * @Description:  获取session 
	 * @param:        @param session
	 * @param:        @return        
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:54:26
	 */
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		  if(sessionId == null){
	            logger.error("session id is null");
	            return null;
	        }
		  Session session  = (Session)cache.get(sessionId+"");
	        return session;
	}

	public void setCacheManager(LoaderCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	public long getTimeout() {
		// TODO Auto-generated method stub
		return session_timeout;
	}

}

/**
 * 
 * @ClassName:     RedisSessionDao.java
 * @Description:   获取session中的值并赋值
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月5日 下午4:52:24
 */
class RedisSimpleSession extends SimpleSession implements RedisObjProxy<Session>{
	private  String id = null; 
    private  Date startTimestamp = null;
    private  Date stopTimestamp = null;
    private  Date lastAccessTime = null;
    private  long timeout ;
    private  boolean expired ;
    private  String host = null;
    private  Map<Object, Object> attributes;
    public RedisSimpleSession(SimpleSession session) {
    	this.id=session.getId().toString();
    	this.startTimestamp = session.getStartTimestamp();
    	this.stopTimestamp = session.getStopTimestamp();
    	this.lastAccessTime = session.getLastAccessTime();
    	this.timeout=session.getTimeout();
    	this.expired = session.isExpired();
    	this.host = session.getHost();
    	this.attributes = session.getAttributes();
    }

    public RedisSimpleSession(String host) {
    	super(host);
    }
    
    /**
     * 
     * @Title:        getObj 
     * @Description:  将值设置到session中 
     * @param:        @return        
     * @throws 
     * @author        join
     * @Date          2017年1月5日 下午4:53:08
     */
    @Override
	public Session getObj(){
    	SimpleSession session = new SimpleSession();
    	session.setAttributes(attributes);
    	session.setExpired(expired);
    	session.setHost(host);
    	session.setId(id);
    	session.setLastAccessTime(lastAccessTime);
    	session.setStartTimestamp(startTimestamp);
    	session.setStopTimestamp(stopTimestamp);
    	session.setTimeout(timeout);
    	return session;
    	
    }

}