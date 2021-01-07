package bean.settlement;

import java.util.Date;

public class ViewDetail1 extends Detail1 {
    private String cardId;//员工身份证号
    private String name;//员工姓名
    private Date month;//月份
    private float manage;//管理费
    private float tax2;//税费
    private float total1;//个人社保合计
    private float total2;//单位社保合计
    private float summary;//汇款总额
    public ViewDetail1() {
    }

    public ViewDetail1(long id, long sid, long eid, float base, float pension1, float medicare1, float unemployment1, float disease1, float fund1, float pension2, float medicare2, float unemployment2, float injury, float disease2, float birth, float fund2, float tax, float payable, float paid, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, byte status, String cardId, String name) {
        super(id, sid, eid, base, pension1, medicare1, unemployment1, disease1, fund1, pension2, medicare2, unemployment2, injury, disease2, birth, fund2, tax, payable, paid, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, status);
        this.cardId = cardId;
        this.name = name;
    }

    public ViewDetail1(String cardId, String name, Date month) {
        this.cardId = cardId;
        this.name = name;
        this.month = month;
    }

    public ViewDetail1(long id, long sid, long eid, float base, float pension1, float medicare1, float unemployment1, float disease1, float fund1, float pension2, float medicare2, float unemployment2, float injury, float disease2, float birth, float fund2, float tax, float payable, float paid, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, byte status, String cardId, String name, Date month) {
        super(id, sid, eid, base, pension1, medicare1, unemployment1, disease1, fund1, pension2, medicare2, unemployment2, injury, disease2, birth, fund2, tax, payable, paid, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, status);
        this.cardId = cardId;
        this.name = name;
        this.month = month;
    }

    public ViewDetail1(long id, long sid, long eid, float base, float pension1, float medicare1, float unemployment1, float disease1, float fund1, float pension2, float medicare2, float unemployment2, float injury, float disease2, float birth, float fund2, float tax, float free, float extra1, float extra2, float manage, float sum, float payable, float paid, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, byte status, String comments1, String comments2, String cardId, String name, Date month) {
        super(id, sid, eid, base, pension1, medicare1, unemployment1, disease1, fund1, pension2, medicare2, unemployment2, injury, disease2, birth, fund2, tax, free, extra1, extra2, manage, sum, payable, paid, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, status, comments1, comments2);
        this.cardId = cardId;
        this.name = name;
        this.month = month;
    }

    public ViewDetail1(long id, long sid, long eid, float base, float pension1, float medicare1, float unemployment1, float disease1, float fund1, float pension2, float medicare2, float unemployment2, float injury, float disease2, float birth, float fund2, float tax, float tax2, float free, float extra1, float extra2, float manage, float sum, float payable, float paid, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, byte status, String comments1, String comments2, String cardId, String name, Date month) {
        super(id, sid, eid, base, pension1, medicare1, unemployment1, disease1, fund1, pension2, medicare2, unemployment2, injury, disease2, birth, fund2, tax, tax2, free, extra1, extra2, manage, sum, payable, paid, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, status, comments1, comments2);
        this.cardId = cardId;
        this.name = name;
        this.month = month;
    }

    public ViewDetail1(long id, long sid, long eid, float base, float pension1, float medicare1, float unemployment1, float disease1, float fund1, float pension2, float medicare2, float unemployment2, float injury, float disease2, float birth, float fund2, float tax, float tax2, float free, float extra1, float extra2, float manage, float sum, float payable, float paid, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, byte status, String comments1, String comments2, String cardId, String name, Date month, float manage1, float tax21, float total1, float total2, float summary) {
        super(id, sid, eid, base, pension1, medicare1, unemployment1, disease1, fund1, pension2, medicare2, unemployment2, injury, disease2, birth, fund2, tax, tax2, free, extra1, extra2, manage, sum, payable, paid, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, status, comments1, comments2);
        this.cardId = cardId;
        this.name = name;
        this.month = month;
        this.manage = manage1;
        this.tax2 = tax21;
        this.total1 = total1;
        this.total2 = total2;
        this.summary = summary;
    }

    public float getTotal1() {
        return total1;
    }

    public void setTotal1(float total1) {
        this.total1 = total1;
    }

    public float getTotal2() {
        return total2;
    }

    public void setTotal2(float total2) {
        this.total2 = total2;
    }

    public float getSummary() {
        return summary;
    }

    public void setSummary(float summary) {
        this.summary = summary;
    }

    public float getManage() {
        return manage;
    }

    public void setManage(float manage) {
        this.manage = manage;
    }

    public float getTax2() {
        return tax2;
    }

    public void setTax2(float tax2) {
        this.tax2 = tax2;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ViewDetail1{" +
                "cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
