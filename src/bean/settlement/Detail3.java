package bean.settlement;


//商业结算单明细
public class Detail3 extends Detail {
    public static final byte STATUS_INSERT = 0;//新增;
    public static final byte STATUS_CONFIRMED = 1;//参保;
    public static final byte STATUS_REPLACED = -1;//被替换;
    private byte day;//生效日
    private byte status;//状态


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
}
