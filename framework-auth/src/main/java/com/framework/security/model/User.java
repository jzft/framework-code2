package com.framework.security.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.framework.security.SecurityFilterUtil;

/**
 * 
 * @ClassName:     User.java
 * @Description:   用户信息实体类 
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月6日 上午9:09:42
 */
public class User implements Serializable {
		/**
		 * 用户id
		 */
		private Integer id;
		/**
		 * 别名
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
	    private static final long serialVersionUID = 1L;

	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }


	    public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getUserName() {
	        return userName;
	    }

	    public void setUserName(String userName) {
	        this.userName = userName;
	    }

	    public String getPwd() {
	        return pwd;
	    }

	    public void setPwd(String pwd) {
	        this.pwd = pwd;
	    }

	    public Integer getStatus() {
	        return status;
	    }

	    public void setStatus(Integer status) {
	        this.status = status;
	    }

		public List<Role> getRoles() {
			return roles;
		}

		public void setRoles(List<Role> roles) {
			this.roles = roles;
		}
		/**
		 * 
		 * @Title:        getRoleUris 
		 * @Description:  加载用户角色权限内的uri
		 * @param:        @return    
		 * @return:       List<Uri>    
		 * @throws IOException 
		 * @throws JsonMappingException 
		 * @throws JsonParseException 
		 * @throws 
		 * @author        join
		 * @Date          2017年1月6日 上午9:10:03
		 */
		public List<Uri> getRoleUris() throws JsonParseException, JsonMappingException, IOException{
			if(roles!=null&&roles.size()!=0){
				List<Uri> list = new ArrayList<Uri>();
				Set<String> set = new TreeSet<String>();
				for(Role role : roles){
					Map<String ,Uri> uris = SecurityFilterUtil.roleUriMap.get(role.getName());
					if(uris!=null){
						for(String key:uris.keySet()){
							if(!set.contains(key)){
								list.add(uris.get(key));
							}
							set.add(key);
						}
					}
				}
				return list;
			}
			return null;
		}

}