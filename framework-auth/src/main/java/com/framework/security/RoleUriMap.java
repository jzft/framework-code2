package com.framework.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cache.RedisHelper;
//import com.framework.cache.RedisHelper;
import com.framework.security.model.Uri;

/**
 * 
 * @ClassName:     RoleUriMap.java
 * @Description:   角色集合
 * @author         join
 * @version        V1.0  
 * @Date           2017年1月5日 下午4:59:09
 */
public class RoleUriMap{
	
	
	public void put(String roleCode, Map <String,Uri> value){
		RedisHelper.set(SecurityFilterUtil.REDIS_ROLE_URIS+roleCode , value,SecurityFilterUtil.REDIS_ROLE_URIS_EXPIRESECONDS );
	}
	
	/**
	 * 
	 * @Title:        put 
	 * @Description:  设置角色编码
	 * @param:        @param roleCode
	 * @param:        @param value    
	 * @return:       void    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午5:00:00
	 */
	public void put(String roleCode, Uri value){
		Map <String, Uri>map = new HashMap<String, Uri>();
		map.put(value.getUri(), value);
		RedisHelper.set(SecurityFilterUtil.REDIS_ROLE_URIS+roleCode , map,SecurityFilterUtil.REDIS_ROLE_URIS_EXPIRESECONDS );
	}
	public Map<String,Uri> get(String roleCode) throws JsonParseException, JsonMappingException, IOException{
		String uriStr = RedisHelper.get(SecurityFilterUtil.REDIS_ROLE_URIS+roleCode, String.class);
		ObjectMapper mapper = new ObjectMapper();     
		if(StringUtils.isEmpty(uriStr)){
			return null;
		}
		Map <String,Uri> uri= mapper.readValue(uriStr,new TypeReference<HashMap<String,Uri>>(){}); //json转换成map 		
		return uri;
	}
	
	public Set<String> keySet(){
		return RedisHelper.keys(SecurityFilterUtil.REDIS_ROLE_URIS+"*");
	}
	
}