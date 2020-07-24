package com.framework.auth.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:/shiro-config.properties","classpath:${env}/my-shiro-config.properties"})
@ImportResource(locations= {"classpath:/spring-shiro-context.xml"})
public class AuthShiroConfig {

}
