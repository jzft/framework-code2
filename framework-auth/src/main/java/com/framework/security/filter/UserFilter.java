package com.framework.security.filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.servlet.Cookie;
/**
 * 
 * @ClassName:     UserFilter.java
 * @Description:   clear JSESSIONID in URL if session id is not null  
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月6日 上午9:16:10
 */
public class UserFilter extends AccessControlFilter {

    private Cookie sessionIdCookie;
    
    public Cookie getSessionIdCookie() {
        return sessionIdCookie;
    }


    public void setSessionIdCookie(Cookie sessionIdCookie) {
        this.sessionIdCookie = sessionIdCookie;
    }
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginRequest(request, response)) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
            // If principal is not null, then the user is known and should be allowed access.
            return subject.getPrincipal() != null;
        }
    }
    
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        saveRequest(request);
//        String sessionid = sessionIdCookie.readValue(WebUtils.toHttp(request), WebUtils.toHttp(response));
	        // clear JSESSIONID in URL if session id is not null 
	/*        if(sessionid != null){
	            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
	            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionid);
	            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
	        }*/
	        redirectToLogin(request, response);
	        return false;
    }
}