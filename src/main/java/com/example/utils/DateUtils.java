package com.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Calendar;
import java.util.Date;

/**
 * JDK8 时间日期库
 *  Instant——它代表的是时间戳
    LocalDate——不包含具体时间的日期，比如2014-01-14。它可以用来存储生日，周年纪念日，入职日期等。
    LocalTime——它代表的是不含日期的时间
    LocalDateTime——它包含了日期及时间，不过还是没有偏移信息或者说时区。
    ZonedDateTime——这是一个包含时区的完整的日期时间，偏移量是以UTC/格林威治时间为基准的。
 *
 *
 *
 *
 */
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


    /**
     * @param args
     *
     *
     */
    public static void main(String[] args) {
        //示例1 如何 在Java 8中获取当天的日期    2018-06-21
//        LocalDate today = LocalDate.now();
//        System.out.println("Today's Local date : " + today);

        //示例2 如何在Java 8中获取当前的年月日
//        LocalDate today = LocalDate.now();
//        int year = today.getYear();
//        int month = today.getMonthValue();
//        int day = today.getDayOfMonth();
//        System.out.printf("Year : %d Month : %d day : %d \t %n", year, month, day);

        //示例3 在Java 8中如何获取某个特定的日期
//        LocalDate dateOfBirth = LocalDate.of(2010, 01, 14);
//        System.out.println("Your Date of birth is : " + dateOfBirth);

        //示例4 在Java 8中如何检查两个日期是否相等
//        LocalDate date1 = LocalDate.of(2018, 06, 21);
//        if(date1.equals(today)){
//            System.out.printf("Today %s and date1 %s are same date %n", today, date1);
//        }

        //示例5 在Java 8中如何检查重复事件，比如说生日
//        LocalDate dateOfBirth = LocalDate.of(2010, 01, 14);
//        MonthDay birthday = MonthDay.of(dateOfBirth.getMonth(), dateOfBirth.getDayOfMonth());
//        MonthDay currentMonthDay = MonthDay.from(today);
//        if(currentMonthDay.equals(birthday)){
//            System.out.println("Many Many happy returns of the day !!");
//        }else{
//            System.out.println("Sorry, today is not your birthday");
//        }



    }

}
