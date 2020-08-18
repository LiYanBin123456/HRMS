package bean.employee;
//员工个税专项扣除
public class EnsureSetting {
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
    private float fundPer;//公积金比例
    private float fundBase;//公积金基数
    private float product;//保险产品 0 无   1 购买


    public EnsureSetting() {
    }

    public EnsureSetting(long eid, String city, byte settingM, float valueM, byte settingS, float valueS, float fundPer, float fundBase, float product) {
        this.eid = eid;
        this.city = city;
        this.settingM = settingM;
        this.valueM = valueM;
        this.settingS = settingS;
        this.valueS = valueS;
        this.fundPer = fundPer;
        this.fundBase = fundBase;
        this.product = product;
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

    public float getProduct() {
        return product;
    }

    public void setProduct(float product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "EnsureSetting{" +
                "eid=" + eid +
                ", city='" + city + '\'' +
                ", settingM=" + settingM +
                ", valueM=" + valueM +
                ", settingS=" + settingS +
                ", valueS=" + valueS +
                ", fundPer=" + fundPer +
                ", fundBase=" + fundBase +
                ", product=" + product +
                '}';
    }
}
