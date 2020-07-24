package com.ddd.framework;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableDiscoveryClient
//@SpringCloudApplication
@EnableConfigServer
@EnableEurekaClient
public class ConfigMainApp {

    @Bean
    public ExitCodeGenerator exitCodeGenerator() {
        return () -> 0;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigMainApp.class, args);
    }
}