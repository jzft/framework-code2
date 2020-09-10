package com.framework.store.hbdao.common;

import java.util.List;

/**
 * hbase 对应对象属性类型
 * @author giant
 *
 */
public class FieldMeta {
	private String fieldName = null;
	private Class<?> type = null;
	private String format = null;
	private List<Class<?>> argTypes = null;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public List<Class<?>> getArgTypes() {
		return argTypes;
	}
	public void setArgTypes(List<Class<?>> argTypes) {
		this.argTypes = argTypes;
	}
}
