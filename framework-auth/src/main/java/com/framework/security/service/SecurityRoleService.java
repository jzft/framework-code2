package com.framework.security.service;

import java.util.List;

import com.framework.security.model.Role;

public interface SecurityRoleService {
	/** 
	 * @Title:        queryRolesByUserId 
	 * @Description:  查询用户角色 
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       List<Role>    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月6日 上午9:18:45 
	 */
	public List<Role> queryRolesByUserId(Integer userId);
	
	public List<Role> getRoles();
	
}