package bean.employee;

import java.util.Date;

public class ViewEmployee extends Employee {
    private long aid;//派遣单位所属管理员
    private String cname;//派遣单位名称
    /**户口性质
     0_外地城镇、
     1_本地城镇、
     2_外地农村、
     3_城镇、
     4_农村、
     5_港澳台、
     6_外籍
     *
     */

    public ViewEmployee() {
    }

    public ViewEmployee(long id, long did, long cid, String cardId, String name, String phone, byte degree, byte type, Date entry, byte status, String department, String post, byte category, float price, String cname) {
        super(id, did, cid, cardId, name, phone, degree, type, entry, status, department, post, category, price);

        this.cname = cname;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

}
