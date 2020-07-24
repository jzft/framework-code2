package com.framework.sql.generator;

import java.util.Iterator;

/**
 * date 2020/6/17 下午1:33
 *
 * @author casper
 **/
public class DefaultSqlFactory implements SqlFactory {


    private final Iterator<String> sqlIterator;

    /**
     * @param filename ddl文件完整路径
     */
    public DefaultSqlFactory(String filename, SqlScriptParser sqlScriptParser) {
        this.sqlIterator = sqlScriptParser.parse(filename);
    }

    /**
     * 下一条sql语句
     *
     * @return
     */
    @Override
    public String next() {
        return sqlIterator == null ? "" : sqlIterator.next();
    }

    /**
     * 是否还有下一条
     *
     * @return
     */
    @Override
    public boolean hasNext() {
        return sqlIterator != null && sqlIterator.hasNext();
    }
}
