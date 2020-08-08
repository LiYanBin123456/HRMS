package bean.client;

//客户基类
public class Client {
    private long id;
    private long aid;//所属管理员id
    private String rid;//档案编号
    private String name;//全称
    private String nickname;//昵称
    private String address;
    private String contact;
    private String phone;
    private String wx;
    private String qq;
    private String mail;
    private String intro;

    public Client() {
    }

    public Client(long id, long aid, String rid, String name, String nickname, String address, String contact, String phone, String wx, String qq, String mail, String intro) {
        this.id = id;
        this.aid = aid;
        this.rid = rid;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
        this.contact = contact;
        this.phone = phone;
        this.wx = wx;
        this.qq = qq;
        this.mail = mail;
        this.intro = intro;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", aid=" + aid +
                ", rid='" + rid + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", wx='" + wx + '\'' +
                ", qq='" + qq + '\'' +
                ", mail='" + mail + '\'' +
                ", intro='" + intro + '\'' +
                '}';
    }
}
