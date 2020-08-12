package bean.settlement;

import java.sql.Date;

//结算单明细基类
public class Detail {
    private long id;//工资表id
    private long sid;//结算单id
    private long eid;//员工id
    private Date month;//月份
    /**状态
     0正常
     1补缴
     2补差
     */
    private byte status;

    public Detail() {
    }

    public Detail(long id, long sid, long eid, Date month, byte status) {
        this.id = id;
        this.sid = sid;
        this.eid = eid;
        this.month = month;
        this.status = status;
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

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "id=" + id +
                ", sid=" + sid +
                ", eid=" + eid +
                ", month=" + month +
                ", status=" + status +
                '}';
    }
}
