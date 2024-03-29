package bean.settlement;

import java.util.Date;

public class ViewSettlement4 extends Settlement4{
    private String name;//合作客户名称

    public ViewSettlement4() {
    }

    public ViewSettlement4(long id, long did, long cid, String ccid, Date month, float amount, byte type, byte status, float manage, float tax, float paid, String name) {
        super(id, did, cid, ccid, month, amount, type, status, manage, tax, paid);
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
        return "ViewSettlement4{" +
                "name='" + name + '\'' +
                '}';
    }
}
