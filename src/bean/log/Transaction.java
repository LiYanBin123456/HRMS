package bean.log;

import java.sql.Date;
//资金明细表
public class Transaction {
    private long cid;//合作单位公司id
    private Date time;//时间
    private float money;//往来资金金额
    private String comments;//内容

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
}
