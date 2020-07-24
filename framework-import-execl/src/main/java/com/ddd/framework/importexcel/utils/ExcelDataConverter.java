
/*
 * 文件名: DataValidater.java
 * 版权: 
 * 描述:
 * 修改人: Zhuang Shao Bin
 * 修改时间: 2012-6-27
 * 修改内容: 
 */
package com.ddd.framework.importexcel.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;


/**
 * @author Zhuang Shao Bin
 * @version
 * @see
 * @since 
 */
public class ExcelDataConverter extends DataConverter {
	
	public final static int DEFAULT_INCOUNT=500;
	
	public final static String DEFAULT_FORMAT="yyyy-MM-dd";
	
	
	/**
	 *  返回 to_date(?,format)
	 *  @author Zhuang Shao Bin
	 *  @version 2012-7-25
	 *  @param format
	 *  @return
	 *  @see
	 *  @since
	 */
	public static String getSqlToDate(String format){
		StringBuffer buffer=new StringBuffer();
		buffer.append(" to_date(?, '").
		append(format).append("') ");
		return buffer.toString();
	}
	
	/**
	 *   to_date(?,'yyyy-MM-dd')
	 *  @author Zhuang Shao Bin
	 *  @version 2012-7-25
	 *  @return
	 *  @see
	 *  @since
	 */
	public static String getSqlToDate(){
		return getSqlToDate(DEFAULT_FORMAT);
	}

	/**
	 * 加入 -01
	 *  @author Zhuang Shao Bin
	 *  @version 2012-8-2
	 *  @param month
	 *  @return
	 *  @see
	 *  @since
	 */
	public static String strDateJoin(String month){
		final String suffix="-01";
		return month+suffix;
	}
	
	/**
	 *  
	 *  @author Zhuang Shao Bin
	 *  @version 2012-7-20
	 *  @param list
	 *  @return
	 *  @see
	 *  @since
	 */
	public static List<Long> toLongList(List list){
		List<Long> longList=new ArrayList<Long>();
		if(list != null){
			for(Object obj:list){
				if(!DataValidater.isObjNull(obj)){
					longList.add(stringToLong(obj.toString()));
				}
			}
		}
		return longList;
	}
	
	
	/**
	 *  
	 *  @author Zhuang Shao Bin
	 *  @version 2012-7-23
	 *  @param array
	 *  @return
	 *  @see
	 *  @since
	 */
	public static String arrayToString(Object []array){
		String strKey=Arrays.toString(array);
		strKey=StringUtils.substringBetween(strKey, Constants.LEFT_Z_KUO, Constants.RIGHT_Z_KUO);	
		return strKey;
	}
	
	
	/**
	 *  返回 in 条件 字符串
	 *  @author Zhuang Shao Bin
	 *  @version 2012-7-23
	 *  @param ids
	 *  @param columName
	 *  @return
	 *  @see
	 *  @since
	 */
	public static String getInColumnCondStr(String ids,String columName){
		return getInColumnCondStr(ids, columName,false);
	}
	
	/**
	 *  返回 in 条件 字符串 
	 *  @author Zhuang Shao Bin
	 *  @version 2012-7-17
	 *  @param ids
	 *  @param columName
	 *  @param withAnd
	 *  @return
	 *  @see
	 *  @since
	 */
	public static String getInColumnCondStr(String ids,String columName,boolean withAnd){
		final String AND=" and ";
		StringBuffer buffer=new StringBuffer();
		List codeList=ExcelDataConverter.getItems(ids,Constants.DOU_SIGN);
		if(DataValidater.isCollectionEmpty(codeList)){
			return null;
		}
		if(withAnd){
			buffer.append(AND);
		}
		buffer.append(ExcelDataConverter.inStringSql(codeList,columName));
		return buffer.toString();
	}
	
	/**
	 * 返回 in sql 如果 codeList的值 大于500个时会加入 or in()
	 * (non-javadoc)
	 * @seeinStringSql
	 * @author zsb
	 * @date Dec 9, 2011 4:13:32 PM
	 *  @版本 V 1.0
	 * @param codeList
	 * @param columnName
	 * @return
	 */
	public static String inStringSql(List<Object>codeList,String columnName){
		
		final String OR="or";
		final String IN=" in ";
		
    	StringBuffer codeStB=new StringBuffer();
    	List<String> strList=listToString(codeList,DEFAULT_INCOUNT);
    	for(String str:strList){
    		//codeStB.append(Constants.EMPTY_ONE_STR).append(columnName+" in ( "+str+" ) ").append(OR);  
    		codeStB.append(Constants.EMPTY_ONE_STR).append(columnName).append(IN).
    		append(Constants.LEFT_X_KUO)
    		.append(str).append(Constants.RIGHT_X_KUO).append(Constants.EMPTY_ONE_STR).append(OR); 
    	}
    	if( StringUtils.endsWith(codeStB.toString(),OR)){
    		codeStB=new StringBuffer(StringUtils.substringBeforeLast(codeStB.toString(),OR));
    	}
    	return codeStB.toString();
    }	
	
