package dao.employee;

import bean.client.ViewFinance;
import bean.employee.EnsureSetting;
import bean.employee.ViewDeduct;
import database.*;

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
        String sql = "update ensureSetting set city=?,settingM=?,valM=?,settingS=?,vaLS=?,fundPer=?,fundBase=?,product=? where eid=? ";
        Object []params = {s.getCity(),s.getSettingM(),s.getValM(),s.getSettingS(),s.getValS(),s.getFundPer(),s.getFundBase(),s.getProduct(),s.getEid()};
        return  DbUtil.update(conn,sql,params);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, EnsureSetting s) {
        String sql = "insert ensureSetting (eid,city,settingM,valM,settingS,valS,fundPer,fundBase,product) values (?,?,?,?,?,?,?,?,?)";
        Object []params = {s.getEid(),s.getCity(),s.getSettingM(),s.getValM(),s.getSettingS(),s.getValS(),s.getFundPer(),s.getFundBase(),s.getProduct()};
        return  DbUtil.insert(conn,sql,params);
    }

    //判断是否已经存在
    public static DaoExistResult exist(Connection conn, long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.exist(conn,"ensureSetting",conditions);
    }
}
