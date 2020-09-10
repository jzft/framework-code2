package com.framework.store.hbdao.annontation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 在hbase数据库没有对应的对象
 * @author giant
 *
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Transient {

}
