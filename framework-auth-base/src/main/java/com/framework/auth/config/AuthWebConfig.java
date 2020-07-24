package com.framework.auth.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebMvc
@WebFilter(filterName = "CorsFilter", urlPatterns = "/*")
public class AuthWebConfig implements WebMvcConfigurer,Filter  {

	@Value("${spring.resources.static-locations}")
	private String staticLocations;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//            registry.addInterceptor()
//    }

	 //解决跨域
    @Bean
    public CorsFilter corsFilter() {
    	
        CorsConfiguration conf = new CorsConfiguration();
        conf.addAllowedHeader("*");
        conf.addAllowedMethod("*");
        conf.addAllowedOrigin("*");
        conf.setAllowCredentials(true);
        conf.setMaxAge(3600L);
//        conf.addExposedHeader("*");
        conf.addExposedHeader("Set-Cookie");
        conf.addExposedHeader("access-control-allow-headers");
        
        conf.addExposedHeader("access-control-allow-methods");
        conf.addExposedHeader("access-control-allow-origin");
        conf.addExposedHeader("access-control-max-age");
        conf.addExposedHeader("X-Frame-Options");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf); // 4 对接口配置跨域设置
        return new CorsFilter(source);
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-control-Allow-Origin",request.getHeader("Origin"));
//    	res.setHeader("Access-Control-Allow-Methods",req.getMethod());
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
//        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    	response.setHeader("Access-Control-Expose-Headers", "content-disposition");
    	response.setHeader("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,biu-token");
    	response.setStatus(HttpServletResponse.SC_OK);
        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
        	 // 或者直接输入204、HttpStatus.SC_OK、200，等这些都可以   import org.apache.http.HttpStatus;
//        	response.setStatus(HttpStatus.OK.value());
        	return ;
        }
        filterChain.doFilter(servletRequest, response);
    }
    
   
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    	String [] paths = StringUtils.split(staticLocations,",");
//    	registry.addResourceHandler("/**").addResourceLocations(paths);
//	}
}
