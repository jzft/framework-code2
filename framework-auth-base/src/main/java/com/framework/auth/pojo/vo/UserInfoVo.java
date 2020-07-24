package com.framework.auth.pojo.vo;


import com.framework.security.model.Role;
import com.framework.security.model.Uri;

import java.util.List;

public class UserInfoVo extends BaseVo  {

	private List<Uri> perms = null;
	private List<Role> roles = null;
	private String nick = null;

	private String name = null;
	private String uid;     // 用户id
	private String oldPwd;
	private String pwd;     // 已加密的登录密码
	private Boolean lock;   // 是否锁定

	public List<Uri> getPerms() {
		return perms;
	}
	public void setPerms(List<Uri> perms) {
		this.perms = perms;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getOldPwd() {
		return oldPwd;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public Boolean getLock() {
		return lock;
	}
	public void setLock(Boolean lock) {
		this.lock = lock;
	}
	
}
