package com.school.biz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;

import com.school.biz.exception.UtilException;

/**
 * 时间工具类
 */
public class DateUtil {
	/** 毫秒 */
	public final static long MS = 1;
	/** 每秒钟的毫秒数 */
	public final static long SECOND_MS = MS * 1000;
	/** 每分钟的毫秒数 */
	public final static long MINUTE_MS = SECOND_MS * 60;
	/** 每小时的毫秒数 */
	public final static long HOUR_MS = MINUTE_MS * 60;
	/** 每天的毫秒数 */
	public final static long DAY_MS = HOUR_MS * 24;

	/** 标准日期格式 */
	public final static String NORM_DATE_PATTERN = "yyyy-MM-dd";
	/** 标准时间格式 */
	public final static String NORM_TIME_PATTERN = "HH:mm:ss";
	/** 标准日期时间格式，精确到分 */
	public final static String NORM_DATETIME_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	/** 标准日期时间格式，精确到秒 */
	public final static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** 标准日期时间格式，精确到毫秒 */
	public final static String NORM_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	/** HTTP头中日期时间格式 */
	public final static String HTTP_DATETIME_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
	/** 上传文件文件名日期格式*/
	public final static String FILE_DATETIME_PATTERN = "yyyyMMddHHmmssSSS";

	/** 标准日期（不含时间）格式化器 */
//	private final static SimpleDateFormat NORM_DATE_FORMAT = new SimpleDateFormat(NORM_DATE_PATTERN);
	private static ThreadLocal<SimpleDateFormat> NORM_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(NORM_DATE_PATTERN);
		};
	};
	/** 标准时间格式化器 */
//	private final static SimpleDateFormat NORM_TIME_FORMAT = new SimpleDateFormat(NORM_TIME_PATTERN);
	private static ThreadLocal<SimpleDateFormat> NORM_TIME_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(NORM_TIME_PATTERN);
		};
	};
	/** 标准日期时间格式化器 */
//	private final static SimpleDateFormat NORM_DATETIME_FORMAT = new SimpleDateFormat(NORM_DATETIME_PATTERN);
	private static ThreadLocal<SimpleDateFormat> NORM_DATETIME_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(NORM_DATETIME_PATTERN);
		};
	};
	/** HTTP日期时间格式化器 */
