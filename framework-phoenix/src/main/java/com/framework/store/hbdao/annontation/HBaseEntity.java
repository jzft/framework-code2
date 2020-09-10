package com.framework.store.hbdao.annontation;


import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * hbase列定义注解
 *
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface HBaseEntity {

	String name() default "";
	String defaultFamily() default "";
}
