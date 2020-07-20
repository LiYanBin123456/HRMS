package bean.admin;

public class Client {
    private long id;
    private String rid;//档案编号
    private String name;//全称
    private String nickname;//昵称
    /**
     * 客户类型
      0_平台管理员管理劳务派遣单位
      1_劳务派遣单位管理合作单位
     */
    private int type;
    /**
     *是否为合作客户
     0_ 潜在客户
     1_合作客户
     2_供应商(劳务派遣单位才有)

     */
    private int status;
    /**
     * 客户性质，只有派遣单位才有
     0_政府部门
     1_事业单位
     2_人才市场、
     3_学校
     4_内资企业
     5_外资企业
     6_港澳台企业
     7_内资工厂
     8_外资工厂
     9_港澳台工厂
     10_其它
     */
    private int category;
    /**
     * 业务类型（供应商才有）
     0_顶岗实习
     1_就业安置
     2_其他
     */
    private int business;
    private String address;
    private String contact;
    private String phone;
    private String wx;
    private String qq;
    private String intro;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
    public int getBusiness() {
        return business;
    }
    public void setBusiness(int business) {
        this.business = business;
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


    public String getRid() {

        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Client() {
    }

    public Client(long id, String rid, String name, String nickname, int type, int status, int category, int business, String address, String contact, String phone, String wx, String qq, String intro) {
        this.id = id;
        this.rid = rid;
        this.name = name;
        this.nickname = nickname;
        this.type = type;
        this.status = status;
        this.category = category;
        this.business = business;
        this.address = address;
        this.contact = contact;
        this.phone = phone;
        this.wx = wx;
        this.qq = qq;
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", rid='" + rid + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", category=" + category +
                ", business=" + business +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", wx='" + wx + '\'' +
                ", qq='" + qq + '\'' +
                ", intro='" + intro + '\'' +
                '}';
    }
}
