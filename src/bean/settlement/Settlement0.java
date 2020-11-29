package bean.settlement;

import java.sql.Date;

public class Settlement0 {
    private long id;
    private long did;
    private long cid;
    private Date month;
    private float amount;
    private byte type;//类型 0派遣  1 外包
    private byte status;//状态
    private  float tax;

    public Settlement0() {
    }

    public Settlement0(long id, long did, long cid, Date month, float amount, byte type, byte status, float tax) {
        this.id = id;
        this.did = did;
        this.cid = cid;
        this.month = month;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.tax = tax;
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

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Settlement0{" +
                "id=" + id +
                ", did=" + did +
                ", cid=" + cid +
                ", month=" + month +
                ", amount=" + amount +
                ", tax=" + tax +
                '}';
    }
}
