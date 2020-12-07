package bean.settlement;

import java.sql.Date;

//商业保险结算单
public class Settlement3 extends Settlemet {
    private long pid;//产品id
    private float price;//单价
    private int amount;//人数
    private double summary;//保费

    public Settlement3() {
    }

    public Settlement3(long id, long did, long cid, String ccid, Date month, byte status, byte flag, long pid, float price) {
        super(id, did, cid, ccid, month, status, flag);
        this.pid = pid;
        this.price = price;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getSummary() {
        return summary;
    }

    public void setSummary(double summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "Settlement3{" +
                "pid=" + pid +
                ", price=" + price +
                '}';
    }
}
