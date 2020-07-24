package com.framework.security.exception;

/**
 * 
 * @ClassName:     SecurityException.java
 * @Description:   安全异常 
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月6日 上午9:12:38
 */
@SuppressWarnings("serial")
public class SecurityException extends RuntimeException {

	public SecurityException(String code,String msg){
		super("security_"+code+":"+msg);
	}  
}