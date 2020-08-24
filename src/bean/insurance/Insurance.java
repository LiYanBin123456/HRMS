package bean.insurance;

import java.sql.Date;

//参保单
public class Insurance {
    private long eid;//员工id
    /**类型
     0_社保参保单
     1_医保参保单
     2_公积金参保单
     */
    private byte type;
    private String code;//个人代码
    private Date start;//参保时间
    private float money;//月缴费工资
    /**参保状态
     0_新增
     1_在保
     2_拟停
     3_停保
     */
    private byte status;
    private String reason;//变更原因

    public Insurance() {
    }

    public Insurance(long eid, byte type, String code, Date start, float money, byte status, String reason) {
        this.eid = eid;
        this.type = type;
        this.code = code;
        this.start = start;
        this.money = money;
        this.status = status;
        this.reason = reason;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Insurance{" +
                "eid=" + eid +
                ", type=" + type +
                ", code='" + code + '\'' +
                ", start=" + start +
                ", money=" + money +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}
