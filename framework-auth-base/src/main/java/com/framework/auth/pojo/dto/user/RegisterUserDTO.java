package com.framework.auth.pojo.dto.user;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class RegisterUserDTO {
	private Integer status = 1;
	private String msg; 
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
