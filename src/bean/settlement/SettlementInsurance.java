package bean.settlement;

import java.sql.Date;
//商业保险结算单
public class SettlementInsurance {
    private long id;//
    private long sid;//商业保险结算单
    private long eid;//员工id
    private long pid;//产品id
    private Date month;//月份
    private String place;//工作地点
    private float price;//保费
    /**状态
     0正常
     1补缴
     2补差
     */
    private byte status;

}
