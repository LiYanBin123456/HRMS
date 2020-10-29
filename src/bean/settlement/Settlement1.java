package bean.settlement;

import java.sql.Date;

//结算单
public class Settlement1 extends Settlemet{

    private byte type;//0 普通结算  1 人事代理结算单
    private float salary;//应发工资总额
    private float social;//单位社保总额
    private float medicare;//单位医保总额
    private float fund;//单位公积金总额
    private float manage;//管理费
    private float tax;//税费
    private float extra;//单位补收核减
    private float free;//国家扣除项
    private float summary;//总额
    private String comments;//备注

    public Settlement1() {
    }

    public Settlement1(long id, long did, long cid, Date month, byte status, byte source, float salary, float social, float medicare, float fund, float manage, float tax, float summary) {
        super(id, did, cid, month, status, source);
        this.salary = salary;
        this.social = social;
        this.medicare = medicare;
        this.fund = fund;
        this.manage = manage;
        this.tax = tax;
        this.summary = summary;
    }

    public Settlement1(long id, long did, long cid, String ccid, Date month, byte status, byte source, byte type, float salary, float social, float medicare, float fund, float manage, float tax, float summary) {
        super(id, did, cid, ccid, month, status, source);
        this.type = type;
        this.salary = salary;
        this.social = social;
        this.medicare = medicare;
        this.fund = fund;
        this.manage = manage;
        this.tax = tax;
        this.summary = summary;
    }

    public Settlement1(long id, long did, long cid, String ccid, Date month, byte status, byte source, byte type, float salary, float social, float medicare, float fund, float manage, float tax, float extra, float free, float summary, String comments) {
        super(id, did, cid, ccid, month, status, source);
        this.type = type;
        this.salary = salary;
        this.social = social;
        this.medicare = medicare;
        this.fund = fund;
        this.manage = manage;
        this.tax = tax;
        this.extra = extra;
        this.free = free;
        this.summary = summary;
        this.comments = comments;
    }

    public float getFree() {
        return free;
    }

    public void setFree(float free) {
        this.free = free;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public float getSocial() {
        return social;
    }

    public void setSocial(float social) {
        this.social = social;
    }

    public float getMedicare() {
        return medicare;
    }

    public void setMedicare(float medicare) {
        this.medicare = medicare;
    }

    public float getFund() {
        return fund;
    }

    public void setFund(float fund) {
        this.fund = fund;
    }

    public float getManage() {
        return manage;
    }

    public void setManage(float manage) {
        this.manage = manage;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getSummary() {
        return summary;
    }

    public void setSummary(float summary) {
        this.summary = summary;
    }

    public float getExtra() {
        return extra;
    }

    public void setExtra(float extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "Settlement1{" +
                "salary=" + salary +
                ", social=" + social +
                ", fund=" + fund +
                ", manage=" + manage +
                ", tax=" + tax +
                ", summary=" + summary +
                '}';
    }
}
