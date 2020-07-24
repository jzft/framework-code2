package com.framework.auth.service.impl;


import com.framework.auth.service.SecurityService;
import com.framework.security.model.Role;
import com.framework.security.model.User;
import com.framework.security.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("securityUserService")
public class SecurityUserServiceImpl implements SecurityUserService{
//	private String memberHttp=ConfigContainer.getProperty("MEMEBER_REGISTER");;//PropertiesUtil.getValue("common.properties", "memberHttp");

//	@Autowired
//	RestTemplate restTemplate;
	@Autowired
SecurityService securityService;
	
	@Override
	public User findUserByUserNum(String username) {
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("dateMonth", "1");
//		try {
//			 ObjectMapper mapper = new ObjectMapper();
//		     String json = mapper.writeValueAsString(params);
//			 HttpEntity<String> formEntity = new HttpEntity<String>(json, getHeader());
//			 String result = restTemplate.postForObject(memberHttp+"/findUserByUserNum?username="+username, formEntity, String.class);
			User user = securityService.findUserByUserNum(username);
			return user;
//			if(result!=null){
//				 Map<String, Object> map = mapper.readValue(result,Map.class);
//				 if(map.get("data")!=null){
//					 Map<String, Object> umap = (Map<String, Object>) map.get("data");
//					 User user =new User();
//					 user.setId((Integer) umap.get("id"));
//					 user.setUserName(umap.get("userName")+"");
//					 user.setPwd(umap.get("pwd")+"");
//					 user.setUserNum(umap.get("userName")+"");
//					 user.setStatus((Integer) umap.get("status"));
//					 return user;
//				 }
//		     }
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}  
//		return null;
	}


	@Override
	public boolean addUserRole(User user, Role role) {
		
		return securityService.addUserRole(user.getId(), role.getId());
	}

	@Override
	public boolean delUserRole(User user, Role role) {
		// TODO Auto-generated method stub
		return securityService.delUserRole(user.getId(), role.getId());
	}

}