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
        String sql = "update ensureSetting set city=?,settingM=?,valM=?,medicare=?,settingS=?,vaLS=?,social=?,fundPer=?,fundBase=?,product=? where eid=? ";
        Object []params = {s.getCity(),s.getSettingM(),s.getValM(),s.getMedicare(),s.getSettingS(),s.getValS(),s.getSocial(),s.getFundPer(),s.getFundBase(),s.getProduct(),s.getEid()};
        return  DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult updateInjuryPer(Connection conn, String[] eids, float per) {
        String sql = String.format("update ensureSetting set injuryPer = %f where eid = ?",per);
        Object [][]params = new Object[eids.length][];
        for (int i = 0; i < eids.length; i++) {
            params[i] = new Object[]{eids[i]};
        }
        return DbUtil.batch(conn,sql,params);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, EnsureSetting s) {
        String sql = "insert ensureSetting (eid,city,settingM,valM,medicare,settingS,valS,social,injuryPer,fundPer,fundBase,product) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {s.getEid(),s.getCity(),s.getSettingM(),s.getValM(),s.getMedicare(),s.getSettingS(),s.getValS(),s.getSocial(),s.getInjuryPer(),s.getFundPer(),s.getFundBase(),s.getProduct()};
        return  DbUtil.insert(conn,sql,params);
    }

    //判断是否已经存在
    public static DaoExistResult exist(Connection conn, long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.exist(conn,"ensureSetting",conditions);
    }
}
