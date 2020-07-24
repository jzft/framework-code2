package com.ddd.framework.importexcel.utils;

/**
 *
 */

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created by MyElipse
 * @author huangweijin
 * Date: 2011-3-22
 * Time: 10:47:54
 * Declare：数据格式验证
 */
public class DataValidater {

	/**
	 * 判断集合为Null或Size=0
	 * @param col 集合
	 * @return boolean
	 */
	public static boolean isCollectionEmpty(Collection col){
		if(isObjNull(col)){
			return true;
		}
		if(col.size() == 0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 判断集合为Null或Size=0
	 * @param map map
	 * @return boolean
	 */
	public static boolean isMapEmpty(Map map){
		if(isObjNull(map)){
			return true;
		}
		if(map.size() == 0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 判断字符串数组为Null或Length=0
	 * @param arr 对象数组
	 * @return boolean
	 */
	public static boolean isArrEmpty(Object[] arr){
		if(isObjNull(arr)){
			return true;
		}
		if(arr.length == 0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Declare：字符串是否为整型
	 * 
	 * @param str 字符串
	 * @return boolean 是否整形
	 */
	public static boolean isStrLong(String str){
		
		if(isStrNull(str)){
			return false;
		}
		String str2 = str.trim();
		try{
			Long.parseLong(str2);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Declare：字符串是否为Null
	 * 
	 * @param str 字符串
	 * @return boolean 是否为Null
	 */
    public static boolean isStrNull(String str) {

        if (null == str) {
            return true;
        }
        return false;
    }

	/**
	 * Declare：对象是否为Null
	 * 
	 * @param obj 对象
	 * @return boolean 是否为Null
	 */
    public static boolean isObjNull(Object obj) {

        if (null == obj) {
            return true;
        }
        return false;
    }

	/**
	 * Declare：对象是否为空串（包括null,"")
	 * 
	 * @param str 字符串
	 * @return boolean 是否为空串
	 */
    public static boolean isStrEmpty(String str) {

        if (isStrNull(str)) {
            return true;
        }
        String str2 = str.trim();
        if (Constants.EMPTY_STR.equals(str2)) {
            return true;
        }

        return false;
    }

	/**
	 * Declare：字符串是否为空串（包括null,"")
	 * 
	 * @param str 字符串
	 * @return boolean 是否为空串
	 */
    public static boolean isStrEmptyOrNull(String str) {

        if (isStrNull(str)) {
            return true;
        }
        if (isStrEmpty(str)) {
            return true;
        }

        return false;
    }

	/**
	 * Declare：字符串是否为整型
	 * 
	 * @param str 字符串
	 * @return boolean 是否为整型
	 */
    public static boolean isStrInteger(String str) {

        if (isStrNull(str)) {
            return false;
        }
        String str2 = str.trim();
        try {
            Integer.parseInt(str2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
	 * Declare：字符串是否为整型
	 * 
	 * @param str 字符串
	 * @return boolean 是否为整型
	 */
    public static boolean isStrNumber(String str) {

       if(isStrLong(str)){
    	   return true;
       }
       if(isStrDouble(str)){
    	   return true;
       }
       
       return false;
    }

	/**
	 * Declare：字符串是否为大于指定长度
	 * 
	 * @param str 字符串
	 * @param len 长度
	 * @return boolean 是否大于
	 */
    public static boolean isStrLenGt(String str, int len) {
        if (isStrNull(str)) {
            return false;
        }
        if (length(str) <= len) {
            return false;
        }

        return true;
    }
    
    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     * 
     * @param value
     *            指定的字符串
     * @return 字符串的长度
     */
    public static int length(String value) {
        
        try {
			return value.getBytes(Constants.UTF_8).length;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
    }

	/**
	 * Declare：字符串是否为小于指定长度
	 * 
	 * @param str 字符串
	 * @param len 长度
	 * @return boolean 是否小于
	 */
    public static boolean isStrLenLt(String str, int len) {
        if (isStrNull(str)) {
            return false;
        }
        if (length(str) >= len) {
            return false;
        }

        return true;
    }

	/**
	 * Declare：字符串是否为等于指定长度
	 * 
	 * @param str 字符串
	 * @param len 长度
	 * @return boolean 是否等于
	 */
    public static boolean isStrLenEq(String str, int len) {
        if (isStrNull(str)) {
            return false;
        }
        if (length(str) != len) {
            return false;
        }

        return true;
    }

	/**
	 * Declare：对象是否为空或空串
	 * 
	 * @param obj 对象
	 * @return boolean 是否为空或空串
	 */
    public static boolean isObjNullOrStrEmpty(Object obj) {

        if (isObjNull(obj)) {
            return true;
        }
        if (isStrEmpty(obj.toString())) {
            return true;
        }
        return false;
    }
    
    /**
	 * Declare：字符串是否为日期时间
	 * 
	 * @param str 日期时间字符串
	 * @return boolean 是否为日期时间
	 */
    public static boolean isDatetime(String str) {

    	boolean result = false;
		try {
			DataConverter.fmtStrToDatetime(str);
			result = true;
		} catch (Exception e) {
			result = false;
		}
		
		return result;
    }
    
    /**
	 * Declare：字符串是否为日期
	 * 
	 * @param str 日期字符串
	 * @return boolean 是否为日期
	 */
    public static boolean isDay(String str) {

    	boolean result = false;
		try {
			DataConverter.fmtStrToDay(str);
			result = true;
		} catch (Exception e) {
			result = false;
		}
		
		return result;
    }
    
    public static boolean isDate(String str) {

    	if(isDatetime(str)){
    		return true;
    	}
    	if(isDay(str)){
    		return true;
    	}
    	
    	return false;
    }
    
    /**
	 * Declare：字符串是否为时间
	 * 
	 * @param str 时间字符串
	 * @return boolean 是否为时间
	 */
    public static boolean isTime(String str) {

    	boolean result = false;
		try {
			DataConverter.fmtStrToTime(str);
			result = true;
		} catch (Exception e) {
			result = false;
		}
		
		return result;
    }
	
	/**
	 * Declare：字符串是否为浮点数
	 * 
	 * @param str 字符串
	 * @return boolean 是否为浮点数
	 */
    public static boolean isStrDouble(String str) {

        if (isStrNull(str)) {
            return false;
        }
        String str2 = str.trim();
        try {
        	Double.parseDouble(str2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
	 * 检验xml文档的结构合法性
	 * @param xmlDoc
	 * @return  ture 合法；false 非法
	 * @throws SAXException 
	 */
	public static boolean validateXml(String xmlDoc){
		
		boolean result = false;
		if(DataValidater.isStrEmpty(xmlDoc)){
			return false;
		}
		StringReader read = new StringReader(xmlDoc);
		InputSource source = new InputSource(read);
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = builderFactory.newDocumentBuilder();
			builder.parse(source);
			result = true;
		} catch (Exception e) {
			result = false;
		} 
		
		return result;
	}
	
	/**
	 * 正则表达式检查
	 * @param regx 表达式
	 * @param str 字符串
	 * @return boolean
	 */
	public static boolean checkRegx(String regx, String str){
		
		boolean result = false;
		
		if(isStrEmpty(regx)){
			return true;
		}
		if(null == str){
			return false;
		}
		try{
			result = Pattern.matches(regx, str);
		}catch(Exception e){
			return false;
		}
		
		return result;
		
	}
	
	public static boolean isFalse(boolean f){
		
		return !f;
	}
	
}
