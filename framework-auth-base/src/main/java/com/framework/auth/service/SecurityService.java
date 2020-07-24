package com.framework.auth.service;


import com.framework.security.model.Permission;
import com.framework.security.model.Role;
import com.framework.security.model.User;
import com.framework.auth.pojo.entity.PermissionEntity;
import com.framework.auth.pojo.entity.RoleEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName:  SecurityService   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: xuaj
 * @date:   2017年11月23日 上午10:21:44   
 *     
 */
@Service
public interface SecurityService {
//	boolean addRolePermission(RoleEntity role, PermissionEntity permission);
	boolean delRolePermission(Integer permissionId, Integer roleId);
	boolean addUserRole(Integer userId,Integer roleId);
	boolean delUserRole(Integer userId,Integer roleId);
	List<Permission> queryPermissionByRoleId(List<Integer> ids);
	List<Role> queryRolesByUserId(Integer userId);
	User findUserByUserNum(String username) ;
	boolean addRolePermission(RoleEntity role, PermissionEntity permission);
}