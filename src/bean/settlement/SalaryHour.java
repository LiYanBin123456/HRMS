package bean.settlement;


import java.sql.Date;
//小时工结算单明细
public class SalaryHour {
    private long id;//id
    private long sid;//小时工结算id
    private long eid;//员工id
    private Date month;//月份
    private int hours;//工时
    private float price;//员工表中的价格
    private float food;//餐费
    private float traffic;//交通费
    private float accommodation;//住宿费
    private float utilities;//水电费
    private float insurance;//保险费
    private float tax;//个税
    private float other1;//
    private float other2;//
    private float payable;//应付金额
    private float paid;//实付金额
    /**状态
     0正常
     1补缴
     2补差
     */
    private byte status;


}
