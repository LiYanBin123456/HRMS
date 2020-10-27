package bean.insurance;

import java.sql.Date;

//参保单
public class Insurance {
    public static byte STATUS_APPENDING = 0;//新增
    public static byte STATUS_NORMAL = 1;//在保
    public static byte STATUS_STOPING = 2;//拟停
    public static byte STATUS_STOPED = 3;//停保

    private long eid;//员工id
    private String code;//个人代码
    private Date date1;//医保参保时间
    private Date date2;//公积金参保时间
    private Date date3;//养老保险参保时间
    private Date date4;//失业保险参保时间
    private Date date5;//工伤保险参保时间
    private byte status1;//医保状态 0 新增 1在保 2拟停 3停保
    private byte status2;//公积金状态 0 新增 1在保 2拟停 3停保
    private byte status3;//养老保险状态 0 新增 1在保 2拟停 3停保
    private byte status4;//失业保险状态 0 新增 1在保 2拟停 3停保
    private byte status5;//工伤保险状态 0 新增 1在保 2拟停 3停保
    private float base1;//医保基数
    private float base2;//公积金基数
    private float base3;//社保基数

    public Insurance() {
    }

    public Insurance(long eid, String code, Date date1, Date date2, Date date3, Date date4, Date date5, byte status1, byte status2, byte status3, byte status4, byte status5, float base1, float base2, float base3) {
        this.eid = eid;
        this.code = code;
        this.date1 = date1;
        this.date2 = date2;
        this.date3 = date3;
        this.date4 = date4;
        this.date5 = date5;
        this.status1 = status1;
        this.status2 = status2;
        this.status3 = status3;
        this.status4 = status4;
        this.status5 = status5;
        this.base1 = base1;
        this.base2 = base2;
        this.base3 = base3;
    }



    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getDate3() {
        return date3;
    }

    public void setDate3(Date date3) {
        this.date3 = date3;
    }

    public Date getDate4() {
        return date4;
    }

    public void setDate4(Date date4) {
        this.date4 = date4;
    }

    public Date getDate5() {
        return date5;
    }

    public void setDate5(Date date5) {
        this.date5 = date5;
    }

    public byte getStatus1() {
        return status1;
    }

    public void setStatus1(byte status1) {
        this.status1 = status1;
    }

    public byte getStatus2() {
        return status2;
    }

    public void setStatus2(byte status2) {
        this.status2 = status2;
    }

    public byte getStatus3() {
        return status3;
    }

    public void setStatus3(byte status3) {
        this.status3 = status3;
    }

    public byte getStatus4() {
        return status4;
    }

    public void setStatus4(byte status4) {
        this.status4 = status4;
    }

    public byte getStatus5() {
        return status5;
    }

    public void setStatus5(byte status5) {
        this.status5 = status5;
    }

    public float getBase1() {
        return base1;
    }

    public void setBase1(float base1) {
        this.base1 = base1;
    }

    public float getBase2() {
        return base2;
    }

    public void setBase2(float base2) {
        this.base2 = base2;
    }

    public float getBase3() {
        return base3;
    }

    public void setBase3(float base3) {
        this.base3 = base3;
    }

    public static byte getStatusAppending() {
        return STATUS_APPENDING;
    }

    public static void setStatusAppending(byte statusAppending) {
        STATUS_APPENDING = statusAppending;
    }

    public static byte getStatusNormal() {
        return STATUS_NORMAL;
    }

    public static void setStatusNormal(byte statusNormal) {
        STATUS_NORMAL = statusNormal;
    }

    public static byte getStatusStoping() {
        return STATUS_STOPING;
    }

    public static void setStatusStoping(byte statusStoping) {
        STATUS_STOPING = statusStoping;
    }

    public static byte getStatusStoped() {
        return STATUS_STOPED;
    }

    public static void setStatusStoped(byte statusStoped) {
        STATUS_STOPED = statusStoped;
    }

    @Override
    public String toString() {
        return "Insurance{" +
                "eid=" + eid +
                ", code='" + code + '\'' +
                ", date1=" + date1 +
                ", date2=" + date2 +
                ", date3=" + date3 +
                ", date4=" + date4 +
                ", date5=" + date5 +
                ", status1=" + status1 +
                ", status2=" + status2 +
                ", status3=" + status3 +
                ", status4=" + status4 +
                ", status5=" + status5 +
                '}';
    }
}
