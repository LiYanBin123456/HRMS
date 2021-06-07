package utills;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Date parse(String str,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String format(Date date,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date getLastDayofMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //上个月最后一天
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        return calendar.getTime();
    }

    /**
     * 获取指定日期所在月份的最后一天
     * @param str 日期，格式为yyyy-mm-dd
     * @return 指定月最后一天
     */
    public static Date getLastDayofMonth(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return getLastDayofMonth(sdf.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static boolean equal(Date d1, Date d2){
        return d1.getYear()==d2.getYear() && d1.getMonth()==d2.getMonth() && d1.getDate()==d2.getDate();
    }

    /**
     * 获取指定日期间隔N个月后的日期
     * @param startTime 指定日期
     * @param interval  间隔月数
     * @return
     */
    public static Date getExpireTime(Date startTime,int interval){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MONTH, interval);//得到间隔几个月的日期
        return  calendar.getTime();
    }
}
