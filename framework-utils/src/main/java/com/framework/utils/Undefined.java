package com.framework.utils;


public class Undefined {

	private String msg = "未定义";
	 Undefined(){}
	 Undefined(String message){
//		 System.out.println(message);
		ClassUtils.logger.error(message);
		this.msg = message;
	}
	@Override
	public String toString(){
		return msg;
	}

}
