package com.framework.sql.generator;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;

/**
 * date 2020/6/17 下午2:20
 *
 * @author casper
 **/
public class DefaultSqlExecutor implements SqlExecutor {

    private final Logger log = LoggerFactory.getLogger(DefaultSqlExecutor.class);

    private final Connection connection;

    public DefaultSqlExecutor(Connection connection) {
        this.connection = connection;
    }

    /**
     * 执行sql
     *
     * @param sql sql语句或者脚本内容
     */
    @Override
    public void execute(String sql) {
        if (connection == null) {
            return;
        }
        InputStream inputStream = this.getClass().getResourceAsStream(sql);
        if (inputStream == null) {
            return;
        }
        ScriptRunner runner = new ScriptRunner(connection);
        try (InputStreamReader reader = new InputStreamReader(inputStream);) {
            runner.runScript(reader);
        } catch (FileNotFoundException e) {
            log.error("can not get script file:{}", sql);
        } catch (IOException e) {
            log.error("IOException:{}", e.getMessage());
        }
        runner.closeConnection();
    }
}
