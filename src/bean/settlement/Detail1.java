package bean.settlement;

import bean.client.MapSalary;
import bean.employee.Deduct;
import bean.employee.EnsureSetting;
import bean.insurance.Insurance;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import utills.Salary.Tax;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
    private float free;//国家减免项
    private float extra1;//补收核减（个人）
    private float extra2;//补收核减（单位）
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
    private float f19;//用于存放自定义工资之和
    private float f20;//用于存放商业保险费用

    public static final byte STATUS_NORMAL = 0;//正常
    public static final byte STATUS_REPLENISH = 1;//补缴
    public static final byte STATUS_BALANCE = 2;//补差
    public static final byte STATUS_CUSTOM = 3;//自定义（什么都不计算）
    public static final byte STATUS_MAKEUP = 4;//补发工资（只计算个税）
    private byte status;
    private String comments1;//个人备注
    private String comments2;//单位备注

    public Detail1() {
    }

    public Detail1(long id, long sid, long eid, float base, float pension1, float medicare1, float unemployment1, float disease1, float fund1, float pension2, float medicare2, float unemployment2, float injury, float disease2, float birth, float fund2, float tax, float payable, float paid, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, byte status) {
        super(id, sid, eid);
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
        this.status = status;
    }

    public Detail1(long id, long sid, long eid, float base, float pension1, float medicare1, float unemployment1, float disease1, float fund1, float pension2, float medicare2, float unemployment2, float injury, float disease2, float birth, float fund2, float tax, float free, float extra1, float extra2, float manage, float sum, float payable, float paid, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, byte status, String comments1, String comments2) {
        super(id, sid, eid);
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
        this.free = free;
        this.extra1 = extra1;
        this.extra2 = extra2;
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
        this.status = status;
        this.comments1 = comments1;
        this.comments2 = comments2;
    }

    public Detail1(long id, long sid, long eid, float base, float pension1, float medicare1, float unemployment1, float disease1, float fund1, float pension2, float medicare2, float unemployment2, float injury, float disease2, float birth, float fund2, float tax, float tax2, float free, float extra1, float extra2, float manage, float sum, float payable, float paid, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, byte status, String comments1, String comments2) {
        super(id, sid, eid);
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

        this.free = free;
        this.extra1 = extra1;
        this.extra2 = extra2;

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
        this.status = status;
        this.comments1 = comments1;
        this.comments2 = comments2;
    }

    public float getExtra1() {
        return extra1;
    }

    public void setExtra1(float extra1) {
        this.extra1 = extra1;
    }

    public float getExtra2() {
        return extra2;
    }

    public void setExtra2(float extra2) {
        this.extra2 = extra2;
    }

    public String getComments1() {
        return comments1;
    }

    public void setComments1(String comments1) {
        this.comments1 = comments1;
    }

    public String getComments2() {
        return comments2;
    }

    public void setComments2(String comments2) {
        this.comments2 = comments2;
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

    public float getFree() {
        return free;
    }

    public void setFree(float free) {
        this.free = free;
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

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
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
                ", status=" + status +
                '}';
    }

    /**
     * 计算医保、社保差额
     * @param d1
     * @param d2
     * @return
     */
    public static Detail1 subtract(Detail1 d1,Detail1 d2){
        Detail1 d = new Detail1();
        d.setMedicare2(d1.getMedicare1() - d2.getMedicare1());
        d.setMedicare2(d1.getMedicare2() - d2.getMedicare2());
        d.setBirth(d1.getBirth() - d2.getBirth());
        d.setDisease1(d1.getDisease1() - d2.getDisease1());
        d.setDisease2(d1.getDisease2() - d2.getDisease2());
        d.setPension1(d1.getPension1() - d2.getPension1());
        d.setPension2(d1.getPension2() - d2.getPension2());
        d.setUnemployment1(d1.getUnemployment1() - d2.getUnemployment1());
        d.setUnemployment2(d1.getUnemployment2() - d2.getUnemployment2());
        d.setInjury(d1.getInjury() - d2.getInjury());
        return d;
    }

    /**
     * 计算个人社保总额
     * @return
     */
    public float getSocialPerson(){
        //个人社保总额=个人养老金+个人失业金
        return pension1+unemployment1;
    }

    /**
     * 计算个人医保总额
     * @return
     */
    public float getMedicalePerson(){
        //个人医保总额=个人大病+个人医疗
        return disease1+medicare1;
    }

    /**
     * 计算单位社保总额
     * @return
     */
    public float getSocialDepartment(){
        //单位社保总额=单位养老金+单位失业金+单位工伤
        return pension2+unemployment2+injury;
    }

    /**
     * 计算单位医保总额
     * @return
     */
    public float getMedicaleDepartment(){
     //单位医保总额=单位大病+单位医疗+生育
        return disease2+medicare2+birth;
    }

    /**
     * 获取社保、医保个人部分
     * @return
     */
    public float getTotalPerson(){
        return getMedicalePerson()+getSocialPerson();
    }

    /**
     * 获取社保单位和单位公积金
     * @return
     */
    public float getTotalDepartment(){
        return getMedicaleDepartment()+getSocialDepartment();
    }

    /**
     * 获取不考虑自定义工资项的应发工资
     * @param flag 是否扣除个人社保医保-个人公积金+核收补减
     * @return
     */
    public float getPayable0(boolean flag){
        return flag?base - getTotalPerson() - getFund1() + extra1:base;
    }

    /**
     * 计算派遣单位应税额
     * @return
     */
    public float getTaxDueOfDepartment(){
        //基本工资+自定义工资+单位五险一金+单位核收补减+商业保险费
        return base+f19+getTotalDepartment()+fund2+extra2+f20;
    }


    /**
     * 正常计算五险一金
     * @param insurances 员工险种集合
     */
    public void calcInsurance(List<Insurance> insurances){
        for(Insurance i:insurances){
            float base = i.getBase();//基数
            float v1 = i.getV1();//个人比例
            float v2 = i.getV2();//单位比例
            switch (i.getCategory()){
                case Insurance.CATEGORY0:////养老保险
                    this.pension1 = base*v1/100;//个人养老保险
                    this.pension2 = base*v2/100;//单位养老保险
                    break;
                case Insurance.CATEGORY1://工伤保险
                    this.injury = base*v2/100+v1;//工伤保险
                    break;
                case Insurance.CATEGORY2://失业保险
                    this.unemployment1 = base*v1/100;//个人失业保险
                    this.unemployment2 = base*v2/100;//单位失业保险
                    break;
                case Insurance.CATEGORY3://医疗保险
                    this.medicare1=base*v1/100;//个人医疗保险
                    this.medicare2=base*v2/100;//单位医疗保险
                    break;
                case Insurance.CATEGORY4://大病保险
                    this.disease1 = base*v1/100;//个人大病
                    this.disease2 = base*v2/100;//单位大病
                    break;
                case Insurance.CATEGORY5://生育保险
                    this.birth = base*v2/100;//单位大病生育保险
                    break;
                case Insurance.CATEGORY6://公积金
                    this.fund1 =base*v1/100;//单位和个人公积金比例一致
                    this.fund2 =base*v1/100;
                    break;
                case Insurance.CATEGORY7://商业保险
                    this.f20 =base;//商业保险金额
                    break;
            }
        }
    }

//    /**
//     * 计算医保
//     * @param setting 员工设置
//     * @param ruleMedicare  所属地方的医保
//     * @return
//     */
//    public void calculateMedicare(EnsureSetting setting, RuleMedicare ruleMedicare){
//        int SettingM = setting.getSettingM();//员工医保设置
//        float base = 0;
//        switch (SettingM) {
//            case 0://最低标准
//                base = ruleMedicare.getBase();
//                break;
//            case 1://不缴纳
//                base = 0;
//                break;
//            case 2://自定义基数
//                base = setting.getBaseM();
//                break;
//        }
//        byte medicare = setting.getMedicare();//要计算的医保类别
//        if((medicare&((byte)1)) != 0){
//            this.medicare1 = base*(ruleMedicare.getPer2());//个人医疗
//            this.medicare2 =base*(ruleMedicare.getPer1());//单位医疗
//        }
//        if((medicare&((byte)2)) != 0){
//            this.disease1 = ruleMedicare.getFin2();//个人大病
//            this.disease2 = ruleMedicare.getFin1();//单位大病
//        }
//        if((medicare&((byte)4)) != 0){
//            this.birth = base*(ruleMedicare.getPer3());//单位生育
//        }
//    }
//
//    /**
//     * 计算社保
//     * @param setting
//     * @param ruleSocial
//     */
//    public void calculateSocial(EnsureSetting setting,RuleSocial ruleSocial){
//        float base = 0;
//        switch (setting.getSettingS()){
//            case 0://最低标准
//                base = ruleSocial.getBase();
//                break;
//            case 1://不缴纳
//                base=0;
//                break;
//            case 2://自定义工资
//                base=setting.getBaseS();//自定义的基数
//                break;
//        }
//
//        byte social = setting.getSocial();//要计算的社保类别
//        if((social&((byte)1)) != 0){
//            this.pension1=base*(ruleSocial.getPer2());//个人养老
//            this.pension2=base*(ruleSocial.getPer1());//单位养老
//        }
//        if((social&((byte)2)) != 0){
//            this.unemployment1=base*(ruleSocial.getPer5());//个人失业
//            this.unemployment2=base*(ruleSocial.getPer4());//单位失业
//        }
//        if((social&((byte)4)) != 0) {
//            this.injury = base * (setting.getPerInjury())+ruleSocial.getExtra();//单位工伤
//        }
//    }
//
//    /**
//     * 计算公积金（计算结果通过工资明细带回）
//     * @param setting 医社保设置
//     */
//    public void calcFund(EnsureSetting setting) {
//        float fund = setting.getBaseFund()*setting.getPerFund();
//        this.fund1 = fund;//个人公积金
//        this.fund2 = fund;//单位公积金
//    }



    /**
     * 计算应发工资
     * @param flag 是否需要计算五险一金和个人核收补减
     */
    public void calcPayable( boolean flag) {
        this.payable = flag?base - getTotalPerson()-fund1 + extra1:base;
    }



    /**
     * 计算自定义工资项目
     * @param mapSalary
     */
    public void calcMapSalary(MapSalary mapSalary) {
        if (mapSalary != null && mapSalary.getItems() != null && mapSalary.getItems().length() > 0) {//如果有自定义工资
            this.sumDefinedSalaryItem(mapSalary);
        }
    }


    /**
     *计算自定义工资项总额
     * @param mapSalary 自定义工资
     * @return  f19 存放所有的自定义工资和
     */
    public void sumDefinedSalaryItem(MapSalary mapSalary){
        /**
         * 思路：
         * 1、获取自定义工资条[{type:1,field:考勤扣款},{type:0,field:加班工资}]形式
         * 2、遍历通过反射获取对应的值，第一条数据对应getF1(),依次类推;
         * 3、判断加项还是减项
         * 4、返回自定义工资总额
         */
        this.f19=0;//初始化自定义工资项。
        List<MapSalary.SalaryItem> itemList = mapSalary.getItemList();
        for (int i = 0; i <itemList.size(); i++) {
            int index = i + 1;
            String name = "getF" + index;
            float v = 0;
            Method method;
            try {//通过反射获取对应的值
                method = this.getClass().getMethod(name);
                v = Float.parseFloat(method.invoke(this).toString());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if(itemList.get(i).getType()==0){//加项
                this.f19 += v;
            }else {//减项
                this.f19 -= v;
            }
        }
    }


    /**
     * 计算普通个税
     * @param deduct
     */
    public void calculateTax(Deduct deduct){
        float taxDue=deduct.total()+this.getPayable();//应税额 = 累计应税额+ 本期收入
        float tax = Tax.tax1(taxDue);//计算普通个税
        tax -= deduct.getPrepaid();//本期个税 = 累计应缴个税-累计预缴个税
        this.tax = tax<=0?0:tax;
    }

    /**
     * 计算实发工资
     */
    public void calcPayed() {
        this.paid = this.payable - this.tax;
    }


}
