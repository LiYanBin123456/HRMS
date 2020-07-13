package bean.admin;

public class a_contract {
    private String id;//合同id（规则为类型+年份+六位编号，如“A2020000001”）
    private long cid;//乙方id 此处为客户id
    private String start;//合同生效时间
    private String end;//合同失效时间
    private int status;//状态：0-正常；1-过期
    private String intro;//备注

    public a_contract(String id, long cid, String start, String end, int status, String intro) {
        this.id = id;
        this.cid = cid;
        this.start = start;
        this.end = end;
        this.status = status;
        this.intro = intro;
    }

    public a_contract() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
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

    @Override
    public String toString() {
        return "a_contract{" +
                "id='" + id + '\'' +
                ", cid=" + cid +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", status=" + status +
                ", intro='" + intro + '\'' +
                '}';
    }
}
