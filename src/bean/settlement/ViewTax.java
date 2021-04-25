package bean.settlement;
//个税申报表
public class ViewTax {
    private long id;//结算单明细id
    private long sid;//结算单id
    private long eid;//员工id
    private long did;//派遣方id
    private String name;//员工姓名
    private String cardId;//员工身份证号码
    private String month;//结算单所属月份
    private float payable;//结算单明细中的应发，也是本期收入
    private float pension1;//个人养老
    private float medicare1;//个人医疗
    private float unemployment1;//个人失业
    private float disease1;//个人大病
    private float fund1;//个人公积金
    private float deduct1;//子女教育扣除额
    private float deduct2;//赡养老人
    private float deduct3;//继续教育
    private float deduct4;//大病医疗
    private float deduct5;//住房贷款利息
    private float deduct6;//住房租金

    public ViewTax() {
    }

    public ViewTax(long id, long sid, long eid, long did, String name, String cardId, String month, float payable, float pension1, float medicare1, float unemployment1, float disease1, float fund1, float deduct1, float deduct2, float deduct3, float deduct4, float deduct5, float deduct6) {
        this.id = id;
        this.sid = sid;
        this.eid = eid;
        this.did = did;
        this.name = name;
        this.cardId = cardId;
        this.month = month;
        this.payable = payable;
        this.pension1 = pension1;
        this.medicare1 = medicare1;
        this.unemployment1 = unemployment1;
        this.disease1 = disease1;
        this.fund1 = fund1;
        this.deduct1 = deduct1;
        this.deduct2 = deduct2;
        this.deduct3 = deduct3;
        this.deduct4 = deduct4;
        this.deduct5 = deduct5;
        this.deduct6 = deduct6;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getPayable() {
        return payable;
    }

    public void setPayable(float payable) {
        this.payable = payable;
    }

    public float getPension1() {
        return pension1;
    }

    public void setPension1(float pension1) {
        this.pension1 = pension1;
    }

    public float getMedicare1() {
        return medicare1;
    }

    public void setMedicare1(float medicare1) {
        this.medicare1 = medicare1;
    }

    public float getUnemployment1() {
        return unemployment1;
    }

    public void setUnemployment1(float unemployment1) {
        this.unemployment1 = unemployment1;
    }

    public float getDisease1() {
        return disease1;
    }

    public void setDisease1(float disease1) {
        this.disease1 = disease1;
    }

    public float getFund1() {
        return fund1;
    }

    public void setFund1(float fund1) {
        this.fund1 = fund1;
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
        return "ViewTax{" +
                "id=" + id +
                ", sid=" + sid +
                ", eid=" + eid +
                ", name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                ", month='" + month + '\'' +
                ", payable=" + payable +
                ", pension1=" + pension1 +
                ", medicare1=" + medicare1 +
                ", unemployment1=" + unemployment1 +
                ", disease1=" + disease1 +
                ", fund1=" + fund1 +
                ", deduct1=" + deduct1 +
                ", deduct2=" + deduct2 +
                ", deduct3=" + deduct3 +
                ", deduct4=" + deduct4 +
                ", deduct5=" + deduct5 +
                ", deduct6=" + deduct6 +
                '}';
    }
}
