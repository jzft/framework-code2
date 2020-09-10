package com.framework.shard.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.framework.shard.aop.TransAop;

/**
 *  用于修改aop中指定生效范围的注解；该类证实不能用，废弃，修改注解后不生效
 * @author lyq
 * @date 2020年8月14日 下午7:45:48
 */
@Deprecated
//@Component
//@PropertySource("shard-db.properties")
public class ShardAspectBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardAspectBeanPostProcessor.class);
    @Value("${shard.aop.package:execution(* replace..*(..))}")
    private String pointValue;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//    	Object object = bean;
        
    	return bean;
    }

    /**
     * 功能描述: 采用反射的方式处理该问题
     *
     * @param:
     * @return:
     * @auther: lyq
     **/
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    	System.out.println("====================="+bean.getClass().getName());
        if (bean instanceof TransAop) {
            Class beanClass = bean.getClass();
            try {
                /**
                 * 切面
                 */
                Method aroundMethod = beanClass.getDeclaredMethod("doAround",ProceedingJoinPoint.class);
                /**
                 * 切点注解
                 */
                Around pointcut = aroundMethod.getAnnotation( Around.class);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("注解原始值={}", pointcut.value());
                }

                if (Objects.isNull(pointcut)) {
                    return bean;
                }
                /**
                 * 注解使用中实质为代理类
                 */
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(pointcut);
                // 获取 AnnotationInvocationHandler 的 memberValues 字段
                Field declaredField = invocationHandler.getClass().getDeclaredField("memberValues");
                declaredField.setAccessible(true);
                Map<String, Object> valMap = (Map<String, Object>) declaredField.get(invocationHandler);
                valMap.put("value", this.pointValue);

                debug(LOGGER, () -> {
                    String s = "新值为：" + pointcut.value();
                    return s;
                });

            } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
                LOGGER.error("处理新值失败", e);
            }

        }
        return bean;
    }

    /**
     * 功能描述: TODO
     *
     * @param:
     * @return:
     * @auther: lyq
     * 
     **/
    private final void debug(Logger logger, Supplier<String> content) {

        if (logger.isDebugEnabled()) {
            logger.debug(content.get());
        }
    }
}