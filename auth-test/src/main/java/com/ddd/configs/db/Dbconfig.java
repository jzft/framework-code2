package com.configs.db;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;

@Configuration
@PropertySource("classpath:${env}/datasource.properties")
public class Dbconfig {

    /**
     * @return
     */
    @ConfigurationProperties(prefix = "parent.datasource")
    @Bean
    public Map<String, String> parentProps() {
        return new HashMap();
    }

    @ConfigurationProperties(prefix = "security.batis")
    @Bean
    public Map<String, String> batisProps() {
        return new HashMap<>();
    }

    @ConfigurationProperties(prefix = "security.datasource")
    @Bean
    public DataSource secDatashard() {
        DataSource datasource = new DruidDataSource();
        Map<String, String> props = parentProps();
        initDataSource(datasource, props);
        return datasource;
    }


    private void initDataSource(DataSource datasource, Map<String, String> props) {
        Set<String> keys = props.keySet();
        for (String key : keys) {
//			datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
            try {
                org.apache.commons.beanutils.BeanUtils.setProperty(datasource, key, props.get(key));
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean(name = "secSqlSessionFactory")
    public SqlSessionFactory secSqlSessionFactory(@Qualifier("secDatashard") DataSource ds) throws Exception {

        Map<String, String> map = batisProps();
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(ds);// 指定数据源(这个必须有，否则报错)
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        fb.setTypeAliasesPackage(map.get("typeAliasesPackage"));// 指定基包
        fb.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources(map.get("mapperLocations")));//
        Interceptor[] plugins = new Interceptor[1];
        plugins[0] = new PageInterceptor();
        //helperDialect 会根据  url 去选择
        Properties properties = new Properties();
        plugins[0].setProperties(properties);
        fb.setPlugins(plugins);
        return fb.getObject();
    }

    @Bean(name = "secSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate2(@Qualifier("secSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 配置事务管理器(muti TransactionManagementConfigurer )
     */
    @Bean
    public PlatformTransactionManager secTransactionManager(@Qualifier("secDatashard") DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("mainDataSource")
    public DataSource mainDataSource(@Qualifier("secDatashard") DataSource dataSource) {
        return dataSource;
    }


}