package dao.employee;

import bean.employee.EnsureSetting;
import database.*;

import java.sql.Connection;
import java.util.List;

public class SettingDao {
    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.get(conn,"ensureSetting",conditions,EnsureSetting.class);
    }

    //获取个税扣除视图列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"ensureSetting",param, EnsureSetting.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, EnsureSetting s) {
        String sql = "update ensureSetting set city=?,settingM=?,baseM=?,medicare=?,settingS=?,baseS=?,social=?,perInjury=?,perFund=?,baseFund=? where eid=? ";
        Object []params = {s.getCity(),s.getSettingM(),s.getBaseM(),s.getMedicare(),s.getSettingS(),s.getBaseS(),s.getSocial(),s.getPerInjury(),s.getPerFund(),s.getBaseFund(),s.getEid()};
        return  DbUtil.update(conn,sql,params);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, List<EnsureSetting> settings) {
        String sql = "update ensureSetting set city=?,settingM=?,baseM=?,medicare=?,settingS=?,baseS=?,social=?,perInjury=?,perFund=?,baseFund=? where eid=? ";
        Object [][]params = new Object[settings.size()][];
        for (int i = 0; i < settings.size(); i++) {
            EnsureSetting s = settings.get(i);
            params[i] = new Object[]{s.getCity(),s.getSettingM(),s.getBaseM(),s.getMedicare(),s.getSettingS(),s.getBaseS(),s.getSocial(),s.getPerInjury(),s.getPerFund(),s.getBaseFund(),s.getEid()};
        }
        return DbUtil.insertBatch(conn,sql,params);
    }

    public static DaoUpdateResult updateInjuryPer(Connection conn, String[] eids, float per) {
        String sql = String.format("update ensureSetting set perInjury = %f where eid = ?",per);
        Object [][]params = new Object[eids.length][];
        for (int i = 0; i < eids.length; i++) {
            params[i] = new Object[]{eids[i]};
        }
        return DbUtil.batch(conn,sql,params);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, EnsureSetting s) {
        String sql = "insert ensureSetting (eid,city,settingM,baseM,medicare,settingS,baseS,social,perInjury,perFund,baseFund) values (?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {s.getEid(),s.getCity(),s.getSettingM(),s.getBaseM(),s.getMedicare(),s.getSettingS(),s.getBaseS(),s.getSocial(),s.getPerInjury(),s.getPerFund(),s.getBaseFund()};
        return  DbUtil.insert(conn,sql,params);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, List<EnsureSetting> settings) {
        String sql = "insert ensureSetting (eid,city,settingM,baseM,medicare,settingS,baseS,social,perInjury,perFund,baseFund) values (?,?,?,?,?,?,?,?,?,?,?)";
        Object [][]params = new Object[settings.size()][];
        for (int i = 0; i < settings.size(); i++) {
            EnsureSetting s = settings.get(i);
            params[i] = new Object[]{s.getEid(),s.getCity(),s.getSettingM(),s.getBaseM(),s.getMedicare(),s.getSettingS(),s.getBaseS(),s.getSocial(),s.getPerInjury(),s.getPerFund(),s.getBaseFund()};
        }
        return DbUtil.insertBatch(conn,sql,params);
    }

    //判断是否已经存在
    public static DaoExistResult exist(Connection conn, long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.exist(conn,"ensureSetting",conditions);
    }
}
