package bean.employee;

//员工社保设置
public class EnsureSetting {
    private long eid;//员工id
    private String city;//社保所在地市
    private byte settingM;//医保设置:0_不交纳;1_最低标准;2_定义基数
    private float baseM;//医保基数
    private float perM;//医保比例
    private byte medicare;//医保参保情况,采用位运算:第一位 医疗保险;第二位 大病医疗;第三位 生育保险
    private byte settingS;//社保设置:0_不交纳;1_最低标准;2_定义基数
    private float baseS;//社保基数
    private float perS;//社保比例
    private byte social;//社保参保情况,采用位运算:第一位 养老保险;第二位 失业保险;第三位 工伤保险
    private float perInjury;//工伤比例，只有购买工伤保险才有
    private float perFund;//公积金比例
    private float baseFund;//公积金基数

    public byte getMedicare() {
        return medicare;
    }

    public void setMedicare(byte medicare) {
        this.medicare = medicare;
    }

    public byte getSocial() {
        return social;
    }

    public void setSocial(byte social) {
        this.social = social;
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

    public byte getSettingM() {
        return settingM;
    }

    public void setSettingM(byte settingM) {
        this.settingM = settingM;
    }

    public float getBaseM() {
        return baseM;
    }

    public void setBaseM(float baseM) {
        this.baseM = baseM;
    }

    public byte getSettingS() {
        return settingS;
    }

    public void setSettingS(byte settingS) {
        this.settingS = settingS;
    }

    public float getBaseS() {
        return baseS;
    }

    public void setBaseS(float baseS) {
        this.baseS = baseS;
    }

    public float getPerFund() {
        return perFund;
    }

    public void setPerFund(float perFund) {
        this.perFund = perFund;
    }

    public float getBaseFund() {
        return baseFund;
    }

    public void setBaseFund(float baseFund) {
        this.baseFund = baseFund;
    }

    public float getPerInjury() {
        return perInjury;
    }

    public void setPerInjury(float perInjury) {
        this.perInjury = perInjury;
    }

    public float getPerM() {
        return perM;
    }

    public void setPerM(float perM) {
        this.perM = perM;
    }

    public float getPerS() {
        return perS;
    }

    public void setPerS(float perS) {
        this.perS = perS;
    }
}
