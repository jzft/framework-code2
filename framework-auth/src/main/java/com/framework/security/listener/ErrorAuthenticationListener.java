package com.framework.security.listener;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.subject.WebSubject;

import com.framework.security.constant.AuthConstant;
import com.framework.security.exception.MyAuthenticationException;
/**
 * 
 * @author lyq
 *
 */
public class ErrorAuthenticationListener implements AuthenticationListener {

	@Override
	public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * request保存业务异常信息
	 */
	@Override
	public void onFailure(AuthenticationToken token, AuthenticationException ae) {
		SecurityException a = null;
		if(ae instanceof MyAuthenticationException){
			ServletRequest request = ((WebSubject)SecurityUtils.getSubject()).getServletRequest();   
	//		HttpServletRequest request = attr.getRequest();
			request.setAttribute(AuthConstant.AUTHENTICATIONEXCEPTION,ae.getMessage());
		}
	}

	@Override
	public void onLogout(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		
	}

}
