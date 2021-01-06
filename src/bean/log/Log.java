package bean.log;

import java.util.Date;
//日志单
public class Log {
    private long sid;//结算单id
    /**结算单类别
     0_普通结算单
     1_小时工结算单
     2_商业结算单
     */
    private byte type;
    private String operator;//操作者姓名（编号）
    private Date time;//插入时间
    private String content;//操作内容

    public Log() {
    }

    public Log(long sid, byte type, String operator, Date time, String content) {
        this.sid = sid;
        this.type = type;
        this.operator = operator;
        this.time = time;
        this.content = content;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Log{" +
                "sid=" + sid +
                ", type=" + type +
                ", operator='" + operator + '\'' +
                ", time=" + time +
                ", content='" + content + '\'' +
                '}';
    }
}
