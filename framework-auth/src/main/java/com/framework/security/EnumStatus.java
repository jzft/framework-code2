package com.framework.security;
/**
 * @ClassName:     EnumStatus.java
 * @Description:   状态枚举 
 * @author         lenovo
 * @version        V1.0  
 * @Date           2017年1月5日 下午4:15:06 
 */
public enum EnumStatus {
	/**
	 * 有效
	 */
	VALID(1),
	/**
	 * 无效
	 */
	INVALID(0);
	private final int value;
	private EnumStatus(int val) {
	     this.value = val;
	}
	public int getStatus() {
	     return value;
	}
}