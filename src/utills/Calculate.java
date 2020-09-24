package utills;

import bean.client.MapSalary;
import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.Detail1;
import bean.settlement.Settlement1;
import bean.settlement.ViewDetail1;
import dao.client.MapSalaryDao;
import dao.employee.EmployeeDao;
import dao.employee.SettingDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import dao.settlement.Settlement1Dao;
import database.ConnUtil;
import database.QueryConditions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

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
     * 计算五险一金
     * @param v 结算单明细
     * @return v 计算好五险一金后的明细
     */
    public static  ViewDetail1 calculateInsurance(ViewDetail1 v){
        Connection conn = ConnUtil.getConnection();
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


        QueryConditions conditions = new QueryConditions();//根据身份证获取员工
        conditions.add("cardId","=",v.getCardId());
        Employee employee=(Employee)EmployeeDao.get(conn,conditions).data;

        long eid = employee.getId();//员工id
        boolean existSetting=SettingDao.exist(conn,eid).exist;//员工设置是否存在
        if(!existSetting){//不存在，直接返回
            return  v;
        }
        EnsureSetting setting = (EnsureSetting) SettingDao.get(conn,eid).data;//员工设置
        String city = setting.getCity();//员工地市
        RuleMedicare medicare= (RuleMedicare) RuleMedicareDao.getLast(conn,city).data;//获取该地市的最新医保
        RuleSocial social = (RuleSocial) RuleSocialDao.getLast(conn,city).data;//获取该地市的最新社保

        float base = v.getBase();//实际工资

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

        //获取公积金相关
        float FundBase = setting.getFundBase();//自定义的公积金基数
        float FundPer  = setting.getFundPer();//自定义公积金比例
        fund1=FundBase*FundPer;//个人公积金
        fund2=FundBase*FundPer;//单位公积金

        v.setPension1(pension1);
        v.setMedicare1(medicare1);
        v.setUnemployment1(unemployment1);
        v.setDisease1(disease1);
        v.setFund1(fund1);
        v.setPension2(pension2);
        v.setMedicare2(medicare2);
        v.setUnemployment2(unemployment2);
        v.setDisease2(disease2);
        v.setInjury(injury);
        v.setBirth(birth);
        v.setFund2(fund2);
        return  v;
    }

    /**
     * 计算普通结算单明细应付金额
     * @param v  结算单明细
     * @return v 计算好应付金额的明细
     */
    public static  ViewDetail1 calculatePayable(ViewDetail1 v){
        Connection conn = ConnUtil.getConnection();
        long sid = v.getSid();//结算单id
        Settlement1 settlement1 =(Settlement1) Settlement1Dao.get(conn,sid).data;

        long cid = settlement1.getCid();//合作单位id
        float payable = v.getBase();
        //查询自定义工资
        MapSalary mapSalary = (MapSalary) MapSalaryDao.getLast(cid,conn).data;
        String[] maps = mapSalary.getItems().split(";");//maps[{加班工资,1},{考勤扣款,0}];
        for (int i = 0; i < maps.length; i++) {
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

            String[] value = maps[i].split(",");//[value加班工资,1];
            if(value[1].equals(0)){//减项
               payable-=value2;
            }else {//加项
                payable+=value2;
            }
        }
        v.setPayable(payable);
      return v;
    }
}