package com.framework.security.model;
/**
 * 
 * @ClassName:     RoleUri.java
 * @Description:   角色权限 
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月6日 上午9:07:33
 */
public class RoleUri {
	/**
	 * 角色代码
	 */
	private String roleCode;
	/**
	 * uri对象
	 */
	private Uri uri;
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public Uri getUri() {
		return uri;
	}
	public void setUri(Uri uri) {
		this.uri = uri;
	}
	
}