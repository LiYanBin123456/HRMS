package bean.log;

import java.sql.Date;
//日志单
public class Logs {
    private long sid;//结算单id
    /**结算单类别
     0_普通结算单
     1_小时工结算单
     2_商业结算单
     */
    private byte type;
    private String operator;//操作者姓名（编号）
    private Date time;//插入时间
    private String content;//操作内容

}
