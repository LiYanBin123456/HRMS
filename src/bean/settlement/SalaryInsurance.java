package bean.settlement;

import java.sql.Date;
//商业结算单明细
public class SalaryInsurance {
    private long id;//id
    private long did;//派遣方id
    private long cid;//合作单位id
    private long pid;//保险产品id
    private Date month;//月份
    private float price;//保费
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
    /**类别
     0_派遣单位录入
     1_合作单位录入
     */
    private byte source;

}
