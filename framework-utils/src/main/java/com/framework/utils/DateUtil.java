package com.framework.utils;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/***
 * 
 * @author yong_jiang
 * 
 *         日期类公共方法
 *
 */
public class DateUtil {

	/**
	 * 两个日期相差多少天
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {
	       Calendar aCalendar = Calendar.getInstance();
	       aCalendar.setTime(fDate);
	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       aCalendar.setTime(oDate);
	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       return day2 - day1;
	}
	/**
	 * 两个日期相差多少天
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int daysOfTwo(String fDateStr, String oDateStr) {
			Date fDate = null;
			Date oDate = null;
			try {
				fDate = stringToDate3(fDateStr);
				oDate = stringToDate3(oDateStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
	       Calendar aCalendar = Calendar.getInstance();
	       aCalendar.setTime(fDate);
	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       aCalendar.setTime(oDate);
	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
	       return day2 - day1;
	}
	/**
	 * 获取n年后今天开始时间
	 * 
	 * 年/月/日 0:00:00
	 * 
	 */

	public static Date getNYearDateBeginTime(int n) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd 0:00:00");
		Calendar curr = Calendar.getInstance();
		curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + n);
		Date date = curr.getTime();
		String str = formatter.format(date);
		try {
			date = formatter.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}

	/**
	 * 获取当天开始时间
	 * 
	 * 年/月/日 0:00:00
	 * 
	 */

	public static Date getDateBeginTime() {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd 0:00:00" + "");

		String str = formatter.format(new Date());
//		System.out.println(str);
		try {
			date = formatter.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}

	/**
	 * 计算指定日期的偏移日期
	 * 
	 * @param selectDate
	 * @param offsetDay
	 * @return
	 */
	public static Date calDate(Date selectDate, int offsetDay) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(selectDate);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + offsetDay);
		return calendar.getTime();

	}

	/***
	 * 转换成date类型格式,yyyy-MM-dd HH:mm
	 * 
	 * @param dataStr
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String dataStr) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.parse(dataStr);
	}
	
	public static Date stringToDate(String dataStr,String format) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.parse(dataStr);
	}

	/**
	 * yyyy-MM-dd HH:mm
	 * 
	 * @param dataStr
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate2(String dataStr) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return df.parse(dataStr);
	}

	/**
	 * yyyy-MM-dd
	 * 
	 * @param dataStr
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate3(String dataStr) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.parse(dataStr);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dataStr
	 * @return
	 */
	public static Date stringToDate4(String dataStr) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(dataStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dataStr
	 * @return
	 * @throws Exception
	 */
	public static Date stringToDateYMD(String dataStr) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(dataStr);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * yyyy-MM-dd HH:mm
	 * 
	 * @param date
	 * @return
	 */
	public static String datetoString2(Date date) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return df.format(date);
		}
		return "";
	}

	/**
	 * yyyy-MM-dd HH:mm:ss date类型转化为指定的字符串类型格式
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.format(date);
		}
		return "";
	}
	
	public static String dateToString(Date date,String format) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(format);
			return df.format(date);
		}
		return "";
	}


	/**
	 * yyyy-MM-dd HH:mm:ss.SSS date类型转化为带毫秒的指定的字符串类型格式
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStringWithSSS(Date date) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			return df.format(date);
		}
		return "";
	}

	/**
	 * date类型转化为指定的字符串类型格式,yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString3(Date date) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.format(date);
		}
		return "";
	}

	/**
	 * date类型转化为指定的字符串类型格式,HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString2(Date date) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			return df.format(date);
		}
		return "";
	}

	/**
	 * date类型转化为指定的字符串类型格式,yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static Date dateToDateYYYYMMDD(Date date) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return df.parse(df.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * date类型转化为指定的字符串类型格式,HH:mm:dd
	 * 
	 * @param date
	 * @return
	 */
	public static Date dateToDateHHMMDD(Date date) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:dd");
			try {
				return df.parse(df.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String dateToStringYYYYMMDD(Date date) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.format(date);
		}
		return "";
	}

	public static String dateToStringYYYY(Date date) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			return df.format(date);
		}
		return "";
	}

	/**
	 * 查询2个日期是否同1天
	 * 
	 * @param t
	 * @param t2
	 * @return
	 */
	public static boolean IsSameDay(Timestamp t, Timestamp t2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(t);
		int y = cal.get(Calendar.YEAR);
		int d = cal.get(Calendar.DAY_OF_YEAR);

		cal.setTime(t2);
		int y2 = cal.get(Calendar.YEAR);
		int d2 = cal.get(Calendar.DAY_OF_YEAR);

		return (y == y2) && (d == d2);
	}

	/** 获取当前时间的Timestamp值 */
	public static Timestamp NowTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 获取当前时间(注意:返回的是String 已经格式化好的格式)
	 * 
	 * @return
	 */
	public static String getNowTime() {
		Date nowDate = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = formatter.format(now.getTime());
		return str;
	}
	
	/**
	 * 获取当前时间(注意:返回的是String 已经格式化好的格式)
	 * 
	 * @return
	 */
	public static String getNowHour() {
		Date nowDate = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowDate);
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		String str = formatter.format(now.getTime());
		return str;
	}

	/**
	 * 获取当前时间(注意:返回的是String 已经格式化好的格式)
	 * 
	 * @return
	 */
	public static String getNowTimeFormat() {
		Date nowDate = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = formatter.format(now.getTime());
		return str;
	}

	public static String getNowTimeFormat(Date nowDate) {
		if (nowDate == null)
			nowDate = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = formatter.format(now.getTime());
		return str;
	}

	public static String getTimeFormat(Date nowDate) {
		if (nowDate == null)
			nowDate = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		String str = formatter.format(now.getTime());
		return str;
	}

	public static long getTimeInMillis(String sDate, String eDate) {
		Timestamp sd = Timestamp.valueOf(sDate);
		Timestamp ed = Timestamp.valueOf(eDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sd);
		long timethis = calendar.getTimeInMillis();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(ed);
		long timeend = calendar2.getTimeInMillis();
		long thedaymillis = timeend - timethis;
		return thedaymillis;
	}

	/**
	 * HH:mm:ss格式化的时间
	 * 
	 * @param dTime
	 * @return
	 */
	public static String formatTime(String dTime) {
		String dateTime = "";
		if (dTime != null && !"".equals(dTime)) {
			Timestamp t = Timestamp.valueOf(dTime);
			SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
			dateTime = formatter.format(t);
		}
		return dateTime;
	}

	/**
	 * 强制转化为时间格式
	 * 
	 * @param strDate
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parses(String strDate, String pattern) throws ParseException {
		return new SimpleDateFormat(pattern).parse(strDate);
	}

	public static Date parses(Date date, String pattern) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.parse(formatter.format(date));
	}

	/**
	 * 当前日期是第几周
	 * 
	 * @return
	 */
	public static String getWeekOfYear() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String week = calendar.get(Calendar.WEEK_OF_YEAR) + "";
		return week;
	}

	/**
	 * 返回毫秒
	 * 
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	public static String getTimeInMillis(Date sDate, Date eDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);
		long timethis = calendar.getTimeInMillis();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(eDate);
		long timeend = calendar2.getTimeInMillis();
		long thedaymillis = timeend - timethis;
		return thedaymillis < 1000 ? thedaymillis + "毫秒!" : (thedaymillis / 1000) + "秒钟!";
	}

	/**
	 * 获取第i月之后的时间
	 * 
	 * @param ts
	 * @param i
	 * @return
	 */
	public static String getNextDate(String ts, int i) {
		Calendar now = Calendar.getInstance();
		Timestamp t = Timestamp.valueOf(ts + " 00:00:00.000");
		now.setTime(t);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		now.add(Calendar.DAY_OF_MONTH, +(i));
		String dt = formatter.format(now.getTime());
		return dt;
	}

	/**
	 * 获取第i分钟之后的时间
	 * 
	 * @param ts
	 * @param i
	 * @return
	 */
	public static String getNextTime(String ts, int i) {
		Calendar now = Calendar.getInstance();
		Timestamp t = Timestamp.valueOf(ts);
		now.setTime(t);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		now.add(Calendar.MINUTE, +(i));
		String dt = formatter.format(now.getTime());
		return dt;
	}

	/***
	 * 取Unix时间戳
	 * 
	 * @param dateTime
	 * @return
	 */
	public static long getUnixTime(String dateTime) {
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
			date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 08:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long l = (date1.getTime() - date2.getTime()) / 1000;
		return l;
	}

	/***
	 * 当前月1日
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date thisMonth1st(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/***
	 * 下月1日
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date nextMonth1st(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/***
	 * 上月1日
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date lastMonth1st(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/***
	 * 当前月最后1日
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date thisMonthLastDay(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(nextMonth1st(d));
		cal.set(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	/***
	 * 日期分钟加减
	 * 
	 * @param date
	 * @param addMonths
	 * @return
	 */
	public static Date addMinutes(Date date, int addMinutes) {

		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + addMinutes);

		return calendar.getTime();
	}

	/***
	 * 日期日份加减
	 * 
	 * @param date
	 * @param addMonths
	 * @return
	 */
	public static Date addDay(Date date, int addDays) {

		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + addDays);

		return calendar.getTime();
	}

	/***
	 * 日期月份加减
	 * 
	 * @param date
	 * @param addMonths
	 * @return
	 */
	public static Date addMonth(Date date, int addMonths) {

		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + addMonths);

		return calendar.getTime();
	}

	/***
	 * 日期年份加减
	 * 
	 * @param date
	 * @param addMonths
	 * @return
	 */
	public static Date addYear(Date date, int addYears) {

		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + addYears);

		return calendar.getTime();
	}

	public static Date defautDate() throws ParseException {
		return DateUtil.stringToDate3("0001-01-01");
	}

	public static Integer getDaysOfTheMonth(Date date) {// 获取当月天数
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date); // 要计算你想要的月份，改变这里即可
		int days = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);

		return days;
	}

	/**
	 * date类型转化为指定的字符串类型格式
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString4(Date date) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			return df.format(date);
		}
		return "";
	}

	public static String formatDay(Date startTime, String string) {
		if (startTime != null) {
			SimpleDateFormat df = new SimpleDateFormat(string);
			return df.format(startTime);
		}
		return "";
	}

	/**
	 * 获取时间字符串的小时与分钟
	 * 
	 * @param timeStr
	 * @return string HH:mm
	 */
	public static String getHHmm(String timeStr) {
		if (timeStr == null) {
			return null;
		}
		if (timeStr.length() >= 12) {
			timeStr = timeStr.substring(8, 10) + ":" + timeStr.substring(10, 12);
		} else {
			return null;
		}
		return timeStr;

	}
	


}
