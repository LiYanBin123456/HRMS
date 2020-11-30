package bean.settlement;

import java.sql.Date;

public class ViewSettlement0 extends Settlement0{
    private String name;//合作客户名称

    public ViewSettlement0() {
    }

    public ViewSettlement0(long id, long did, long cid, Date month, float amount, byte type, byte status, float tax, float paid, String name) {
        super(id, did, cid, month, amount, type, status, tax, paid);
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
        return "ViewSettlement0{" +
                "name='" + name + '\'' +
                '}';
    }
}
