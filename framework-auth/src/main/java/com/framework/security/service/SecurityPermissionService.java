package com.framework.security.service;

import java.util.List;

import com.framework.security.model.Permission;
import com.framework.security.model.Role;

public interface SecurityPermissionService {
	/**
	 * 
	 * @Title:        queryPermissionByRoleId 
	 * @Description:  根据角色查询权限 
	 * @param:        @param roleIds
	 * @param:        @return    
	 * @return:       List<Permission>    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月6日 上午9:17:21
	 */
	public List<Permission> queryPermissionByRoleId(List<Integer> roleIds);
	
	/** 
	 * @Title:        addRolePermission 
	 * @Description:  角色添加
	 * @param:        @param role
	 * @param:        @param permission
	 * @param:        @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月6日 上午9:17:35 
	 */
	public boolean addRolePermission(Role role,Permission permission);
	
	/** 
	 * @Title:        delRolePermission 
	 * @Description:  角色删除 
	 * @param:        @param roleCode
	 * @param:        @param permissionId
	 * @param:        @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月6日 上午9:18:21 
	 */
	public boolean delRolePermission(String roleCode,Integer permissionId);
}