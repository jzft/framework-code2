package com.framework.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.http.HttpStatus;

import com.framework.security.SecurityFilterUtil;
import com.framework.security.ShiroFilterFactoryBean;
import com.framework.security.constant.AuthConstant;
import com.framework.security.model.Role;
import com.framework.security.model.User;
import com.google.gson.Gson;

public class UriAuthorizationFilter extends AuthorizationFilter {

	Gson gson = new Gson();
//	Set<String> authcUrls = new HashSet<String>();
	 @Override
	    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//	    	 response.setHeader("Access-control-Allow-Origin","*");
//	         response.setHeader("Access-Control-Allow-Methods","*");
//	         response.setHeader("Access-Control-Allow-Credentials", "true");
//	         response.setHeader("Access-Control-Allow-Headers0", "*");
//		 System.out.println("3==============");
//	    	HttpServletResponse res = ((HttpServletResponse)response);
//	    	HttpServletRequest req = (HttpServletRequest)request;
//	    	res.setHeader("Access-control-Allow-Origin",req.getHeader("Origin"));
////	    	res.setHeader("Access-Control-Allow-Methods",req.getMethod());
//	    	res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
//	    	res.setHeader("Access-Control-Allow-Credentials", "true");
//	    	res.setHeader("Access-Control-Expose-Headers", "content-disposition");
//	    	res.setHeader("Access-Control-Allow-Headers", "X-Requested-With");
//	    	res.setStatus(HttpStatus.OK.value());
	    	return super.preHandle(request, response);
	    }
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		Set<String> urls = SecurityFilterUtil.getFilterChainDefinitionMap().keySet();
//		if(CollectionUtils.isNotEmpty(authcUrls)){
//			for(String key:urls){
//				if(StringUtils.equalsIgnoreCase("authc", SecurityFilterUtil.getFilterChainDefinitionMap().get(key))){
//					authcUrls.add(SecurityFilterUtil.getFilterChainDefinitionMap().get(key));
//				}
//				
//			}
//		}
		 String requestURI = getPathWithinApplication(request);
		 
		 if(StringUtils.equalsIgnoreCase(ShiroFilterFactoryBean.instance.getUnauthorizedUrl(), requestURI)){
			 return true;
		 }
		 
		 if(StringUtils.equalsIgnoreCase(ShiroFilterFactoryBean.instance.getRedirectUrl(), requestURI)){
			 return true;
		 }
		 
		 if(StringUtils.equalsIgnoreCase(ShiroFilterFactoryBean.instance.getLoginUrl(), requestURI)){
			 return true;
		 }
		
		 Subject subject = SecurityUtils.getSubject();
		 if(subject.isAuthenticated()&&StringUtils.equalsIgnoreCase(ShiroFilterFactoryBean.instance.getSuccessUrl(), requestURI)){
			 return true;
		 }
		 
		if(!subject.isAuthenticated()){
			return false;
		}
		for(String path : urls){
			if(super.pathsMatch(path, requestURI)){
//				if(path.equals("/**")){
//					return true;
//				}else{
					String filterAuthc = SecurityFilterUtil.getFilterChainDefinitionMap().get(path);
					String filterRoles = StringUtils.substringBetween(filterAuthc, "[","]");
					String [] roleCodes = null;
					if(StringUtils.isNotEmpty(filterRoles)){
						roleCodes = StringUtils.split(filterRoles,"|");
					}
					User user = null;
					if(subject.getSession()!=null){
						user = (User) subject.getSession().getAttribute(AuthConstant.USER);
					}
					if(user==null){
						return false;
					}
					if(user!=null&&CollectionUtils.isEmpty(user.getRoles())){
						return false;
					}
					for(Role role : user.getRoles()){
						if(roleCodes!=null){
							if(ArrayUtils.contains(roleCodes, role.getRoleCode())){
								return true;
							}
						}
					}
//					if(StringUtils.isNotEmpty(filterRoles)){
//						return true;
//					}
				
			}
//			if(StringUtils.equalsIgnoreCase("authc", SecurityFilterUtil.getFilterChainDefinitionMap().get(path))){
//				return true;
//			}
		}
		if (isAjax(request)) {// ajax请求
			Map<String, String> resultMap = new HashMap<String, String>();
			//LoggerUtils.debug(getClass(), "当前用户没有登录，并且是Ajax请求！");
			resultMap.put("code", "503");
			resultMap.put("msg","当前用户无权限");// 当前用户没有登录！
			out(response, resultMap);
		}
		return false;
	}
	
	/**
	 * 是否是Ajax请求
	 * @param request
	 * @return
	 */
	public static boolean isAjax(ServletRequest request){
		String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
		if("XMLHttpRequest".equalsIgnoreCase(header)){
			//LoggerUtils.debug(CLAZZ, "当前请求为Ajax请求");
			return Boolean.TRUE;
		}
		//LoggerUtils.debug(CLAZZ, "当前请求非Ajax请求");
		return Boolean.FALSE;
	}
	/**
	 *  使用	response 输出JSON
	 * @param hresponse
	 * @param resultMap
	 * @throws IOException
	 */
	public void out(ServletResponse response, Map<String, String> resultMap){
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");//设置编码
			response.setContentType("application/json");//设置返回类型
			out = response.getWriter();
			out.println(gson.toJson(resultMap));//输出
		} catch (Exception e) {
			//LoggerUtils.fmtError(CLAZZ, e, "输出JSON报错。");
		}finally{
			if(null != out){
				out.flush();
				out.close();
			}
		}
	}

}