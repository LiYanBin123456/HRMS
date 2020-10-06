package bean.settlement;

import java.sql.Date;

public class ViewSettlement3 extends Settlement3 {
    private String cname;//合作客户姓名
    private String pname;//保险产品名称
    private String post;//工作岗位

    public ViewSettlement3() {
    }

    public ViewSettlement3(long id, long did, long cid, Date month, byte status, byte source, long pid, float price, String cname, String pname, String post) {
        super(id, did, cid, month, status, source, pid, price);
        this.cname = cname;
        this.pname = pname;
        this.post = post;
    }

    public ViewSettlement3(long id, long did, long cid, String ccid, Date month, byte status, byte source, long pid, float price, String cname, String pname, String post) {
        super(id, did, cid, ccid, month, status, source, pid, price);
        this.cname = cname;
        this.pname = pname;
        this.post = post;
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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "ViewSettlement3{" +
                "cname='" + cname + '\'' +
                ", pname='" + pname + '\'' +
                ", post='" + post + '\'' +
                '}';
    }
}
