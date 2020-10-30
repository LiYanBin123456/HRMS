package utills;

import bean.client.Items;
import bean.client.MapSalary;
import bean.contract.ViewContractCooperation;
import bean.employee.Deduct;
import bean.employee.EnsureSetting;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.*;
import dao.employee.SettingDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import database.ConnUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;

public class Calculate {

    /**
     * 计算五险一金，下载模板的时候需要的，其他地方不需要
     * @param eid 员工id
     * @return detail 计算好五险一金的结算单明细
     */
    public static  Detail1 calculateInsurance(Long eid){
        Connection conn = ConnUtil.getConnection();
        //初始化明细
        Detail1 detail= new Detail1();
        detail.setEid(eid);
        boolean existSetting=SettingDao.exist(conn,eid).exist;//员工设置是否存在
        if(!existSetting){//不存在则养老等字段为0，直接返回
            return  detail;
        }
        EnsureSetting setting = (EnsureSetting) SettingDao.get(conn,eid).data;//员工设置
        String city = setting.getCity();//员工地市
        RuleMedicare medicare= (RuleMedicare) RuleMedicareDao.getLast(conn,city).data;//获取该地市的最新医保
        RuleSocial social = (RuleSocial) RuleSocialDao.getLast(conn,city).data;//获取该地市的最新社保

        //先获取医保相关
        int SettingM = setting.getSettingM();//员工医保设置
        float baseM = 0;
        switch (SettingM){
            case 0://最低标准
                if(medicare!=null){
                    baseM = medicare.getBase();
                }else {
                    baseM = 0;
                }
                break;
            case 1://实际工资，因为模板中暂时没有实际工资，为0
                baseM=0;
                break;
            case 2://不交纳，为0
                baseM = 0;
                break;
            case 3://自定义基数
                float ValM = setting.getValM();//自定义的基数
                baseM = ValM;
                break;
        }
        detail = calculateMedicare(detail,setting,baseM,medicare);

        //获取社保相关
        int SettingS = setting.getSettingS();//员工社保设置
        float baseS = 0;
        switch (SettingS){
            case 0://最低标准
                if(social!=null){
                    baseS =social.getBase();
                }else {
                    baseS=0;
                }
                break;
            case 1://实际工资
                baseS = 0;
                break;
            case 2://不缴纳，为0
                baseS=0;
                break;
            case 3://自定义基数
                float ValS=setting.getValS();//自定义的基数
                baseS = ValS;
                break;
        }
        calculateSocial(detail,setting,baseS,social);

        //获取公积金相关
        float FundBase = setting.getFundBase();//获取自定义的公积金基数
        float FundPer  = setting.getFundPer()/100;//获取自定义公积金比例

        detail.setFund1(FundBase*FundPer);//个人公积金
        detail.setFund2(FundBase*FundPer);//个人公积金
        return  detail;
    }


    /**
     * 计算普通结算单明细
     * @param detail //结算单明细
     * @param medicare //所在地市的医保规则
     * @param social //所在地市的社保规则
     * @param setting //员工社保设置
     * @param mapSalary //合作单位的自定义工资
     * @param deduct   //所属员工的个税专项扣除
     * @return detail1 //计算好的结算单
     */
    public static Detail1 calculateDetail1(Detail1 detail, RuleMedicare medicare, RuleSocial social, EnsureSetting setting, MapSalary mapSalary, Deduct deduct){
        //计算医保相关
        int SettingM = setting.getSettingM();//员工医保设置
        float baseM = 0;
        switch (SettingM){
            case 0://最低标准
                baseM = medicare.getBase();
                 break;
            case 1://不缴纳
                baseM = 0;
                break;
            case 2://自定义基数
                baseM = setting.getValM();
                break;
        }
        detail = calculateMedicare(detail,setting,baseM,medicare);

        //计算社保相关
        int SettingS = setting.getSettingS();//员工社保设置
        float baseS = 0;
        switch (SettingS){
            case 0://最低标准
                baseS = social.getBase();
                break;
            case 1://不缴纳
                baseS=0;
                break;
            case 2://自定义工资
                baseS=setting.getValS();//自定义的基数
                break;
        }
        detail = calculateSocial(detail,setting,baseS,social);

        //计算公积金相关
        float FundBase = setting.getFundBase();//自定义的公积金基数
        float FundPer  = setting.getFundPer()/100;//自定义公积金比例
        detail.setFund1(FundBase*FundPer);//个人公积金
        detail.setFund2(FundBase*FundPer);//单位公积金

        //计算应发工资
        float payable =detail.getBase();//初始是基本工资
        if(mapSalary!=null){//如果有自定义工资
           payable = calculatePayable(payable,detail,mapSalary);
        }
        detail.setPayable(payable);

        //计算个税
        double tax =calculateTax(detail,deduct);
        detail.setTax((float) tax);

        //计算国家减免项=单位养老+单位失业+单位工伤-工伤补充
        float free = detail.getPension2()+detail.getUnemployment2()+detail.getInjury()-social.getExtra();
        detail.setFree(free);

        //计算实发=应发-个人五险一金-个税+补收核减（个人）
        float paid = payable-detail.getPension1()-detail.getMedicare1()-detail.getUnemployment1()-detail.getDisease1()-detail.getFund1()-(float) tax+detail.getExtra1();
        detail.setPaid(paid);

        return  detail;
    }

