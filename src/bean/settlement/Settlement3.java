package bean.settlement;

import java.sql.Date;

//商业保险结算单
public class Settlement3 extends Settlemet {
    private long pid;//产品id
    private float price;//保费

    public Settlement3() {
    }

    public Settlement3(long id, long did, long cid, Date month, byte status, byte source, long pid, float price) {
        super(id, did, cid, month, status, source);
        this.pid = pid;
        this.price = price;
    }

    public Settlement3(long id, long did, long cid, String ccid, Date month, byte status, byte source, long pid, float price) {
        super(id, did, cid, ccid, month, status, source);
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

    @Override
    public String toString() {
        return "Settlement3{" +
                "pid=" + pid +
                ", price=" + price +
                '}';
    }
}