//	private final static SimpleDateFormat HTTP_DATETIME_FORMAT = new SimpleDateFormat(HTTP_DATETIME_PATTERN, Locale.US);
	private static ThreadLocal<SimpleDateFormat> HTTP_DATETIME_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(HTTP_DATETIME_PATTERN, Locale.US);
		};
	};

	/** 上传文件文件名日期格式化器*/
	private static ThreadLocal<SimpleDateFormat> FILE_DATETIME_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(FILE_DATETIME_PATTERN);
		};
	};
	
	/**
	 * 当前时间，格式 yyyy-MM-dd HH:mm:ss
	 * @return 当前时间的标准形式字符串
	 */
	public static String now() {
		return formatDateTime(new DateTime());
	}

	/**
	 * 当前日期，格式 yyyy-MM-dd
	 * @return 当前日期的标准形式字符串
	 */
	public static String today() {
		return formatDate(new DateTime());
	}

	/**
	 * @return 当前时间
	 */
	public static DateTime date() {
		return new DateTime();
	}

	/**
	 * Long类型时间转为Date
	 * @param date Long类型Date（Unix时间戳）
	 * @return 时间对象
	 */
	public static DateTime date(long date) {
		return new DateTime(date);
	}

	/**
	 * 获得指定日期年份和季节<br>
	 * 格式：[20131]表示2013年第一季度
	 * @param date 日期
	 * @return Season ，类似于 20132
	 */
	public static String yearAndSeason(Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return yearAndSeason(cal);
	}

	/**
	 * 获得指定日期区间内的年份和季节<br>
	 * @param startDate 其实日期（包含）
	 * @param endDate 结束日期（包含）
	 * @return Season列表 ，元素类似于 20132
	 */
	public static LinkedHashSet<String> yearAndSeasons(Date startDate, Date endDate) {
		final LinkedHashSet<String> seasons = new LinkedHashSet<String>();
		if(startDate == null || endDate == null) {
			return seasons;
		}


		final Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		while(true) {
			//如果开始时间超出结束时间，让结束时间为开始时间，处理完后结束循环
			if(startDate.after(endDate)) {
				startDate = endDate;
			}

			seasons.add(yearAndSeason(cal));

			if(startDate.equals(endDate)) {
				break;
			}

			cal.add(Calendar.MONTH, 3);
			startDate = cal.getTime();
		}

		return seasons;
	}
	// ------------------------------------ Format start ----------------------------------------------
	/**
	 * 根据特定格式格式化日期
	 * @param date 被格式化的日期
	 * @param format 格式
	 * @return 格式化后的字符串
	 */
	public static String format(Date date, String format){
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 格式 yyyy-MM-dd HH:mm:ss
	 * @param date 被格式化的日期
	 * @return 格式化后的日期
	 */
	public static String formatDateTime(Date date) {
		return NORM_DATETIME_FORMAT.get().format(date);
	}

	/**
	 * 格式 yyyy-MM-dd
	 * @param date 被格式化的日期
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date) {
		return NORM_DATE_FORMAT.get().format(date);
	}

	/**
	 * 格式化为Http的标准日期格式
	 * @param date 被格式化的日期
	 * @return HTTP标准形式日期字符串
	 */
	public static String formatHttpDate(Date date) {
		return HTTP_DATETIME_FORMAT.get().format(date);
	}
	
	/**
	 * 格式化为上传文件文件名的日期格式
	 * @param date 被格式化的日期
	 * @return 上传文件文件名日期字符串
	 */
	public static String formatFileDatetime(Date date){
		return FILE_DATETIME_FORMAT.get().format(date);
	}
	// ------------------------------------ Format end ----------------------------------------------

	// ------------------------------------ Parse start ----------------------------------------------

	/**
	 * 构建DateTime对象
	 * @param dateStr Date字符串
	 * @param simpleDateFormat 格式化器
	 * @return DateTime对象
	 */
	public static DateTime parse(String dateStr, SimpleDateFormat simpleDateFormat) {
		try {
			return new DateTime(simpleDateFormat.parse(dateStr));
		} catch (Exception e) {
			throw new UtilException(StrUtil.format("Parse [{}] with format [{}] error!", dateStr, simpleDateFormat.toPattern()), e);
		}
	}

	/**
	 * 将特定格式的日期转换为Date对象
	 * @param dateString 特定格式的日期
	 * @param format 格式，例如yyyy-MM-dd
	 * @return 日期对象
	 */
	public static DateTime parse(String dateString, String format){
		return parse(dateString, new SimpleDateFormat(format));
	}

	/**
	 * 格式yyyy-MM-dd HH:mm:ss
	 * @param dateString 标准形式的时间字符串
	 * @return 日期对象
	 */
	public static DateTime parseDateTime(String dateString){
		return parse(dateString, NORM_DATETIME_FORMAT.get());
	}

	/**
	 * 格式yyyy-MM-dd
	 * @param dateString 标准形式的日期字符串
	 * @return 日期对象
	 */
	public static DateTime parseDate(String dateString){
		return parse(dateString, NORM_DATE_FORMAT.get());
	}

	/**
	 * 格式HH:mm:ss
	 * @param timeString 标准形式的日期字符串
	 * @return 日期对象
	 */
	public static DateTime parseTime(String timeString){
		return parse(timeString, NORM_TIME_FORMAT.get());
	}

	/**
	 * 格式：<br>
	 * 1、yyyy-MM-dd HH:mm:ss<br>
	 * 2、yyyy-MM-dd<br>
	 * 3、HH:mm:ss<br>
	 * 4、yyyy-MM-dd HH:mm
	 * @param dateStr 日期字符串
	 * @return 日期
	 */
	public static DateTime parse(String dateStr) {
		if(null == dateStr) {
			return null;
		}
		dateStr = dateStr.trim();
		int length = dateStr.length();
		try {
			if(length == NORM_DATETIME_PATTERN.length()) {
				return parseDateTime(dateStr);
			}else if(length == NORM_DATE_PATTERN.length()) {
				return parseDate(dateStr);
			}else if(length == NORM_TIME_PATTERN.length()){
				return parseTime(dateStr);
			}else if(length == NORM_DATETIME_MINUTE_PATTERN.length()){
				return parse(dateStr, NORM_DATETIME_MINUTE_PATTERN);
			}else if(length == NORM_DATETIME_MS_PATTERN.length()){
				return parse(dateStr, NORM_DATETIME_MS_PATTERN);
			}
		}catch(Exception e) {
			throw new UtilException(StrUtil.format("Parse [{}] with format normal error!", dateStr));
		}

		//没有更多匹配的时间格式
		throw new UtilException(StrUtil.format(" [{}] format is not fit for date pattern!", dateStr));
	}
	// ------------------------------------ Parse end ----------------------------------------------

	// ------------------------------------ Offset start ----------------------------------------------
	/**
	 * 获取某天的开始时间
	 * @param date 日期
	 * @return 某天的开始时间
	 */
	public static DateTime getBeginTimeOfDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new DateTime(calendar.getTime());
	}

	/**
	 * 获取某天的结束时间
	 * @param date 日期
	 * @return 某天的结束时间
	 */
	public static DateTime getEndTimeOfDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new DateTime(calendar.getTime());
	}

	/**
	 * 昨天
	 * @return 昨天
	 */
	public static DateTime yesterday() {
		return offsiteDay(new DateTime(), -1);
	}

	/**
	 * 上周
	 * @return 上周
	 */
	public static DateTime lastWeek() {
		return offsiteWeek(new DateTime(), -1);
	}

	/**
	 * 上个月
	 * @return 上个月
	 */
	public static DateTime lastMouth() {
		return offsiteMonth(new DateTime(), -1);
	}

	/**
	 * 偏移天
	 * @param date 日期
	 * @param offsite 偏移天数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteDay(Date date, int offsite) {
		return offsiteDate(date, Calendar.DAY_OF_YEAR, offsite);
	}

	public static DateTime offsiteMinute(Date date, int offsite) {
		return offsiteDate(date, Calendar.MINUTE, offsite);
	}
	
	/**
	 * 偏移小时
	 * @param date 日期
	 * @param offsite 偏移小时数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteHour(Date date, int offsite) {
		return offsiteDate(date, Calendar.HOUR_OF_DAY, offsite);
	}

	/**
	 * 偏移周
	 * @param date 日期
	 * @param offsite 偏移周数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteWeek(Date date, int offsite) {
		return offsiteDate(date, Calendar.WEEK_OF_YEAR, offsite);
	}

	/**
	 * 偏移月
	 * @param date 日期
	 * @param offsite 偏移月数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteMonth(Date date, int offsite) {
		return offsiteDate(date, Calendar.MONTH, offsite);
	}

	/**
	 * 获取指定日期偏移指定时间后的时间
	 * @param date 基准日期
	 * @param calendarField 偏移的粒度大小（小时、天、月等）使用Calendar中的常数
	 * @param offsite 偏移量，正数为向后偏移，负数为向前偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteDate(Date date, int calendarField, int offsite){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(calendarField, offsite);
		return new DateTime(cal.getTime());
	}
	// ------------------------------------ Offset end ----------------------------------------------

	/**
	 * 判断两个日期相差的时长<br/>
	 * 返回 minuend - subtrahend 的差
	 * @param subtrahend 减数日期
	 * @param minuend 被减数日期
	 * @param diffField 相差的选项：相差的天、小时
	 * @return 日期差
	 */
	public static long diff(Date subtrahend, Date minuend, long diffField){
		long diff = minuend.getTime() - subtrahend.getTime();
		return diff/diffField;
	}

	/**
	 * 计时，常用于记录某段代码的执行时间，单位：纳秒
	 * @param preTime 之前记录的时间
	 * @return 时间差，纳秒
	 */
	public static long spendNt(long preTime) {
		return System.nanoTime() - preTime;
	}

	/**
	 * 计时，常用于记录某段代码的执行时间，单位：毫秒
	 * @param preTime 之前记录的时间
	 * @return 时间差，毫秒
	 */
	public static long spendMs(long preTime) {
		return System.currentTimeMillis() - preTime;
	}

	/**
	 * 格式化成yyMMddHHmm后转换为int型
	 * @param date 日期
	 * @return int
	 */
	public static int toIntSecond(Date date) {
		return Integer.parseInt(DateUtil.format(date, "yyMMddHHmm"));
	}

	/**
	 * 计算指定指定时间区间内的周数
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 周数
	 */
	public static int weekCount(Date start, Date end) {
		final Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		final Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(end);

		final int startWeekofYear = startCalendar.get(Calendar.WEEK_OF_YEAR);
		final int endWeekofYear = endCalendar.get(Calendar.WEEK_OF_YEAR);

		int count = endWeekofYear - startWeekofYear + 1;

		if(Calendar.SUNDAY != startCalendar.get(Calendar.DAY_OF_WEEK)) {
			count --;
		}

		return count;
	}

	//------------------------------------------------------------------------ Private method start
	/**
	 * 获得指定日期年份和季节<br>
	 * 格式：[20131]表示2013年第一季度
	 * @param cal 日期
	 */
	private static String yearAndSeason(Calendar cal) {
		return new StringBuilder().append(cal.get(Calendar.YEAR)).append(cal.get(Calendar.MONTH) / 3 + 1).toString();
	}
	//------------------------------------------------------------------------ Private method end

	/**
	 * 序列号生成器
	 * @return
	 */
	public static String getSerialNo(){
		StringBuffer bf = new StringBuffer();
		bf.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		bf.append(Thread.currentThread().getId());
		System.out.println(bf.toString());
		return bf.toString();
	}
	/**
	 * 获取指定月份的第一天 格式  2015-11-01 00:00:00  
	 * @param date 当前时间
	 * @param off 偏移  
	 * @return
	 */
	public static String getFirstDayOfMonth(Date date,int off)   {     
		Calendar calendar = Calendar.getInstance();     
		calendar.setTime(date);  
		calendar.set(Calendar.MONTH, off);
		calendar.set(Calendar.DAY_OF_MONTH, 1);  
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return formatDateTime(calendar.getTime());     
	}     
	/** 
	 * 得到本月最后一天的日期 
	 * @Methods Name getLastDayOfMonth 
	 * @return Date 
	 */  
	public static String getLastDayOfMonth(Date date)   {     
		Calendar calendar = Calendar.getInstance();     
		calendar.setTime(date);  
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return formatDateTime(calendar.getTime()); 
	} 

	/**
	 * 计算天数
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public static int countDay(String startTime,String endTime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = format.parse(endTime); //结束时间
		Date d2 = format.parse(startTime);//开始时间
		long diffMinutes = d1.getTime()-d2.getTime();
		int day = (int) Math.ceil(
				diffMinutes/(24*60*60*1000d)); //租用天数
		return day;
	}


	/**
	 * @Title: getCountDate 
	 * @Description: 计算时间 时间相加
	 * @param @param cashCycle
	 * @param @return
	 * @return String  
	 * @throws
	 */
	public static String getCountDate(Integer day){
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR,day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(calendar.getTime());
	}

	/**
	 * 获得年
	 * @param date
	 * @return
	 */
	public static int getYear(Date date){
		Calendar calendar = Calendar.getInstance();     
		calendar.setTime(date); 
		return calendar.get(Calendar.YEAR);
	}
	/**
	 * 获得月
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date){
		Calendar calendar = Calendar.getInstance();     
		calendar.setTime(date); 
		return calendar.get(Calendar.MONTH)+1;
	}
	/**
	 * 星期中的第几天
	 */
	public static int getWeek(Date date) {
		Calendar calendar = Calendar.getInstance();     
		calendar.setTime(date); 
		int week = calendar.get(Calendar.DAY_OF_WEEK);//1是星期日，7是星期六
		switch (week) {
		case 1:
			week = 7;
			break;
		default:
			week--;
		}
		return week;
	}
	
	/**
	 * 获得月份天数
	 * @return
	 */
	public static Integer countNowMonthDays(Integer year,Integer month) {
		int day = 30;
		if (month == 2) {
			return leapYear(year);
		}
		switch (month) {
		case 1:
			day = 31;
			break;
		case 3:
			day = 31;
			break;
		case 4:
			day = 30;
			break;
		case 5:
			day = 31;
			break;
		case 6:
			day = 30;
			break;
		case 7:
			day = 31;
			break;
		case 8:
			day = 31;
			break;
		case 9:
			day = 30;
			break;
		case 10:
			day = 31;
			break;
		case 11:
			day = 30;
			break;
		case 12:
			day = 31;
			break;
		}
		return day;
	}
	//计算二月天数
	public static int leapYear(int year) {
		if ((year % 400 == 0) || (year % 4 == 0) && (year % 100 != 0)) { //闰年
			return 29;
		}
		return 28;
	}
	public static void main(String[] args) {
		Date date = offsiteDay(new Date(), 1);
		System.out.println(date);
		System.out.println("---年:"+getYear(date));
		System.out.println("---月:"+getMonth(date));
		System.out.println("---星期:"+getWeek(date));
	}
}
