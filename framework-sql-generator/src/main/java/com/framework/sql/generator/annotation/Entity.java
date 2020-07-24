package com.framework.sql.generator.annotation;

/**
 * @author casper
 * @date 2020/6/12 下午5:20
 **/
public @interface Entity {

    /**
     * 表名称
     *
     * @return
     */
    String tableName() default "";







}
