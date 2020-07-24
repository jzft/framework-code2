package com.framework.security;

import org.apache.shiro.session.mgt.eis.SessionDAO;

public class DeriveWebSessionManager extends org.apache.shiro.web.session.mgt.DefaultWebSessionManager{
	@Override
	public void setSessionDAO(SessionDAO sessionDAO){
		if(sessionDAO instanceof RedisSessionDao){
			super.setGlobalSessionTimeout(((RedisSessionDao) sessionDAO).getTimeout());
		}
		super.setSessionDAO(sessionDAO);
	}
	@Override
	public void setGlobalSessionTimeout(long globalSessionTimeout){
		if(sessionDAO instanceof RedisSessionDao){
			//不再由SessionManager设置过期时间
		}else{
			super.setGlobalSessionTimeout(globalSessionTimeout);
		}
	}
	
	
}