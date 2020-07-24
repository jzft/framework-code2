package com.framework.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 使用正则表达式
 * 
 * @author Giant
 *
 */
public class RegUtil {

	public static final Log LOG = LogFactory.getLog(RegUtil.class);

	/**
	 * 提起str第一个ip:port 字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getIpPort(String str) {
		Pattern p = Pattern.compile("[\\d]{1,3}[\\.][\\d]{1,3}[\\.][\\d]{1,3}[\\.][\\d]{1,3}[:][\\d]{1,6}",
				Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
		return new MYMatcher<String>(p,str) {
			@Override
			public String run(Matcher m) {
				if (m.find()) {
					return m.group(0);
				}
				return null;
			}
		}.value();
	}

	/**
	 * 字符串是否ip
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIp(String str) {
		Pattern p = Pattern.compile("[\\d]{1,3}[\\.][\\d]{1,3}[\\.][\\d]{1,3}[\\.][\\d]{1,3}",
				Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
		return new MYMatcher<Boolean>(p,str) {
			@Override
			public Boolean run(Matcher m) {
				return !m.find();// 不能找到非数字，返回true
			}
		}.value();
	}

	/**
	 * 判断字符串是否正整数,if(null) return false;
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (str == null) {
			return false;
		}
		Pattern p = Pattern.compile("\\D", Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
		return new MYMatcher<Boolean>(p,str) {
			@Override
			public Boolean run(Matcher m) {
				return !m.find();// 不能找到非数字，返回true
			}
		}.value();
	}

	/**
	 * 获取正则表达式一行数据
	 * 
	 * @param str
	 * @param regex
	 * @return
	 */
	public static List<String> getRowStrs(String str, String regex) {
		List<List<String>> list = getStrss(str, regex);
		if (list == null || list.size() == 0) {
			return new ArrayList<String>();
		}
		return list.get(0);
	}

	/**
	 * 获取正则表达式一列数据
	 * 
	 * @param str
	 * @param regex
	 * @return
	 */
	public static List<String> getColStrs(String str, String regex, Integer i) {
		List<List<String>> cols = getStrss(str, regex);
		if (cols == null || cols.size() == 0) {
			return new ArrayList<String>();
		}
		List<String> result = new ArrayList<String>();
		for (List<String> col : cols) {
			result.add(col.get(i));
		}
		return result;
	}

	public static List<List<String>> getStrss(String str, String regex) {
		List<List<String>> result = new ArrayList<List<String>>();
		if (StringUtils.isEmpty(str)) {
			return result;
		}
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
		return new MYMatcher<List<List<String>>>(p,str) {

			@Override
			public List<List<String>> run(Matcher m) {
				while (m.find()) {
					List<String> list = new ArrayList<String>();
					for (int j = 0; j <= m.groupCount(); j++) {
						String cell = m.group(j);
						cell = cell == null ? "" : cell;
						list.add(cell);
					}
					result.add(list);
				}
				return result;
			}
		}.value();
	}

