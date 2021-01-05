package bean.settlement;


import java.util.Date;

//小时工结算单
public class Settlement2 extends Settlemet{

    private  int hours;//总工时
    private  float price;//合作客户所给的单价
    private  float traffic;//交通费
    private  float extra;//附加
    private  float summary;//总额

    public Settlement2() {

    }

    public Settlement2(long id, long did, long cid, Date month, byte status, byte source, int hours, float price, float traffic, float extra, float summary) {
        super(id, did, cid, month, status, source);
        this.hours = hours;
        this.price = price;
        this.traffic = traffic;
        this.extra = extra;
        this.summary = summary;
    }

    public Settlement2(long id, long did, long cid, String ccid, Date month, byte status, byte source, int hours, float price, float traffic, float extra, float summary) {
        super(id, did, cid, ccid, month, status, source);
        this.hours = hours;
        this.price = price;
        this.traffic = traffic;
        this.extra = extra;
        this.summary = summary;
    }

    public Settlement2(int hours, float price, float traffic, float extra, float summary) {
        this.hours = hours;
        this.price = price;
        this.traffic = traffic;
        this.extra = extra;
        this.summary = summary;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTraffic() {
        return traffic;
    }

    public void setTraffic(float traffic) {
        this.traffic = traffic;
    }

    public float getExtra() {
        return extra;
    }

    public void setExtra(float extra) {
        this.extra = extra;
    }

    public float getSummary() {
        return summary;
    }

    public void setSummary(float summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "Settlement2{" +
                "hours=" + hours +
                ", price=" + price +
                ", traffic=" + traffic +
                ", extra=" + extra +
                ", summary=" + summary +
                '}';
    }
}
