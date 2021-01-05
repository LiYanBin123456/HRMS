package utills.Salary;

import bean.client.Items;
import bean.client.MapSalary;
import bean.contract.Serve;
import bean.contract.ViewContractCooperation;
import bean.employee.Deduct;
import bean.employee.EnsureSetting;
import bean.employee.ViewDeduct;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.*;
import utills.CollectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

//工资计算
public class Salary {
    /**
     *计算普通结算单
     * @param settlement 结算单
     * @param contract 合同视图
     * @param details 明细
     * @return
     */
    public static void calculateSettlement1(Settlement1 settlement, ViewContractCooperation contract, List<ViewDetail1> details){
        float salary=0;//应发总额
        float social=0;//单位社保
        float medicare=0;//单位医保
        float fund = 0;//单位公积金
        float summary=0;//实发总额
        float manage =0;//管理费
        float extra = 0;//额外
        float free = 0;//国家减免项
        float tax = 0;//税费

        //计算社保、医保、公积金和核收补减总额
        for (ViewDetail1 detail : details) {
            //应发总额+=明细中的应发总额
            salary += detail.getPayable();
            social += detail.getSocialDepartment();//单位社保总额
            medicare += detail.getMedicaleDepartment();//单位医保总额
            fund += detail.getFund2();//单位公积金总额
            if (settlement.getType() == 3) {//代缴社保还需加上个人部分
                social += detail.getSocialPerson();
                medicare += detail.getMedicalePerson();
                fund += detail.getFund1();
            }
            //补收核减
            extra += detail.getExtra2();
        }

        //计算管理费和税费
        int type = contract.getStype();//合同服务项目中的类型
        int category = contract.getCategory();//合同服务项目中的结算方式
        int invoice = contract.getInvoice();//合同基础信息中的发票类型
        float per = contract.getPer()/100;//税费比例（选择增值税专用发票（全额）需要用到）
        float value = contract.getValue();//结算值，根据结算方式的不同，代表的意义不同
        float num = details.size();//总人数
        switch (type){
            case 0://劳务派遣
                if(category==0){//按人数收取的结算方式
                    //管理费总额=人数*管理费,如果不需要计算社保（即补发），不需要重复计算管理费
                    manage = settlement.isNeedCalculateSocial()?num*value:0;
                    if(invoice==0){//增值税专用发票（全额）
                      tax=(salary+social+medicare+fund+manage)*per;//税费 = （应发总额+单位五险一金+管理费）*税率（基准6.72，但可以浮动）
                    }
                }else if(category==1){//按比例收取的结算方式
                    //此时服务项目中value为比例所以需要转成小数
                    manage = (salary+social+medicare+fund)*(value/100);//管理费 = （应发总额+单位五险一金）*比例（从服务项目中的比例）
                    tax = 0;
                }else {//按外包整体核算方式

                }
                break;
            case 1://人事代理
                tax=0;
                manage = num*value;//管理费=人数*单价
                break;
        }

        //计算总额
        byte stype = settlement.getType();//结算单类型
        switch (stype){
            case 0://派遣结算单
            case 1://外包结算单
                summary=salary+social+medicare+fund+manage+tax+extra-free;//总额
                break;
            case 2://代发工资
                summary=salary+manage+tax+extra-free;
                break;
            case 3://代缴社保
                summary=social+medicare+fund+manage+extra-free;
                salary=0;
                tax = 0;
                break;

        }

        settlement.setSalary(salary);
        settlement.setSocial(social);
        settlement.setMedicare(medicare);
        settlement.setFund(fund);
        settlement.setManage(manage);
        settlement.setTax(tax);
        settlement.setSummary(summary);
        settlement.setExtra(extra);
        settlement.setFree(free);
    }

