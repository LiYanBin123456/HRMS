package bean.rule;

import java.sql.Date;

public class Rule {
    private long id;
    private String city;
    private Date start;//生效时间
    private float per1;
    private float per2;

    public Rule() {
    }

    public Rule(long id, String city, Date start, float per1, float per2) {
        this.id = id;
        this.city = city;
        this.start = start;
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
        return "Rule{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", start=" + start +
                ", per1=" + per1 +
                ", per2=" + per2 +
                '}';
    }
}
