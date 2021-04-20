package bean.settlement;

import java.util.Date;

public class ViewSettlement1 extends Settlement1{
    private String name;//合作客户名字

    public ViewSettlement1() {

    }

    public ViewSettlement1(long id, long did, long cid, String ccid, Date month, byte status, byte flag, byte type, float salary, float social, float medicare, float fund, float buss, float manage, float tax, float extra, float free, float summary, String comments, String name) {
        super(id, did, cid, ccid, month, status, flag, type, salary, social, medicare, fund, buss, manage, tax, extra, free, summary, comments);
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
