package utills;
import java.util.Calendar;

public class CreateGetNextId {
    /**list集合为表中全部信息,str为ID最开始的字符(如学生就为SD)，
     *strid为截取的自动增长的数值部分(最后的4位) 故ID就为SD201801010001样式
     */
    public static String NextId(long count,String type){

        long intid=count;
        String sid = String.valueOf(intid+1);
        switch (sid.length()) {
            case 1:
                sid = "00000" +sid;
                break;
            case 2:
                sid = "0000" + sid;

                break;
            case 3:
                sid = "000" + sid;
                break;
            case 4:
                sid = "00" + sid;
               break;
            case 5:
                sid = "0" + sid;
                break;
            case 6:
                sid = "" + sid;
                break;
        }
        Calendar now = Calendar.getInstance();

        String nextid=type+now.get(Calendar.YEAR)+sid;

        return nextid;

    }
    public static String NextId(String id,String type){
        String out = id.substring(5,11);
        long intid= Long.parseLong(out);
        String sid = String.valueOf(intid+1);
        switch (sid.length()) {
            case 1:
                sid = "00000" +sid;
                break;
            case 2:
                sid = "0000" + sid;
                break;
            case 3:
                sid = "000" + sid;
                break;
            case 4:
                sid = "00" + sid;
                break;
            case 5:
                sid = "0" + sid;
                break;
            case 6:
                sid = "" + sid;
                break;
        }
        Calendar now = Calendar.getInstance();
        String nextid=type+now.get(Calendar.YEAR)+sid;

        return nextid;

    }


}
