package bean.settlement;

import java.sql.Date;
//小时工结算单
public class SettlementHour {
    private long id;//
    private long did;//派遣方id
    private long cid;//合作客户id
    private Date month;//月份
    private  int hours;//总工时
    private  float price;//合作客户所给的单价
    private  float traffic;//交通费
    private  float extra;//附加
    private  float summary;//总额
    /**状态
     0_编辑
     1_提交
     2_一审
     3_二审
     4_终审
     5_扣款
     6_发放
     */
    private  byte status;
    /**
     * 类别
     0_派遣单位录入
     1_合作单位录入
     */
    private  byte source;

}
