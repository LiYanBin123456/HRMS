package bean.insurance;

import java.sql.Date;

//参保单视图
public class ViewInsurance extends Insurance{
    private String name;//员工姓名
    private String cardId;//员工身份证号
    private byte household;//户籍性质
    private String address;//户籍地址
    private String phone;//电话号码
    private Date entry;//入职时间
    private byte reason;//变更原因

    public ViewInsurance() {
    }

    public ViewInsurance(long eid, String code, Date date1, Date date2, Date date3, Date date4, Date date5, byte status1, byte status2, byte status3, byte status4, byte status5, float base1, float base2, float base3, String name, String cardId, byte household, String address, String phone, Date entry, byte reason) {
        super(eid, code, date1, date2, date3, date4, date5, status1, status2, status3, status4, status5, base1, base2, base3);
        this.name = name;
        this.cardId = cardId;
        this.household = household;
        this.address = address;
        this.phone = phone;
        this.entry = entry;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getEntry() {
        return entry;
    }

    public void setEntry(Date entry) {
        this.entry = entry;
    }

    public byte getReason() {
        return reason;
    }

    public void setReason(byte reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ViewInsurance{" +
                "name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                ", household=" + household +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", entry=" + entry +
                ", reason=" + reason +
                '}';
    }
}
