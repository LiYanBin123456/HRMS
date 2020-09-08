package bean.employee;

import java.sql.Date;

public class ViewEmployee extends Employee {
    private String rid;
    //毕业院校
    private String school;
    //毕业专业
    private String major;
    //派遣单位名称
    private String cname;

    public ViewEmployee() {
    }

    public ViewEmployee(long id, long did, long cid, String cardId, String name, String phone, byte degree, byte type, Date entry, byte status, String department, String post, byte category, float price, String rid, String school, String major, String cname) {
        super(id, did, cid, cardId, name, phone, degree, type, entry, status, department, post, category, price);
        this.rid = rid;
        this.school = school;
        this.major = major;
        this.cname = cname;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "ViewEmployee{" +
                "rid='" + rid + '\'' +
                ", school='" + school + '\'' +
                ", major='" + major + '\'' +
                '}';
    }
}
