package com.framework.security.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.subject.WebSubject;

import com.framework.security.ShiroFilterFactoryBean;
import com.framework.security.constant.AuthConstant;
import com.framework.security.model.User;

/**
 * 获取web容器对象
 * @author lyq
 *
 */
public class WebUtils {

	public static ServletRequest getRequest(){
		ServletRequest request = ((WebSubject)SecurityUtils.getSubject()).getServletRequest();
		return request;
	}
	
	public static User getUser(){
		User user = (User)SecurityUtils.getSubject().getSession().getAttribute(AuthConstant.USER);
		return user;
	}
	public static Session getSession(){
		return SecurityUtils.getSubject().getSession();
	}
	
	public static ServletResponse getResponse(){
		ServletResponse response = ((WebSubject)SecurityUtils.getSubject()).getServletResponse();
		return response;
	}
	
	public static void forward(String path) throws ServletException, IOException{
		getRequest().getRequestDispatcher(path).forward(getRequest(), getResponse());
	}
	
	public static void forwardLogin() throws ServletException, IOException{
		getRequest().getRequestDispatcher(ShiroFilterFactoryBean.instance.getLoginUrl()).forward(getRequest(), getResponse());
	}
}
