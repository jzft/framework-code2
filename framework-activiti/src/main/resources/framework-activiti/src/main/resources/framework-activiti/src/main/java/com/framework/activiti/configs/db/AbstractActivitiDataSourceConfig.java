package com.framework.activiti.configs.db;



import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
 

public abstract class AbstractActivitiDataSourceConfig extends AbstractProcessEngineAutoConfiguration {

    public abstract DataSource activitiDataSource() ;
    @Bean
    public PlatformTransactionManager activititransactionManager() {
        return new DataSourceTransactionManager(activitiDataSource());
    }

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(SpringAsyncExecutor springAsyncExecutor) throws IOException {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(activitiDataSource());
        configuration.setJobExecutorActivate(true);
        configuration.setTransactionManager(activititransactionManager());
        configuration.setTablePrefixIsSchema(true);
        configuration.setDbIdentityUsed(false);
//        configuration.setpro
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        configuration.setDatabaseSchema("ACTIVITI");
//        //设置自动建表
//        //创建一个流程引擎对象，在创建流程引擎对象时会自动建表
//        ProcessEngine engine= configuration.buildProcessEngine();
//    	 return super.baseSpringProcessEngineConfiguration(activitiDataSource(), activititransactionManager(), springAsyncExecutor);
        return configuration;
    }


}
