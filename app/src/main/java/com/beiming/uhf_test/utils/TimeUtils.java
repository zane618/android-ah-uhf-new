package com.beiming.uhf_test.utils;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TimeUtils {
    public static long ssc = 86400000; //一天的时间戳量

    public static long beginTime;

    public static long endTime;

    public static long beginTime() {
        beginTime = System.currentTimeMillis();
        return beginTime;
    }

    public static long endTime() {
        endTime = System.currentTimeMillis();
        return endTime;
    }

    public static long subTime() {

        return endTime - beginTime;
    }

    public static long currentTime() {
        return System.currentTimeMillis();
    }

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(currentTime());
    }

    public static String getTime(long ts) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(currentTime());
    }
    public static String formatDate(int year, int monthOfYear, int dayOfMonth) {
        return year + "-" + String.format(Locale.getDefault(), "%02d-%02d", monthOfYear, dayOfMonth);
    }

    /**
     * 字符串转时间戳
     */
    public static long toTs(String sData) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long ts = 0;
        try {
            Date date = sdf.parse(sData);
            ts = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ts;
    }

    public static String getY_M_D_Time() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(currentTime());
    }

    public static String getTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static String dateToStr(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getStartQueryTime() {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - 7 * 24 * 60 * 60 * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(startTime) + "-" + sdf.format(endTime);
    }

    /**
     * 获取截止日期
     *
     * @param timeType      时间类型(1:年,2:月 )
     * @param availableTime 有效时长
     * @param startTime     起始时间
     * @return
     */
    public static String getEndTime(String timeType, String availableTime, String startTime) {
        long endTime;

        Date date = new Date(Long.parseLong(startTime));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//设置起时间
        //System.out.println("111111111::::"+cal.getTime());
        //cal.add(Calendar.DATE, n);//增加一天
        //cal.add(Calendar.DATE, -10);//减10天
        //cal.add(Calendar.MONTH, n);//增加一个月

        if (timeType.equals("1")) {//年
            cal.add(Calendar.YEAR, Integer.parseInt(availableTime));//增加年份
            endTime = cal.getTimeInMillis();
        } else {//月
            cal.add(Calendar.MONTH, Integer.parseInt(availableTime));//增加n个月
            endTime = cal.getTimeInMillis();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(endTime);
    }

    /**
     * 判断是否超过截止日期
     *
     * @param timeType      时间类型(1:年,2:月 )
     * @param availableTime 有效时长
     * @param startTime     起始时间
     * @return
     */
    public static boolean isOutTime(String timeType, String availableTime, String startTime) {
        long endTime;
        Date date = new Date(Long.parseLong(startTime));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//设置起时间
        //System.out.println("111111111::::"+cal.getTime());
        //cal.add(Calendar.DATE, n);//增加一天
        //cal.add(Calendar.DATE, -10);//减10天
        //cal.add(Calendar.MONTH, n);//增加一个月

        if (timeType.equals("1")) {//年
            cal.add(Calendar.YEAR, Integer.parseInt(availableTime));//增加年份
            endTime = cal.getTimeInMillis();
        } else {//月
            cal.add(Calendar.MONTH, Integer.parseInt(availableTime));//增加n个月
            endTime = cal.getTimeInMillis();
        }
        if (currentTime() > endTime) {
            return false;
        } else {
            return true;
        }
    }

}
