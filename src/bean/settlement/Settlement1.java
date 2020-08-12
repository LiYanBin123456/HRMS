package bean.settlement;

import java.sql.Date;

//结算单
public class Settlement1 extends Settlemet{
    private float salary;//工资
    private float social;//社保
    private float fund;//公积金
    private float manage;//管理费
    private float tax;//税费
    private float summary;//总额

    public Settlement1() {
    }

    public Settlement1(long id, long did, long cid, Date month, byte status, byte source, float salary, float social, float fund, float manage, float tax, float summary) {
        super(id, did, cid, month, status, source);
        this.salary = salary;
        this.social = social;
        this.fund = fund;
        this.manage = manage;
        this.tax = tax;
        this.summary = summary;
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
