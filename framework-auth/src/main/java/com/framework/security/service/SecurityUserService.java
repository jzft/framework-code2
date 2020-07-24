package com.framework.security.service;

import com.framework.security.model.Role;
import com.framework.security.model.User;


public interface SecurityUserService {
	
	/** 
	 * @Title:        findUserByUserNum 
	 * @Description:  查询用户代码（这里一定要查询密码出来校验，才能验证密码。请悉知）
	 * @param:        @param name
	 * @param:        @return    
	 * @return:       User    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月6日 上午9:19:22 
	 */
	public User findUserByUserNum(String name);
	
	public boolean addUserRole(User user,Role role);
	
	public boolean delUserRole(User user,Role role);
	
}