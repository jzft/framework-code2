package com.test;

//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

import com.framework.shard.annotation.MapperScaner;

//@Configuration
//扫描 Mapper 接口并容器管理
//@MapperScan(basePackages= "com.ddd.seo.dao.mapper", sqlSessionTemplateRef = "shardSqlSessionTemplate")
@MapperScaner(basePackages= "${batis.basePackages}", sqlSessionTemplateRef = "shardSqlSessionTemplate")
public class DynamicMapperScanConfig {  

}  