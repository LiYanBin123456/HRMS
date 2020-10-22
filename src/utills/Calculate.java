package utills;

import bean.client.Items;
import bean.client.MapSalary;
import bean.contract.ViewContractCooperation;
import bean.employee.Deduct;
import bean.employee.EnsureSetting;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.*;
import com.alibaba.fastjson.JSONObject;
import dao.employee.SettingDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import database.ConnUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
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
        Detail1 detail;
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
        JSONObject object;
        float baseM = 0;
        switch (SettingM){
            case 0://最低标准
                baseM = medicare.getBase();
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
        object = calculateMedicare(setting,baseM,medicare);
        medicare1 = Float.parseFloat(object.getString("medicare1"));//个人医疗
        medicare2 = Float.parseFloat(object.getString("medicare2"));//单位医疗
        birth = Float.parseFloat(object.getString("birth"));//单位生育
        disease1 = Float.parseFloat(object.getString("disease1"));//个人大病
        disease2 = Float.parseFloat(object.getString("disease2"));//单位大病

        //获取社保相关
        int SettingS = setting.getSettingS();//员工社保设置
        JSONObject object2;
        float baseS = 0;
        switch (SettingS){
            case 0://最低标准
                baseS =social.getBase();
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
        object2 = calculateSocial(setting,baseS,social);
        pension1 = Float.parseFloat(object2.getString("pension1"));
        unemployment1 = Float.parseFloat(object2.getString("unemployment1"));
        pension2 = Float.parseFloat(object2.getString("pension2"));
        unemployment2 = Float.parseFloat(object2.getString("unemployment2"));
        injury = Float.parseFloat(object2.getString("injury"));

        //获取公积金相关
        float FundBase = setting.getFundBase();//获取自定义的公积金基数
        float FundPer  = setting.getFundPer()/100;//获取自定义公积金比例

        fund1=FundBase*FundPer;//个人公积金
        fund2=FundBase*FundPer;//单位公积金
        detail= new Detail1(pension1,medicare1,unemployment1,disease1,fund1,pension2,medicare2,unemployment2,injury,disease2,birth,fund2);
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
        //初始化数据
        float pension1;//个人养老
        float medicare1;//个人医疗
        float unemployment1;//个人失业
        float disease1;//个人大病
        float fund1;//个人公积金

        float pension2;//单位养老
        float medicare2;//单位医疗
        float unemployment2;//单位失业
        float injury;//单位工伤
        float disease2;//单位大病
        float birth;//单位生育
        float fund2;//单位公积金

        float base = detail.getBase();//实际工资

        //计算医保相关
        int SettingM = setting.getSettingM();//员工医保设置
        JSONObject object;
        float baseM = 0;
        switch (SettingM){
            case 0://最低标准
                baseM = medicare.getBase();
                 break;
            case 1://实际工资
                baseM = base;
                break;
            case 2://不交纳，为0
                baseM = 0;
                break;
            case 3://自定义基数
                float ValM = setting.getValM();//自定义的基数
                baseM = ValM;
                break;
        }
        object = calculateMedicare(setting,baseM,medicare);
        medicare1 = Float.parseFloat(object.getString("medicare1"));//个人医疗
        medicare2 = Float.parseFloat(object.getString("medicare2"));//单位医疗
        birth = Float.parseFloat(object.getString("birth"));//单位生育
        disease1 = Float.parseFloat(object.getString("disease1"));//个人大病
        disease2 = Float.parseFloat(object.getString("disease2"));//单位大病


        //计算社保相关
        int SettingS = setting.getSettingS();//员工社保设置
        JSONObject object2;
        float baseS = 0;
        switch (SettingS){
            case 0://最低标准
                baseS = social.getBase();
                break;
            case 1://实际工资
                baseS = base;
                break;
            case 2://不缴纳，为0
                baseS=0;
                break;
            case 3://自定义基数
                baseS=setting.getValS();//自定义的基数
                break;
        }
        object2 = calculateSocial(setting,baseS,social);
        pension1 = Float.parseFloat(object2.getString("pension1"));
        unemployment1 = Float.parseFloat(object2.getString("unemployment1"));
        pension2 = Float.parseFloat(object2.getString("pension2"));
        unemployment2 = Float.parseFloat(object2.getString("unemployment2"));
        injury = Float.parseFloat(object2.getString("injury"));

        //计算公积金相关
        float FundBase = setting.getFundBase();//自定义的公积金基数
        float FundPer  = setting.getFundPer()/100;//自定义公积金比例

        fund1=FundBase*FundPer;//个人公积金
        fund2=FundBase*FundPer;//单位公积金

        detail.setPension1(pension1);
        detail.setMedicare1(medicare1);
        detail.setUnemployment1(unemployment1);
        detail.setDisease1(disease1);
        detail.setFund1(fund1);
        detail.setPension2(pension2);
        detail.setMedicare2(medicare2);
        detail.setUnemployment2(unemployment2);
        detail.setDisease2(disease2);
        detail.setInjury(injury);
        detail.setBirth(birth);
        detail.setFund2(fund2);


        //计算应发工资
        float payable =base;//初始是基本工资
        if(mapSalary!=null){//如果有自定义工资
           payable = calculatePayable(detail,mapSalary);
        }
        detail.setPayable(payable);

        //计算个税
        double tax =calculateTax(detail,deduct);
        detail.setTax((float) tax);

        //计算实发=应发-个人五险一金-个税
        float paid = payable-pension1-medicare1-unemployment1-disease1-fund1-(float) tax;
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
    public static JSONObject calculateMedicare(EnsureSetting setting, float base, RuleMedicare ruleMedicare){

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


        JSONObject o = new JSONObject();
        o.put("medicare1",medicare1);
        o.put("disease1",disease1);
        o.put("medicare2",medicare2);
        o.put("disease2",disease2);
        o.put("birth",birth);

        return o;

    }

    /**
     * 计算社保
     * @param setting 员工设置
     * @param base 基数
     * @param ruleSocial  所属地方的社保
     * @return
     */
    public static JSONObject calculateSocial(EnsureSetting setting, float base,RuleSocial ruleSocial){

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
            injury = base * (ruleSocial.getPer3() / 100);//单位工伤
        }
            JSONObject o = new JSONObject();
            o.put("pension1", pension1);
            o.put("unemployment1", unemployment1);
            o.put("pension2", pension2);
            o.put("unemployment2", unemployment2);
            o.put("injury", injury);
            return o;
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
           salary+=viewDetail1.getPayable();
            //单位社保总额+=（单位失业+单位养老+单位工商）
           social+=(viewDetail1.getPension2()+viewDetail1.getUnemployment2()+viewDetail1.getInjury());
           //单位医保总额+=（单位医保+单位大病+单位生育）
           medicare+=(viewDetail1.getMedicare2()+viewDetail1.getDisease2()+viewDetail1.getBirth());
            //单位公积金总额+=单位公积金
           fund+=viewDetail1.getFund2();
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
    private static float calculatePayable(Detail1 v, MapSalary mapSalary){
        /**
         * 思路：
         */
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
        float income1;//本期收入 = 本月应发（G）– 本月个人五险一金（H）
        income1 = v.getPayable() - v.getPension1()-v.getMedicare1()-v.getUnemployment1()-v.getDisease1()-v.getFund1();
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
