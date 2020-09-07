package bean.employee;

import java.sql.Date;

public class ViewEmployee extends Employee {
    private String rid;
    //毕业院校
    private String school;
    //毕业专业
    private String major;


    public ViewEmployee() {
    }

    public ViewEmployee(long id, long did, long cid, String cardId, String name, String phone, byte degree, byte type, Date entry, byte status, String department, String post, byte category, float price, String rid, String school, String major) {
        super(id, did, cid, cardId, name, phone, degree, type, entry, status, department, post, category, price);
        this.rid = rid;
        this.school = school;
        this.major = major;
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


    @Override
    public String toString() {
        return "ViewEmployee{" +
                "rid='" + rid + '\'' +
                ", school='" + school + '\'' +
                ", major='" + major + '\'' +
                '}';
    }
}
