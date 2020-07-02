package bean.admin;

public class Rule_social {
    private long id;
    private String city;//地市代码
    private String start;//开始日期
    private int base;//基数
    private float per1;//养老单位比例
    private float per2;//养老个人比例
    private float per3;//工伤单位比例
    private float extra;//工伤补充
    private float per4;//失业单位比例
    private float per5;//失业个人比例

    public Rule_social() {
    }

    public Rule_social(long id, String city, String start, int base, float per1, float per2, float per3, float extra, float per4, float per5) {
        this.id = id;
        this.city = city;
        this.start = start;
        this.base = base;
        this.per1 = per1;
        this.per2 = per2;
        this.per3 = per3;
        this.extra = extra;
        this.per4 = per4;
        this.per5 = per5;
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
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

    public float getExtra() {
        return extra;
    }

    public void setExtra(float extra) {
        this.extra = extra;
    }

    public float getPer4() {
        return per4;
    }

    public void setPer4(float per4) {
        this.per4 = per4;
    }

    public float getPer5() {
        return per5;
    }

    public void setPer5(float per5) {
        this.per5 = per5;
    }

    @Override
    public String toString() {
        return "Rule_social{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", start='" + start + '\'' +
                ", base=" + base +
                ", per1=" + per1 +
                ", per2=" + per2 +
                ", per3=" + per3 +
                ", extra=" + extra +
                ", per4=" + per4 +
                ", per5=" + per5 +
                '}';
    }
}
