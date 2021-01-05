package bean.settlement;

import java.util.Date;

public class ViewDetailTotal {
    private long eid;
    private long cid;
    private float taxs;
    private float payables;
    private Date month;
    private byte type;
    private byte status;

    public ViewDetailTotal() {
    }

    public ViewDetailTotal(long eid, long cid, float taxs, float payables, Date month, byte type, byte status) {
        this.eid = eid;
        this.cid = cid;
        this.taxs = taxs;
        this.payables = payables;
        this.month = month;
        this.type = type;
        this.status = status;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public float getTaxs() {
        return taxs;
    }

    public void setTaxs(float taxs) {
        this.taxs = taxs;
    }

    public float getPayables() {
        return payables;
    }

    public void setPayables(float payables) {
        this.payables = payables;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }
}
