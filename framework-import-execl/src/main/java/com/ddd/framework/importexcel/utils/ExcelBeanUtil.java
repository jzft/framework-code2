package com.ddd.framework.importexcel.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

public class ExcelBeanUtil {
	
	private static final String MSG3 = "'  转化错误！！\n请确保导入excel数值类型正确！！";
	private static final String MSG2 = " Excel域值： ‘";
	private static final String MSG1 = "字段 ：";
	private static final String BAR = "-";
	private static final String COLON = ":";
	private static final String STR_EMPTY = " ";
	private static final String _002 = ":00";
	private static final String _00 = " 00";
	private static final String STR_FINAL = " final ";
	private static final String STR_FLOAT = "float";
	private static final String STR_DOUBLE = "double";
	private static final String STR_LONG = "long";
	private static final String STR_INT = "int";
	private static final String STR_INTEGER = "Integer";
	private static final String STR_STRING = "String";
	private static final String STR_DATE = "Date";
	private static final String FORMAT_CELL = " 00:00:00";
	private static final String DATE_REGEX_1 = "^[\\d][\\d][\\d][\\d]-[\\d]?[\\d]-[\\d]?[\\d]$";
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_REGEX_2 = "^(\\d{4}).(\\d{1,2}).(\\d{1,2})\\s*((\\d{1,2}):(\\d{1,2})(:(\\d{1,2}))*)*$"; 
	
	/**
	 *  设置bean 对象相对位置属性，0开始，（final field 不包括在内）
	 * author liyongqiang
	 * 2012-7-10
	 *  @param bean
	 *  @param fieldSite
	 *  @param value
	 *  @param alias
	 *  @param errorMap
	 *  @throws Exception
	 *  @see
	 *  @since
	 */
	public static void setProperty(Object bean ,int fieldSite,String value,String alias,Map<String,List<String>>errorMap) throws Exception{
		Class cla = bean.getClass();
		Field []fields = cla.getDeclaredFields();
		List<Field> list = new LinkedList<Field>();
		
		for(int i=0;i<fields.length;i++){
			//忽略final类型
			if(!isFinalField(fields[i])){
				list.add(fields[i]);
			}
		}
			int fileLength=list.size();
			if(fieldSite>=fileLength){
				throw new Exception("导入文件格式不正确");
			}
			Field field = list.get(fieldSite);
			Class type = field.getType();
			try{
				BeanUtils.setProperty(bean, field.getName(), parse(value,type));
			}catch(Exception e){ 
				alias = alias==null?field.getName():alias;
				StringBuffer keyBuffer=new StringBuffer();
				keyBuffer.append(alias).append("非法数据：");
				
				StringBuffer message=new StringBuffer();
				message.append(MSG1).append(keyBuffer).append(MSG2).append(value).append(MSG3).append(Constants.ENTER_SIGN);			
				if(errorMap == null){
					throw new Exception(message.toString());
				}else{
					List<String> errorMessage=errorMap.get(keyBuffer.toString());
					if(errorMessage == null){
						errorMessage=new ArrayList<String>();
					}
					errorMessage.add(value);
					errorMap.put(keyBuffer.toString(), errorMessage);
				}
			}
		
	}
	
	
	/**
	 * 
	 * 设置bean 对象相对位置属性，0开始，（final field 不包括在内）
	 * author liyongqiang
	 *  @param bean
	 *  @param fieldSite
	 *  @param value
	 *  @param alias
	 *  @throws Exception
	 *  @see
	 *  @since
	 */
	public static void setProperty(Object bean ,int fieldSite,String value,String alias) throws Exception{
		Class cla = bean.getClass();
		Field []fields = cla.getDeclaredFields();
		List<Field> list = new LinkedList<Field>();
		
		for(int i=0;i<fields.length;i++){
			//忽略final类型
			if(!isFinalField(fields[i])){
				list.add(fields[i]);
			}
		}
		
		Field field = list.get(fieldSite);
		Class type = field.getType();
		
		try{
			BeanUtils.setProperty(bean, field.getName(), parse(value,type));
		}catch(Exception e){ 
			alias = alias==null?field.getName():alias;
			throw new Exception(MSG1+alias+MSG2+value+MSG3+"\n");
		}
	}
	
	
	public static void setProperty(Object bean ,int fieldSite,String value) throws Exception{
		setProperty(bean,fieldSite,value,null);
	}
	  
