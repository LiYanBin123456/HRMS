package bean.settlement;


import java.util.Date;
import java.util.List;

//小时工结算单
public class Settlement2 extends Settlement {

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

    /**
     * 计算小时工结算单
     * @param details 员工明细
     * @param payer 支付方 0_派遣方发工资  1_合作方发工资
     */
    public void calc(List<Detail2> details, byte payer) {
        hours=0;//总工时
        traffic=0;//交通费
        extra=0;//附加
        summary = 0;//总额
        for (Detail2 detail2:details){
            hours+=detail2.getHours();
            traffic+=detail2.getTraffic();
            extra+=(detail2.getOther1()+detail2.getOther2());
        }
        if(payer==0){//派遣方发工资
            //总额= 总工时*单位的单价+交通费+额外收入
            summary=hours*price+traffic+extra;
        }else {//合作方发工资
            //差额 = 单位的单价-小时工的单价
            float difference = price-details.get(0).getPrice();//差额 = 单位的单价-小时工的单价
            //总额= 总工时*单位与员工的单价差+交通费+额外收入
            summary=hours*difference;
            extra=0;
            traffic=0;
        }
    }
}
