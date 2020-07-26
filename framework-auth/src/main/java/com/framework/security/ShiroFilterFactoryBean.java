package com.framework.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import com.framework.security.filter.FormAuthenticationFilter;

public class ShiroFilterFactoryBean extends org.apache.shiro.spring.web.ShiroFilterFactoryBean  {
	
	public static ShiroFilterFactoryBean instance = null;
	Map<String,String>filterClassNames = new HashMap<String,String>();
	protected Map<String,String> initFilterChainDefinitionMap = new HashMap<>();
	private String captchaSessionKey = null;
	
	
	
	private String redirectUrl = "/login.jsp";
	
	public ShiroFilterFactoryBean(){
		if(instance==null){
			instance = this;
		}
	}

	@Override
	public void setFilterChainDefinitions(String definitions){
		super.setFilterChainDefinitions(definitions);
		initFilterChainDefinitionMap.putAll(super.getFilterChainDefinitionMap());
	}
	
	
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
		Map<String, Filter> filters = this.getFilters()==null? new  HashMap<String, Filter>():this.getFilters();
		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setRedirectUrl(redirectUrl);
		filters.put("logout", logoutFilter);
		super.setFilters(filters);
	}


	public Map<String, String> getFilterClassNames() {
		return filterClassNames;
	}


	public String getRedirectUrl() {
		return redirectUrl;
	}
	
	


	/*public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}*/


	public String getCaptchaSessionKey() {
		return captchaSessionKey;
	}

	public void setCaptchaSessionKey(String captchaSessionKey) {
		this.captchaSessionKey = captchaSessionKey;
	}

	public void setFilterClassNames(Map<String, String> filterClassNames) {
		this.filterClassNames = filterClassNames;
		if(filterClassNames==null||filterClassNames.size()==0){
			return ;
		}
		Set<String> keys = filterClassNames.keySet();
		Map<String, Filter> filters = super.getFilters()==null? new  HashMap<String, Filter>():this.getFilters();
		DefaultWebSecurityManager securityManager = null;
		DeriveWebSessionManager sessionManager = null;
		if(super.getSecurityManager() instanceof DefaultWebSecurityManager ){
			securityManager =(DefaultWebSecurityManager)super.getSecurityManager();
			SessionManager sessionM = securityManager.getSessionManager();
			if(sessionM instanceof DeriveWebSessionManager){
				sessionManager = (DeriveWebSessionManager)sessionM;
			}
		}
		
		for(String key:keys ){
			String className = filterClassNames.get(key);
			if(StringUtils.isNoneEmpty(className)){
				 try {
					Object obj = Class.forName(className).newInstance();
					if(obj instanceof AccessControlFilter){
						AccessControlFilter f = (AccessControlFilter)obj;
						if(sessionManager!=null){
							if(f instanceof FormAuthenticationFilter){
								((FormAuthenticationFilter)f).setSessionIdCookie(sessionManager.getSessionIdCookie());
								((FormAuthenticationFilter)f).setCaptchaSessionKey(captchaSessionKey);
							}
						}
						filters.put(key, f);
					}else if(obj instanceof org.apache.shiro.web.filter.authc.LogoutFilter){
						BeanUtils.setProperty(obj, "redirectUrl",redirectUrl);
					}
				} catch (Exception e) {
					throw new ClassFormatError("filterClassNames 转换错误");
				}
			}
			
		}
		super.setFilters(filters);
	}


//	@Override
//	public void setFilters(Map<String, Filter> proxyFilters) {
//		
//	}
	
	
}