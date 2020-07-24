package com.framework.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.servlet.Cookie;
import org.springframework.http.HttpStatus;

import com.framework.security.ShiroFilterFactoryBean;

/**
 * 
 * @ClassName:     FormAuthenticationFilter.java
 * @Description:   身份认证过滤器
 * @author         join
 * @version        V1.0   
 * @Date           2017年1月6日 上午9:13:41
 */
@Deprecated
public class RolesAuthenticationFilter extends org.apache.shiro.web.filter.authz.RolesAuthorizationFilter {

	    @Override
	    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//	    	 response.setHeader("Access-control-Allow-Origin","*");
//	         response.setHeader("Access-Control-Allow-Methods","*");
//	         response.setHeader("Access-Control-Allow-Credentials", "true");
//	         response.setHeader("Access-Control-Allow-Headers", "*");
	    	HttpServletResponse res = ((HttpServletResponse)response);
	    	HttpServletRequest req = (HttpServletRequest)request;
	    	String requestHeaders = req.getHeader("Access-Control-Request-Headers"); 
			if (StringUtils.isEmpty(requestHeaders)) {
				requestHeaders = "";
			}
	    	res.setHeader("Access-control-Allow-Origin",req.getHeader("Origin"));
//	    	res.setHeader("Access-Control-Allow-Methods",req.getMethod());
	    	res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
	    	res.setHeader("Access-Control-Allow-Credentials", "true");
	    	res.setHeader("Access-Control-Allow-Headers",  "Accept, Origin, XRequestedWith, Content-Type, LastModified,"+requestHeaders);
	    	res.setStatus(HttpServletResponse.SC_OK);
	    	return super.preHandle(request, response);
	    }
	    
	   


}