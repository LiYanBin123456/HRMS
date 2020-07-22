package bean.admin;

public class Rule_fund {
    private long id;
    private String city;
    private String start;//生效时间
    private int base;//基数
    private int min;//公积金下限
    private int max;//公积金上限
    private float per1;//单位比例
    private float per2;//个人比例

    public Rule_fund() {
    }

    public Rule_fund(long id, String city, String start, int base, int min, int max, float per1, float per2) {
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

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
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
        return "Rule_fund{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", start='" + start + '\'' +
                ", base=" + base +
                ", min=" + min +
                ", max=" + max +
                ", per1=" + per1 +
                ", per2=" + per2 +
                '}';
    }
}
