package bean.settlement;


public class Detail0 {
    private long id;
    private long sid;
    private long eid;
    private float amount;
    private float tax;
    private float paid;

    public Detail0() {
    }

    public Detail0(long id, long sid, long eid, float amount, float tax, float paid) {
        this.id = id;
        this.sid = sid;
        this.eid = eid;
        this.amount = amount;
        this.tax = tax;
        this.paid = paid;
    }

    public Detail0(long sid, long eid, float amount, float tax, float paid) {
        this.sid = sid;
        this.eid = eid;
        this.amount = amount;
        this.tax = tax;
        this.paid = paid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "Detail0{" +
                "id=" + id +
                ", sid=" + sid +
                ", eid=" + eid +
                ", amount=" + amount +
                ", tax=" + tax +
                ", paid=" + paid +
                '}';
    }
}
