package bean.settlement;

import java.sql.Date;

//结算单明细基类
public class Detail {
    private long id;//工资表id
    private long sid;//结算单id
    private long eid;//员工id
    private Date month;//月份
    /**状态
     0正常
     1补缴
     2补差
     */
    private byte status;

}
