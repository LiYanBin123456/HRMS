package bean.settlement;

import java.sql.Date;
//结算单明细
public class SalaryCommon {
    private long id;//工资表id
    private long sid;//结算单id
    private long eid;//员工id
    private Date month;//月份
    private float base;//基础工资
    private float reward;//绩效奖金
    private float allowance;//岗位补贴
    private float social1;//个人社保
    private float social2;//单位社保
    private float fund1;//个人公积金
    private float fund2;//单位公积金
    private float tax;//个税
    private float payable;//应发
    private float paid;//实发
    private float f1;//自定字段
    private float f2;
    private float f3;
    private float f4;
    private float f5;
    private float f6;
    private float f7;
    private float f8;
    private float f9;
    private float f10;
    private byte status;

}
