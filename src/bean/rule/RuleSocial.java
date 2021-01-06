package bean.rule;

import java.util.Date;

//社保规则
public class RuleSocial extends Rule{

    private float per1;//养老单位比例
    private float per2;//养老个人比例
    private float base;//基数
    private float per3;//工伤单位比例
    private float extra;//工伤补充
    private float per4;//失业单位比例
    private float per5;//失业个人比例

    public RuleSocial() {
    }

    public RuleSocial(long id, String city, Date start, float per1, float per2, float base, float per3, float extra, float per4, float per5) {
        super(id, city, start);
        this.per1 = per1;
        this.per2 = per2;
        this.base = base;
        this.per3 = per3;
        this.extra = extra;
        this.per4 = per4;
        this.per5 = per5;
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
}
