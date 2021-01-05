package bean.settlement;

import java.util.Date;

public class ViewDetail2 extends Detail2{
    private String cardId;//员工身份证号
    private String name;//员工姓名

    public ViewDetail2() {
    }

    public ViewDetail2(long id, long sid, long eid,  int hours, float price, float food, float traffic, float accommodation, float utilities, float insurance, float tax, float other1, float other2, float payable, float paid, String cardId, String name) {
        super(id, sid, eid, hours, price, food, traffic, accommodation, utilities, insurance, tax, other1, other2, payable, paid);
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
        return "ViewDetail2{" +
                "cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
