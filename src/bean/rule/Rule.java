package bean.rule;

import java.util.Date;

public class Rule {
    private long id;
    private String city;
    private Date start;//生效时间


    public Rule() {
    }

    public Rule(long id, String city, Date start) {
        this.id = id;
        this.city = city;
        this.start = start;
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

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", start=" + start +
                '}';
    }
}
