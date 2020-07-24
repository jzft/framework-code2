package com.ddd.framework;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class EurekaMainApp {
	 @Bean
	    public ExitCodeGenerator exitCodeGenerator() {
	        return () -> 0;
	    }

	    public static void main(String[] args) {
	    	
	        SpringApplication.run(EurekaMainApp.class, args);
	    }
}