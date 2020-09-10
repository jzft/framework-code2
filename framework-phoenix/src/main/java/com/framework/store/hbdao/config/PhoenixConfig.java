/**
 * 
 */
package com.framework.store.hbdao.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author lyq
 * @date 2020年9月1日 上午10:44:38 
 */
@Configuration
public class PhoenixConfig {
	
	@Bean
	@ConfigurationProperties(prefix = "phoenix.datasource")
	protected BasicDataSource phoenixDataSource(){
		BasicDataSource phoenixDataSource = new BasicDataSource();
		return phoenixDataSource;
	}
	@Bean
	protected JdbcTemplate phoenixJdbcTemplete(){
		JdbcTemplate phoenixJdbcTemplete = new JdbcTemplate(phoenixDataSource());
		return phoenixJdbcTemplete;
	}
}
