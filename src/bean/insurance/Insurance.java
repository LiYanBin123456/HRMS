package bean.insurance;

import java.util.Date;

//参保单
public class Insurance {
    public final static byte CATEGORY0 = 0;//养老保险
    public final static byte CATEGORY1 = 1;//工伤
    public final static byte CATEGORY2 = 2;//失业保险

    public final static byte CATEGORY3 = 3;//医疗保险
    public final static byte CATEGORY4 = 4;//大病保险
    public final static byte CATEGORY5 = 5;//生育保险
    public final static byte CATEGORY6 = 6;//公积金
    public final static byte CATEGORY7 = 7;//商业保险

    public static byte TYPE_MIN = 0;//最低标准
    public static byte TYPE_CUSTOM = 1;//自定义标准

    public static byte STATUS_APPENDING = 1;//新增
    public static byte STATUS_NORMAL = 2;//在保
    public static byte STATUS_STOPING = 3;//拟停
    public static byte STATUS_STOPED = 4;//停保
    public static byte STATUS_APPENDING_ERROR = 5;//新增异常
    public static byte STATUS_NORMAL_ERROR = 6;//在保异常
    public static byte STATUS_STOPING_ERROR = 7;//拟停异常
    public static byte STATUS_STOPED_ERROR = 8;//停保异常
    public static byte STATUS_ERROR = 9;//异常

    private long eid;//员工id1
    private String city;//参保城市1
    private byte category;//险种
    private String code;//代码
    private float base;//基数
    private byte baseType;//基数取值方式
    private float v1;//取决于险种 医疗、生育、养老、工伤、失业保险和公积金时为单位比例，大病保险时为单位金额
    private float v2;//取决于险种 医疗、养老、失业保险和公积金时为个人比例，生育保险为0，大病保险时为个人金额，工伤保险时为工伤补充
    private Date date;//参保时间
    private byte status;//状态 0 未参保 1 新增 2 在保 3拟停 4 停保 5异常
    private String reason;//变动原因


    public Insurance(long eid, String city, byte category, String code, float base, byte baseType, float v1, float v2, Date date, byte status, String reason) {
        this.eid = eid;
        this.city = city;
        this.category = category;
        this.code = code;
        this.base = base;
        this.baseType = baseType;
        this.v1 = v1;
        this.v2 = v2;
        this.date = date;
        this.status = status;
        this.reason = reason;
    }

    public Insurance() {
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public byte getCategory() {
        return category;
    }

    public void setCategory(byte category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public byte getBaseType() {
        return baseType;
    }

    public void setBaseType(byte baseType) {
        this.baseType = baseType;
    }

    public float getV1() {
        return v1;
    }

    public void setV1(float v1) {
        this.v1 = v1;
    }

    public float getV2() {
        return v2;
    }

    public void setV2(float v2) {
        this.v2 = v2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