    /**
     * 计算小时工结算单
     * @param settlement   小时工结算单
     * @param details  小时工结算明细列表
     * @param payer  支付方
     */
    public static void calculateSettlement2(Settlement2 settlement, List<Detail2> details, byte payer){
        int hours=0;//总工时
        float traffic=0;//交通费
        float extra=0;//附加
        float summary;//总额
        for (Detail2 detail2:details){
            hours+=detail2.getHours();
            traffic+=detail2.getTraffic();
            extra+=(detail2.getOther1()+detail2.getOther2());
        }
        if(payer==0){//派遣方发工资
            //总额= 总工时*单位的单价+交通费+额外收入
            summary=hours*settlement.getPrice()+traffic+extra;
        }else {//合作方发工资
            //差额 = 单位的单价-小时工的单价
            float difference = settlement.getPrice()-details.get(0).getPrice();//差额 = 单位的单价-小时工的单价
            //总额= 总工时*单位与员工的单价差+交通费+额外收入
            summary=hours*difference;
            extra=0;
            traffic=0;
        }
        settlement.setHours(hours);
        settlement.setSummary(summary);
        settlement.setExtra(extra);
        settlement.setTraffic(traffic);
    }

    /**
     * 计算补发结算单明细
     * @param details
     * @param mapSalary
     * @param totals
     * @param deducts
     * @return
     */
    public static String calculateDetail1(List<ViewDetail1> details, MapSalary mapSalary, List<ViewDetailTotal> totals, List<ViewDeduct> deducts) {
        for (Detail1 d : details) {
            //计算个税累计扣除总额和应发总额
            ViewDetailTotal total = CollectionUtil.getElement(totals,"eid",d.getEid());
            if(total==null){//如果不存在
                total = new ViewDetailTotal();
            }

            //计算本期应发
            float payable =d.getPayable0(false);//初始是基本工资
            if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0){//如果有自定义工资
                payable += sumDefinedSalaryItem(d,mapSalary);
            }
            d.setPayable(payable);

            //计算个税
            float income=payable+total.getPayables();//本期累计收入
            ViewDeduct deduct = CollectionUtil.getElement(deducts,"eid",d.getEid());
            if(deduct == null){
                return "请完善" + deduct.getName() + "的个税专项扣除";
            }
            //确认发放之后个人专项扣除已经累计过，所以需要变回本月的初始状态
            deduct.setIncome(deduct.getIncome()-d.getPayable());
            deduct.setFree(deduct.getFree()-5000);
            deduct.setPrepaid(deduct.getPrepaid()-d.getTax());
            deduct.setDeduct(deduct.getDeduct()-deduct.getDeduct1()-deduct.getDeduct2()-deduct.getDeduct3()-deduct.getDeduct4()-deduct.getDeduct5()-deduct.getDeduct6());
            double tax =calculateTax(income,deduct);
            tax=tax-total.getTaxs();
            d.setTax((float) tax);

            //计算实发
            d.setPaid(payable-d.getTax());
        }
        return null;
    }

    /**
     * 计算普通结算单明细
     * @param settlement
     * @param details
     * @param medicares
     * @param socials
     * @param settings
     * @param mapSalary
     * @param deducts
     * @return
     */
    public static String calculateDetail1(Settlement1 settlement, List<ViewDetail1> details, HashMap<String, RuleMedicare> medicares, HashMap<String, RuleSocial> socials, List<EnsureSetting> settings, MapSalary mapSalary, List<ViewDeduct> deducts) {
        for (ViewDetail1 d:details) {
            EnsureSetting setting = CollectionUtil.getElement(settings,"eid",d.getEid());

            //计算医保(不是代缴工资，且需要交纳医保才需要计算医保)
            if(settlement.getType()!=2){
                if(setting == null){
                    return "请完善" + d.getName() + "的社保设置";
                }
                if(setting.getSettingM()!=1) {
                    RuleMedicare medicare = medicares.get(setting.getCity());
                    calculateMedicare(d,setting,medicare);
                }
            }

            //计算社保
            if(settlement.getType()!=2){
                if(setting == null){
                    return "请完善" + d.getName() + "的社保设置";
                }
                if(setting.getSettingS()!=1) {
                    RuleSocial social = socials.get(setting.getCity());
                    calculateSocial(d,setting,social);
                }
            }

            //计算公积金
            float fund = setting.getFundBase()*setting.getFundPer()/100;
            d.setFund1(fund);//个人公积金
            d.setFund2(fund);//单位公积金

            //计算应发工资，应发=应发-个人五险一金+个人核收补缴
            if(settlement.getType() == 3) {
                d.setPayable(0);
            }else {
                float payable = d.getPayable0(true);
                if (mapSalary != null && mapSalary.getItems() != null && mapSalary.getItems().length() > 0) {//如果有自定义工资
                    payable += sumDefinedSalaryItem(d, mapSalary);
                }
                d.setPayable(payable);
            }

            //计算个税(代缴社保不需要计算个税)
            if(settlement.getType() == 3){
                d.setTax(0);
            }else {
                ViewDeduct deduct = CollectionUtil.getElement(deducts,"eid",d.getEid());
                if(deduct == null){
                    return "请完善" + d.getName() + "的个税专项扣除";
                }
                double tax = settlement.getType() == 3 ? 0 : calculateTax(d.getPayable(), deduct);
                d.setTax((float) tax);
            }

            //计算国家减免项=单位养老+单位失业+单位工伤-工伤补充
            /*float free = d.getPension2()+d.getUnemployment2()+d.getInjury()-(d.getInjury()==0?0:social.getExtra());
            d.setFree(free);*/

            //计算实发工资
            d.setPaid(d.getPayable()-d.getTax());
        }
        return null;
    }

    /**
     * 计算小时工结算单明细
     * @param d 小时工结算单明细
     * @param deduct 所属员工的各项扣除
     * @return 计算好的结算单明细
     */
    public static void calculateDetail2(Detail2 d, Deduct deduct){
        float income = d.total();//本期收入
        float taxDue=deduct.total()+income;//应税额=累计应税额+本期收入
        double tax = Tax.tax1(taxDue);
        tax -= deduct.getPrepaid();

        float paid = income-(float) tax;

        d.setPayable(income);
        d.setTax((float) tax);
        d.setPaid(paid);
    }

    /**
     * 计算管理费和税费（只适用于导出结算单）
     * @param type 合同服务项目中的类型
     * @param category 合同服务项目中的结算方式
     * @param invoice 合同基础信息中的发票类型
     * @param per 税费比例（选择增值税专用发票（全额）需要用到）
     * @param val 结算值，根据结算方式的不同，代表的意义不同
     * @param manage 管理费
     * @param tax2 税费
     * @param v 明细
     */
    public  static HashMap<String, Float> calculateManageAndTax2(int type, int category, int invoice, float per, float val, float manage, float tax2, ViewDetail1 v,float salary){
        HashMap<String,Float> map = new HashMap<String, Float>();
        //计算管理费
        switch (type){
            case 0://劳务派遣
                if(category==0){//按人数收取的结算方式
                    manage=val;//管理费=管理费
                    if(invoice==0){//增值税专用发票（全额）
                        //税费=（基本工资+自定义工资项+单位五险一金+管理费-国家减免+单位核收补减）
                        tax2 += (v.getBase()+salary+v.getPension2()+v.getUnemployment2()+v.getMedicare2()+v.getDisease2()+v.getInjury()+v.getBirth()+v.getFund2()+manage-v.getFree()+v.getExtra2())*per;
                    }
                }else if(category==1){//按比例收取的结算方式
                    //此时服务项目中value为比例所以需要转成小数
                    //管理费 = （基本工资+自定义工资项+单位五险一金-国家减免+单位核收补减）*比例（从服务项目中的比例）
                    manage += (v.getBase()+salary+v.getPension2()+v.getUnemployment2()+v.getMedicare2()+v.getDisease2()+v.getBirth()+v.getFund2()+v.getInjury()-v.getFree()+v.getExtra2())*(val/100);
                    tax2=0;
                }else {//按外包整体核算方式

                }
                break;
            case 1://人事代理
                tax2=0;
                manage = val;//管理费=人数*单价
                break;
        }
        map.put("manage",manage);
        map.put("tax2",tax2);
        return map;
    }

    /**
     *计算自定义工资项总额
     * @param detail 结算单明细
     * @param mapSalary 自定义工资
     * @return
     */
    private static float sumDefinedSalaryItem(Detail1 detail, MapSalary mapSalary){
        /**
         * 思路：
         */
        float value = 0;
        List<Items> itemList = mapSalary.getItemList();
        for (int i = 0; i <itemList.size(); i++) {
            int index = i + 1;
            String name = "getF" + index;
            float v = 0;
            Method method;
            try {//通过反射获取对应的值
                method = detail.getClass().getSuperclass().getMethod(name);
                v = Float.parseFloat(method.invoke(detail).toString());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if(itemList.get(i).getType()==0){//加项
                value+=v;
            }else {//减项
                value-=v;
            }
        }
        return  value;
    }


    /**
     * 计算普通结算单的个税
     * @param income 本期收入
     * @return v
     */
    public static double calculateTax(float income, Deduct deduct){
        float taxDue=deduct.total()+income;//应税额 = 累计应税额+ 本期收入
        double tax = Tax.tax1(taxDue);
        tax -= deduct.getPrepaid();//本期个税 = 累计应缴个税-累计预缴个税
        return tax<=0?0:tax;
    }
    /**
     * 计算医保
     * @param setting 员工设置
     * @param ruleMedicare  所属地方的医保
     * @return
     */
    public static void calculateMedicare(Detail1 detail, EnsureSetting setting, RuleMedicare ruleMedicare){

        float medicare1=0;//个人医疗
        float disease1=0;//个人大病
        float medicare2=0;//单位医疗
        float disease2=0;//单位大病
        float birth=0;//单位生育
        int SettingM = setting.getSettingM();//员工医保设置
        float base = 0;
        switch (SettingM) {
            case 0://最低标准
                base = ruleMedicare.getBase();
                break;
            case 1://不缴纳
                base = 0;
                break;
            case 2://自定义基数
                base = setting.getValM();
                break;
        }
        byte medicare = setting.getMedicare();//要计算的医保类别
        if((medicare&((byte)1)) != 0){
            medicare1 = base*(ruleMedicare.getPer2()/100);//个人医疗
            medicare2 =base*(ruleMedicare.getPer1()/100);//单位医疗
        }
        if((medicare&((byte)2)) != 0){
            disease1 = ruleMedicare.getFin2();//个人大病
            disease2 = ruleMedicare.getFin1();//单位大病
        }
        if((medicare&((byte)4)) != 0){
            birth = base*(ruleMedicare.getPer3()/100);//单位生育
        }
        detail.setMedicare1(medicare1);
        detail.setMedicare2(medicare2);
        detail.setDisease1(disease1);
        detail.setDisease2(disease2);
        detail.setBirth(birth);
    }

    public static void calculateSocial(Detail1 detail, EnsureSetting setting,RuleSocial ruleSocial){
        float pension1=0;//个人养老
        float unemployment1=0;//个人失业
        float pension2=0;//单位养老
        float unemployment2=0;//单位失业
        float injury=0;//单位工伤

        float base = 0;
        switch (setting.getSettingS()){
            case 0://最低标准
                base = ruleSocial.getBase();
                break;
            case 1://不缴纳
                base=0;
                break;
            case 2://自定义工资
                base=setting.getValS();//自定义的基数
                break;
        }

        byte social = setting.getSocial();//要计算的社保类别
        if((social&((byte)1)) != 0){
            pension1=base*(ruleSocial.getPer2()/100);//个人养老
            pension2=base*(ruleSocial.getPer1()/100);//单位养老
        }
        if((social&((byte)2)) != 0){
            unemployment1=base*(ruleSocial.getPer5()/100);//个人失业
            unemployment2=base*(ruleSocial.getPer4()/100);//单位失业
        }
        if((social&((byte)4)) != 0) {
            injury = base * (setting.getInjuryPer()/100)+ruleSocial.getExtra();//单位工伤
        }

        detail.setPension1(pension1);
        detail.setPension2(pension2);
        detail.setUnemployment1(unemployment1);
        detail.setUnemployment2(unemployment2);
        detail.setInjury(injury);
    }

}
