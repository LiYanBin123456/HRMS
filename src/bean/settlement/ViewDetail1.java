package bean.settlement;

import java.sql.Date;

public class ViewDetail1 extends Detail1 {
    private String cardId;//员工身份证号
    private String name;//员工姓名

    public ViewDetail1() {
    }

    public ViewDetail1(long id, long sid, long eid, Date month, byte status, float base, float pension1, float medicare1, float unemployment1, float disease1, float fund1, float pension2, float medicare2, float unemployment2, float injury, float disease2, float birth, float fund2, float tax, float payable, float paid, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, String cardId, String name) {
        super(id, sid, eid, month, status, base, pension1, medicare1, unemployment1, disease1, fund1, pension2, medicare2, unemployment2, injury, disease2, birth, fund2, tax, payable, paid, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20);
        this.cardId = cardId;
        this.name = name;
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
