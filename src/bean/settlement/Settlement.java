package bean.settlement;

import utills.Bit;

import java.util.Date;
import java.util.BitSet;

//结算单基类
public class Settlement {
    public static final byte STATUS_EDITING = 0;//编辑
    public static final byte STATUS_COMMITED = 1;//提交
    public static final byte STATUS_CHECKED1 = 2;//初审
    public static final byte STATUS_CHECKED2 = 3;//终审
    public static final byte STATUS_PAYED1 = 4;//扣款
    public static final byte STATUS_PAYED2 = 5;//发放
    private long id;
    private long did;//派遣方id
    private long cid;//合作客户id
    private String ccid;//合同id
    private Date month;//月份
    private byte status;

    private byte flag;//来源 0 派遣单位创建  1合作单位创建

    public Settlement() {
    }

    public Settlement(long id, long did, long cid, Date month, byte status, byte flag) {
        this.id = id;
        this.did = did;
        this.cid = cid;
        this.month = month;
        this.status = status;
        this.flag = flag;
    }

    public Settlement(long id, long did, long cid, String ccid, Date month, byte status, byte flag) {
        this.id = id;
        this.did = did;
        this.cid = cid;
        this.ccid = ccid;
        this.month = month;
        this.status = status;
        this.flag = flag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
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

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    //是否需要计算社保，补发工资和代发工资不需要计算五险一金
    public void setNeedCalculateSocial(boolean need){
        if(need){
            flag = (byte)Bit.set(flag,0);
        }else {
            flag = (byte)Bit.reset(flag,0);
        }
    }

    public String getCcid() {
        return ccid;
    }

    public void setCcid(String ccid) {
        this.ccid = ccid;
    }

    //判断是否需要计算五险一金，补发工资和代发工资不需要计算五险一金
    public boolean isNeedCalcInsurance(){
        return Bit.test(flag,0);
    }

    @Override
    public String toString() {
        return "Settlemet{" +
                "id=" + id +
                ", did=" + did +
                ", cid=" + cid +
                ", month=" + month +
                ", status=" + status +
                ", flag=" + flag +
                '}';
    }
}
