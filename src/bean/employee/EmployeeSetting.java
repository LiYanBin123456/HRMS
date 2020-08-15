package bean.employee;
//员工个税专项扣除
public class EmployeeSetting {
    private long eid;//员工id
    private String city;//社保所在地市
    /**医保设置
     0_最低标准
     1_实际工资
     2_不交纳
     3_定义基数
     *
     */
    private byte settingM;
    /**自定义的医保基数（不能低于最低标准）
     默认为null
     只有选择了自定义才有用
     *
     */
    private float valueM;
    /**社保设置
     0_最低标准
     1_实际工资
     2_不交纳
     3自定义基数
     *
     */
    private byte settingS;
    /**自定义社保基数（不能低于最低标准）
     默认为null
     只有选择了自定义才有用
     *
     */
    private float valueS;
    /**公积金设置
     0_按比例
     1_自定义
     */
    private float settingF;
    private float fundPer;//公积金比例
    private float fundBase;//公积金基数
    private float valueF;//自定义公积金金额
    private float deduct1;//子女教育扣除额
    private float deduct2;//赡养老人
    private float deduct3;//继续教育
    private float deduct4;//大病医疗
    private float deduct5;//住房贷款利息
    private float deduct6;//住房租金

    public EmployeeSetting() {
    }

    public EmployeeSetting(long eid, String city, byte settingM, float valueM, byte settingS, float valueS, float settingF, float fundPer, float fundBase, float valueF, float deduct1, float deduct2, float deduct3, float deduct4, float deduct5, float deduct6) {
        this.eid = eid;
        this.city = city;
        this.settingM = settingM;
        this.valueM = valueM;
        this.settingS = settingS;
        this.valueS = valueS;
        this.settingF = settingF;
        this.fundPer = fundPer;
        this.fundBase = fundBase;
        this.valueF = valueF;
        this.deduct1 = deduct1;
        this.deduct2 = deduct2;
        this.deduct3 = deduct3;
        this.deduct4 = deduct4;
        this.deduct5 = deduct5;
        this.deduct6 = deduct6;
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

    public float getValueM() {
        return valueM;
    }

    public void setValueM(float valueM) {
        this.valueM = valueM;
    }

    public byte getSettingS() {
        return settingS;
    }

    public void setSettingS(byte settingS) {
        this.settingS = settingS;
    }

    public float getValueS() {
        return valueS;
    }

    public void setValueS(float valueS) {
        this.valueS = valueS;
    }

    public float getSettingF() {
        return settingF;
    }

    public void setSettingF(float settingF) {
        this.settingF = settingF;
    }

    public float getFundPer() {
        return fundPer;
    }

    public void setFundPer(float fundPer) {
        this.fundPer = fundPer;
    }

    public float getFundBase() {
        return fundBase;
    }

    public void setFundBase(float fundBase) {
        this.fundBase = fundBase;
    }

    public float getValueF() {
        return valueF;
    }

    public void setValueF(float valueF) {
        this.valueF = valueF;
    }

    public float getDeduct1() {
        return deduct1;
    }

    public void setDeduct1(float deduct1) {
        this.deduct1 = deduct1;
    }

    public float getDeduct2() {
        return deduct2;
    }

    public void setDeduct2(float deduct2) {
        this.deduct2 = deduct2;
    }

    public float getDeduct3() {
        return deduct3;
    }

    public void setDeduct3(float deduct3) {
        this.deduct3 = deduct3;
    }

    public float getDeduct4() {
        return deduct4;
    }

    public void setDeduct4(float deduct4) {
        this.deduct4 = deduct4;
    }

    public float getDeduct5() {
        return deduct5;
    }

    public void setDeduct5(float deduct5) {
        this.deduct5 = deduct5;
    }

    public float getDeduct6() {
        return deduct6;
    }

    public void setDeduct6(float deduct6) {
        this.deduct6 = deduct6;
    }

    @Override
    public String toString() {
        return "EmployeeSetting{" +
                "eid=" + eid +
                ", city='" + city + '\'' +
                ", settingM=" + settingM +
                ", valueM=" + valueM +
                ", settingS=" + settingS +
                ", valueS=" + valueS +
                ", settingF=" + settingF +
                ", fundPer=" + fundPer +
                ", fundBase=" + fundBase +
                ", valueF=" + valueF +
                ", deduct1=" + deduct1 +
                ", deduct2=" + deduct2 +
                ", deduct3=" + deduct3 +
                ", deduct4=" + deduct4 +
                ", deduct5=" + deduct5 +
                ", deduct6=" + deduct6 +
                '}';
    }
}
