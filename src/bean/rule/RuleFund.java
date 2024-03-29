package bean.rule;

import java.util.Date;

//公积金规则
public class RuleFund extends Rule{

    private float per1;//比例上限
    private float per2;//比例下限
    private float min;//公积金下限
    private float max;//公积金上限

    public RuleFund() {
    }

    public RuleFund(long id, String city, Date start, float per1, float per2, float min, float max) {
        super(id, city, start);
        this.per1 = per1;
        this.per2 = per2;
        this.min = min;
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

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "RuleFund{" +
                "per1=" + per1 +
                ", per2=" + per2 +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
