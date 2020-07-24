package com.framework.activiti.configs.web;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAutoConfiguration(exclude = 
{ 
		org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class,
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class})
@Configuration
public class ActivitiWebConfig implements WebMvcConfigurer {
	@Override
	  public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    	registry.addResourceHandler("modeler.html").addResourceLocations("classpath:activiti/");
	    	registry.addResourceHandler("/diagram-viewer/**").addResourceLocations("classpath:activiti/diagram-viewer/");
	    	registry.addResourceHandler("/editor-app/**").addResourceLocations("classpath:activiti/editor-app/");
		}
}
