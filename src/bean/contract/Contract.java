package bean.contract;

import java.sql.Date;

//合同
public class Contract {
    private String id;//合同id（规则为类型+年份+四位编号，如“A2020000001”）
    private long aid;//甲方id 平台为0
    private long bid;//乙方id  客户id
    /**合同类型
     * A平台和人力资源派遣单位的合同
     * B人力资源和合作单位之间的合同
     * C人力资源派遣单位和员工之间的合同
     */
    private String type;
    private Date start;//合同生效时间
    private Date end;//合同失效时间
    private byte status;//状态：0-正常；1-过期
    private String comments;//备注
    /**发票类型(派遣公司),其他类型的合同默认为null
     * 0_有增值税专用发票（全额）
     * 1_增值税专用发票（差额）
     * 2_增值税普通发票
     */
    private byte invoice;
    private String project;//开票项目（派遣公司） 其他类型默认为null
    private float per;//税费比例（选择增值税专用发票（全额）需要用到）
    private byte times;//签订次数（员工） 其他类型合同默认为null

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public String getType() {
        return type;
    }

    public byte getTimes() {
        return times;
    }

    public void setTimes(byte times) {
        this.times = times;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getInvoice() {
        return invoice;
    }

    public void setInvoice(byte invoice) {
        this.invoice = invoice;
    }

    public float getPer() {
        return per;
    }

    public void setPer(float per) {
        this.per = per;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }



    public Contract() {
    }

    public Contract(String id, long aid, long bid, String type, Date start, Date end, byte status, String comments, byte invoice, String project, float per, byte times) {
        this.id = id;
        this.aid = aid;
        this.bid = bid;
        this.type = type;
        this.start = start;
        this.end = end;
        this.status = status;
        this.comments = comments;
        this.invoice = invoice;
        this.project = project;
        this.per = per;
        this.times = times;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id='" + id + '\'' +
                ", aid=" + aid +
                ", bid=" + bid +
                ", type='" + type + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", status=" + status +
                ", comments='" + comments + '\'' +
                ", invoice=" + invoice +
                ", project='" + project + '\'' +
                ", per=" + per +
                ", times=" + times +
                '}';
    }
}
