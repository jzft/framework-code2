package com.framework.spring.utils;


import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;


/**
 * 
 * 如果需要在程序里面引用本地环境配置文件，请在spring容器注入该对象
 * @author lyq
 *
 */
@AutoConfigureOrder(1)
public class BootPropertyUtil implements InitializingBean {
	@Value("${locations}/*.properties")
	public String locations ;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		  if(StringUtils.isEmpty(locations)){
			  return ;
		  }
		  String [] locationArr = locations.split(",");
		 if( ArrayUtils.isEmpty(locationArr)){
			 return ;
		 }
		 Properties props = new Properties();
		 for(int i=0;i<locationArr.length;i++){
			 String resourcePattern = locationArr[0];
			 PathMatchingResourcePatternResolver resolover = new PathMatchingResourcePatternResolver();
				Resource[] resources = resolover.getResources(resourcePattern);
				if(ArrayUtils.isEmpty(resources)){
					continue;
				}
				for(Resource resource:resources){
					EncodedResource encodedResource = new EncodedResource(resource,"UTF-8");
					PropertiesLoaderUtils.fillProperties(props ,encodedResource);
				}
			 
//			 resources[i] = resource;
		 }
		 for (Object key : props.keySet()) {
	          String keyStr = key.toString();
	          PropertiesUtil.props.put(keyStr, props.getProperty(keyStr));
	      }
		  
	}
	
	public String getProperty(String key) {
		return PropertiesUtil.getProperty(key);
	}


	public List<Object> getPropertys(String key) {
		return PropertiesUtil.getPropertys(key);
	}
	
	public Map<String ,String> getKeyFromValues(String parentKey){
		return PropertiesUtil.getKeyFromValues(parentKey);
	}
	public static Map<String,String> keyValue(String prefix) {
		return PropertiesUtil.keyValue(prefix);
	}

}
