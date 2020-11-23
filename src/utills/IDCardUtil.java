package utills;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description：身份证号的util
 * @Author:
 * @Date: Created in 11:26 2019-03-27
 * @Modified By:
 */
public class IDCardUtil {
    /**
     * 15位身份证号
     */
    private static final Integer FIFTEEN_ID_CARD=15;
    /**
     * 18位身份证号
     */
    private static final Integer EIGHTEEN_ID_CARD=18;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 根据身份证号获取性别
     * @param IDCard
     * @return
     */
    public static String getSex(String IDCard){
        String sex ="";
            //15位身份证号
            if (IDCard.length() == FIFTEEN_ID_CARD){
                if (Integer.parseInt(IDCard.substring(14, 15)) % 2 == 0) {
                    sex = "女";
                } else {
                    sex = "男";
                }
                //18位身份证号
            }else if(IDCard.length() == EIGHTEEN_ID_CARD){
                // 判断性别
                if (Integer.parseInt(IDCard.substring(16).substring(0, 1)) % 2 == 0) {
                    sex = "女";
                } else {
                    sex = "男";
                }
            }
        return sex;
    }

    /**
     * 根据身份证号获取年龄
     * @param IDCard
     * @return
     */
    public static Integer getAge(String IDCard){
        Integer age = 0;
        Date date = new Date();
        if (isValid(IDCard)){
            //15位身份证号
            if (IDCard.length() == FIFTEEN_ID_CARD){
                // 身份证上的年份(15位身份证为1980年前的)
                String uyear = "19" + IDCard.substring(6, 8);
                // 身份证上的月份
                String uyue = IDCard.substring(8, 10);
                // 当前年份
                String fyear = format.format(date).substring(0, 4);
                // 当前月份
                String fyue = format.format(date).substring(5, 7);
                if (Integer.parseInt(uyue) <= Integer.parseInt(fyue)) {
                    age = Integer.parseInt(fyear) - Integer.parseInt(uyear) + 1;
                    // 当前用户还没过生
                } else {
                    age = Integer.parseInt(fyear) - Integer.parseInt(uyear);
                }
                //18位身份证号
            }else if(IDCard.length() == EIGHTEEN_ID_CARD){
                // 身份证上的年份
                String year = IDCard.substring(6).substring(0, 4);
                // 身份证上的月份
                String yue = IDCard.substring(10).substring(0, 2);
                // 当前年份
                String fyear = format.format(date).substring(0, 4);
                // 当前月份
                String fyue = format.format(date).substring(5, 7);
                // 当前月份大于用户出身的月份表示已过生日
                if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) {
                    age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
                    // 当前用户还没过生日
                } else {
                    age = Integer.parseInt(fyear) - Integer.parseInt(year);
                }
            }
        }
        return age;
    }

    /**
     * 获取出生日期  yyyy年MM月dd日
     * @param IDCard
     * @return
     */
    public static String getBirthday(String IDCard){
        String birthday;
        String year="";
        String month="";
        String day="";
            //15位身份证号
            if (IDCard.length() == FIFTEEN_ID_CARD){
                // 身份证上的年份(15位身份证为1980年前的)
                year = "19" + IDCard.substring(6, 8);
                //身份证上的月份
                month = IDCard.substring(8, 10);
                //身份证上的日期
                day= IDCard.substring(10, 12);
                //18位身份证号
            }else if(IDCard.length() == EIGHTEEN_ID_CARD){
                // 身份证上的年份
                year = IDCard.substring(6).substring(0, 4);
                // 身份证上的月份
                month = IDCard.substring(10).substring(0, 2);
                //身份证上的日期
                day=IDCard.substring(12).substring(0,2);
            }
            birthday=year+"-"+month+"-"+day;
        return birthday;
    }

    /**
     * 身份证验证
     * @param id 号码内容
     * @return 是否有效
     */
    public static boolean isValid(String id){
        Boolean validResult = true;
        //校验长度只能为15或18
        int len = id.length();
        if (len != FIFTEEN_ID_CARD && len != EIGHTEEN_ID_CARD){
            validResult = false;
        }
        //校验生日
        if (!validDate(id)){
            validResult = false;
        }
        return validResult;
    }

    /**
     * 校验生日
     * @param id
     * @return
     */
    private static boolean validDate(String id)
    {
        try
        {
            String birth = id.length() == 15 ? "19" + id.substring(6, 12) : id.substring(6, 14);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date birthDate = sdf.parse(birth);
            if (!birth.equals(sdf.format(birthDate))){
                return false;
            }
        }
        catch (ParseException e)
        {
            return false;
        }
        return true;
    }

    /**
     * 获取某个月最后一天
     * @param date
     * @return
     */
    public static String getLastday_Month(String date)  {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //上个月最后一天
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        String day_last = df.format(calendar.getTime());

        return day_last;
    }


}
