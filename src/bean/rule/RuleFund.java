package bean.rule;

import java.sql.Date;

//公积金规则
public class RuleFund extends Rule{

    private float base;//基数
    private float min;//公积金下限
    private float max;//公积金上限

    public RuleFund() {
    }

    public RuleFund(long id, String city, Date start, float per1, float per2, float base, float min, float max) {
        super(id, city, start, per1, per2);
        this.base = base;
        this.min = min;
        this.max = max;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
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
                "base=" + base +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
