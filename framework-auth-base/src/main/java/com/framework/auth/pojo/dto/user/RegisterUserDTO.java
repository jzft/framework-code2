package com.framework.auth.pojo.dto.user;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class RegisterUserDTO {

	UserInfoDTO userInfo;
	@ApiModelProperty(name = "角色id集合")
    private List<Integer> roleIds;
	
	public UserInfoDTO getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfoDTO userInfo) {
		this.userInfo = userInfo;
	}
	public List<Integer> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}
}
