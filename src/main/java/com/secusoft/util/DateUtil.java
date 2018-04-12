package com.secusoft.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 * @author yaojiacheng
 * 2018年4月12日
 */
public class DateUtil {

	public static String format_YYYYMMDD = "yyyy-MM-dd";
	public static String format_YYYYMMDDHHmmSS = "yyyy-MM-dd HH:mm:ss";
	public static String format_YYYYMMDDHHmmssSSS = "yyyyMMddHHmmssSSS";
	
	public static Date parseDate(String dateStr,String format){
		if(StringUtil.isAnyBlank(dateStr,format)){
			return null;
		}
		SimpleDateFormat fr = new SimpleDateFormat(format);
		try {
			return fr.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String parseDateStr(Date date,String format){
		SimpleDateFormat fr = new SimpleDateFormat(format);
		return fr.format(date);
	}
	
	/**
	 * 计算两个时间间隔多少天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Long betweenDays(Date date1,Date date2){
		long betweenDays = (date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
		return betweenDays;
	}
	
	/**
	 * 根据出生日期计算年龄
	 * @param dateOfBirth
	 * @return
	 */
	public static int getAge(Date dateOfBirth) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                throw new IllegalArgumentException("年龄不能超过当前日期");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
            int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
            if (nowDayOfYear < bornDayOfYear) {
                age -= 1;
            }
        }
        return age;
    }
	
}
