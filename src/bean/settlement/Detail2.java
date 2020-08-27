package bean.settlement;

import java.sql.Date;

//小时工结算单明细
public class Detail2 extends Detail {

    private int hours;//工时
    private float price;//员工表中的价格
    private float food;//餐费
    private float traffic;//交通费
    private float accommodation;//住宿费
    private float utilities;//水电费
    private float insurance;//保险费
    private float tax;//个税
    private float other1;//
    private float other2;//
    private float payable;//应付金额
    private float paid;//实付金额

    public Detail2() {
    }

    public Detail2(long id, long sid, long eid, Date month, byte status, int hours, float price, float food, float traffic, float accommodation, float utilities, float insurance, float tax, float other1, float other2, float payable, float paid) {
        super(id, sid, eid, month);
        this.hours = hours;
        this.price = price;
        this.food = food;
        this.traffic = traffic;
        this.accommodation = accommodation;
        this.utilities = utilities;
        this.insurance = insurance;
        this.tax = tax;
        this.other1 = other1;
        this.other2 = other2;
        this.payable = payable;
        this.paid = paid;
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

    public float getFood() {
        return food;
    }

    public void setFood(float food) {
        this.food = food;
    }

    public float getTraffic() {
        return traffic;
    }

    public void setTraffic(float traffic) {
        this.traffic = traffic;
    }

    public float getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(float accommodation) {
        this.accommodation = accommodation;
    }

    public float getUtilities() {
        return utilities;
    }

    public void setUtilities(float utilities) {
        this.utilities = utilities;
    }

    public float getInsurance() {
        return insurance;
    }

    public void setInsurance(float insurance) {
        this.insurance = insurance;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getOther1() {
        return other1;
    }

    public void setOther1(float other1) {
        this.other1 = other1;
    }

    public float getOther2() {
        return other2;
    }

    public void setOther2(float other2) {
        this.other2 = other2;
    }

    public float getPayable() {
        return payable;
    }

    public void setPayable(float payable) {
        this.payable = payable;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "Detail2{" +
                "hours=" + hours +
                ", price=" + price +
                ", food=" + food +
                ", traffic=" + traffic +
                ", accommodation=" + accommodation +
                ", utilities=" + utilities +
                ", insurance=" + insurance +
                ", tax=" + tax +
                ", other1=" + other1 +
                ", other2=" + other2 +
                ", payable=" + payable +
                ", paid=" + paid +
                '}';
    }
}
