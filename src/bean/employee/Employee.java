package bean.employee;

import java.sql.Date;

//员工表
public class Employee {
   private long id;
    private long did;
    private long cid;
    private String  cardId;
    private String name;
    private String phone;
    private byte degree;
    private byte type;
    private Date entry;
    private byte status;
    private String department;
    private String post;
    private byte category;
    private float price;

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
