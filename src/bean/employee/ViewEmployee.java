package bean.employee;

import java.sql.Date;

public class ViewEmployee extends Employee {
    private String rid;
    //毕业院校
    private String school;
    //毕业专业
    private String major;
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
    private byte household;
    //户籍地址
    private String address;
    //离职时间
    private Date date1;
    //退休时间
    private Date date2;
    /**离职原因(默认为null)
     0_合同到期、
     1_被用人单位解除劳动合同、
     2_被用人单位开除、
     3_被用人单位除名、
     4_被用人单位辞退、
     5_公司倒闭、
     6_公司破产、
     7_单位人员减少、
     8_养老在职转退休、
     9_参军、
     10_入学、
     11_劳改劳教、
     12_出国定居、
     13_异地转移、
     14_不足缴费年限、
     15_人员失踪、
     16_错误申报、
     17_其他原因
     *
     */
    private byte reason;

    public ViewEmployee() {
    }

    public ViewEmployee(long id, long did, long cid, String cardId, String name, String phone, byte degree, byte type, Date entry, byte status, String department, String post, byte category, float price, String rid, String school, String major, byte household, String address, Date date1, Date date2, byte reason) {
        super(id, did, cid, cardId, name, phone, degree, type, entry, status, department, post, category, price);
        this.rid = rid;
        this.school = school;
        this.major = major;
        this.household = household;
        this.address = address;
        this.date1 = date1;
        this.date2 = date2;
        this.reason = reason;
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

    public byte getHousehold() {
        return household;
    }

    public void setHousehold(byte household) {
        this.household = household;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public byte getReason() {
        return reason;
    }

    public void setReason(byte reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ViewEmployee{" +
                "rid='" + rid + '\'' +
                ", school='" + school + '\'' +
                ", major='" + major + '\'' +
                ", household=" + household +
                ", address='" + address + '\'' +
                ", date1=" + date1 +
                ", date2=" + date2 +
                ", reason=" + reason +
                '}';
    }
}
