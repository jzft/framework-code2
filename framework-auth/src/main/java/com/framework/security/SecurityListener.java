package com.framework.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.framework.cache.RedisHelper;
import com.framework.security.model.RoleUri;
import com.framework.security.model.Uri;  



public class SecurityListener implements ApplicationListener<ContextRefreshedEvent>{

	@Override
	public  void onApplicationEvent(ContextRefreshedEvent event) {
		
//		if(event.getApplicationContext().getParent() == null){//root application context 娌℃湁parent锛屼粬灏辨槸鑰佸ぇ.  
		if(RedisHelper.getRedisTemplate()==null){
			return ;
		} 
		Set<String> keys = SecurityFilterUtil.roleUriMap.keySet();
		 if(CollectionUtils.isNotEmpty(keys)){
			 for(String key:keys){
				 String roleCode = StringUtils.substringAfter(key, SecurityFilterUtil.REDIS_ROLE_URIS);
				 Map<String, Uri> map = null;
				 try {
					 map = SecurityFilterUtil.roleUriMap.get(roleCode);
				 }catch (Exception e) {
						throw new RuntimeException(e);
					}
				 if(map==null){
					 return ;
				 }
				 
					Collection<Uri> urls = map.values();
					for(Uri uri:urls){
						RoleUri roleRri = new  RoleUri();
						roleRri.setRoleCode(roleCode);
//						Uri uri = map.get(url);
						roleRri.setUri(uri);
						try {
						SecurityFilterUtil.addFilterRoleBean(roleRri);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				
			 }
			 try {
		    	  //閫氳繃shiro鎷︽埅宸ュ巶鐨勪唬鐞嗘潵淇敼宸ュ巶鐨勯噸鏂扮敓鎴愭嫤鎴厤缃�
		    	 new ShiroFilterFactoryBeanProxy(SecurityFilterUtil.getShiroFilter()).reloadFilterChainResolver();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		 }
		 
//		}
		
		
	} 



}