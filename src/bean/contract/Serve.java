package bean.contract;

import java.sql.Date;

//合同服务项目
public class Serve {

    private String cid;//外键  合同id
    /**
     * 类型
     0_劳务外包派遣
     1_小时工
     2_商业保险
     */
    private byte type;
    private byte category;//结算方式  0_按人数收取  1_按比例收取  2_外包整体核算  3_小时工
    private byte payment;//工资放发日，即不能晚于每月的之地呢日期
    private byte settlement;//结算日
    private byte receipt;//回款日

    private long pid;//保险产品id
    private float value;//结算值 根据结算方式的不同而不同，因为按比例是百分比，因此设置为float类型
    private float tax;//税率（只有选择了按人数收取才有）默认null
    public Serve() {
    }



}
