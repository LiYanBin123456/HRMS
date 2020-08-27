package bean.settlement;

import java.sql.Date;

//结算单明细基类
public class Detail {
    private long id;//工资表id
    private long sid;//结算单id
    private long eid;//员工id
    private Date month;//月份


    public Detail() {
    }

    public Detail(long id, long sid, long eid, Date month) {
        this.id = id;
        this.sid = sid;
        this.eid = eid;
        this.month = month;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }


    @Override
    public String toString() {
        return "Detail{" +
                "id=" + id +
                ", sid=" + sid +
                ", eid=" + eid +
                ", month=" + month +
                '}';
    }
}
