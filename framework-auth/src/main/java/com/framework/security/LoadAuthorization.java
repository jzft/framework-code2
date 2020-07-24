package com.framework.security;

import org.apache.shiro.authz.AuthorizationInfo;
/**
 * @ClassName:     LoadAuthorization.java
 * @Description:   加载用户权限接口 
 * @author         Administrator
 * @version        V1.0  
 * @Date           2017年1月5日 下午4:18:14 
 */
public interface LoadAuthorization{

	/** 
	 * @Title:        load 
	 * @Description:  加载用户权限 
	 * @param:        @param username
	 * @param:        @return    
	 * @return:       AuthorizationInfo    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:18:39 
	 */
	public AuthorizationInfo load(String username);
}