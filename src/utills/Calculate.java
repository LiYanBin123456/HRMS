package utills;

import bean.client.Items;
import bean.client.MapSalary;
import bean.contract.Contract;
import bean.contract.Serve;
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

import java.awt.geom.FlatteningPathIterator;
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
        //初始化数据
         float pension1=0;//个人养老
         float medicare1=0;//个人医疗
         float unemployment1=0;//个人失业
         float disease1=0;//个人大病
         float fund1=0;//个人公积金
         float pension2=0;//单位养老
         float medicare2=0;//单位医疗
         float unemployment2=0;//单位失业
         float injury=0;//单位工伤
         float disease2=0;//单位大病
         float birth=0;//单位生育
         float fund2=0;//单位公积金
        //初始化明细
        Detail1 detail = null;
        detail= new Detail1(pension1,medicare1,unemployment1,disease1,fund1,pension2,medicare2,unemployment2,injury,disease2,birth,fund2);
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
        switch (SettingM){
            case 0://最低标准
                medicare1 = medicare.getBase()*medicare.getPer2();//个人医疗
                medicare2 = medicare.getBase()*medicare.getPer1();//单位医疗
                birth = medicare.getBase()*medicare.getPer3();//单位生育
                disease1 = medicare.getFin2();//个人大病
                disease2 = medicare.getFin1();//单位大病
                break;
            case 1://实际工资，因为模板中暂时没有实际工资，为0
                medicare1 =0;//个人医疗
                medicare2 = 0;//单位医疗
                birth = 0;//单位生育
                disease1 = medicare.getFin2();//个人大病
                disease2 = medicare.getFin1();//单位大病
                break;
            case 2://不交纳，为0
                medicare1 =0;//个人医疗
                medicare2 = 0;//单位医疗
                birth = 0;//单位生育
                disease1 = 0;//个人大病
                disease2 =0;//单位大病
                break;
            case 3://自定义基数
                float ValM = setting.getValM();//自定义的基数
                medicare1 = ValM*medicare.getPer2();//个人医疗
                medicare2 =ValM*medicare.getPer1();//单位医疗
                birth = ValM*medicare.getPer3();//单位生育
                disease1 = medicare.getFin2();//个人大病
                disease2 = medicare.getFin1();//单位大病
                break;
        }

        //获取社保相关
        int SettingS = setting.getSettingS();//员工社保设置
        switch (SettingS){
            case 0://最低标准
                pension1=social.getBase()*social.getPer2();//个人养老
                unemployment1=social.getBase()*social.getPer5();//个人失业
                pension2=social.getBase()*social.getPer1();//单位养老
                unemployment2=social.getBase()*social.getPer4();//单位失业
                injury=social.getBase()*social.getPer3();//单位工伤
                break;
            case 1://实际工资
                pension1=0;//个人养老
                unemployment1=0;//个人失业
                pension2=0;//单位养老
                unemployment2=0;//单位失业
                injury=0;//单位工伤
                break;
            case 2://不缴纳，为0
                pension1=0;//个人养老
                unemployment1=0;//个人失业
                pension2=0;//单位养老
                unemployment2=0;//单位失业
                injury=0;//单位工伤
                break;
            case 3://自定义基数
                float ValS=setting.getValS();//自定义的基数
                pension1=ValS*social.getPer2();//个人养老
                unemployment1=ValS*social.getPer5();//个人失业
                pension2=ValS*social.getPer1();//单位养老
                unemployment2=ValS*social.getPer4();//单位失业
                injury=ValS*social.getPer3();//单位工伤
                break;
        }

        //获取公积金相关
        float FundBase = setting.getFundBase();//自定义的公积金基数
        float FundPer  = setting.getFundPer();//自定义公积金比例
        fund1=FundBase*FundPer;//个人公积金
        fund2=FundBase*FundPer;//单位公积金
        detail= new Detail1(pension1,medicare1,unemployment1,disease1,fund1,pension2,medicare2,unemployment2,injury,disease2,birth,fund2);
        return  detail;
    }


    /**
     * 计算普通结算单
     * @param detail1 //结算单明细
     * @param medicare //所在地市的医保规则
     * @param social //所在地市的社保规则
     * @param setting //所在地市的公积金规则
     * @param mapSalary //合作单位的自定义工资
     * @param deduct   //所属员工的个税专项扣除
     * @return detail1 //计算好的结算单
     */
    public static Detail1 calculateDetail1(Detail1 detail1, RuleMedicare medicare, RuleSocial social, EnsureSetting setting, MapSalary mapSalary, Deduct deduct){
        //初始化数据
        float pension1=0;//个人养老
        float medicare1=0;//个人医疗
        float unemployment1=0;//个人失业
        float disease1=0;//个人大病
        float fund1;//个人公积金

        float pension2=0;//单位养老
        float medicare2=0;//单位医疗
        float unemployment2=0;//单位失业
        float injury=0;//单位工伤
        float disease2=0;//单位大病
        float birth=0;//单位生育
        float fund2;//单位公积金
        float base = detail1.getBase();//实际工资

        //计算医保相关
        int SettingM = setting.getSettingM();//员工医保设置
        switch (SettingM){
            case 0://最低标准
                medicare1 = medicare.getBase()*medicare.getPer2();//个人医疗
                medicare2 = medicare.getBase()*medicare.getPer1();//单位医疗
                birth = medicare.getBase()*medicare.getPer3();//单位生育
                disease1 = medicare.getFin2();//个人大病
                disease2 = medicare.getFin1();//单位大病
                break;
            case 1://实际工资
                medicare1 = base*medicare.getPer2();//个人医疗
                medicare2 = base*medicare.getPer1();//单位医疗
                birth = base*medicare.getPer3();//单位生育
                disease1 = medicare.getFin2();//个人大病
                disease2 = medicare.getFin1();//单位大病
                break;
            case 2://不交纳，为0
                medicare1 =0;//个人医疗
                medicare2 = 0;//单位医疗
                birth = 0;//单位生育
                disease1 = 0;//个人大病
                disease2 =0;//单位大病
                break;
            case 3://自定义基数
                float ValM = setting.getValM();//自定义的基数
                medicare1 = ValM*medicare.getPer2();//个人医疗
                medicare2 =ValM*medicare.getPer1();//单位医疗
                birth = ValM*medicare.getPer3();//单位生育
                disease1 = medicare.getFin2();//个人大病
                disease2 = medicare.getFin1();//单位大病
                break;
        }

        //计算社保相关
        int SettingS = setting.getSettingS();//员工社保设置
        switch (SettingS){
            case 0://最低标准
                pension1=social.getBase()*social.getPer2();//个人养老
                unemployment1=social.getBase()*social.getPer5();//个人失业
                pension2=social.getBase()*social.getPer1();//单位养老
                unemployment2=social.getBase()*social.getPer4();//单位失业
                injury=social.getBase()*social.getPer3();//单位工伤
                break;
            case 1://实际工资
                pension1=base*social.getPer2();//个人养老
                unemployment1=base*social.getPer5();//个人失业
                pension2=base*social.getPer1();//单位养老
                unemployment2=base*social.getPer4();//单位失业
                injury=base*social.getPer3();//单位工伤
                break;
            case 2://不缴纳，为0
                pension1=0;//个人养老
                unemployment1=0;//个人失业
                pension2=0;//单位养老
                unemployment2=0;//单位失业
                injury=0;//单位工伤
                break;
            case 3://自定义基数
                float ValS=setting.getValS();//自定义的基数
                pension1=ValS*social.getPer2();//个人养老
                unemployment1=ValS*social.getPer5();//个人失业
                pension2=ValS*social.getPer1();//单位养老
                unemployment2=ValS*social.getPer4();//单位失业
                injury=ValS*social.getPer3();//单位工伤
                break;
        }

        //计算公积金相关
        float FundBase = setting.getFundBase();//自定义的公积金基数
        float FundPer  = setting.getFundPer();//自定义公积金比例
        fund1=FundBase*FundPer;//个人公积金
        fund2=FundBase*FundPer;//单位公积金

        detail1.setPension1(pension1);
        detail1.setMedicare1(medicare1);
        detail1.setUnemployment1(unemployment1);
        detail1.setDisease1(disease1);
        detail1.setFund1(fund1);
        detail1.setPension2(pension2);
        detail1.setMedicare2(medicare2);
        detail1.setUnemployment2(unemployment2);
        detail1.setDisease2(disease2);
        detail1.setInjury(injury);
        detail1.setBirth(birth);
        detail1.setFund2(fund2);


        //计算应发工资
        float payable =base;//初始是基本工资
        List<Items> itemList = mapSalary.getItemList();

        for (int i = 0; i <itemList.size(); i++) {
            int index = i + 1;
            String name = "getF" + index;
            float value2 = 0;
            Method method;
            try {//通过反射获取对应的值
                method = detail1.getClass().getSuperclass().getMethod(name);
                value2 = Float.parseFloat(method.invoke(detail1).toString());
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
        detail1.setPayable(payable);

        //计算个税
        double tax=0;//个税 = 应税额*税率（A） – 速算扣除（B） – 累计已预缴税额（C）
        float income1;//本期收入 = 本月应发（G）– 本月个人五险一金（H）
        float taxDue;//应税额 = 累计收入额（D）+ 本期收入 – 个税累计专项扣除（E）– 累计减除费用（F）
        income1 = detail1.getPayable() - detail1.getPension1()-detail1.getMedicare1()-detail1.getUnemployment1()-detail1.getDisease1()-detail1.getFund1();
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
        detail1.setTax((float) tax);

        //计算实发=应发-个人五险一金-个税
        float paid = payable-pension1-medicare1-unemployment1-disease1-fund1-(float) tax;
        detail1.setPaid(paid);

        return  detail1;
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
        float summary=0;//实发总额
        float manage =0;//管理费
        float tax = 0;//税费

        for(ViewDetail1 viewDetail1:detail1s){
            //应发总额+=明细中的应发总额
           salary+=viewDetail1.getPaid();
            //单位社保总额+=（单位失业+单位养老+单位工商）
           social+=(viewDetail1.getPension2()+viewDetail1.getUnemployment2()+viewDetail1.getInjury());
           //单位医保总额+=（单位医保+单位大病+单位生育）
           medicare+=(viewDetail1.getMedicare2()+viewDetail1.getDisease2()+viewDetail1.getBirth());
            //单位公积金总额+=（单位医保+单位大病+单位生育）
           fund+=viewDetail1.getFund2();
        }

        int type = vc.getStype();//合同服务项目中的类型
        int category = vc.getCategory();//合同服务项目中的结算方式
        int invoice = vc.getInvoice();//合同基础信息中的发票类型
        float per = vc.getPer();//税费比例（选择增值税专用发票（全额）需要用到）
        float value = vc.getValue();//结算值，根据结算方式的不同，代表的意义不同

        float num = detail1s.size();//总人数
        switch (type){
            case 0://劳务外包派遣
                if(category==0){//按人数收取的结算方式
                    manage = num*value;//管理费=人数*单价
                    if(invoice==0){//增值税专用发票（全额）
                      tax=(salary+social+medicare+fund+manage)*per;//税费 = （应发总额+单位五险一金+管理费）*税率（基准6.72，但可以浮动）
                    }else if(invoice==1){//增值税专用发票（差额）
                      tax=0;
                    }else {//普通发票
                      tax=(salary+social+medicare+fund+manage)*per;//税费 = （应发总额+单位五险一金+管理费）*税率（基准6.72，但可以浮动）
                    }
                }else if(category==1){//按比例收取的结算方式
                    //此时服务项目中value为比例所以需要转成小数
                    manage = (salary+social+medicare+fund)*(value/100);//管理费 = （应发总额+单位五险一金）*比例（从服务项目中的比例）
                    tax = 0;
                }else {//按外包整体核算方式（暂时不考虑）

                }
                break;
            case 1://人事代理
                tax=0;
                manage = num*value;//管理费=人数*单价
                break;
        }

        summary=salary+social+medicare+fund+manage+tax;//总额
        settlement1.setSalary(salary);
        settlement1.setSocial(social);
        settlement1.setMedicare(medicare);
        settlement1.setFund(fund);
        settlement1.setManage(manage);
        settlement1.setTax(tax);
        settlement1.setSummary(summary);
        return settlement1;
    }


    /**
     *计算普通结算单应发工资
     * @param v 结算单明细
     * @param mapSalary 自定义工资
     * @return
     */
    public static  ViewDetail1 calculatePayable(ViewDetail1 v, MapSalary mapSalary){
        float payable = 0;
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
        v.setPayable(payable);
        return v;
    }


    /**
     * 计算普通结算单的个税
     * @param v 结算单明细
     * @return v
     */
    public static double calculateTax(ViewDetail1 v, Deduct deduct){
        double tax=0;//个税 = 应税额*税率（A） – 速算扣除（B） – 累计已预缴税额（C）
        float income1;//本期收入 = 本月应发（G）– 本月个人五险一金（H）
        float taxDue;//应税额 = 累计收入额（D）+ 本期收入 – 个税累计专项扣除（E）– 累计减除费用（F）
        income1 = v.getPayable() - v.getPension1()-v.getMedicare1()-v.getUnemployment1()-v.getDisease1()-v.getFund1();
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
         float summary=0;//总额

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
