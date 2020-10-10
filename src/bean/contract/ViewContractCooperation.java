package bean.contract;

import java.sql.Date;

//合作客户合同视图
public class ViewContractCooperation extends Contract{
    private String name;//客户名称
    private byte stype;//类型 0_劳务外包派遣 1_人事代理 2小时工 3商业保险
    private byte category;//结算方式  0_按人数收取  1_按比例收取  2_外包整体核算  3_小时工
    private byte payment;//工资放发日，即不能晚于每月的之地呢日期
    private byte settlement;//结算日
    private byte receipt;//回款日

    private long pid;//保险产品id
    private float value;//结算值 根据结算方式的不同而不同，因为按比例是百分比，因此设置为float类型

    public ViewContractCooperation() {
    }

    public ViewContractCooperation(String id, long aid, long bid, String type, Date start, Date end, byte status, String comments, byte invoice, String project, float per, byte times, String name, byte stype, byte category, byte payment, byte settlement, byte receipt, long pid, float value) {
        super(id, aid, bid, type, start, end, status, comments, invoice, project, per, times);
        this.name = name;
        this.stype = stype;
        this.category = category;
        this.payment = payment;
        this.settlement = settlement;
        this.receipt = receipt;
        this.pid = pid;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getStype() {
        return stype;
    }

    public void setStype(byte stype) {
        this.stype = stype;
    }

    public byte getCategory() {
        return category;
    }

    public void setCategory(byte category) {
        this.category = category;
    }

    public byte getPayment() {
        return payment;
    }

    public void setPayment(byte payment) {
        this.payment = payment;
    }

    public byte getSettlement() {
        return settlement;
    }

    public void setSettlement(byte settlement) {
        this.settlement = settlement;
    }

    public byte getReceipt() {
        return receipt;
    }

    public void setReceipt(byte receipt) {
        this.receipt = receipt;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ViewContractCooperation{" +
                "name='" + name + '\'' +
                ", stype=" + stype +
                '}';
    }
}
