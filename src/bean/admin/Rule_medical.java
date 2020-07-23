package bean.admin;

import java.sql.Date;

public class Rule_medical {
    private long id;
    private String city;//地市代码
    private Date start;//开始日期
    private float base;//基数
    private float per1;//医疗单位比例
    private float per2;//医疗个人比例
    private float per3;//生育单位比例
    private float fin1;//大病单位
    private float fin2;//大病个人

    public Rule_medical() {
    }


    public Rule_medical(long id, String city, Date start, float base, float per1, float per2, float per3, float fin1, float fin2) {
        this.id = id;
        this.city = city;
        this.start = start;
        this.base = base;
        this.per1 = per1;
        this.per2 = per2;
        this.per3 = per3;
        this.fin1 = fin1;
        this.fin2 = fin2;
    }

    public float getFin1() {
        return fin1;
    }

    public void setFin1(float fin1) {
        this.fin1 = fin1;
    }

    public float getFin2() {
        return fin2;
    }

    public void setFin2(float fin2) {
        this.fin2 = fin2;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
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


    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
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

    public float getPer3() {
        return per3;
    }

    public void setPer3(float per3) {
        this.per3 = per3;
    }

    @Override
    public String toString() {
        return "Rule_medical{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", start='" + start + '\'' +
                ", base=" + base +
                ", per1=" + per1 +
                ", per2=" + per2 +
                ", per3=" + per3 +
                ", fin1=" + fin1 +
                ", fin2=" + fin2 +
                '}';
    }
}
