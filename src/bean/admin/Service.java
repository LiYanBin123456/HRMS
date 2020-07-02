package bean.admin;

public class Service {
    private long id;//服务项目id
    private String cid;//外键  合同id
    private int project;//位运算，每一位表示一种服务项目的有无  第0位 发放工资  第1位  代缴社保   第2位 代缴公积金  例如（010 代表  代缴社保）
    private int category;//结算方式  0_按人数收取  1_按比例收取  2_外包整体核算  3_小时工
    private float value;//结算值 根据结算方式的不同而不同，因为按比例是百分比，因此设置为float类型
    private int payment;//工资放发日，即不能晚于每月的之地呢日期
    private int settlement;//结算日
    private int receipt;//回款日

    public Service() {
    }

    public Service(long id, String cid, int project, int category, float value, int payment, int settlement, int receipt) {
        this.id = id;
        this.cid = cid;
        this.project = project;
        this.category = category;
        this.value = value;
        this.payment = payment;
        this.settlement = settlement;
        this.receipt = receipt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getSettlement() {
        return settlement;
    }

    public void setSettlement(int settlement) {
        this.settlement = settlement;
    }

    public int getReceipt() {
        return receipt;
    }

    public void setReceipt(int receipt) {
        this.receipt = receipt;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", cid='" + cid + '\'' +
                ", project=" + project +
                ", category=" + category +
                ", value=" + value +
                ", payment=" + payment +
                ", settlement=" + settlement +
                ", receipt=" + receipt +
                '}';
    }
}
