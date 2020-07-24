package com.configs.db;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.druid.pool.DruidDataSource;
import com.framework.activiti.configs.db.AbstractActivitiDataSourceConfig;

@PropertySource("classpath:datasource/activiti.properties")
@Configuration
//public class ActivitiDataSourceConfig {
public class ActivitiDatasourceConfig extends AbstractActivitiDataSourceConfig {
	@ConfigurationProperties(prefix="activiti.datasource")
	@Bean
	public DataSource activitiDataSource() {
		DruidDataSource datasource = new  DruidDataSource();
		datasource.setDefaultAutoCommit(true);
		return datasource;
  }
}