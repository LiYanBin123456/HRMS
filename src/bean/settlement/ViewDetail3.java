package bean.settlement;


public class ViewDetail3 extends Detail3 {
    private String cardId;//身份证号码
    private String name;//员工姓名
    private String place;//工作地点
    private String post;//工作岗位
    private byte category;//人员类别

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
