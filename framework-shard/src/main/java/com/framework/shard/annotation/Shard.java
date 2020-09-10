package com.framework.shard.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  * 通过shade注解的value值来指定使用那个数据源
 * 分库的数据源spring容器beanName命名规则datasource-${target}
 * 如果方法内部又有方法要切换数据源，必须同时加入@Transactional(TxType.REQUIRES_NEW)或者@Transactional(TxType.NOT_SUPPORTED)
 * @author lyq
 * @date 2020年8月14日 下午7:45:31
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Shard {
	/**
	 * 制定数据源
	 * @return
	 */
	String value() default "";
	

}
