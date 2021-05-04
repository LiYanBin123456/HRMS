package bean.settlement;

import java.util.Date;

public class ViewSettlement3 extends Settlement3 {
    private String name;//合作客户姓名
    private String pname;//保险产品名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public ViewSettlement3() {
    }

    public ViewSettlement3(long id, long did, long cid, String ccid, Date month, byte status, byte flag, long pid, float price, int amount, double summary, String name, String pname) {
        super(id, did, cid, ccid, month, status, flag, pid, price, amount, summary);
        this.name = name;
        this.pname = pname;
    }
}
