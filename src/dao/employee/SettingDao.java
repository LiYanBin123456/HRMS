package dao.employee;

import bean.employee.Employee;
import bean.employee.EnsureSetting;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryConditions;

import java.sql.Connection;

public class SettingDao {
    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.get(conn,"ensureSetting",conditions,EnsureSetting.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, EnsureSetting s) {
        String sql = "update ensureSetting set city=?,settingM=?,valueM=?,settingS=?,valueS=?,fundPer=?,fundBase=?,product=? where eid=? ";
        Object []params = {s.getCity(),s.getSettingM(),s.getValueM(),s.getSettingS(),s.getValueS(),s.getFundPer(),s.getFundBase(),s.getProduct(),s.getEid()};
        return  DbUtil.update(conn,sql,params);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, EnsureSetting s) {
        String sql = "insert ensureSetting (eid,city,settingM,valueM,settingS,valueS,fundPer,fundBase,product) values (?,?,?,?,?,?,?,?,?)";
        Object []params = {s.getEid(),s.getCity(),s.getSettingM(),s.getValueM(),s.getSettingS(),s.getValueS(),s.getFundPer(),s.getFundBase(),s.getProduct()};
        return  DbUtil.insert(conn,sql,params);
    }

}
