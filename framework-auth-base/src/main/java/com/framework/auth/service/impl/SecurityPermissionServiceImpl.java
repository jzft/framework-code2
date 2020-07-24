package com.framework.auth.service.impl;


import com.framework.auth.pojo.entity.PermissionEntity;
import com.framework.auth.pojo.entity.RoleEntity;
import com.framework.auth.service.SecurityService;
import com.framework.security.model.Permission;
import com.framework.security.model.Role;
import com.framework.security.service.SecurityPermissionService;
import com.framework.utils.NumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**   
 * @ClassName:  SecurityPermissionService   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: xuaj
 * @date:   2017年11月15日 上午11:07:24   
 *     
 */
@Service("SecurityPermissionService")
public class SecurityPermissionServiceImpl implements SecurityPermissionService{
	
//    private String memberHttp=PropertiesUtil.getValue("common.properties", "memberHttp");
	
	
//	@Autowired
//	RestTemplate restTemplate;
	@Autowired
SecurityService securityService;
	
	@Override
	public boolean addRolePermission(Role role, Permission permission) {
		
		PermissionEntity permissionEntity = new PermissionEntity();
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setCreateTime(new Date());
		roleEntity.setDeleteFlag(role.getStatus() == 0);
		roleEntity.setDescription(role.getType());
		roleEntity.setName(role.getName());
		
		permissionEntity.setCreateTime(new Date());
		permissionEntity.setDeleteFlag(false);
		permissionEntity.setPermission(permission.getName());
		permissionEntity.setUrl(permission.getUri());
		return securityService.addRolePermission(roleEntity, permissionEntity);
	}

	@Override
	public boolean delRolePermission(String permissionId, Integer roleId) {
		return securityService.delRolePermission(NumUtil.intValue(permissionId), roleId);
	}

	@Override
	public List<Permission> queryPermissionByRoleId(List<Integer> ids) {
//		List<Permission> list = new ArrayList<Permission>();
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("dateMonth", "1");
//		try {
//			 ObjectMapper mapper = new ObjectMapper();
//		     String json = mapper.writeValueAsString(params);
//			 HttpEntity<String> formEntity = new HttpEntity<String>(json, getHeader());
//			 String paramIds="";
//			 for(Integer id:ids){
//				 if(!StringUtils.isBlank(paramIds+"")){
//					 paramIds+=","+id;
//				 }else{
//					 paramIds=id+""; 
//				 }
//			 }
//			 String result = restTemplate.postForObject(memberHttp+"/queryPermissionByRoleId?ids="+paramIds, formEntity, String.class);
			 List<Permission> list = securityService.queryPermissionByRoleId(ids);
			 return list == null?new ArrayList<>():list;
//			 if(result!=null){
//				 Map<String, Object> map = mapper.readValue(result,Map.class);
//				 if(map.get("data")!=null){
//					 List<Map<String, Object>> datalist=  (List<Map<String, Object>>) map.get("data");
//					 for (Map<String, Object> l:datalist) {
//						 Permission p = new Permission();
//						 p.setId((Integer) l.get("id"));
//						 p.setName(l.get("name")+"");
//						 p.setUri(l.get("uri")+"");
//						 list.add(p);
//					 }
//					 return list;
//				 }
//		     }
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}  
//		return null;
	}
	/* public HttpHeaders getHeader(){
    	 HttpHeaders headers = new HttpHeaders();
         MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
         headers.setContentType(type);
         headers.add("Accept", MediaType.APPLICATION_JSON.toString());
         return headers;
     }*/
}