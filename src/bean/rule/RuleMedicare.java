package bean.rule;


import java.util.Date;

//医保规则
public class RuleMedicare extends Rule{

    private float per1;//医疗单位比例
    private float per2;//医疗个人比例
    private float base1;//医疗基数
    private float base2;//生育基数
    private float base3;//大病基数
    private float per3;//生育单位比例
    private float fin1;//大病单位
    private float fin2;//大病个人

    public RuleMedicare() {
    }

    public RuleMedicare(long id, String city, Date start, float per1, float per2, float base1, float base2, float base3, float per3, float fin1, float fin2) {
        super(id, city, start);
        this.per1 = per1;
        this.per2 = per2;
        this.base1 = base1;
        this.base2 = base2;
        this.base3 = base3;
        this.per3 = per3;
        this.fin1 = fin1;
        this.fin2 = fin2;
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

    public float getBase1() {
        return base1;
    }

    public void setBase1(float base1) {
        this.base1 = base1;
    }

    public float getBase2() {
        return base2;
    }

    public void setBase2(float base2) {
        this.base2 = base2;
    }

    public float getBase3() {
        return base3;
    }

    public void setBase3(float base3) {
        this.base3 = base3;
    }
}
