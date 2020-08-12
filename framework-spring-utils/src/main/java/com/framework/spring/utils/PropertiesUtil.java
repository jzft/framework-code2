package com.framework.spring.utils;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

/**
 * 获取属性文件的的值
 * 
 * @author Giant
 * 
 */
public class PropertiesUtil {

	static Map<String, String> props = new HashMap<String, String>();

	public static String getProperty(String key) {
		return props.get(key) == null ? "" : props.get(key);
	}


	public static List<Object> getPropertys(String key) {
		List<Object> value = new ArrayList<Object>();
		// 是否为模糊搜索
		List<String> keyList = new ArrayList<String>();
		TreeSet<String> treeSet = (TreeSet<String>) keySet();
		for (String string : treeSet) {
			// 通过排序后,key是有序的.
			if (string.indexOf(key) != -1) {
				keyList.add(string);
				value.add(props.get(string));
			} else if (string.indexOf(key) == -1 && keyList.size() == 0) {
				// 当不包含这个key时而且key.size()等于0时,说明还没找到对应的key的开始
				continue;
			} else {
				// 当不包含这个key时而且key.size()大于0时,说明对应的key到当前这个key已经结束.不必要在往下找
				break;
			}
		}
		keyList.clear();
		keyList = null;
		return value;
	}
	
	public static Map<String ,String> getKeyFromValues(String parentKey){
		Map<String ,String> map = new HashMap<String, String>();
		// 是否为模糊搜索
		List<String> keyList = new ArrayList<String>();
		TreeSet<String> treeSet = (TreeSet<String>) keySet();
		for (String string : treeSet) {
			// 通过排序后,key是有序的.
			if (string.indexOf(parentKey) != -1) {
				keyList.add(string);
				map.put(props.get(string), string);
			} else if (string.indexOf(parentKey) == -1 && keyList.size() == 0) {
				// 当不包含这个key时而且key.size()等于0时,说明还没找到对应的key的开始
				continue;
			} else {
				// 当不包含这个key时而且key.size()大于0时,说明对应的key到当前这个key已经结束.不必要在往下找
				break;
			}
		}
		keyList.clear();
		keyList = null;
		return map;
	}
	
	private static Set<String> keySet() {
		Set<String> set = props.keySet();
		TreeSet<String> tSet = null;
		if (set != null) {
			// 对已存在的key进行排序
			tSet = new TreeSet<String>(set);
		}
		return tSet;
	}
	
	public static Map<String,String> keyValue(String prefix) {
		Set<String> set = props.keySet();
		Map<String,String> map = new HashMap<String,String>();
		if (set == null) {
			return null;
		}
		prefix = prefix+".";
		for(String key:set){
			
			if(key.indexOf(prefix)!=-1){
				map.put(StringUtils.substringAfter(key, prefix), props.get(key));
			}
		}
		return map;
	}

}