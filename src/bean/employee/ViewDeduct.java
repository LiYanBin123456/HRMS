package bean.employee;

//员工个税视图
public class ViewDeduct extends Deduct{
    private String name;
    private String cardId;
    private long did;//派遣方id
    private long cid;//合作方id

    public ViewDeduct() {
    }

    public ViewDeduct(long eid, float income, float free, float prepaid, float deduct, float deduct1, float deduct2, float deduct3, float deduct4, float deduct5, float deduct6, boolean flag, String name, String cardId, long did, long cid) {
        super(eid, income, free, prepaid, deduct, deduct1, deduct2, deduct3, deduct4, deduct5, deduct6, flag);
        this.name = name;
        this.cardId = cardId;
        this.did = did;
        this.cid = cid;
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

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "ViewDeduct{" +
                "name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                '}';
    }
}
