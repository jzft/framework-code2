package com.framework.cache.execption;

import java.io.PrintWriter;
import java.io.StringWriter;

public class RedisExecption extends RuntimeException {
private static final long serialVersionUID = 155L;
//private Logger log =  Logger.getLogger(this.getClass());
	public RedisExecption() {
	}

	public RedisExecption(String message) {
		super(message);
	}

	public RedisExecption(Throwable cause) {
		super(cause);
	}

	public RedisExecption(String message, Throwable cause) {
		super(message, cause);
	}
	
	/** 
	  * 以字符串形式返回异常堆栈信息 
	  * @param e 
	  * @return 异常堆栈信息字符串 
	  */ 
	public static String getStackTrace(Exception e) { 
	  StringWriter writer = new StringWriter(); 
	  e.printStackTrace(new PrintWriter(writer,true)); 
	  return writer.toString(); 
	} 
}

