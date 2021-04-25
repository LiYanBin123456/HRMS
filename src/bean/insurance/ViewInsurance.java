package bean.insurance;

import java.util.Date;

//参保单视图
public class ViewInsurance extends Insurance{
    private String name;//员工姓名
    private String cardId;//员工身份证号
    private byte household;//户籍性质
    private String address;//户籍地址
    private String phone;//电话号码
    private Date entry;//入职时间
    private String cname;//合作单位名称
    private long did;//派遣方单位id


    public ViewInsurance() {
    }

    public ViewInsurance(long eid, String city, byte category, String code, float base, byte baseType, float v1, float v2, Date date, byte status, String reason, String name, String cardId, byte household, String address, String phone, Date entry, String cname, long did) {
        super(eid, city, category, code, base, baseType, v1, v2, date, status, reason);
        this.name = name;
        this.cardId = cardId;
        this.household = household;
        this.address = address;
        this.phone = phone;
        this.entry = entry;
        this.cname = cname;
        this.did = did;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
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

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

}
