package com.framework.spring.utils;





import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;




/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 * 
 */
@Service
@Lazy(false)
public class SpringContextUtil implements InitializingBean {

	@Autowired
	private ApplicationContext applicationContextTmp;

	private static ApplicationContext applicationContext;

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return applicationContext;
	}

	public static String getRootRealPath() {
		String rootRealPath = "";
		try {
			rootRealPath = getApplicationContext().getResource("").getFile().getAbsolutePath();
		} catch (IOException e) {
		}
		return rootRealPath;
	}

	public static String getResourceRootRealPath() {
		String rootRealPath = "";
		try {
			rootRealPath = new DefaultResourceLoader().getResource("").getFile().getAbsolutePath();
		} catch (IOException e) {
		}
		return rootRealPath;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}
	
	public static  void setBean(String beanName,Object bean) {
		assertContextInjected();
		if(applicationContext.getAutowireCapableBeanFactory() instanceof SingletonBeanRegistry){
			SingletonBeanRegistry singletonBeanRegistry = (SingletonBeanRegistry)applicationContext.getAutowireCapableBeanFactory();
			singletonBeanRegistry.registerSingleton(beanName, bean);
		}
	}

	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContextInjected() {
		Validate.isTrue(applicationContext != null, "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
	}

	/**
	 * getSpringBean 获取SpringBean
	 * 
	 * @param <T>
	 *            type
	 * @param t
	 *            t
	 * @return <T> type
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(T t) {
		assertContextInjected();
		if (applicationContext.containsBean(t.getClass().getSimpleName())) {
			return (T) applicationContext.getBean(t.getClass().getSimpleName());
		}
		return (T) applicationContext.getBean(getBeanName(t.getClass().getSimpleName()));
	}

	/**
	 * getSpringBean 获取SpringBean
	 * 
	 * @param <T>
	 *            type
	 * @param clazz
	 *            class
	 * @param name
	 *            name
	 * @return <T> type
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz, String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name,clazz);
	}

	/**
	 * getSpringBeansByType 根据类型获取SpringBean
	 * 
	 * @param clazz
	 *            类
	 * @return String[] String[]
	 */
	public static String[] getSpringBeansByType(Class<?> clazz) {
		assertContextInjected();
		return applicationContext.getBeanNamesForType(clazz);
	}
	

	/**
	 * getSpringBeansByType 根据类型获取SpringBean
	 * 
	 * @param clazz
	 *            类
	 * @return String[] String[]
	 */
	public static <T> T getSpringBeanByType(Class<?> clazz) {
		assertContextInjected();
		String[] names = applicationContext.getBeanNamesForType(clazz);
		if(null == names || names.length == 0){
			return null;
		}
		if(StringUtils.isBlank(names[0])){
			return null;
		}

		return (T) applicationContext.getBean(names[0]);
	}

	/**
	 * getBeanName 获取Bean名字
	 * 
	 * @param className
	 *            类名
	 * @return String
	 */
	public static String getBeanName(String className) {
		assertContextInjected();
		String firstChar = className.substring(0, 1);
		return firstChar.toLowerCase() + className.substring(1);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		if (this.applicationContextTmp == null) {
		}

		SpringContextUtil.applicationContext = applicationContextTmp; // NOSONAR
	}
}