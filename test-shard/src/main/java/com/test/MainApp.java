/**
 * 
 */
package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.framework.spring.utils.BootPropertyUtil;


/**
 * @author lyq
 * @date 2020年8月25日 上午10:47:50 
 */

@EnableAspectJAutoProxy(exposeProxy=true)
@EnableTransactionManagement
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,DataSourceTransactionManagerAutoConfiguration.class,XADataSourceAutoConfiguration.class,TransactionAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@ComponentScan(value="com.test,com.framework")
@Configuration
@PropertySource({"application.properties","${env}/hbase-entity-class.properties","shard-db.properties"})
@EnableScheduling
public class MainApp {
	@Bean
	public BootPropertyUtil bootPropertyUtil(){
		return new BootPropertyUtil();
	}
  public static void main(String[] args) {
      SpringApplication.run(MainApp.class, args);
  }
  
  }
  