package com.framework.sql.generator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * date 2020/6/16 下午6:07
 *
 * @author casper
 **/
@Configuration
@ConfigurationProperties(prefix = "sql")
public class ConfigBean {

    /**
     * ddl文件位置
     */
    private String scriptFilePath;


    public String getScriptFilePath() {
        return scriptFilePath;
    }

    public void setScriptFilePath(String scriptFilePath) {
        this.scriptFilePath = scriptFilePath;
    }
}
