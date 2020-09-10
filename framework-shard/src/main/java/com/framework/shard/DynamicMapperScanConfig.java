package com.framework.shard;

import org.springframework.context.annotation.Configuration;

import com.framework.shard.annotation.MapperScaner;

@Configuration
/**
 * 使用自定义注解，扫描 Mapper 接口并容器管理
 * @author lyq
 * @date 2020年8月14日 下午7:41:52
 */
@MapperScaner(basePackages= "${batis.basePackages}", sqlSessionTemplateRef = "shardSqlSessionTemplate")
public class DynamicMapperScanConfig {  

}  