    /**
     * 计算医保
     * @param setting 员工设置
     * @param base 基数
     * @param ruleMedicare  所属地方的医保
     * @return
     */
    public static Detail1 calculateMedicare(Detail1 detail, EnsureSetting setting, float base, RuleMedicare ruleMedicare){

        float medicare1=0;//个人医疗
        float disease1=0;//个人大病
        float medicare2=0;//单位医疗
        float disease2=0;//单位大病
        float birth=0;//单位生育

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

        return detail;

    }

    public static Detail1 calculateSocial(Detail1 detail, EnsureSetting setting, float base, RuleSocial ruleSocial){

        float pension1=0;//个人养老
        float unemployment1=0;//个人失业
        float pension2=0;//单位养老
        float unemployment2=0;//单位失业
        float injury=0;//单位工伤

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
        return detail;
    }

    /**
     *计算普通结算单
     * @param settlement1 结算单
     * @param vc 合同视图
     * @param detail1s 明细
     * @return
     */
    public static Settlement1 calculateSettlement1(Settlement1 settlement1, ViewContractCooperation vc, List<ViewDetail1> detail1s){
        float salary=0;//应发总额
        float social=0;//单位社保
        float medicare=0;//单位医保
        float fund = 0;//单位公积金
        float summary;//实发总额
        float manage =0;//管理费
        float extra = 0;//额外
        float free = 0;//国家减免项
        float tax = 0;//税费

        for(ViewDetail1 viewDetail1:detail1s){
            //应发总额+=明细中的应发总额
           salary+=viewDetail1.getPayable();
            //单位社保总额+=（单位失业+单位养老+单位工商）
           social+=(viewDetail1.getPension2()+viewDetail1.getUnemployment2()+viewDetail1.getInjury());
           //单位医保总额+=（单位医保+单位大病+单位生育）
           medicare+=(viewDetail1.getMedicare2()+viewDetail1.getDisease2()+viewDetail1.getBirth());
            //单位公积金总额+=单位公积金
           fund+=viewDetail1.getFund2();
            //补收核减
           extra+=viewDetail1.getExtra2();
           //国家减免
           free+=viewDetail1.getFree();
        }

        int type = vc.getStype();//合同服务项目中的类型
        int category = vc.getCategory();//合同服务项目中的结算方式
        int invoice = vc.getInvoice();//合同基础信息中的发票类型
        float per = vc.getPer()/100;//税费比例（选择增值税专用发票（全额）需要用到）
        float value = vc.getValue();//结算值，根据结算方式的不同，代表的意义不同

        float num = detail1s.size();//总人数
        switch (type){
            case 0://劳务外包派遣
                if(category==0){//按人数收取的结算方式
                    manage = num*value;//管理费总额=人数*管理费
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

        summary=salary+social+medicare+fund+manage+tax+extra-free;//总额
        settlement1.setSalary(salary);
        settlement1.setSocial(social);
        settlement1.setMedicare(medicare);
        settlement1.setFund(fund);
        settlement1.setManage(manage);
        settlement1.setTax(tax);
        settlement1.setSummary(summary);
        settlement1.setExtra(extra);
        settlement1.setFree(free);
        return settlement1;
    }


    /**
     *计算普通结算单应发工资
     * @param v 结算单明细
     * @param mapSalary 自定义工资
     * @return
     */
    private static float calculatePayable(float payable,Detail1 v, MapSalary mapSalary){
        /**
         * 思路：
         */
        List<Items> itemList = mapSalary.getItemList();
        for (int i = 0; i <itemList.size(); i++) {
            int index = i + 1;
            String name = "getF" + index;
            float value2 = 0;
            Method method;
            try {//通过反射获取对应的值
                method = v.getClass().getSuperclass().getMethod(name);
                value2 = Float.parseFloat(method.invoke(v).toString());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if(itemList.get(i).getType()==0){//减项
                payable-=value2;
            }else {//加项
                payable+=value2;
            }
        }
        return  payable;
    }


    /**
     * 计算普通结算单的个税
     * @param v 结算单明细
     * @return v
     */
    private static double calculateTax(Detail1 v, Deduct deduct){
        double tax;//个税 = 应税额*税率（A） – 速算扣除（B） – 累计已预缴税额（C）
        float taxDue;//应税额 = 累计收入额（D）+ 本期收入 – 个税累计专项扣除（E）– 累计减除费用（F）
        float income1;//本期收入 = 本月应发（G）– 本月个人五险一金（H）+ 补收核减（个人）
        income1 = v.getPayable() - v.getPension1()-v.getMedicare1()-v.getUnemployment1()-v.getDisease1()-v.getFund1()+v.getExtra1();
        taxDue=deduct.getIncome()+income1-deduct.getDeduct()-deduct.getFree();

        float rate = 0;//税率
        float d = 0;//速算扣除
        if(taxDue>0&&taxDue<=36000){//根据个税比例报表计算个税
            rate = 0.03f;
            d = 0;
        }else if(taxDue<=144000){
            rate = 0.1f;
            d = 2520;
        }else if(taxDue<=300000){
            rate = 0.2f;
            d = 16920;
        }else if(taxDue<=420000){
            rate = 0.25f;
            d = 31920;
        }else if(taxDue<=660000){
            rate = 0.3f;
            d = 52920;
        }else if(taxDue<=960000){
            rate = 0.35f;
            d = 85920;
        }else if(taxDue>960000){
            rate = 0.45f;
            d = 181920;
        }

        tax = taxDue*rate-d-deduct.getPrepaid();

        if(tax < 0 ){
            tax = 0;
        }
        return tax;
    }

    /**
     * 计算小时工结算单明细
     * @param d 小时工结算单明细
     * @param deduct 所属员工的各项扣除
     * @return 计算好的结算单明细
     */
    public static Detail2 calculatteDetail2(Detail2 d,Deduct deduct){
        float payable;//应付金额
        float paid;//实付金额

        payable = d.getHours()*d.getPrice();//应付=工时*单价

        double tax=0;//个税 = 应税额*税率（A） – 速算扣除（B） – 累计已预缴税额（C）
        float income1;//本期收入 = 本月应发（G）
        float taxDue;//应税额 = 累计收入额（D）+ 本期收入 – 个税累计专项扣除（E）– 累计减除费用（F）

        income1 = payable;
        taxDue=deduct.getIncome()+income1-deduct.getDeduct()-deduct.getFree();

        if(taxDue>0&&taxDue<=36000){//根据个税比例报表计算个税
            tax = taxDue*0.03-deduct.getPrepaid();
        }else if(taxDue>36000&&taxDue<=144000){
            tax = taxDue*0.1-2520-deduct.getPrepaid();
        }else if(taxDue>144000&&taxDue<=300000){
            tax = taxDue*0.2-16920-deduct.getPrepaid();
        }else if(taxDue>300000&&taxDue<=420000){
            tax = taxDue*0.25-31920-deduct.getPrepaid();
        }else if(taxDue>420000&&taxDue<=660000){
            tax = taxDue*0.3-52920-deduct.getPrepaid();
        }else if(taxDue>660000&&taxDue<=960000){
            tax = taxDue*0.35-85920-deduct.getPrepaid();
        }else if(taxDue>960000){
            tax = taxDue*0.45-181920-deduct.getPrepaid();
        }

        paid = payable-d.getInsurance()-d.getTraffic()-d.getUtilities()-d.getAccommodation()-d.getFood()-d.getOther1()-d.getOther2()-(float) tax;

        d.setPayable(payable);
        d.setTax((float) tax);
        d.setPaid(paid);
       return d;
    }


    /**
     * 计算小时工结算单
     * @param settlement2   小时工结算单
     * @param detail2s  小时工结算明细列表
     * @return  计算好的结算单
     */
    public static Settlement2 calculateSettlement2(Settlement2 settlement2,List<Detail2> detail2s){
         int hours=0;//总工时
         float traffic=0;//交通费
         float extra=0;//附加
         float summary;//总额

        for (Detail2 detail2:detail2s){
            hours+=detail2.getHours();
            traffic+=detail2.getTraffic();
            extra+=(detail2.getOther1()+detail2.getOther2());
        }

        summary=hours*settlement2.getPrice();
        settlement2.setHours(hours);
        settlement2.setSummary(summary);
        settlement2.setExtra(extra);
        settlement2.setTraffic(traffic);

        return settlement2;
    }

}
