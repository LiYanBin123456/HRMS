package bean.admin;

public class Contract {
    private String id;//合同id（规则为类型+年份+四位编号，如“A2020000001”）
    private long aid;//甲方id 平台为0
    private long bid;//乙方id  客户id

    /**合同类型
     * A平台和人力资源派遣单位的合同
     * B平台和用人单位之间的合同
     * C人力资源和合作单位之间的合同
     * D人力资源派遣单位和员工之间的合同
     */
    private String type;
    private String start;//合同生效时间
    private String end;//合同失效时间
    private int status;//状态：0-正常；1-过期
    private String intro;//备注
    /**发票类型(派遣公司),其他类型的合同默认为null
     * 0_有增值税专用发票（全额）
     * 1_增值税专用发票（差额）
     * 2_增值税普通发票
     */
    private int invoice;
    private String project;//开票项目（派遣公司） 其他类型默认为null
    private int times;//签订次数（员工） 其他类型合同默认为null

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

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getInvoice() {
        return invoice;
    }

    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public Contract() {
    }

    public Contract(String id, long aid, long bid, String type, String start, String end, int status, String intro, int invoice, String project, int times) {
        this.id = id;
        this.aid = aid;
        this.bid = bid;
        this.type = type;
        this.start = start;
        this.end = end;
        this.status = status;
        this.intro = intro;
        this.invoice = invoice;
        this.project = project;
        this.times = times;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id='" + id + '\'' +
                ", aid=" + aid +
                ", bid=" + bid +
                ", type='" + type + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", status=" + status +
                ", intro='" + intro + '\'' +
                ", invoice=" + invoice +
                ", project='" + project + '\'' +
                ", times=" + times +
                '}';
    }
}
