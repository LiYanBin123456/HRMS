package bean.settlement;

import java.sql.Date;

//结算单基类
public class Settlemet {
    private long id;//
    private long did;//派遣单id
    private long cid;//合作客户id
    private Date month;//月份
    /**状态
     0_编辑
     1_提交
     2_一审
     3_二审
     4_终审
     5_扣款
     6_发放
     */
    private byte status;
    /**来源
     0_派遣单位录入
     1_合作单位录入
     */
    private byte source;

}
