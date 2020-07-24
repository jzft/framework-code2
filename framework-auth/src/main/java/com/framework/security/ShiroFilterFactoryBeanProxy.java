package com.framework.security;

//import org.apache.ftpserver.config.spring.SpringUtil;
import org.apache.shiro.web.servlet.AbstractShiroFilter;


/**
 * 
 * @ClassName:     ShiroFilterFactoryBeanProxy.java
 * @Description:   shiro拦截处理 
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月5日 下午5:05:29
 */
public class ShiroFilterFactoryBeanProxy extends ShiroFilterFactoryBean {
	ShiroFilterFactoryBean shiroFilterFactoryBean = null;
	
	/**
	 * 
	 * @Description:  过滤器接口代理
	 * @param:        @param shiroFilterFactoryBean       
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午5:05:55
	 */
	public ShiroFilterFactoryBeanProxy(ShiroFilterFactoryBean shiroFilterFactoryBean){
		this.setLoginUrl(shiroFilterFactoryBean.getLoginUrl());
		this.setSuccessUrl(shiroFilterFactoryBean.getSuccessUrl());
		this.setUnauthorizedUrl(shiroFilterFactoryBean.getUnauthorizedUrl());
		this.setRedirectUrl(shiroFilterFactoryBean.getRedirectUrl());
		this.shiroFilterFactoryBean = shiroFilterFactoryBean;
		this.setFilterChainDefinitionMap(shiroFilterFactoryBean.getFilterChainDefinitionMap());
		this.initFilterChainDefinitionMap = shiroFilterFactoryBean.initFilterChainDefinitionMap;
		this.setSecurityManager(shiroFilterFactoryBean.getSecurityManager());
		this.setFilterClassNames(shiroFilterFactoryBean.getFilterClassNames());
//		Filter logoutFilter = SpringContextUtil.getBean(Filter.class,"logoutFilter");
//		filters.put("logout", logoutFilter);
	}
	
	/**
	 * 
	 * @Title:        reloadFilterChainResolver 
	 * @Description:  重置shiro 的路径拦截
	 * @param:        @throws Exception    
	 * @return:       void    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午5:04:57
	 */
	
	 public void reloadFilterChainResolver() throws Exception{
		AbstractShiroFilter shiroFilter = ( AbstractShiroFilter)shiroFilterFactoryBean.getObject();
//		PathMatchingFilterChainResolver resolver = (PathMatchingFilterChainResolver)shiroFilter.getFilterChainResolver();
//		DefaultFilterChainManager filterChainManger = (DefaultFilterChainManager)resolver.getFilterChainManager();
//		filterChainManger.getFilter("roles");
//		filterChainManger.addFilter(name, filter, init);
//		Map<String, NamedFilterList> filterChains = filterChainManger.getFilterChains();
//		filterChains.remove(arg0)
//		filterChainManger.getFilterChains().remove(arg0)
//		String authc = getFilterChainDefinitionMap().get("/**");
//		getFilterChainDefinitionMap().put("/**", authc);
//		getFilterChainDefinitionMap().clear();
//		getFilterChainDefinitionMap().putAll(this.initFilterChainDefinitionMap);
		AbstractShiroFilter instance = createInstance();
		//充值拦截
		shiroFilter.setFilterChainResolver(instance.getFilterChainResolver());
	 }
}