	/**
	 * 
	* 把value转化成clz类型的格式
	 *  @author liyongqiang
	 *  @param value
	 *  @param clz
	 *  @return
	 *  @throws Exception
	 *  @see
	 *  @since
	 */
	public static Object parse(String value,Class clz) throws Exception{
		if(StringUtils.isBlank(value)||clz==null){
			return value;
		}
		String clzName = clz.getName();
		Object obj = null;
		if(clzName.endsWith(STR_DATE)){
			DateFormat format = new SimpleDateFormat(DATE_FORMAT);
			Date date = format.parse(toDateFormat(value));
			obj = date;
		}else if(clzName.endsWith(STR_STRING)){
			obj = value;
		}else if(clzName.endsWith(STR_INTEGER)
				||clzName.endsWith(STR_INT)){
			obj = Integer.parseInt(value);
		}else if(StringUtils.endsWithIgnoreCase(clzName,STR_LONG)){
			obj = Long.parseLong(value);
		}else if(StringUtils.endsWithIgnoreCase(clzName, STR_DOUBLE)){
			obj = Double.parseDouble(value);
		}else if(StringUtils.endsWithIgnoreCase(clzName, STR_FLOAT)){
			obj = Float.parseFloat(value);
		}else{
			obj = value;
		}
		return obj;
	}
	
	
	/**
	 * 判断属性是否final类型
	 * author liyongqiang
	 * 2012-7-11
	 * @param field
	 * @return
	 */
	private static boolean isFinalField(Field field){
		final Integer INDEX=-1;
		boolean flag=false;
		if(INDEX!=StringUtils.indexOf(field.toGenericString(), STR_FINAL)){
			flag=true;
		}
		return flag;
	}
	/**
	 * 保存列表
	 * author liyongqiang
	 * 2012-7-11
	 * @param service 业务类
	 * @param methodName
	 * @param list
	 * @param canSave
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void saveExcelDB(Object service,String methodName ,List list,Boolean canSave) throws Exception{
		Class clz = service.getClass();
		Method method = clz.getDeclaredMethod(methodName, List.class,Boolean.class);
		method.invoke(service, list,canSave);
	}
	
	
	/**
	 * 如果是 yyyy-MM-dd转化成 yyyy-MM-dd HH-mm-ss
	 * author liyongqiang
	 * 2012-7-10
	 * @param dateStr
	 * @return
	 */
	private static String toDateFormat(String dateStr){
		
		Pattern p = Pattern.compile(DATE_REGEX_1, Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(dateStr);
		if(matcher.find()){
			return dateStr+FORMAT_CELL;
		}else{
			p=Pattern.compile(DATE_REGEX_2, Pattern.CASE_INSENSITIVE);
			Matcher matcher2 = p.matcher(dateStr);
			if(matcher2.find()){
				dateStr = matcher2.group(1)+BAR+matcher2.group(2)+BAR+matcher2.group(3);
				dateStr = StringUtils.isBlank(matcher2.group(5))?dateStr+_00:(dateStr+STR_EMPTY+matcher2.group(5));
				dateStr = StringUtils.isBlank(matcher2.group(6))?dateStr+_002:(dateStr+COLON+matcher2.group(6));
				dateStr = StringUtils.isBlank(matcher2.group(8))?dateStr+_002:(dateStr+COLON+matcher2.group(7));			
			}
		}
		return dateStr;
	}
	
	/**
	 * 
	 *  @param list
	 *  @param fieldName
	 *  @return
	 *  @throws Exception
	 *  @see
	 *  @since
	 */
	public static Map getMapByList(List list,String fieldName) throws Exception{
		Map resultMap = new HashMap();
		for(Object bean :list){
			Object value = getValue(bean,fieldName);
			resultMap.put(value, bean);
		}
		return resultMap;
	}
	
	public static Object getValue(Object bean ,String fieldName) throws Exception{
		Object fieldValue = null;
		if( bean instanceof Map){
			Map tempMap =(Map) bean;
			fieldValue = tempMap.get(fieldName).toString();
		}else{
			String fieldValueStr = BeanUtils.getProperty(bean, fieldName);
			Class fieldType = bean.getClass().getField(fieldName).getType();
			fieldValue = parse(fieldValueStr,fieldType);
		}
		return fieldValue;
	}
	
	
	
	
	
	public static void notEmptySet(Set set,String str){
		if(StringUtils.isNotBlank(str)){
			set.add(str);
		}
	}
	
	//返回 是否为空 不为空时false
	public static boolean notEmptySet(Set set,String str,boolean flag){
		if(StringUtils.isNotBlank(str)){
			set.add(str);
			flag=false;
		}
		return flag;
	}
	
//	public static Set getColSetByKey(List<Map l>){}
	
	public static void main(String[] args) throws Exception {
//		Map map = new HashMap();
//		map.put("NO", 323);
//		List list = new ArrayList();
//		list.add(map);
//		getMapByList(list,"NO");
//		String str="2012/3/1 12:10";
//		System.out.println(toDateFormat(str));
//		Pattern p = Pattern.compile(DATE_REGEX_2, Pattern.CASE_INSENSITIVE);
//		Matcher matcher = p.matcher(str);
//		if(matcher.find()){
//			System.out.println(str);
//		}
	}
	
}
