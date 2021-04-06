package service.employee;

import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.insurance.Insurance;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import com.alibaba.fastjson.JSONObject;
import dao.employee.EmployeeDao;
import dao.employee.SettingDao;
import dao.insurance.InsuranceDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import database.*;

import java.sql.Connection;
import java.util.*;

public class SettingService {
    //今天日期
   static Date date = new Date(System.currentTimeMillis());

    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        return SettingDao.get(conn,id);
    }

    //修改完之后自动修改员工参保单
    public static DaoUpdateResult update(Connection conn, EnsureSetting setting) {
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult result = new DaoUpdateResult();
        RuleMedicare ruleMedicare = (RuleMedicare) RuleMedicareDao.getLast(conn, setting.getCity()).data;
        RuleSocial ruleSocial = (RuleSocial) RuleSocialDao.getLast(conn,setting.getCity()).data;
        if(ruleMedicare==null||ruleSocial==null){
            result.success = false;
            result.msg = "该员工社保规则所在地不存在，请核对";
            return result;
        }
        result = SettingDao.update(conn,setting);

        if(result.success){//自动插入参保单
            QueryConditions conditions = new QueryConditions();
            conditions.add("id","=",setting.getEid());
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;
            Date date = employee.getEntry();//入职时间，也就是参保时间

            //调用自动生成参保单的方法
            Insurance insurance = InsuranceDao.autoCreateInsurance(setting,date,ruleMedicare,ruleSocial,conn);
            DaoUpdateResult result1;
            if(InsuranceDao.exist(conn,setting.getEid()).exist){
                //修改参保单
                result1 = InsuranceDao.update(conn,insurance);
            }else {
                //插入
                result1 = InsuranceDao.insert(conn,insurance);
            }

            if(result1.success){
                ConnUtil.commit(conn);
            }else {
                ConnUtil.rollback(conn);
                result1.msg = "自动生成参保单失败";
                return result1;
            }
        }
        return result;
    }

    //增加完员工社保设置之后自动生成参保单
    public static DaoUpdateResult insert(Connection conn, EnsureSetting setting) {
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult result = new DaoUpdateResult();
        if(SettingDao.exist(conn,setting.getEid()).exist){
            result.success = false;
            result.msg = "该员工社保设置已存在，请勿重复添加";
            return result;
        }
        RuleMedicare ruleMedicare = (RuleMedicare) RuleMedicareDao.getLast(conn, setting.getCity()).data;
        RuleSocial ruleSocial = (RuleSocial) RuleSocialDao.getLast(conn,setting.getCity()).data;
        if(ruleMedicare==null||ruleSocial==null){
            result.success = false;
            result.msg = "该员工社保规则所在地不存在，请核对";
            return result;
        }
        result = SettingDao.insert(conn,setting);
        if(result.success){//自动插入参保单
            QueryConditions conditions = new QueryConditions();
            conditions.add("id","=",setting.getEid());
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;
            Date date = employee.getEntry();//入职时间，也就是参保时间

            //调用自动生成参保单的方法
            Insurance insurance = InsuranceDao.autoCreateInsurance(setting,date,ruleMedicare,ruleSocial,conn);
            DaoUpdateResult result1;
            if(InsuranceDao.exist(conn,setting.getEid()).exist){
                //修改参保单
                result1 = InsuranceDao.update(conn,insurance);
            }else {
                //插入
                result1 = InsuranceDao.insert(conn,insurance);
            }

            if(result1.success){
                ConnUtil.commit(conn);
            }else {
                ConnUtil.rollback(conn);
                result1.msg = "自动生成参保单失败";
                return result1;
            }
        }
     return result;
    }

    public static String settingBatch(Connection conn, String[] eids, EnsureSetting setting) {
        RuleMedicare ruleMedicare = (RuleMedicare) RuleMedicareDao.getLast(conn, setting.getCity()).data;
        RuleSocial ruleSocial = (RuleSocial) RuleSocialDao.getLast(conn,setting.getCity()).data;
        if(ruleMedicare==null||ruleSocial==null){
            return DaoResult.fail("该员工社保规则所在地不存在，请核对");
        }

        List<EnsureSetting> settings_update=new ArrayList<>();//待修改的医社保设置集合
        List<EnsureSetting> settings_insert=new ArrayList<>();//待添加的医社保设置集合
        List<Insurance> insurances_update=new ArrayList<>();//待修改的参保集合
        List<Insurance> insurances_insert=new ArrayList<>();//待添加的参保集合
        DaoUpdateResult result=new DaoUpdateResult();
        for(int i=0;i<eids.length;i++){
            EnsureSetting s=new EnsureSetting();
            s.setEid(Long.parseLong(eids[i]));
            s.setCity(setting.getCity());
            s.setBaseFund(setting.getBaseFund());
            s.setPerFund(setting.getPerFund());
            s.setPerInjury(setting.getPerInjury());
            s.setMedicare(setting.getMedicare());
            s.setSettingM(setting.getSettingM());
            s.setSettingS(setting.getSettingS());
            s.setSocial(setting.getSocial());
            s.setBaseM(setting.getBaseM());
            s.setBaseS(setting.getBaseS());

            if(SettingDao.exist(conn,s.getEid()).exist){
                settings_update.add(s);
            }else{
                settings_insert.add(s);
            }

            //调用自动生成参保单的方法
            Insurance insurance = InsuranceDao.autoCreateInsurance(s,date,ruleMedicare,ruleSocial,conn);
            if(InsuranceDao.exist(conn,setting.getEid()).exist){
                insurances_update.add(insurance);
            }else {
                insurances_insert.add(insurance);
            }
        }

        ConnUtil.closeAutoCommit(conn);
        DaoUpdateResult res1 = SettingDao.insert(conn,settings_insert);
        DaoUpdateResult res2 = SettingDao.update(conn,settings_update);
        DaoUpdateResult res3 = InsuranceDao.insert(conn,insurances_insert);
        DaoUpdateResult res4 = InsuranceDao.update(conn,insurances_update);
        if(res1.success && res2.success && res3.success && res4.success){
            ConnUtil.commit(conn);
            return JSONObject.toJSONString(res1);
        }else{
            ConnUtil.rollback(conn);
            return DaoUpdateResult.fail("数据库操作错误");
        }
    }

}
