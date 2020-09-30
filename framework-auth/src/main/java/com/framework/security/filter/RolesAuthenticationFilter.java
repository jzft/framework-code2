package com.framework.security.filter;

import static org.apache.shiro.util.StringUtils.split;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName:     RolesAuthenticationFilter.java
 * @Description:   身份认证过滤器
 * @author         join
 * @version        V1.0   
 * @Date           2017年1月6日 上午9:13:41
 */

public class RolesAuthenticationFilter extends org.apache.shiro.web.filter.authz.RolesAuthorizationFilter {
	//对路径长度倒序，方便更快的匹配合适的链接
		TreeMap<Integer, SortedSet<String>> treeMap = new TreeMap<>(Comparator.reverseOrder());
//	    @Override
//	    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
////	    	 response.setHeader("Access-control-Allow-Origin","*");
////	         response.setHeader("Access-Control-Allow-Methods","*");
////	         response.setHeader("Access-Control-Allow-Credentials", "true");
////	         response.setHeader("Access-Control-Allow-Headers", "*");
//	    	HttpServletResponse res = ((HttpServletResponse)response);
//	    	HttpServletRequest req = (HttpServletRequest)request;
//	    	String requestHeaders = req.getHeader("Access-Control-Request-Headers"); 
//			if (StringUtils.isEmpty(requestHeaders)) {
//				requestHeaders = "";
//			}
//	    	res.setHeader("Access-control-Allow-Origin",req.getHeader("Origin"));
////	    	res.setHeader("Access-Control-Allow-Methods",req.getMethod());
//	    	res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
//	    	res.setHeader("Access-Control-Allow-Credentials", "true");
//	    	res.setHeader("Access-Control-Allow-Headers",  "Accept, Origin, XRequestedWith, Content-Type, LastModified,"+requestHeaders);
//	    	res.setStatus(HttpServletResponse.SC_OK);
//	    	return super.preHandle(request, response);
//	    }
	
		/**
		 * 角色权限加载
		 */
//		public Filter processPathConfig(String path, String config) {
//	        String[] values = null;
//	        if (config != null) {
//	            values = split(config);
//	        }
//	        if(treeMap.get(path.length())!=null){
//	        	SortedSet<String> set = treeMap.get(path.length());
//	        	set.add(path);
//	        }else{
//	        	SortedSet<String> set = new TreeSet<>();
//	        	set.add(path);
//	        	treeMap.put(path.length(), set);
//	        }
////	        treeMap.tailMap(fromKey).lowerEntry(key)
//	        this.appliedPaths.put(path, values);
//	        
//	        return this;
//	    }
		

	    
//	    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//
//	        if (this.appliedPaths == null || this.appliedPaths.isEmpty()) {
//	            return true;
//	        }
//	        boolean bool = false;
//	        String requestURI = getPathWithinApplication(request);
//	        SortedMap<Integer, SortedSet<String>> map = this.treeMap.tailMap(requestURI.length());
//	        for(Integer uriLen:map.keySet()){
//	        	SortedSet<String> paths = map.get(uriLen);
// 	        	if(paths.size()==1){
//	        		bool = rolePathsMatch(request, response, paths.first());
//	        		if(bool){
//	        			return bool;
//	        		}
//	        	}else if(paths.size()>1){
//	        		List <String>pattarnPaths = null;
//	        		for(String path:paths){
//	        			if(StringUtils.endsWith(path, "*")){
//	        				if(pattarnPaths==null){
//	        					pattarnPaths = new ArrayList<String>();
//	        				}
//	        				pattarnPaths.add(path);
//	        			}else{
//	        				bool = rolePathsMatch(request, response, path);
//	    	        		if(bool){
//	    	        			return bool;
//	    	        		}
//	        			}
//	        		}
//	        		if(CollectionUtils.isNotEmpty(pattarnPaths)){
//	        			for(String path:paths){
//	        				bool = rolePathsMatch(request, response, path);
//	    	        		if(bool){
//	    	        			return bool;
//	    	        		}
//	        			}
//	        		}
//	        	}
//	        	
//	        }
//	        
//
//	        return true;
//	    }
	    
	    /**
	     * 去掉roles验证，角色在UriAuthorizationFilter验证就行了
	     */
		protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

//	        if (this.appliedPaths == null || this.appliedPaths.isEmpty()) {
////	            if (log.isTraceEnabled()) {
////	                log.trace("appliedPaths property is null or empty.  This Filter will passthrough immediately.");
////	            }
//	            return true;
//	        }
//
//	        for (String path : this.appliedPaths.keySet()) {
//	            // If the path does match, then pass on to the subclass implementation for specific checks
//	            //(first match 'wins'):
//	            if (pathsMatch(path, request)) {
////	                log.trace("Current requestURI matches pattern '{}'.  Determining filter chain execution...", path);
//	                Object config = this.appliedPaths.get(path);
//	                if(isFilterChainContinued2(request, response, path, config)){
//	                	 return true;
//	                }
//	               
//	            }
//	        }

	        //no path matched, allow the request to go through:
	        return true;
	    }
		private boolean rolePathsMatch(ServletRequest request, ServletResponse response, String path)
				throws Exception {
			boolean bool = false;
			if (super.pathsMatch(path, request)) {
				Object config = this.appliedPaths.get(path);
				bool = isFilterChainContinued2(request, response, path, config);
			    if(bool){
			    	return bool;
			    }
			}
			return bool;
		}
	    private boolean isFilterChainContinued2(ServletRequest request, ServletResponse response,
	                                           String path, Object pathConfig) throws Exception {

	        if (isEnabled(request, response, path, pathConfig)) { //isEnabled check added in 1.2
	            //The filter is enabled for this specific request, so delegate to subclass implementations
	            //so they can decide if the request should continue through the chain or not:
	            return onPreHandle(request, response, pathConfig);
	        }

	        //This filter is disabled for this specific request,
	        //return 'true' immediately to indicate that the filter will not process the request
	        //and let the request/response to continue through the filter chain:
	        return true;
	    }

}