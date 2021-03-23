package com.framework.shard.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;


/**
 * 指定方法参数中的分库参数选择数据源
 * 如果方法内部又有方法要切换数据源，必须同时加入@Transactional(TxType.REQUIRES_NEW)或者@Transactional(TxType.NOT_SUPPORTED)
 * @author lyq
 *
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SplitShard {
	/**
	 * 分库字段位置，0为第一个
	 * @return
	 */
	int paramPosition() default 0;
	/**
	 * 分库字段属性，如果是多级属性用property1.property2表示
	 * @return
	 */
	String splitPPName() default "";
//	
//	/**
//	 * 分表字段，可不填
//	 * @return
//	 */
//	String splitTablePPName() default "";
	
}
