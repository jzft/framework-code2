package com.framework.auth.service.impl;


import com.framework.auth.mapper.RoleEntityMapper;
import com.framework.auth.pojo.entity.RoleEntity;
import com.framework.auth.pojo.entity.RoleEntityExample;
import com.framework.auth.service.SecurityService;
import com.framework.security.model.Role;
import com.framework.security.service.SecurityRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**   
 * @ClassName:  SecurityRoleServiceImpl   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: xuaj
 * @date:   2017年11月15日 上午11:06:15   
 *     
 */
@Service("SecurityRoleService")
public class SecurityRoleServiceImpl implements SecurityRoleService{
//	private String memberHttp=ConfigContainer.getProperty("MEMEBER_REGISTER");;//=PropertiesUtil.getValue("common.properties", "memberHttp");
//	@Autowired
//	RestTemplate restTemplate;
	
	@Autowired
    SecurityService securityService ;
	@Autowired
    RoleEntityMapper roleMapper ;
	@Override
	public List<Role> queryRolesByUserId(Integer userId) {
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("dateMonth", "1");
//		try {
//			 ObjectMapper mapper = new ObjectMapper();
//		     String json = mapper.writeValueAsString(params);
//			 HttpEntity<String> formEntity = new HttpEntity<String>(json, getHeader());
//			 String result = restTemplate.postForObject(memberHttp+"/queryRolesByUserId?userId="+userId, formEntity, String.class);
			 
			List<Role> list =   securityService.queryRolesByUserId(userId);
			return list == null?new ArrayList<>():list;
//			 if(map!=null){
//				 Map<String, Object> map = mapper.readValue(result,Map.class);
//				 if(map.get("data")!=null){
//					 List<Map<String, Object>> datalist=  (List<Map<String, Object>>) map.get("data");
//					 for (Map<String, Object> l:datalist) {
//						 Role r = new Role();
//						 r.setId((Integer) l.get("id"));
//						 r.setName(l.get("name")+"");
//						 list.add(r);
//					 }
//					 return list;
//				 }
//		     }
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}  
		//return roleMapper.selectRolesByUserId(userId);
//		return null;
	}
//	public HttpHeaders getHeader(){
//   	 HttpHeaders headers = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//        headers.setContentType(type);
//        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//        return headers;
//    }
	@Override
	public List<Role> getRoles() {
		RoleEntityExample example = new RoleEntityExample();
		example.createCriteria().andDeleteFlagNotEqualTo(true);
		List<RoleEntity> entitys = roleMapper.selectByExample(example );
		List<Role> roles = entitys.stream().map(entity-> {
			Role r = new Role();
			r.setId(entity.getId());
			r.setName(entity.getName());
			r.setRoleCode(entity.getName());
			r.setStatus(entity.getDeleteFlag()?0:1);
			return r;
		}).collect(Collectors.toList());
		return roles;
	}
}