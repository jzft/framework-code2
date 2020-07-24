package com.framework.sql.generator;

/**
 * date 2020/6/17 下午1:25
 *
 * @author casper
 **/
public interface SqlFactory {


    /**
     * 下一条sql语句
     *
     * @return
     */
    String next();

    /**
     * 是否还有下一条
     *
     * @return
     */
    boolean hasNext();


}
