package com.framework.security.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 
 * @ClassName:     SecurityException.java
 * @Description:   安全异常 
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月6日 上午9:12:38
 */
@SuppressWarnings("serial")
public class MyAuthenticationException extends AuthenticationException {
	
	private String code = null;
	public MyAuthenticationException(String code,String msg){
		super(msg);
		this.code = code;
	}
	public MyAuthenticationException(String msg){
		super(msg);
	}
	
}