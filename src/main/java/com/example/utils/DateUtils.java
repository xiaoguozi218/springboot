package com.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {


    private static  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static  SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    
    private static  SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMdd");
    
    /**
     * @Author shGuo
     * @Date 2017年3月13日下午3:54:16
     * @return
     * @return String
     * @Desc   获取当前日期（20170313）
     */
    public static String getCurDate(Date d){
    	return sdf3.format(d);
    }
    
    public static String getCurDate(){
    	return sdf3.format(new Date());
    }
    
    /**
     * @Author shGuo
     * @Date 2017年3月13日下午3:54:16
     * @return
     * @return String
     * @Desc   获取当前日期（2017-03-13）
     */
    public static String getNowDate(){
    	return sdf2.format(new Date());
    }
    
    
    public static String getDateStr(Date d){
    	return sdf2.format(d);
    }
    
    /**
     * @Author shGuo
     * @Date 2017年3月13日下午4:03:24
     * @return
     * @return String
     * @Desc   获取昨天日期（20170312）	
     */
    public static String getYesterday(){
    	Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.DAY_OF_MONTH, -1);
        return sdf3.format(now.getTime());
    }
    
    
    public static Date parse(String date, SimpleDateFormat sdf){
    	try{
    		return sdf.parse(date);
    	}catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    } 
    
    /**
     * @Author shGuo
     * @Date 2017年4月6日下午3:43:10
     * @param d
     * @param day
     * @return
     * @return Date
     * @Desc	获取day天前的日期
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.DAY_OF_MONTH, 0-day);
        return now.getTime();
    }
    
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.DAY_OF_MONTH, day);
        return now.getTime();
    }


    public static Date resetHMS(Date date) {


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }
}
