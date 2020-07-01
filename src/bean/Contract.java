package bean;

public class Contract {
    private String id;//合同id（规则为类型+年份+四位编号，如“A20200001”）
    private long cid;//外键  客户id
    private String sign;//合同签订的时间
    private String start;//合同生效时间
    private String end;//合同失效时间
    private String type;//合同类型  A平台和人力资源派遣单位的合同   B平台和用人单位之间的合同  C人力资源和合作单位之间的合同 D人力资源派遣单位和员工之间的合同
    private String accessory;//合同附件  一般是图片
    private int status;//状态：0-正常；1-过期

    public Contract() {
    }

    public Contract(String id, long cid, String sign, String start, String end, String type, String accessory, int status) {
        this.id = id;
        this.cid = cid;
        this.sign = sign;
        this.start = start;
        this.end = end;
        this.type = type;
        this.accessory = accessory;
        this.status = status;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id='" + id + '\'' +
                ", cid=" + cid +
                ", sign='" + sign + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", type='" + type + '\'' +
                ", accessory='" + accessory + '\'' +
                ", status=" + status +
                '}';
    }
}
