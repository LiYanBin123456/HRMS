package bean.admin;

public class Dispatch {
    private long id;
    private String rid;//档案编号
    private String name;//全称
    private String nickname;//昵称
    private String address;
    private String contact;
    private String phone;
    private String wx;
    private String qq;
    private String intro;
    private int status;//是否为合作客户  0_ 潜在客户 1_合作客户

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Dispatch() {
    }

    public Dispatch(long id, String rid, String name, String nickname, String address, String contact, String phone, String wx, String qq, String intro, int status) {
        this.id = id;
        this.rid = rid;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
        this.contact = contact;
        this.phone = phone;
        this.wx = wx;
        this.qq = qq;
        this.intro = intro;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Dispatch{" +
                "id=" + id +
                ", rid='" + rid + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", wx='" + wx + '\'' +
                ", qq='" + qq + '\'' +
                ", intro='" + intro + '\'' +
                ", status=" + status +
                '}';
    }
}
