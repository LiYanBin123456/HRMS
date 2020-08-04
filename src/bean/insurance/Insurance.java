package bean.insurance;

import java.sql.Date;

//参保单
public class Insurance {
    private long eid;//员工id
    /**类型
     0_社保参保单
     1_医保参保单
     2_公积金参保单
     */
    private byte type;
    private String code;//个人代码
    private Date start;//参保时间
    private float money;//月缴费工资
    /**参保状态
     0_新增
     1_在保
     2_拟停
     3_停保
     */
    private byte status;
    private String reason;//变更原因

}
