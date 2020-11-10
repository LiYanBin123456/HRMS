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
    private byte payoff;//工资发放对象  0 派遣单位发放  1 合作单位发放
    public Serve() {
    }

    public Serve(String cid, byte type, byte category, byte payment, byte settlement, byte receipt, long pid, float value, byte payoff) {
        this.cid = cid;
        this.type = type;
        this.category = category;
        this.payment = payment;
        this.settlement = settlement;
        this.receipt = receipt;
        this.pid = pid;
        this.value = value;
        this.payoff = payoff;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
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

    public byte getPayoff() {
        return payoff;
    }

    public void setPayoff(byte payoff) {
        this.payoff = payoff;
    }

    @Override
    public String toString() {
        return "Serve{" +
                "cid='" + cid + '\'' +
                ", type=" + type +
                ", category=" + category +
                ", payment=" + payment +
                ", settlement=" + settlement +
                ", receipt=" + receipt +
                ", pid=" + pid +
                ", value=" + value +
                '}';
    }
}
