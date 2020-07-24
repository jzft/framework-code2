package com.ddd.framework.importexcel.utils;

/**
 * 
 */

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;



/**
 * Created by MyElipse
 * @author huangweijin
 * Date: 2011-3-22
 * Time: 10:47:54
 * Declare：数据格式转换
 */
public class DataConverter {

	/**
	 * Declare：整数转为字符串
	 * 
	 * @param integer 整数
	 * @return String 字符串
	 */
	public static String IntegerToString(Integer integer){
		
		if(null == integer){
			return null;
		}
		return integer + Constants.EMPTY_STR;
	}
	
	/**
	 * Declare：长整数转为字符串
	 * 
	 * @param l 整数
	 * @return String 字符串
	 */
	public static String LongToString(Long l){
		
		if(null == l){
			return null;
		}
		return l + Constants.EMPTY_STR;
	}

	/**
	 * Declare：字符串转为整数
	 * 
	 * @param str 字符串
	 * @return Integer 整数
	 */
	public static Integer stringToInteger(String str){
		
		if(null == str){
			return null;
		}
		String str2 = str.trim();
		if(Constants.EMPTY_STR.equals(str2)){
			return null;
		}
		try{
			return Integer.parseInt(str2);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * Declare：字符串转为浮点数
	 * 
	 * @param str 字符串
	 * @return Double 浮点数
	 */
	public static Double stringToDouble(String str){
		
		if(null == str){
			return null;
		}
		String str2 = str.trim();
		if(Constants.EMPTY_STR.equals(str2)){
			return null;
		}
		try{
			return Double.valueOf(str2);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * Declare：字符串转为浮点数,可转换金额
	 * 
	 * @param str 字符串
	 * @return Double 浮点数
	 */
	public static Double stringToDoubleMoney(String str) {
		if(null == str){
			return null;
		}
		String str2 = str.trim();
		if(Constants.EMPTY_STR.equals(str2)){
			return null;
		}
		try{
			final String regex = "^(\\d{1,3})((,\\d{3})*|(\\d*))(\\.\\d+)$";
			if (Pattern.matches(regex, str2)) {
				String str3 = str2.replaceAll(Constants.DOU_SIGN, Constants.EMPTY_STR);
				return Double.valueOf(str3);
			} else {
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * Declare：字符串转为布尔类型
	 * 
	 * @param str 字符串
	 * @return Boolean 布尔类型
	 */
	public static Boolean stringToBoolean(String str){
		
		if(null == str){
			return null;
		}
		String str2 = str.trim();
		if(Constants.EMPTY_STR.equals(str2)){
			return null;
		}
		try{
			return Boolean.valueOf(str2);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * Declare：字符串转为长整数
	 * 
	 * @param str 字符串
	 * @return Long 长整数
	 */
	public static Long stringToLong(String str){
		
		if(null == str){
			return null;
		}
		String str2 = str.trim();
		if(Constants.EMPTY_STR.equals(str2)){
			return null;
		}
		try{
			return Long.parseLong(str2);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 将对象转化为字符串
	 * @param object
	 * @return  
	 */
	public static String valueOfWN(Object object){
		if(null == object){
			return null;
		}
		
		return valueOf(object);
	}
	
	/**
	 * 将对象转化为字符串
	 * @param objects 对象数组
	 * @return String
	 */
	public static String valueOfWN(Object[] objects){
		if(null == objects){
			return null;
		}
		
		return valueOf(objects);
	}
	
	/**
	 * 将对象转化为字符串
	 * @param object
	 * @return  
	 * Create by fenghanhao on 2011-3-28 下午02:46:10
	 */
	public static String valueOf(Object object){
		if(object==null){
			return Constants.EMPTY_STR;
		}else if(object instanceof Collection){
			StringBuffer buf = new StringBuffer(Constants.EMPTY_STR);
			Collection col = (Collection)object;
			for(Object subObject : col){
				buf.append(subObject);
				buf.append(Constants.DOU_SIGN);
			}
			if(col.size() > 0){
				buf.delete(buf.length()-1, buf.length());
			}
			return buf.toString();
		}else{
			return object.toString();
		}
		
	}
	
	/**
	 * 将数组转化为字符串
	 * @param objs 对象数组
	 * @return String
	 */
	public static String valueOf(Object[] objs){
		if(objs==null){
			return Constants.EMPTY_STR;
		}else{
			StringBuffer buf = new StringBuffer(Constants.EMPTY_STR);
			for(Object object : objs){
				buf.append(object);
				buf.append(Constants.DOU_SIGN);
			}
			if(objs.length > 0){
				buf.delete(buf.length()-1, buf.length());
			}
			return buf.toString();
		}
		
	}
	
	/**
	 * 格式化字符串时间
	 * 
	 * @param str 时间字符串
	 * @return Date
	 */
	public static Date fmtStrToDatetime(String str) {

		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.YYYY_MM_DD_HH_MM_SS, Locale.US);
		SimpleDateFormat dateFormat2 = new SimpleDateFormat(Constants.YYYYMMDDHHMMSS, Locale.US);
		try{
			if(str == null){
				//date = null;
			}else if(str.length() == Constants.DATE_TIME_LEN_19){
				date = dateFormat.parse(str);
			}else if(str.length() == Constants.DATE_TIME_LEN_15){
				date = dateFormat2.parse(str);
			}
		}catch(ParseException e){
			e.getMessage();
//			throw new AmwaySysException(e);
		}
		return date;
	}
	
	/**
	 * 格式化字符串时间
	 * 
	 * @param str 时间字符串
	 * @return Date
	 */
	public static Timestamp fmtStrToTimestamp(String str) {

		Date date = null;
		
		date = fmtStrToDay(str);
		if(null == date){
			date = fmtStrToDatetime(str);
		}
		if(null != date){
			return new Timestamp(date.getTime());
		}else{
			return null;
		}
		
	}
	
	/**
	 * 格式化字符串时间
	 * 
	 * @param str 时间字符串
	 * @return Date
	 */
	public static Date fmtStrToDay(String str){

		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.YYYY_MM_DD, Locale.US);
		SimpleDateFormat dateFormat2 = new SimpleDateFormat(Constants.YYYYMMDD, Locale.US);
		SimpleDateFormat dateFormat3 = new SimpleDateFormat(Constants.YYYYMMDD_X, Locale.US);
		SimpleDateFormat dateFormat4 = new SimpleDateFormat(Constants.YYYY_MM, Locale.US);
		try{
			if(str == null){
				//date = null;
			}else if(str.length() == 10){
				try{
				date = dateFormat.parse(str);
				}catch (ParseException e) {
					date = dateFormat3.parse(str);
				}
			}else if(str.length() == 8){
				date = dateFormat2.parse(str);
			}else if(str.length() == 7){
				date = dateFormat4.parse(str);
			}
		}catch(ParseException e){
			e.getMessage();
//			throw new AmwaySysException(e);
		}
		return date;
	}
	
	/**
	 * 格式化字符串时间
	 * 
	 * @param str 时间字符串
	 * @return Date
	 */
	public static Date fmtStrToTime(String str) {

		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.HH_MM_SS, Locale.US);
		SimpleDateFormat dateFormat2 = new SimpleDateFormat(Constants.HHMMSS, Locale.US);
		try{
			if(str == null){
				//date = null;
			}else if(str.length() == 8){
				date = dateFormat.parse(str);
			}else if(str.length() == 6){
				date = dateFormat2.parse(str);
			}
		}catch(ParseException e){
			throw new RuntimeException(e);
		}
		return date;
	}

	/**
	 * 格式化某种格式字符串时间为另一格式字符串时间
	 * 
	 * @param str 原时间字符串 2001-01-01 01:01:01
	 * @param format 原时间格式 yyyy-MM-dd HH:mm:ss
	 * @param pattern 新时间格式 yyyy/MM/dd
	 * @return String 新时间字符串 2001/01/01
	 */
	public static String fmtDateStrToDateStr(String str, String format, 
			String pattern) {

		String dateStr = null;
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
			Date date = dateFormat.parse(str);
			dateStr = DateFormatUtils.format(date, pattern);
		}catch(ParseException e){
			return str;
		}
		return dateStr;
	}
	
	public static String fmtDateStrToDateStr(String str, String pattern) {

		return fmtDateStrToDateStr(str, Constants.YYYY_MM_DD_HH_MM_SS, pattern);
	}
	
	/**
	 * 格式化时间为格式字符串时间
	 * 
	 * @param date 时间
	 * @param format 时间格式 yyyy-MM-dd HH:mm:ss
	 * @return String 时间字符串 2001-01-01 01:01:01
	 */
	public static String fmtDateToDateStr(Date date, String format) {
		
		String dateStr = DateFormatUtils.format(date, format);
		
		return dateStr;
	}
	
	/**
	 * 数字类型转为指定格式字符串
	 * @param obj
	 * @param format
	 * @return
	 */
	public static String fmtToNumberStr(Object obj, String format){
	
		DecimalFormat df = new DecimalFormat(format);
		String str = df.format(obj);
		
		return str;
	}
	
	/**
	 * 获得数据库当前的时间戳
	 * 
	 * @return Timestamp
	 */
	public final static String getCurDateTime() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				Constants.YYYY_MM_DD_HH_MM_SS, Locale.US);
		return simpleDateFormat.format(date);
	}

	/**
	 * 获得数据库当前的年月
	 * 
	 * @return Timestamp
	 */
	public final static String getYearMonth() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.YYYY_MM, Locale.US);
		return simpleDateFormat.format(date);
	}

	/**
	 * 获得数据库当天的时间戳
	 * 
	 * @return Timestamp
	 */
	public final static String getCurDay() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.YYYY_MM_DD, Locale.US);
		return simpleDateFormat.format(date);
	}
	/**
	 * 将使用符号隔开的字符串转化为arraylist
     * @param stringItem 用分割符分开的字符串
     * @param denotation 分隔符
	 * @return ArrayList<String>
	 */	
	public final static ArrayList<String> getItems(String stringItem,String denotation){
		StringTokenizer stringTokenizer = new StringTokenizer(stringItem,denotation);
		ArrayList<String> arrayListItem = new ArrayList<String>();
		String item;
		while(stringTokenizer.hasMoreTokens()){
		  item = stringTokenizer.nextToken();
		  arrayListItem.add(item);
		}
		return arrayListItem;
	}

	/**
	 * 替换字符串
	 * @param text 原串
	 * @param repl 被替换子串
	 * @param with 替换子串
	 * @return
	*/
	public final static String replace(String text, String repl, String with){
		return StringUtils.replace(text, repl, with);
	}
	  
	/**
	 * 根据URL取域名
	 * 
	 * @param url
	 * @return
	 */
	public final static String getDomainByUrl(String url) {

		String domain = url.split(Constants.UNIX_SEP)[2].split(Constants.MAO_SIGN)[0];

		return domain;
	}
	  
	/**
	 * 去除HTML标记
	 * 
	 * @param htmlstringIn
	 *            包括HTML的源码
	 * @return 已经去除后的文字
	 */
	public static String NoHTML(String htmlstringIn) {

		String htmlstring = htmlstringIn;
		if (htmlstring == null) {
			return null;
		}
		// 删除脚本
		final String strTmp1 = "(?s)<\\s*script[^>]*?>.*?<\\s*/script\\s*>";
		Pattern.compile(strTmp1, Pattern.CASE_INSENSITIVE).matcher(htmlstring)
				.replaceAll(Constants.EMPTY_STR);
		// 删除HTML
		final String strTmp2 = "<\\s*(br|p).*?>";
		final String strTmp3 = "[br]";
		htmlstring = Pattern.compile(strTmp2, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(strTmp3);
		final String strTmp4 = "(?s)<(.[^>]*?)>";
		htmlstring = Pattern.compile(strTmp4, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(Constants.EMPTY_STR);
		final String strTmp5 = "([\\r\\n])[\\s]+";
		htmlstring = Pattern.compile(strTmp5, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(Constants.EMPTY_STR);
		final String strTmp6 = "-->";
		htmlstring = Pattern.compile(strTmp6, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(Constants.EMPTY_STR);
		final String strTmp7 = "<!--.*";
		htmlstring = Pattern.compile(strTmp7, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(Constants.EMPTY_STR);
		final String strTmp8 = "&(quot|#34);";
		htmlstring = Pattern.compile(strTmp8, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(Constants.QUO_D_SIGN);
		final String strTmp9 = "&(amp|#38);";
		htmlstring = Pattern.compile(strTmp9, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(Constants.AND_SIGN);
		final String strTmp10 = "&(lt|#60);";
		htmlstring = Pattern.compile(strTmp10, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(Constants.LEFT_J_KUO);
		final String strTmp11 = "&(gt|#62);";
		htmlstring = Pattern.compile(strTmp11, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(Constants.RIGHT_J_KUO);
		final String strTmp12 = "&(nbsp|#160);";
		final String strTmp13 = "   ";
		htmlstring = Pattern.compile(strTmp12, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(strTmp13);
		final String strTmp14 = "&(iexcl|#161);";
		final String strTmp15 = "\\xa1";
		htmlstring = Pattern.compile(strTmp14, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(strTmp15);
		final String strTmp16 = "&(cent|#162);";
		final String strTmp17 = "\\xa2";
		htmlstring = Pattern.compile(strTmp16, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(strTmp17);
		final String strTmp18 = "&(pound|#163);";
		final String strTmp19 = "\\xa3";
		htmlstring = Pattern.compile(strTmp18, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(strTmp19);
		final String strTmp20 = "&(copy|#169);";
		final String strTmp21 = "\\xa9";
		htmlstring = Pattern.compile(strTmp20, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(strTmp21);
		final String strTmp22 = "&#(\\d+);";
		htmlstring = Pattern.compile(strTmp22, Pattern.CASE_INSENSITIVE).matcher(htmlstring).replaceAll(Constants.EMPTY_STR);
		//htmlstring = htmlstring.replaceAll("[br]", "\r\n");

		return htmlstring;
	}
	
	public static String strGbkToIso(String strGbk){
		String strIso;
		try {
			strIso = new String(strGbk.getBytes(Constants.GBK), Constants.ISO_8859_1);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return strIso;
	}
	
	public static String strGbkToUTF8(String strGbk){
		String strUtf8;
		try {
			strUtf8 = new String(strGbk.getBytes(Constants.GBK), Constants.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return strUtf8;
	}
	
	public static String encodeUTF8Str(String str){
		String strEn;
		try {
			strEn = URLEncoder.encode(str, Constants.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return strEn;
	}
	  
	/**
	 * 获得文件后缀
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String getFileExtention(String fileName) {
		String fileExtend = Constants.EMPTY_STR;
		int pos = fileName.lastIndexOf(Constants.DOT_SIGN);
		if(pos != -1){
			fileExtend = fileName.substring(pos+1);
		}
		return fileExtend;
	}
	
	/**
	 * 获得文件后缀
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String getFileName(String fileName) {
	
		String fileNameRet = fileName;
		int pos = fileName.lastIndexOf(Constants.DOT_SIGN);
		if(pos != -1){
			fileNameRet = fileName.substring(0, pos);
		}
		return fileNameRet;
	}
	
	/**
	 * 数组转为LIST集合
	 * @param array
	 * @return
	 */
	public static List<Object> arrayToList(Object[] array){
		
		if(DataValidater.isArrEmpty(array)){
			return new ArrayList<Object>();
		}else{
			return Arrays.asList(array);
		}
	}
	
	/**
	 * double数值的精度舍入计算（四舍五入）
	 * @param v  double数值
	 * @param scale  精度到第几位
	 * @return   四舍五入后的double值
	 */
	public static double round(double v,int scale){
	    if(scale<0){
	    	final String msg = "The scale must be a positive integer or zero";
	        throw new IllegalArgumentException(msg);
	    }
	    BigDecimal b = new BigDecimal(Double.toString(v));
	    BigDecimal one = new BigDecimal(Constants._1_STR);
	    return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * json格式数据转化为List<Map<String, String>>数据
	 * @param jsonText
	 * @return
	 */
	/*public static List<Map> convertJsonToListMap(String jsonText){
		
		if(DataValidater.isStrEmpty(jsonText)){
			return new ArrayList<Map>();
		}
		
		List<Map> datas = null;
		try {
			datas = (List<Map>) JSONUtil.deserialize(jsonText);
		} catch (JSONException e) {
			throw new AmwaySysException(e);
		}
		
		return datas;
	}*/
	
	/**
	 * vo实体转成map<属性,值>
	 * @param vo
	 * @return
	 */
	public static Map convertVoToMap(Object vo) {
		
		Map map = new HashMap();
		
		Field[] fields = vo.getClass().getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			if(null == name){
				continue;
			}
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(vo);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (value != null) {
				map.put(name, value);
			}
		}
		
		return map;
	}
	
	/**
	 * Collection<Map或Entity>转为List<Map>
	 * @param source
	 * @return
	 */
	public static List<Map> convertColToListMap(Collection source){
		
		List<Map> datas = new ArrayList<Map>();
		for(Object obj: source){
			Map map = null;
			if(obj instanceof Map){
				map = (Map) obj;
			}else{
				map = DataConverter.convertVoToMap(obj);
			}
			datas.add(map);
		}
		
		return datas;
	}
	
	public static String convertMapToStr(Map<String, Object> map){
		
		List<String> result = new ArrayList<String>();
		if(DataValidater.isMapEmpty(map)){
			return Constants.EMPTY_STR;
		}
		Set<Entry<String, Object>> set = map.entrySet();
		for(Entry<String, Object> entry: set){
			String key = entry.getKey();
			Object obj = entry.getValue();
			if(obj == null){
				continue;
			}
			if(obj instanceof Object[]){
				Object[] arr = (Object[])obj;
				for(Object value: arr){
					StringBuffer keyValue = new StringBuffer();
					keyValue.append(key).append(Constants.EQUAL_SIGN).append(value.toString());
					result.add(keyValue.toString());
				}
			}else{
				StringBuffer keyValue = new StringBuffer();
				keyValue.append(key).append(Constants.EQUAL_SIGN).append(obj.toString());
				result.add(keyValue.toString());
			}
		}
		
		return StringUtils.join(result, Constants.AND_SIGN);
	}
	
	public static Map<String, Object> convertStrToMap(String str){
		
		Map<String, Object> result = null;
		if(DataValidater.isStrEmpty(str)){
			return new LinkedHashMap<String, Object>();
		}
		result = new LinkedHashMap<String, Object>();
		String[] strArr = str.split(Constants.AND_SIGN);
		for(String entry: strArr){
			if(DataValidater.isStrEmpty(entry)){
				continue;
			}
			String[] keyValue = entry.split(Constants.EQUAL_SIGN);
			String key = keyValue[0];
			String value = Constants.EMPTY_STR;
			if(keyValue.length>1){
				value = keyValue[1];
			}
			if(result.containsKey(key)){
				Object obj = result.get(key);
				List<Object> list = new ArrayList<Object>(Arrays.asList(obj));
				list.add(value);
				result.put(key, list.toArray());
			}else{
				result.put(key, value);
			}
		}
		
		return result;
	}
	
	public static Object[][] convertListArrToTwoArr(List<Object[]> listArr){
		
		if(null == listArr){
			return null;
		}
		if(listArr.size() <= 0){
			return new Object[0][0];
		}
		int colLen = listArr.get(0).length;
		if(colLen <= 0){
			return new Object[0][0];
		}
		int rowLen = listArr.size();
		Object[][] objsRet = new Object[rowLen][colLen];
		for(int i=0; i<rowLen; i++){
			objsRet[i] = listArr.get(i);
		}
		
		return objsRet;
	}
	
	public static Object getObjectValue(Object obj, String fieldName){
		
		Object value = null;
		
		try{
			int index = fieldName.indexOf(".");
			while(index != -1){
				obj = getObjectValue(obj, fieldName.split(Constants.SPILT_DOT_OPER)[0]);
				fieldName = fieldName.substring(index+1);
				index = fieldName.indexOf(".");
			}
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			value = field.get(obj);
			
		}catch(Exception e){
			
			value = null;
		}
		
		return value;
	}
	
}
