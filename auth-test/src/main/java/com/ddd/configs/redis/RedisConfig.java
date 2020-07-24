package com.configs.redis;



import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.framework.cache.RedisHelper;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("classpath:${env}/redis.properties")
public class RedisConfig {  
  
    private static Logger logger = Logger.getLogger(RedisConfig.class);  
      
    @Bean  
    @ConfigurationProperties(prefix="spring.redis.pool")  
    public JedisPoolConfig getRedisConfig(){  
        JedisPoolConfig config = new JedisPoolConfig();  
        return config;  
    }  
      
    @Bean  
    @ConfigurationProperties(prefix="spring.redis")  
    public JedisConnectionFactory getConnectionFactory(){  
        JedisConnectionFactory factory = new JedisConnectionFactory();  
        JedisPoolConfig config = getRedisConfig(); 
        factory.setUsePool(false);
        factory.setPoolConfig(config);
        logger.info("JedisConnectionFactory bean init success.");  
        return factory;  
    }
    @Bean("redisTemplate")
    public RedisTemplate<?, ?> redisTemplate(){  
        RedisTemplate<?,?> template = new RedisTemplate(); 
        template.setConnectionFactory(getConnectionFactory());
        new RedisHelper().setRedisTemplate(template);
        return template;  
    }  
}  