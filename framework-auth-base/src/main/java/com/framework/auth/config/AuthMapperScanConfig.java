package com.framework.auth.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 扫描 Mapper 接口并容器管理
 */
@Configuration
@MapperScan(basePackages = "com.framework.auth.mapper")
public class AuthMapperScanConfig {

}  