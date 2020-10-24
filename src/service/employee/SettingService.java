package service.employee;

import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.insurance.Insurance;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import dao.employee.EmployeeDao;
import dao.employee.SettingDao;
import dao.insurance.InsuranceDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import database.*;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;

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

}
