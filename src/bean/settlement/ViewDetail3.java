package bean.settlement;

import java.sql.Date;

public class ViewDetail3 extends Detail3 {
    private String cname;//员工姓名
    private String pname;//保险产品名称
    private String cardId;//身份证号码

    public ViewDetail3() {
    }

    public ViewDetail3(long id, long sid, long eid, Date month, byte status, Long pid, String place, float price, String cname, String pname, String cardId) {
        super(id, sid, eid, month, status, pid, place, price);
        this.cname = cname;
        this.pname = pname;
        this.cardId = cardId;
    }

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

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return "ViewDetail3{" +
                "cname='" + cname + '\'' +
                ", pname='" + pname + '\'' +
                ", cardId='" + cardId + '\'' +
                '}';
    }
}
