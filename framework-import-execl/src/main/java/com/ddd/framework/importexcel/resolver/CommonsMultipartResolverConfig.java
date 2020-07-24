package com.ddd.framework.importexcel.resolver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@PropertySource({"classpath:/upload-config.properties"})
@Configuration
public class CommonsMultipartResolverConfig {
	@Value("${upload.file.maxUploadSize}")
	private Integer maxUploadSize;
	 
	@Value("${upload.file.maxInMemorySize}")
	private Integer maxInMemorySize;
	@Bean
	public CommonsMultipartResolver commonsMultipartResolver(){
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		/**
		 * 
		 */
		if(maxUploadSize==null||maxUploadSize==0){
			resolver.setMaxUploadSize(5242880);
		}else{
			resolver.setMaxUploadSize(maxUploadSize);
		}
		
		if(maxInMemorySize==null||maxInMemorySize==0){
			resolver.setMaxInMemorySize(4096);
		}else{
			resolver.setMaxInMemorySize(maxInMemorySize);
		}
		return resolver;
	}
}
