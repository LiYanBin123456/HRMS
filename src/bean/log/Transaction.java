package bean.log;

import java.util.Date;
//资金明细表
public class Transaction {
    private long cid;//合作单位公司id
    private Date time;//时间
    private float money;//往来资金金额
    private String comments;//内容

    public Transaction() {
    }

    public Transaction(long cid, Date time, float money, String comments) {
        this.cid = cid;
        this.time = time;
        this.money = money;
        this.comments = comments;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "cid=" + cid +
                ", time=" + time +
                ", money=" + money +
                ", comments='" + comments + '\'' +
                '}';
    }
}
