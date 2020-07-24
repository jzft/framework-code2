package com.ddd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAspectJAutoProxy(exposeProxy=true)
@EnableTransactionManagement
@SpringBootApplication(exclude={
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        XADataSourceAutoConfiguration.class,
        TransactionAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class}
        ,scanBasePackages = {"com.ddd","com.framework"})
//@ComponentScan(value="com.ddd,com.framework")
@Configuration
@EnableScheduling
public class AuthMainApp {
	
	public static void main(String[] args) {
        SpringApplication.run(AuthMainApp.class, args);
    }
}