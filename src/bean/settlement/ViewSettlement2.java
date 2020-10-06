package bean.settlement;

import java.sql.Date;

public class ViewSettlement2 extends Settlement2{
    private String name;

    public ViewSettlement2() {
    }

    public ViewSettlement2(long id, long did, long cid, Date month, byte status, byte source, int hours, float price, float traffic, float extra, float summary, String name) {
        super(id, did, cid, month, status, source, hours, price, traffic, extra, summary);
        this.name = name;
    }

    public ViewSettlement2(long id, long did, long cid, String ccid, Date month, byte status, byte source, int hours, float price, float traffic, float extra, float summary, String name) {
        super(id, did, cid, ccid, month, status, source, hours, price, traffic, extra, summary);
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
        return "ViewSettlement2{" +
                "name='" + name + '\'' +
                '}';
    }
}
