package bean.insurance;
//保险产品
public class Product {
    private long id;//产品id
    private long did;//派遣单位id
    private String name;//产品名称
    private float fin1;//产品保额
    private float fin2;//医疗保额
    private float allowance;//住院津贴
    /**保险时间
     0_上班时间
     1_24小时
     */
    private byte period;
    /**可参保人员类型（采用位运算）
     第0位第一类
     第1位第二类
     第2位第三类
     第3位第四类
     第4位第五类
     第5位第六类
     */
    private byte allow;
    private byte min;//年龄下限
    private byte max;//年龄上限
    private String intro;//产品介绍

    public Product() {
    }

    public Product(long id, long did, String name, float fin1, float fin2, float allowance, byte period, byte allow, byte min, byte max, String intro) {
        this.id = id;
        this.did = did;
        this.name = name;
        this.fin1 = fin1;
        this.fin2 = fin2;
        this.allowance = allowance;
        this.period = period;
        this.allow = allow;
        this.min = min;
        this.max = max;
        this.intro = intro;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public float getAllowance() {
        return allowance;
    }

    public void setAllowance(float allowance) {
        this.allowance = allowance;
    }

    public byte getPeriod() {
        return period;
    }

    public void setPeriod(byte period) {
        this.period = period;
    }

    public byte getAllow() {
        return allow;
    }

    public void setAllow(byte allow) {
        this.allow = allow;
    }

    public byte getMin() {
        return min;
    }

    public void setMin(byte min) {
        this.min = min;
    }

    public byte getMax() {
        return max;
    }

    public void setMax(byte max) {
        this.max = max;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", did=" + did +
                ", name='" + name + '\'' +
                ", fin1=" + fin1 +
                ", fin2=" + fin2 +
                ", allowance=" + allowance +
                ", period=" + period +
                ", allow=" + allow +
                ", min=" + min +
                ", max=" + max +
                ", intro='" + intro + '\'' +
                '}';
    }
}
