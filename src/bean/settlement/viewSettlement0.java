package bean.settlement;

import java.sql.Date;

public class viewSettlement0 extends Settlement0{
    private String name;//合作客户名称

    public viewSettlement0() {
    }

    public viewSettlement0(long id, long did, long cid, Date month, float amount, byte type, byte status, float tax, String name) {
        super(id, did, cid, month, amount, type, status, tax);
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
        return "viewSettlement0{" +
                "name='" + name + '\'' +
                '}';
    }
}
