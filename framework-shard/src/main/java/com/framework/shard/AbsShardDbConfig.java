package com.framework.shard;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.pagehelper.PageInterceptor;

/**
 * 
 * 如果要横向分库，spring容器注入该配置
 * @author Administrator
 *
 */
public class AbsShardDbConfig {
	
	@Autowired
	private ApplicationContext applicationContextTmp;
	@Value("${batis.basePackages}")
	String basePackages ;
	@SuppressWarnings("unchecked")
	@Bean
	@Primary
	public DynamicDataSource datashard() {
		DynamicDataSource dynamic = new DynamicDataSource();
		Map<String, ShardDataSource> targetDataSources = new HashMap<String, ShardDataSource>();
//		targetDataSources.put(TranHolder.DEFAULT, datashard0());
//		targetDataSources.put(TranHolder.T1, datashard1());
		
		Map<String, String> shardProp = shardProps();
		Map<String, String> parentProp = parentProps();
		shardProp.forEach((key,value)->{
			String target = StringUtils.substringBefore(key, ".");
			String property = StringUtils.substringAfter(key, ".");
			ShardDataSource dataSource = targetDataSources.get(target);
			if(dataSource==null){
				dataSource = new ShardDataSource();
				dataSource.setKey(target);
//				dataSource.setDefaultAutoCommit(true);
				if(applicationContextTmp.getAutowireCapableBeanFactory() instanceof SingletonBeanRegistry){
					SingletonBeanRegistry singletonBeanRegistry = (SingletonBeanRegistry)applicationContextTmp.getAutowireCapableBeanFactory();
					singletonBeanRegistry.registerSingleton("datasource-"+target, dataSource);
				}
				if(targetDataSources.isEmpty()){
					dynamic.setDefaultTargetDataSource(dataSource);
				}
				targetDataSources.put(target, dataSource);
				initDataSource(dataSource, parentProp);
				
			}
			try {
				org.apache.commons.beanutils.BeanUtils.setProperty(dataSource, property, value);
			} catch (Exception e) {
				throw new ShardDbException("数据源没有该属性："+target);
			}
		});
		dynamic.setTargetDataSources(((Map)targetDataSources));
		return dynamic;
    }
	
	@ConfigurationProperties(prefix = "shard.datasource")
    @Bean
    public Map<String, String> shardProps() {
        return new <String, String>HashMap();
    }
	
    /**
     * @return
     */
    @ConfigurationProperties(prefix = "parent.datasource")
    @Bean
    public Map<String, String> parentProps() {
        return new <String, String>HashMap();
    }

    @ConfigurationProperties(prefix = "batis")
    @Bean
    public Map<String, String> batisProps() {
        return new HashMap<>();
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
    @Bean(name = "shardSqlSessionFactory")
    public SqlSessionFactory shardSqlSessionFactory(@Qualifier("datashard") DataSource ds) throws Exception {

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

    @Bean(name = "shardSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate2(@Qualifier("shardSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 配置事务管理器(muti TransactionManagementConfigurer )
     */
    @Bean
    public PlatformTransactionManager secTransactionManager(@Qualifier("datashard") DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

}