package bean.settlement;

import java.sql.Date;

public class ViewSettlement3 extends Settlement3 {
    private String cname;//合作客户姓名
    private String pname;//保险产品名称

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
