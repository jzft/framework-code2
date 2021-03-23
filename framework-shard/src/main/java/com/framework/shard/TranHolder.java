package com.framework.shard;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;



public class TranHolder {
//	public static final String DEFAULT = "t0";
//	public static final String T1 = "t1";
	/**
	 * 
	 * 同一个方法支持交叉使用数据源，事务控制堆栈采用后进先出策略,支持一个事件同时操作两个数据源。
	 * 支持事务类型:
	 * PROPAGATION_REQUIRES_NEW 新建事务，如果当前存在事务，把当前事务挂起。
	 * PROPAGATION_NOT_SUPPORTED 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
	 * 
	 */
	public static ThreadLocal<Stack<KeyValue<String, Object>>> shardStack = new ThreadLocal<Stack<KeyValue<String, Object>>>();
	
	
}