	/**
	 * 
	 *  @author Zhuang Shao Bin
	 *  @version 2012-7-23
	 *  @param list
	 *  @param divideCount
	 *  @return
	 *  @see
	 *  @since
	 */
	public static List<String> listToString(List list,int divideCount){
		List<String> divideList=new ArrayList<String>();
		List list2=null;
		if(list != null){
			int listSize=list.size();
			int length=listSize/divideCount;
			int otherPartLength=listSize-divideCount*length;
			if(length == 0){
				divideList.add(listToString(list));
			}else{
				for(int j=0;j<length;j++){
					list2=new ArrayList<Integer>();
					for(int i=0;i<divideCount;i++){
						int index=j*divideCount+i;  // ---下标位置;	
						list2.add(list.get(index));
					}
					divideList.add(listToString(list2));					
				}
				if(otherPartLength>0){
					list2=new ArrayList<Integer>();
					for(int j=0;j<otherPartLength;j++){
						int index=length*divideCount+j;
						list2.add(list.get(index));
					}
					divideList.add(listToString(list2));	
				}
			}
		}
		return divideList;
	}
	
	
	 /**
	   * list 转为String 与 逗号分开
	   * @author zsb
	   * @date Sep 8, 2010 3:31:57 PM
	   *  @版本 V 1.0
	   *  @param list
	   *  @return
	   */
	  public static String listToString(List list) {
			String str = null;
			if (list != null && !list.isEmpty()) {
				str = Arrays.toString(list.toArray());
			}
			if (StringUtils.isNotBlank(str)) {
				str = StringUtils.substringBetween(str, Constants.LEFT_Z_KUO, Constants.RIGHT_Z_KUO);
			}
			return str;
	 }
	  
	 
	  /**
	   *  字符串 转为时间
	   *  @author Zhuang Shao Bin
	   *  @version 2012-7-11
	   *  @param dateFormat
	   *  @param dateStr
	   *  @return
	   *  @see
	   *  @since
	   */
	  public static Date fmtStrToDate(String dateFormat, String dateStr) {
			DateFormat df = null;
			if (dateFormat == null){
				df = new SimpleDateFormat(DEFAULT_FORMAT);
			}else{
				df = new SimpleDateFormat(dateFormat);
			}
			try {
				return df.parse(dateStr);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
	  
	  /**
	   * 取list中bean属性propertyNames做键值
	   * author liyongqiang
	   * 2012-7-3
	   * @param beanList
	   * @param propertyNames pname1_pname2
	   * @return
	 * @throws Exception 
	   */
	  public static Map beanListToMap(List beanList,String propertyNames) throws Exception{
		 String []pnames = StringUtils.split(propertyNames,Constants.UNDER_SEP);
		 Map beanMap = null;
		 if(beanList!=null){
		   beanMap = new LinkedMap();
		 }
		  for(Object bean :beanList){
			  StringBuffer pvals = new StringBuffer();
			  for(String pname: pnames){
				  pvals.append(BeanUtils.getProperty(bean, pname)) ;
				  pvals.append(Constants.UNDER_SEP);
			  }
			 String key = StringUtils.removeEnd(pvals.toString(), Constants.UNDER_SEP);
			  beanMap.put(key, bean);
		  }
		  return beanMap;
	  }
	 
	  /**
	   * 
	   *  @author Zhuang Shao Bin
	   *  @version 2012-7-23
	   *  @param list
	   *  @param bacthCount
	   *  @return
	   *  @see
	   *  @since
	   */
	public static List<String> divideList(List<String> list, int bacthCount) {
		int listSize = list.size();
		int count = listSize/bacthCount;
		if (listSize % bacthCount > 0) {
			count ++;
		}
		List<String> divideList = new ArrayList<String>(count);  
		for (int i = 0; i < count; i++) {
			if (i == count - 1) {
				divideList.add(StringUtils.join(list.subList(i*bacthCount, i*bacthCount + listSize % bacthCount), Constants.DOU_SIGN));
			} else {
				divideList.add(StringUtils.join(list.subList(i*bacthCount, (i+1)*bacthCount),Constants.DOU_SIGN));
			}
		}
		return divideList;
	}
	  
	public static void main(String[] args) {
//		String ids="aa,bb,cc";
//		List list=PraiseDataConverter.getItems(ids,Constants.DOU_SIGN);
//		System.err.println(list);
		
//		List<Integer> list = new ArrayList<Integer>(3889);
//		for (int i = 0; i < 4754; i++) {
//			list.add(i);
//		}
//		int bacthCount = 1000;
//		int listSize = list.size();
//		int count = listSize/bacthCount;
//		if (listSize % bacthCount > 0) {
//			count ++;
//		}
//		List<String> divideList = new ArrayList<String>(count);
//		for (int i = 0; i < count; i++) {
//			if (i == count - 1) {
//				divideList.add(StringUtils.join(list.subList(i*bacthCount, i*bacthCount + listSize % bacthCount), ","));
//			} else {
//				divideList.add(StringUtils.join(list.subList(i*bacthCount, (i+1)*bacthCount), ","));
//			}
//		}
	}
}
