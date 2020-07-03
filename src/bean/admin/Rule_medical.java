package bean.admin;

public class Rule_medical {
    private long id;
    private String city;//地市代码
    private String start;//开始日期
    private int base;//基数
    private float per1;//医疗单位比例
    private float per2;//医疗个人比例
    private float per3;//生育单位比例
    private float per4;//生育个人比例

    public Rule_medical() {
    }

    public Rule_medical(long id, String city, String start, int base, float per1, float per2, float per3, float per4) {
        this.id = id;
        this.city = city;
        this.start = start;
        this.base = base;
        this.per1 = per1;
        this.per2 = per2;
        this.per3 = per3;
        this.per4 = per4;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
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


    public int getBase() {
        return base;
    }

    public void setBase(int base) {
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

    public float getPer4() {
        return per4;
    }

    public void setPer4(float per4) {
        this.per4 = per4;
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
                ", per4=" + per4 +
                '}';
    }
}