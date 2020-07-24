package com.framework.sql.generator;

import com.framework.sql.generator.config.ConfigBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * date 2020/6/17 上午11:38
 *
 * @author casper
 **/
//@EnableConfigurationProperties(ConfigBean.class)
@Configuration
public class SqlAutoConfigProcessor {

    private final Logger log = LoggerFactory.getLogger(SqlAutoConfigProcessor.class);

    /**
     * 默认的脚本解析器
     *
     * @return
     */
    @Bean
    public SqlScriptParser sqlScriptParser() {
        return new DefaultSqlScriptParser();
    }

    /**
     * 默认的sql语句工厂
     *
     * @param configBean      配置
     * @param sqlScriptParser sql脚本解析器
     * @return
     */
    @Bean
    public SqlFactory sqlFactory(ConfigBean configBean, SqlScriptParser sqlScriptParser) {
        return new DefaultSqlFactory(configBean.getScriptFilePath(), sqlScriptParser);
    }

    /**
     * sql语句执行器
     *
     * @param dataSource 数据源
     * @return
     */
    @Bean
    public SqlExecutor sqlExecutor(@Qualifier("mainDataSource") DataSource dataSource) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            log.error("can not get any [dataSource]. Auto generate database scheme will not work.");
        }
        return new DefaultSqlExecutor(connection);
    }


    /**
     * 初始化程序
     *
     * @param sqlFactory  sql语句工厂
     * @param sqlExecutor sql语句执行器
     * @return
     */
    @Bean
    @ConditionalOnBean({SqlExecutor.class, SqlFactory.class})
    public Initializer initializer(SqlFactory sqlFactory, SqlExecutor sqlExecutor) {
        return new Initializer(sqlExecutor, sqlFactory);
    }


}
