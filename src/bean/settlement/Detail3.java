package bean.settlement;


//商业结算单明细
public class Detail3 extends Detail {
    public static final byte STATUS_INSERT = 0;//新增;
    public static final byte STATUS_CONFIRMED = 2;//参保;
    public static final byte STATUS_REPLACING_UP = 1;//拟换上;
    public static final byte STATUS_REPLACING_DOWN = -1;//拟换下;
    public static final byte STATUS_REPLACED = -2;//替换;

    private byte day;//生效日
    private byte status;//状态
    private long uid;//替换之后的id

    public byte getDay() {
        return day;
    }

    public void setDay(byte day) {
        this.day = day;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
