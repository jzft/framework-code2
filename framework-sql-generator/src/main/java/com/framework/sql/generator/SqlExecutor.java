package com.framework.sql.generator;

/**
 * @author casper
 * @date 2020/6/17 上午11:43
 **/
public interface SqlExecutor {

    /**
     * 执行sql语句
     *
     * @param sql sql语句
     */
    void execute(String sql);


}
