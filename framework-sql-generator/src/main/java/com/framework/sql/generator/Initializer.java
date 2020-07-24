package com.framework.sql.generator;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * date 2020/6/17 上午11:19
 *
 * @author casper
 **/
public class Initializer implements ApplicationListener<ContextRefreshedEvent> {

    private final SqlExecutor sqlExecutor;
    private final SqlFactory sqlFactory;

    public Initializer(SqlExecutor sqlExecutor, SqlFactory sqlFactory) {
        this.sqlExecutor = sqlExecutor;
        this.sqlFactory = sqlFactory;
    }

//    @Override
//    public void onApplicationEvent(ApplicationEvent applicationStartedEvent) {
//        while (sqlFactory.hasNext()) {
//            sqlExecutor.execute(sqlFactory.next());
//        }
//    }
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("容器中初始化Bean数量:" + event.getApplicationContext().getBeanDefinitionCount());
    }
}
