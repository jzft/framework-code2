package com.framework.auth.mapper;


import com.framework.security.model.Permission;
import com.framework.security.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SecurityDao {
	 List<Role> selectRolesByUserId(@Param("userId")Integer userId);
	 List<Permission> queryPermissionByRoleId(@Param("ids") List<Integer> ids);
}
