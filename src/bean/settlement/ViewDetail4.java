package bean.settlement;

public class ViewDetail4 extends Detail4 {
    private String cardId;//员工身份证号
    private String name;//员工姓名

    private float manage;//管理费
    private float tax2;//税费
    private float sum;//总额
    public ViewDetail4() {
    }

    public ViewDetail4(long id, long sid, long eid, float amount, float tax, float paid, String cardId, String name, float manage, float tax2, float sum) {
        super(id, sid, eid, amount, tax, paid);
        this.cardId = cardId;
        this.name = name;
        this.manage = manage;
        this.tax2 = tax2;
        this.sum = sum;
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

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
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
        return "ViewDetail4{" +
                "cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
