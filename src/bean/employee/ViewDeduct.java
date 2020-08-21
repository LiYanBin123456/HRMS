package bean.employee;

//员工个税视图
public class ViewDeduct extends Deduct{
    private String name;
    private String cardId;

    public ViewDeduct() {
    }

    public ViewDeduct(long eid, float deduct1, float deduct2, float deduct3, float deduct4, float deduct5, float deduct6, String name, String cardId) {
        super(eid, deduct1, deduct2, deduct3, deduct4, deduct5, deduct6);
        this.name = name;
        this.cardId = cardId;
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

    @Override
    public String toString() {
        return "ViewDeduct{" +
                "name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                '}';
    }
}
