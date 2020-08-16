package dao.employee;

import bean.employee.Employee;
import bean.employee.EmployeeSetting;
import com.alibaba.fastjson.JSONObject;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryConditions;

import java.sql.Connection;

public class SettingDao {
    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"employee_setting",conditions,Employee.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, EmployeeSetting s, byte category) {
        DaoUpdateResult res = null;
        if(category == 0){//修改社保信息
            String sql = "update employee_setting set city=?,settingM=?,valueM=?,settingS=?,valueS=?,fundPer=?,fundBase=?,product=? where eid=? ";
            Object []params = {s.getCity(),s.getSettingM(),s.getValueM(),s.getSettingS(),s.getValueS(),s.getFundPer(),s.getFundBase(),s.getProduct(),s.getEid()};
            res = DbUtil.update(conn,sql,params);
        }else{//修改个税专项扣除
            String sql = "update employee_setting set deduct1=?,deduct2=?,deduct3=?,deduct4=?,deduct5=?,deduct6=? where eid=? ";
            Object []params = {s.getDeduct1(),s.getDeduct2(),s.getDeduct3(),s.getDeduct4(),s.getDeduct5(),s.getDeduct6(),s.getEid()};
            res = DbUtil.update(conn,sql,params);
        }
        return res;
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, EmployeeSetting s) {
        String sql = "insert employee_setting (eid,city,settingM,valueM,settingS,valueS,fundPer,fundBase,product) values (?,?,?,?,?,?,?,?,?)";
        Object []params = {s.getEid(),s.getCity(),s.getSettingM(),s.getValueM(),s.getSettingS(),s.getValueS(),s.getFundPer(),s.getFundBase(),s.getProduct()};
        return  DbUtil.insert(conn,sql,params);
    }

}
