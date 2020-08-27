package bean.settlement;


import java.sql.Date;

//商业结算单明细
public class Detail3 extends Detail {

    private Long pid;//保险产品id
    private String place;//工作地点
    private float price;//保费

    public Detail3() {
    }


    public Detail3(long id, long sid, long eid, Date month, byte status, Long pid, String place, float price) {
        super(id, sid, eid, month);
        this.pid = pid;
        this.place = place;
        this.price = price;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Detail3{" +
                "pid=" + pid +
                ", place='" + place + '\'' +
                ", price=" + price +
                '}';
    }
}
