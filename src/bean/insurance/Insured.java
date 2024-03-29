package bean.insurance;

//商业保险参保人员
public class Insured {
    private long id;
    private long cid;//合作单位id
    private long did;//派遣单位id
    private String  cardId;//身份证号
    private String name;//姓名
    private String place;//工作地点
    private String post;//工作岗位
    private byte category;//职业类别

    public Insured() {
    }

    public Insured(long id, long cid, String cardId, String name, String place, String post, byte category) {
        this.id = id;
        this.cid = cid;
        this.cardId = cardId;
        this.name = name;
        this.place = place;
        this.post = post;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Insured(long id, long cid, long did, String cardId, String name, String place, String post, byte category) {
        this.id = id;
        this.cid = cid;
        this.did = did;
        this.cardId = cardId;
        this.name = name;
        this.place = place;
        this.post = post;
        this.category = category;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public byte getCategory() {
        return category;
    }

    public void setCategory(byte category) {
        this.category = category;
    }
}
