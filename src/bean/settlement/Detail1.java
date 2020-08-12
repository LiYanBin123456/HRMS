package bean.settlement;

import java.sql.Date;

//结算单明细
public class Detail1 extends Detail {

    private float base;//基础工资
    private float pension1;//个人养老
    private float medicare1;//个人医疗
    private float unemployment1;//个人失业
    private float disease1;//个人大病
    private float fund1;//个人公积金
    private float pension2;//单位养老
    private float medicare2;//单位医疗
    private float unemployment2;//单位失业
    private float injury;//单位工伤
    private float disease2;//单位大病
    private float birth;//单位生育
    private float fund2;//单位公积金
    private float tax;//个税
    private float payable;//应发
    private float paid;//实发
    private float f1;//自定字段
    private float f2;
    private float f3;
    private float f4;
    private float f5;
    private float f6;
    private float f7;
    private float f8;
    private float f9;
    private float f10;
    private float f11;
    private float f12;
    private float f13;
    private float f14;
    private float f15;
    private float f16;
    private float f17;
    private float f18;
    private float f19;
    private float f20;

    public Detail1() {
    }

    public Detail1(long id, long sid, long eid, Date month, byte status, float base, float pension1, float medicare1, float unemployment1, float disease1, float fund1, float pension2, float medicare2, float unemployment2, float injury, float disease2, float birth, float fund2, float tax, float payable, float paid, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20) {
        super(id, sid, eid, month, status);
        this.base = base;
        this.pension1 = pension1;
        this.medicare1 = medicare1;
        this.unemployment1 = unemployment1;
        this.disease1 = disease1;
        this.fund1 = fund1;
        this.pension2 = pension2;
        this.medicare2 = medicare2;
        this.unemployment2 = unemployment2;
        this.injury = injury;
        this.disease2 = disease2;
        this.birth = birth;
        this.fund2 = fund2;
        this.tax = tax;
        this.payable = payable;
        this.paid = paid;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.f4 = f4;
        this.f5 = f5;
        this.f6 = f6;
        this.f7 = f7;
        this.f8 = f8;
        this.f9 = f9;
        this.f10 = f10;
        this.f11 = f11;
        this.f12 = f12;
        this.f13 = f13;
        this.f14 = f14;
        this.f15 = f15;
        this.f16 = f16;
        this.f17 = f17;
        this.f18 = f18;
        this.f19 = f19;
        this.f20 = f20;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
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

    public float getPension2() {
        return pension2;
    }

    public void setPension2(float pension2) {
        this.pension2 = pension2;
    }

    public float getMedicare2() {
        return medicare2;
    }

    public void setMedicare2(float medicare2) {
        this.medicare2 = medicare2;
    }

    public float getUnemployment2() {
        return unemployment2;
    }

    public void setUnemployment2(float unemployment2) {
        this.unemployment2 = unemployment2;
    }

    public float getInjury() {
        return injury;
    }

    public void setInjury(float injury) {
        this.injury = injury;
    }

    public float getDisease2() {
        return disease2;
    }

    public void setDisease2(float disease2) {
        this.disease2 = disease2;
    }

    public float getBirth() {
        return birth;
    }

    public void setBirth(float birth) {
        this.birth = birth;
    }

    public float getFund2() {
        return fund2;
    }

    public void setFund2(float fund2) {
        this.fund2 = fund2;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getPayable() {
        return payable;
    }

    public void setPayable(float payable) {
        this.payable = payable;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public float getF1() {
        return f1;
    }

    public void setF1(float f1) {
        this.f1 = f1;
    }

    public float getF2() {
        return f2;
    }

    public void setF2(float f2) {
        this.f2 = f2;
    }

    public float getF3() {
        return f3;
    }

    public void setF3(float f3) {
        this.f3 = f3;
    }

    public float getF4() {
        return f4;
    }

    public void setF4(float f4) {
        this.f4 = f4;
    }

    public float getF5() {
        return f5;
    }

    public void setF5(float f5) {
        this.f5 = f5;
    }

    public float getF6() {
        return f6;
    }

    public void setF6(float f6) {
        this.f6 = f6;
    }

    public float getF7() {
        return f7;
    }

    public void setF7(float f7) {
        this.f7 = f7;
    }

    public float getF8() {
        return f8;
    }

    public void setF8(float f8) {
        this.f8 = f8;
    }

    public float getF9() {
        return f9;
    }

    public void setF9(float f9) {
        this.f9 = f9;
    }

    public float getF10() {
        return f10;
    }

    public void setF10(float f10) {
        this.f10 = f10;
    }

    public float getF11() {
        return f11;
    }

    public void setF11(float f11) {
        this.f11 = f11;
    }

    public float getF12() {
        return f12;
    }

    public void setF12(float f12) {
        this.f12 = f12;
    }

    public float getF13() {
        return f13;
    }

    public void setF13(float f13) {
        this.f13 = f13;
    }

    public float getF14() {
        return f14;
    }

    public void setF14(float f14) {
        this.f14 = f14;
    }

    public float getF15() {
        return f15;
    }

    public void setF15(float f15) {
        this.f15 = f15;
    }

    public float getF16() {
        return f16;
    }

    public void setF16(float f16) {
        this.f16 = f16;
    }

    public float getF17() {
        return f17;
    }

    public void setF17(float f17) {
        this.f17 = f17;
    }

    public float getF18() {
        return f18;
    }

    public void setF18(float f18) {
        this.f18 = f18;
    }

    public float getF19() {
        return f19;
    }

    public void setF19(float f19) {
        this.f19 = f19;
    }

    public float getF20() {
        return f20;
    }

    public void setF20(float f20) {
        this.f20 = f20;
    }

    @Override
    public String toString() {
        return "Detail1{" +
                "base=" + base +
                ", pension1=" + pension1 +
                ", medicare1=" + medicare1 +
                ", unemployment1=" + unemployment1 +
                ", disease1=" + disease1 +
                ", fund1=" + fund1 +
                ", pension2=" + pension2 +
                ", medicare2=" + medicare2 +
                ", unemployment2=" + unemployment2 +
                ", injury=" + injury +
                ", disease2=" + disease2 +
                ", birth=" + birth +
                ", fund2=" + fund2 +
                ", tax=" + tax +
                ", payable=" + payable +
                ", paid=" + paid +
                ", f1=" + f1 +
                ", f2=" + f2 +
                ", f3=" + f3 +
                ", f4=" + f4 +
                ", f5=" + f5 +
                ", f6=" + f6 +
                ", f7=" + f7 +
                ", f8=" + f8 +
                ", f9=" + f9 +
                ", f10=" + f10 +
                ", f11=" + f11 +
                ", f12=" + f12 +
                ", f13=" + f13 +
                ", f14=" + f14 +
                ", f15=" + f15 +
                ", f16=" + f16 +
                ", f17=" + f17 +
                ", f18=" + f18 +
                ", f19=" + f19 +
                ", f20=" + f20 +
                '}';
    }
}
