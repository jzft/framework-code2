package com.framework.security.model;

import java.util.List;

/**
 * 
 * @ClassName:     UserBuilder.java
 * @Description:   用户登录信息实体类 
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月6日 上午9:10:33
 */
public class UserBuilder {

	/**
	 * 用户id
	 */
	private Integer id;
	/**
	 * 账号
	 */
    private String nickName;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 0：无效；1有效
     */
    private Integer status;
    
    List<Role> roles = null;
	
	public UserBuilder  id (int id){
		this.id = id;
		return this;
	}
	public UserBuilder nickName(String nickName){
    	this.nickName = nickName;
    	return this;
    }
	public UserBuilder userName(String userName){
    	this.userName = userName;
    	return this;
    }
	public UserBuilder status(Integer status){
    	this.status = status;
    	return this;
    }
	public UserBuilder roles(List<Role> roles){
    	this.roles = roles;
    	return this;
    }
	
    public User build(){
    	User user = new User();
    	user.setId(id);
    	user.setRoles(roles);
    	user.setStatus(status);
    	user.setUserName(userName);
    	user.setNickName(nickName);
    	return user;
    } 
}