package com.framework.security;


import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.framework.SpringContextUtil;
import com.framework.security.model.Permission;
import com.framework.security.model.Role;
import com.framework.security.model.RoleUri;
import com.framework.security.model.Uri;
import com.framework.security.service.SecurityPermissionService;
import com.framework.security.service.SecurityRoleService;
import com.framework.security.service.SecurityUserService;


/**
 * @ClassName:     RoleManager.java
 * @Description:   角色管理 
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月5日 下午4:55:18 
 */
public class RoleManager {
	

	
//	public static boolean delUserRole(Integer userId,Integer roleId) throws JsonParseException, JsonMappingException, IOException{
//		User user = new User();
//		user.setId(userId);
//		Role role = new Role();
//		role.setId(roleId);
//		Uri uriObj = SecurityFilterUtil.roleUriMap.get(roleId+"").get(uri);
//		getSecurityUserService().delUserRole(user, role);
//		return true;
//	}
	
	public static List<Role> getRoles(){
		return getSecurityRoleService().getRoles();
	}
	/**
	 * 
	 * @Title:        addRoleUri 
	 * @Description:  添加角色
	 * @param:        @param role
	 * @param:        @param permission
	 * @param:        @return    
	 * @return:       boolean    
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:55:59
	 */
	public static boolean addRoleUri(Role role,Permission permission) throws JsonParseException, JsonMappingException, IOException{
		boolean bool = getPermissionService().addRolePermission(role, permission);
		if(bool){
			RoleUri roleUri = new RoleUri();
			roleUri.setRoleCode(role.getRoleCode());
			Uri uri = new Uri();
			uri.load(permission);
			roleUri.setUri(uri);
			SecurityFilterUtil.addFilterRoleBean(roleUri );
		}else{
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @Title:        delRoleUri 
	 * @Description:  加载并拦截 
	 * @param:        @param roleCode
	 * @param:        @param uri
	 * @param:        @return    
	 * @return:       boolean    
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:57:09
	 */
	public static boolean delRoleUri(String roleCode,String uri) throws JsonParseException, JsonMappingException, IOException{
		Uri uriObj = SecurityFilterUtil.roleUriMap.get(roleCode).get(uri);
		if(uriObj==null||uriObj.getId()==null){
			return false;
		}
		Integer permissionId = Integer.parseInt(uriObj.getId());
		boolean bool = getPermissionService().delRolePermission(roleCode,permissionId);
		
		zkDelRoleUri(roleCode, uri);
		Object zkEnabled= SpringContextUtil.getBean(ZkRoleUriUpdateWatcher.class);
		if(zkEnabled!=null&&bool){
		 ZkPromulgator zkWatcher = (ZkPromulgator)SpringContextUtil.getBean(ZkPromulgator.class,"zkRoleUriUpdateWatcher");
	    	try {
	    		org.apache.shiro.subject.Subject subject = SecurityUtils.getSubject();
	    		 Object principal = subject.getPrincipal();
	    		 if(principal==null){
	    			 return false;
	    		 }
	    		 String username = principal.toString();
	    		 StringBuilder builder = new StringBuilder();
	    		 builder.append(username).append("_").append(roleCode).append("_").append(uri);
	    		//通知其他集群中的应用
				zkWatcher.publish(builder.toString());
				System.out.println("::通知其它应用修改重新加载拦截成功。");
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else{
			return false;
		}
		return true;
		
	}
	/**
	 * 
	 * @Title:        zkDelRoleUri 
	 * @Description:  删除角色
	 * @param:        @param roleCode
	 * @param:        @param uri
	 * @param:        @return    
	 * @return:       boolean    
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午4:56:37
	 */
	 static boolean zkDelRoleUri(String roleCode,String uri) throws JsonParseException, JsonMappingException, IOException{
			SecurityFilterUtil.delFilterRoleUri(roleCode, uri);
			 try {
		    	  //通过shiro拦截工厂的代理来修改工厂的重新生成拦截配置
		    	 new ShiroFilterFactoryBeanProxy(SecurityFilterUtil.getShiroFilter()).reloadFilterChainResolver();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return true;
	}
	 
	 static boolean zkDelRoleUri(String roleCode) throws JsonParseException, JsonMappingException, IOException{
			SecurityFilterUtil.delFilterRoleUri(roleCode);
			 try {
		    	  //通过shiro拦截工厂的代理来修改工厂的重新生成拦截配置
		    	 new ShiroFilterFactoryBeanProxy(SecurityFilterUtil.getShiroFilter()).reloadFilterChainResolver();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return true;
	}
	
	private static SecurityPermissionService getPermissionService(){
		return SpringContextUtil.getBean(SecurityPermissionService.class);
	}
	
	private static SecurityUserService getSecurityUserService(){
		return SpringContextUtil.getBean(SecurityUserService.class);
	}
	private static SecurityRoleService getSecurityRoleService(){
		return SpringContextUtil.getBean(SecurityRoleService.class);
	}
}