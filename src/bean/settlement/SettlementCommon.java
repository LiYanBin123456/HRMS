package bean.settlement;

import java.sql.Date;
//结算单
public class SettlementCommon {
    private long id;//
    private long did;//派遣单id
    private long cid;//合作客户id
    private Date month;//月份
    private float salary;//工资
    private float social;//社保
    private float fund;//公积金
    private float manage;//管理费
    private float tax;//税费
    private float summary;//总额
    /**状态
     0_编辑
     1_提交
     2_一审
     3_二审
     4_终审
     5_扣款
     6_发放
     */
    private float status;
    /**来源
     0_派遣单位录入
     1_合作单位录入
     */
    private float source;

}
