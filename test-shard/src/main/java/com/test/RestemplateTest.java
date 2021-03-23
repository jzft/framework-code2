package com.test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RestemplateTest {

	private static String url = "http://127.0.0.1:8084";
	public static void main(String[] args) {
		RestemplateTest test = new RestemplateTest();
		test.test();
	}
	
	
	
	
	private void test(){
		RestTemplate restTemplate = restTemplate(simpleClientHttpRequestFactory());
		restTemplate.postForEntity(url+"/test/test", null, null);
		System.out.println();
	}
	
	
	
	
	 @Bean
	   // @SessionScope
	    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
		    RestTemplate restTemplate = new RestTemplate(factory);
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			for (HttpMessageConverter<?> messageConverter : messageConverters) {
				if (messageConverter instanceof StringHttpMessageConverter) {
					((StringHttpMessageConverter) messageConverter).setDefaultCharset(Charset.forName("UTF-8"));
				}
			}
	        return restTemplate;
	    }

	    @Bean
	    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
	        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
	        factory.setReadTimeout(300000);//单位为ms
	        factory.setConnectTimeout(10000);//单位为ms
	        return factory;
	    }

}
