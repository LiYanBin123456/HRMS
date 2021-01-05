package bean.employee;

import java.util.Date;

//员工表
public class Employee {
    public static final byte WORKING = 0;
    public static final byte LEAVED = 1;
    public static final byte RETIRE = 2;
    public static final byte OTHER = 3;
   private long id;
    private long did;//派遣方id
    private long cid;//合作单位id
    private String  cardId;//身份证号
    private String name;//姓名
    private String phone;//电话
    /**
     * 高中以下_0
     高中_1
     中专_2
     大专_3
     本科_4
     研究生_5
     博士生_6
     */
    private byte degree;//学历
    /**
     * 内部员工_0
     外派员工_1
     人才库_2
     */
    private byte type;//类型
    private Date entry;//入职时间
    private byte status;//在职状态
    /**
     * 在职_0
     离职_1
     退休_2
     其他_3
     */
    private String department;//部门
    private String post;//岗位
    /**户口性质
     0_null、
     1_外派、
     2_派遣、
     3_小时工、
     4_代缴工资
     5_代缴社保
     */
    private byte category;
    private float price;//小时工时间报酬

    public Employee() {
    }

    public Employee(long id, long did, long cid, String cardId, String name, String phone, byte degree, byte type, Date entry, byte status, String department, String post, byte category, float price) {
        this.id = id;
        this.did = did;
        this.cid = cid;
        this.cardId = cardId;
        this.name = name;
        this.phone = phone;
        this.degree = degree;
        this.type = type;
        this.entry = entry;
        this.status = status;
        this.department = department;
        this.post = post;
        this.category = category;
        this.price = price;
    }

    public static byte getWORKING() {
        return WORKING;
    }

    public static byte getOUTGOING() {
        return LEAVED;
    }

    public static byte getRETIRE() {
        return RETIRE;
    }

    public static byte getOTHER() {
        return OTHER;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte getDegree() {
        return degree;
    }

    public void setDegree(byte degree) {
        this.degree = degree;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Date getEntry() {
        return entry;
    }

    public void setEntry(Date entry) {
        this.entry = entry;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", did=" + did +
                ", cid=" + cid +
                ", cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", degree=" + degree +
                ", type=" + type +
                ", entry=" + entry +
                ", status=" + status +
                ", department='" + department + '\'' +
                ", post='" + post + '\'' +
                ", category=" + category +
                ", price=" + price +
                '}';
    }
}
