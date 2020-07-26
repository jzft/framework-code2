package com.framework.security.filter;

import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.servlet.Cookie;

import com.framework.security.CaptchaToken;
import com.framework.security.ShiroFilterFactoryBean;

/**
 * 
 * @ClassName:     FormAuthenticationFilter.java
 * @Description:   身份认证过滤器
 * @author         join
 * @version        V1.0   
 * @Date           2017年1月6日 上午9:13:41
 */
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	  	private Cookie sessionIdCookie;
	  	 private String captchaSessionKey = null;
	    public Cookie getSessionIdCookie() {
	        return sessionIdCookie;
	    }


	    public void setSessionIdCookie(Cookie sessionIdCookie) {
	        this.sessionIdCookie = sessionIdCookie;
	    }

	    @Override
	    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response){
	    	AuthenticationToken taken = null;
	    	 if(StringUtils.isNotEmpty(this.captchaSessionKey)&&!StringUtils.equals(this.captchaSessionKey, "false")){
	    		 String username = getUsername(request);
	    	     String password = getPassword(request);
	    	     boolean rememberMe = isRememberMe(request);
	    	     String host = getHost(request);
	    	     Enumeration<String> a = request.getParameterNames();
	         	//验证码
	    		String captcha = (String)request.getParameter(this.captchaSessionKey);
	         	taken = new CaptchaToken(username, password, rememberMe, host,this.captchaSessionKey,captcha);
		    }else{
		    	taken = super.createToken(request,response);
		    }
	    	return taken;
	    }
	    


	    @Override
	    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
	        request.setAttribute(getFailureKeyAttribute(), ae);
	    }
	    
	    /**
	     * 
	     * @Title:        issueSuccessRedirect 
	     * @Description:  赋值到request中 
	     * @param:        @param request
	     * @param:        @param response
	     * @param:        @throws Exception        
	     * @throws 
	     * @author        join
	     * @Date          2017年1月6日 上午9:14:07
	     */
	    @Override
	    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
	    	this.setSessionIdCookie(sessionIdCookie);
	        super.issueSuccessRedirect(request, response);
	    }
	    @Override
	    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//	    	 response.setHeader("Access-control-Allow-Origin","*");
//	         response.setHeader("Access-Control-Allow-Methods","*");
//	         response.setHeader("Access-Control-Allow-Credentials", "true");
//	         response.setHeader("Access-Control-Allow-Headers", "*");
	    	HttpServletResponse res = ((HttpServletResponse)response);
	    	HttpServletRequest req = (HttpServletRequest)request;
//	    	System.out.println("1==="+req.getRequestURI());
//	    	res.setHeader("Access-control-Allow-Origin",req.getHeader("Origin"));
////	    	res.setHeader("Access-Control-Allow-Methods",req.getMethod());
//	    	res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
//	    	res.setHeader("Access-Control-Allow-Credentials", "true");
//	    	res.setHeader("Access-Control-Expose-Headers", "content-disposition");
//	    	res.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//	    	res.setStatus(HttpServletResponse.SC_OK);
	    	return super.preHandle(request, response);
	    }
	    
	    @Override 
	    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//	    	Subject subject = getSubject(request, response);
//	       return   subject.isAuthenticated()|| (!isLoginRequest(request, response) && isPermissive(mappedValue));
//	        return  (!isLoginRequest(request, response) && isPermissive(mappedValue));
//	    	 String requestURI = getPathWithinApplication(request);
	    	 String requestURI = getPathWithinApplication(request);
	    	if(StringUtils.equalsIgnoreCase(ShiroFilterFactoryBean.instance.getLoginUrl(), requestURI)){
				 return false;
			 }
//	    	this.getSessionIdCookie();
//	    	 org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
	    	return super.isAccessAllowed(request, response, mappedValue);
	    }
	   

		public String getCaptchaSessionKey() {
			return captchaSessionKey;
		}


		public void setCaptchaSessionKey(String captchaSessionKey) {
			this.captchaSessionKey = captchaSessionKey;
		}

}