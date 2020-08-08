package bean.rule;


import java.sql.Date;

//医保规则
public class RuleMedicare extends Rule{

    private float base;//基数
    private float per3;//生育单位比例
    private float fin1;//大病单位
    private float fin2;//大病个人

    public RuleMedicare() {
    }

    public RuleMedicare(long id, String city, Date start, float per1, float per2, float base, float per3, float fin1, float fin2) {
        super(id, city, start, per1, per2);
        this.base = base;
        this.per3 = per3;
        this.fin1 = fin1;
        this.fin2 = fin2;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public float getPer3() {
        return per3;
    }

    public void setPer3(float per3) {
        this.per3 = per3;
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

    @Override
    public String toString() {
        return "RuleMedicare{" +
                "base=" + base +
                ", per3=" + per3 +
                ", fin1=" + fin1 +
                ", fin2=" + fin2 +
                '}';
    }
}