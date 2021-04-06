package bean.settlement;

public class ViewDetail4 extends Detail4 {
    private String cardId;//员工身份证号
    private String name;//员工姓名

    public ViewDetail4() {
    }

    public ViewDetail4(long id, long sid, long eid, float amount, float tax, float paid, String cardId, String name) {
        super(id, sid, eid, amount, tax, paid);
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
        return "ViewDetail4{" +
                "cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
