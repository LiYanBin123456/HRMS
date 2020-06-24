package bean;


import java.util.Date;

public class Candidate {

    public static byte DEGREE_UNDERHIGHSCHOOL = 0;//高中以下
    public static byte DEGREE_HINHSCHOOL = 1;//高中
    public static byte DEGREE_SPECIALSCHOOL = 2;//中专
    public static byte DEGREE_JUNIORSCHOOL = 3;//大专
    public static byte DEGREE_UNDERGRADUATE = 4;//本科
    public static byte DEGREE_MASTER = 5;//硕士
    public static byte DEGREE_DOCTOR = 6;//博士

    public static byte PREPARE0 = 0;//随时到岗
    public static byte PREPARE7 = 1;//1周以内
    public static byte PREPARE21 = 2;//3周以内
    public static byte PREPARE30 = 3;//1个月以内
    public static byte PREPARE90 = 4;//3个月以内
    public static byte PREPARE_UNCERTAIN = 5;//不确定

    public static byte SEX_MALE = 0;//男
    public static byte SEX_FEMALE = 1;//女

    public static byte STATUS_MODIFING = 0;//待修改
    public static byte STATUS_CHECKING = 1;//待审核
    public static byte STATUS_CHECKED = 2;//已审核
    public static byte STATUS_CLOSED = 3;//已关闭

    public static byte STATE_RESIGNED = 0;//已离职，需求新工作
    public static byte STATE_ONTHEJOB = 1;//在职，考虑更好职位
    public static byte STATE_OTHER = 2;//其他

    public long id;//账号编号，与账号表中的id对应
    private  String name;//姓名
    private  byte sex;//性别
    private Date birthday;//出生年月
    private String origin;//籍贯
    private String address;//居住城市
    private  byte degree;//学历
    private  String phone;//电话号码
    private int years;//工作年限
    private  String mail;//邮箱
    private  int score;//积分余额
    private  byte prepare;//到岗时间
    private  byte state;//求职状态
    private  Date modtime;//最后修改状态
    private  byte flag;//标志位
    private  byte status;//简历状态

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte getPrepare() {
        return prepare;
    }

    public void setPrepare(byte prepare) {
        this.prepare = prepare;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public Date getModtime() {
        return modtime;
    }

    public void setModtime(Date modtime) {
        this.modtime = modtime;
    }

    public byte getFlag() {
        return flag;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte getDegree() {
        return degree;
    }

    public void setDegree(byte degree) {
        this.degree = degree;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
