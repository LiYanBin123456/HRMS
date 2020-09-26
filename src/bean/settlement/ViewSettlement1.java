package bean.settlement;

import java.sql.Date;

public class ViewSettlement1 extends Settlement1{
    private String name;//合作客户名字

    public ViewSettlement1() {

    }

    public ViewSettlement1(long id, long did, long cid, Date month, byte status, byte source, float salary, float social, float medicare, float fund, float manage, float tax, float summary, String name) {
        super(id, did, cid, month, status, source, salary, social, medicare, fund, manage, tax, summary);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ViewSettlement1{" +
                "name='" + name + '\'' +
                '}';
    }
}