	/**
	 * 用正则获取网上的信息
	 * 
	 * @param str
	 * @param mode
	 *            "<span>\\s*\\n*
	 *            <hr>
	 *            \\s*\\n*\\t*<br>
	 *            ([^/]+)<br>
	 *            \\s*\\n*\\t*</div>"
	 * @return
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static String regStr(String sHtml, String mode) {
		Pattern p = Pattern.compile(mode, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
		return new MYMatcher<String>(p,sHtml) {

			@Override
			public String run(Matcher m) {
				String reg_s = null;
				if (m.find()) {
					reg_s = m.group(1);
				}
				return reg_s;
			}
		}.value();
	}
	
//	public static String regStr(String sHtml, String mode) {
//		Pattern p = Pattern.compile(mode, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
//		Matcher m = p.matcher(sHtml);
//		String reg_s = null;
//		if (m.find()) {
//			reg_s = m.group(1);
//		}
//		return reg_s;
//	}

	/**
	 * @param sHtml
	 * @param mode
	 *            正则
	 * @param i
	 *            匹配第几个组
	 * @return
	 */
	public static ArrayList<String> retrieveLinks(String sHtml, String mode, int i) {
			if ((sHtml == null) || (sHtml == ""))
				return null;
			Pattern p = Pattern.compile(mode, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
			return new MYMatcher<ArrayList<String>>(p,sHtml) {

				@Override
				public ArrayList<String> run(Matcher m) {
					ArrayList<String> linkList = new ArrayList<String>();
					while (m.find()) {
						String link = m.group(i).trim();
						linkList.add(link);
					}
					return linkList;
				}
			}.value();
			
	}

	public static ArrayList<List<String>> retrieveLinkss(String sHtml, String mode) {
		Pattern p = Pattern.compile(mode, Pattern.CASE_INSENSITIVE + Pattern.DOTALL + Pattern.MULTILINE);
			if ((sHtml == null) || (sHtml == ""))
				return null;
			return new MYMatcher<ArrayList<List<String>>>(p,sHtml) {

				@Override
				public ArrayList<List<String>> run(Matcher m) {
					ArrayList<List<String>> linkList = new ArrayList<List<String>>();
					@SuppressWarnings("unused")
					int k = 0;
					while (m.find()) {
						List<String> rows = new ArrayList<String>();
						for (int i = 0; i <= m.groupCount(); i++) {
							String link = m.group(i);
							link = link == null ? "" : link.trim();
							rows.add(link);
						}
						k++;
						linkList.add(rows);
					}
					return linkList;
				}
			}.value();
	}
	
	
	public static Map<String,String> retrieveMap(String sHtml, String mode) {
		Pattern p = Pattern.compile(mode, Pattern.CASE_INSENSITIVE + Pattern.DOTALL + Pattern.MULTILINE);
			if ((sHtml == null) || (sHtml == ""))
				return null;
			return new MYMatcher<Map<String,String>>(p,sHtml) {

				@Override
				public Map<String,String> run(Matcher m) {
					Map<String,String> rows = new HashMap<String,String>();
					@SuppressWarnings("unused")
					int k = 0;
					while (m.find()) {
						String key = m.group(1);
						String value = m.group(2);
						rows.put(key, value);
					}
					return rows;
				}
			}.value();
	}


	/**
	 * @param sHtml
	 * @param mode
	 *            正则
	 * @param i
	 *            匹配第几个组
	 * @return
	 */
	public static ArrayList<String> retrieveLinks(String sHtml, String mode, int i, Integer pattern) {
		Pattern p = Pattern.compile(mode, pattern);
			if ((sHtml == null) || (sHtml == ""))
				return null;
			return new MYMatcher<ArrayList<String>>(p,sHtml) {

				@Override
				public ArrayList<String> run(Matcher m) {
					ArrayList<String> linkList = new ArrayList<String>();
					while (m.find()) {
						String link = m.group(i).trim();
						linkList.add(link);
					}
					return linkList;
				}
			}.value();
	}

	/**
	 * 用正则获取网上的信息
	 * 
	 * @param str
	 * @param mode
	 *            "<span>\\s*\\n*
	 *            <hr>
	 *            \\s*\\n*\\t*<br>
	 *            ([^/]+)<br>
	 *            \\s*\\n*\\t*</div>"
	 * @param i
	 *            匹配第几个组
	 * @return
	 */
	public static String regStr(String sHtml, String mode, int i) {
		Pattern p = Pattern.compile(mode, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
		return new MYMatcher<String>(p,sHtml) {

			@Override
			public String run(Matcher m) {
			String reg_s = null;
			if (m.find()) {
				reg_s = m.group(i);
			}
		return reg_s;
			}
		}.value();
	}

	/**
	 * 用正则获取网上的信息
	 * 
	 * @param str
	 * @param mode
	 *            "<span>\\s*\\n*
	 *            <hr>
	 *            \\s*\\n*\\t*<br>
	 *            ([^/]+)<br>
	 *            \\s*\\n*\\t*</div>"
	 * @param i
	 *            匹配第几个组
	 * @return
	 */
	public static List<String> regStrs(String sHtml, String mode) {
		Pattern p = Pattern.compile(mode, Pattern.CASE_INSENSITIVE + Pattern.DOTALL + Pattern.MULTILINE);
		return new MYMatcher<List<String>>(p,sHtml) {

			@Override
			public List<String> run(Matcher m) {
				List<String> list = new ArrayList<>();
				if (m.find()) {
					for (int i = 0; i <= m.groupCount(); i++) {
						list.add(m.group(i));
					}
				}
				return list;
			}
		}.value();
	}
	
	/**
	 * 用正则获取网上的信息
	 * 
	 * @param str
	 * @param mode
	 *            "<span>\\s*\\n*
	 *            <hr>
	 *            \\s*\\n*\\t*<br>
	 *            ([^/]+)<br>
	 *            \\s*\\n*\\t*</div>"
	 * @param i
	 *            匹配几个组
	 * @return
	 */
	public static List<String> regStrBeforeIndex(String sHtml, String mode, int i) {
		Pattern p = Pattern.compile(mode, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
		return new MYMatcher<List<String>>(p,sHtml) {

			@Override
			public List<String> run(Matcher m) {
				List<String> reg_s = new ArrayList<String>();
				if (m.find()) {
					for (int j = 0; j <= i; j++) {
						String str = StringUtils.trimToEmpty(m.group(j));
						reg_s.add(str);
					}
				}
				return reg_s;
			}
		}.value();
	}

	/**
	 * 用正则获取网上的信息
	 * 
	 * @param str
	 * @param mode
	 *            "<span>\\s*\\n*
	 *            <hr>
	 *            \\s*\\n*\\t*<br>
	 *            ([^/]+)<br>
	 *            \\s*\\n*\\t*</div>"
	 * @param i
	 *            匹配第几个组
	 * @return
	 */
	public static String regStr(String sHtml, String mode, int i, int pattern) {
		Pattern p = Pattern.compile(mode, pattern);
		return new MYMatcher<String>(p,sHtml) {

			@Override
			public String run(Matcher m) {
				String reg_s = null;
				if (m.find()) {
					reg_s = m.group(i);
				}
				return reg_s;
			}
		}.value();
	}
	
	public static String removeLtGt(String sHtml) {
		return StringUtils.removeAll(sHtml, "<[/]?.+?>");
	}
	
	public static String remainNum(String sHtml) {
		return StringUtils.replaceAll(sHtml,"[^0-9\\.]", "");
	}

	/**
	 * 验证字符串是否符合某个正则式
	 * 
	 * @param str
	 * @param regex
	 * @return
	 */
	public static boolean regMathcher(String str, String regex) {
		Pattern p = Pattern.compile(regex);
		return new MYMatcher<Boolean>(p,str) {
			@Override
			public Boolean run(Matcher m) {
				return m.find();
			}
		}.value();
	}

	/**
	 * 验证一组表达式里，是否有符合的字符串
	 * 
	 * @param shtml
	 * @param modes
	 * @return
	 */
	public static String regStr(String shtml, List<String> modes) {
		if (modes == null || modes.isEmpty())
			return null;

		for (String s : modes) {
			String result = regStr(shtml, s, 0, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
			if (StringUtils.isNotBlank(result)) {
				return result;
			}
		}
		return null;
	}

	

}
 class InterruptibleCharSequence implements CharSequence{
	CharSequence inner;
	
	public InterruptibleCharSequence(CharSequence inner) {
		super();
		this.inner = inner;
	}
	
	@Override
	public char charAt(int index) {
		if (Thread.currentThread().isInterrupted()) {
			throw new RuntimeException("Interrupted!");
		}
		return inner.charAt(index);
	}
	
	@Override
	public int length() {
		return inner.length();
	}
	
	@Override
	public CharSequence subSequence(int start, int end) {
		return new InterruptibleCharSequence(inner.subSequence(start, end));
	}
	
	@Override
	public String toString() {
		return inner.toString();
	}
}

 abstract class MYMatcher<T>{
	private T val = null;
	MYMatcher(Pattern p,String sHtml){
		ExecutorService pool = Executors.newFixedThreadPool(1); 
		Callable<T> c1 = new Callable<T>() {
			@Override
			public T call() throws Exception {
				Matcher m = p.matcher(new InterruptibleCharSequence(sHtml));
				 return run(m);
			}
		};
		Future<T> f = pool.submit(c1);
		try {
			val = f.get(6000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			pool.shutdownNow();
		}
	}
	
	T value(){
		return val;
	}
		
	public abstract T run(Matcher m);
}