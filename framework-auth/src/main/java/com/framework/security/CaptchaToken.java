package com.framework.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CaptchaToken extends UsernamePasswordToken {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 验证码
    private String captcha;
    //session中key名
    private String captchaSessionKey;
    
	public CaptchaToken(final String username, final String password,
             final boolean rememberMe, final String host,String captchaSessionKey,String captcha) {
		  super(username,password,rememberMe,host);
		  this.captchaSessionKey = captchaSessionKey;
		  this.captcha = captcha;
	 }
	

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getCaptchaSessionKey() {
		return captchaSessionKey;
	}

	public void setCaptchaSessionKey(String captchaSessionKey) {
		this.captchaSessionKey = captchaSessionKey;
	}
    
}
