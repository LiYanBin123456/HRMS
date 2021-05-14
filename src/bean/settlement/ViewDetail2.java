package bean.settlement;

import java.util.Date;

public class ViewDetail2 extends Detail2{
    private String cardId;//员工身份证号
    private String name;//员工姓名
    private Date month;//月份
    private float sum;//汇款明细
    private float price1;//公司单价
    private float price2;//员工单价
    private float price3;//差价
    public ViewDetail2() {
    }

    public ViewDetail2(long id, long sid, long eid, int hours, float price, float food, float traffic, float accommodation, float utilities, float insurance, float tax, float other1, float other2, float payable, float paid, String cardId, String name, Date month, float sum, float price1, float price2, float price3) {
        super(id, sid, eid, hours, price, food, traffic, accommodation, utilities, insurance, tax, other1, other2, payable, paid);
        this.cardId = cardId;
        this.name = name;
        this.month = month;
        this.sum = sum;
        this.price1 = price1;
        this.price2 = price2;
        this.price3 = price3;
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

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public float getPrice1() {
        return price1;
    }

    public void setPrice1(float price1) {
        this.price1 = price1;
    }

    public float getPrice2() {
        return price2;
    }

    public void setPrice2(float price2) {
        this.price2 = price2;
    }

    public float getPrice3() {
        return price3;
    }

    public void setPrice3(float price3) {
        this.price3 = price3;
    }

    @Override
    public String toString() {
        return "ViewDetail2{" +
                "cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
