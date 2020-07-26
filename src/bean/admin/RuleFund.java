package bean.admin;

import java.sql.Date;

//公积金规则
public class RuleFund {
    private long id;
    private String city;
    private Date start;//生效时间
    private float base;//基数
    private float min;//公积金下限
    private float max;//公积金上限
    private float per1;//单位比例
    private float per2;//个人比例

    public RuleFund() {
    }

    public RuleFund(long id, String city, Date start, float base, float min, float max, float per1, float per2) {
        this.id = id;
        this.city = city;
        this.start = start;
        this.base = base;
        this.min = min;
        this.max = max;
        this.per1 = per1;
        this.per2 = per2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getPer1() {
        return per1;
    }

    public void setPer1(float per1) {
        this.per1 = per1;
    }

    public float getPer2() {
        return per2;
    }

    public void setPer2(float per2) {
        this.per2 = per2;
    }

    @Override
    public String toString() {
        return "RuleFund{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", start=" + start +
                ", base=" + base +
                ", min=" + min +
                ", max=" + max +
                ", per1=" + per1 +
                ", per2=" + per2 +
                '}';
    }
}
