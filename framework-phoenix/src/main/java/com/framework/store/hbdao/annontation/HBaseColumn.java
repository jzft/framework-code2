package com.framework.store.hbdao.annontation;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * hbase列定义注解
 *
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface HBaseColumn {

	String family() default "";
	
	String qualifier() default "";
	
	String format() default "";
	
	boolean forceSave() default false;
	
}
