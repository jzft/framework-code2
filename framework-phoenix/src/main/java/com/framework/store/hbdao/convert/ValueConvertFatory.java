package com.framework.store.hbdao.convert;


import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

public final class ValueConvertFatory {
	/**
	 * 
	 * 将值转换指定类型
	 * @author lyq
	 * @date 2020年8月31日 下午6:34:32 
	 * @param clazz
	 * @param value
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Object string2Object(String clazz, String value, String format) throws Exception{
		
		if(StringUtils.isBlank(value)) return null;
		
		if(clazz.equalsIgnoreCase("class java.lang.String")){
			return value;
		}else if(clazz.equalsIgnoreCase("class java.lang.Integer") || clazz.equalsIgnoreCase("int")){
			return Integer.valueOf(value);
		}else if(clazz.equalsIgnoreCase("class java.lang.Short") || clazz.equalsIgnoreCase("short")){
			return Short.valueOf(value);
		}else if(clazz.equalsIgnoreCase("class java.lang.Long") || clazz.equalsIgnoreCase("long")){
			return java.lang.Long.valueOf(value);
		}else if(clazz.equalsIgnoreCase("class java.lang.Double") || clazz.equalsIgnoreCase("double")){
			return java.lang.Double.valueOf(value);
		}else if(clazz.equalsIgnoreCase("class java.lang.Float") || clazz.equalsIgnoreCase("float")){
			return java.lang.Float.valueOf(value);
		}else if(clazz.equalsIgnoreCase("class java.lang.Boolean") || clazz.equalsIgnoreCase("boolean")){
			return java.lang.Boolean.valueOf(value);
		}else if(clazz.equalsIgnoreCase("class java.util.Date")){
			if(StringUtils.isBlank(format))
				format = "yyyy-MM-dd HH:mm:ss";
			return DateUtils.parseDate(value, format);
		}else if(clazz.equalsIgnoreCase("class java.math.BigDecimal")){
			return java.math.BigDecimal.valueOf(java.lang.Double.valueOf(value));
		}
		return null;
	}
	
	/**
	 * 将值转换指定类型
	 * @param clazz
	 * @param value
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String object2String(String clazz, Object value, String format) throws Exception{
		
		if(value == null) return null;
		
		if(clazz.equalsIgnoreCase("class java.lang.String")){
			return value.toString();
		}else if(clazz.equalsIgnoreCase("class java.lang.Integer") || clazz.equalsIgnoreCase("int")){
			return value.toString();
		}else if(clazz.equalsIgnoreCase("class java.lang.Short") || clazz.equalsIgnoreCase("short")){
			return value.toString();
		}else if(clazz.equalsIgnoreCase("class java.lang.Long") || clazz.equalsIgnoreCase("long")){
			return value.toString();
		}else if(clazz.equalsIgnoreCase("class java.lang.Double") || clazz.equalsIgnoreCase("double")){
			return value.toString();
		}else if(clazz.equalsIgnoreCase("class java.lang.Float") || clazz.equalsIgnoreCase("float")){
			return value.toString();
		}else if(clazz.equalsIgnoreCase("class java.lang.Boolean") || clazz.equalsIgnoreCase("boolean")){
			return value.toString();
		}else if(clazz.equalsIgnoreCase("class java.util.Date")){
			if(StringUtils.isBlank(format))
				format = "yyyy-MM-dd HH:mm:ss";
			return DateFormatUtils.format((Date)value, format);
		}else if(clazz.equalsIgnoreCase("class java.math.BigDecimal")){
			return value.toString();
		}
		return null;
	}
}
