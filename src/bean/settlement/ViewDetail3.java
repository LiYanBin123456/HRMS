package bean.settlement;


public class ViewDetail3 extends Detail3 {
    private String cardId;//身份证号码
    private String name;//员工姓名
    private String place;//工作地点
    private String post;//工作岗位
    private byte category;//人员类别
    private String ucardId;//替换的身份证号码
    private String uname;//替换的姓名


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

    public String getUcardId() {
        return ucardId;
    }

    public void setUcardId(String ucardId) {
        this.ucardId = ucardId;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getCategoryString() {
        switch (category){
            case 0:
                return "一类";
            case 1:
                return "二类";
            case 2:
                return "三类";
            case 3:
                return "四类";
            case 4:
                return "五类";
            case 5:
                return "六类";
        }
        return "";
    }
}
