package dao.settlement;

import bean.settlement.Settlement1;
import bean.settlement.ViewSettlement1;
import database.*;

import java.sql.Connection;

public class Settlement1Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            //根据地市模糊查询
            param.addCondition("concat(name,month,status)","like",param.conditions.extra);
        }
        return DbUtil.getList(conn, "view_settlement1", param, ViewSettlement1.class);

    }

    public static DaoUpdateResult insert(Connection conn, Settlement1 s) {
        String sql = "insert into settlement1 (did,cid,month,salary,social,fund,manage,tax,summary,status,source) values (?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {s.getDid(),s.getCid(),s.getMonth(),s.getSalary(),s.getSocial(),s.getFund(),s.getManage(),s.getTax(),s.getSummary(),s.getStatus(),s.getSource()};
        return DbUtil.insert(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"settlement1",conditions);

    }

    public static String saveAs(Connection conn, long id,String month) {
        return null;
    }

    public static DaoUpdateResult commit(Connection conn, long id) {
        String sql = "update settlement1 set status=1 where id = ?";
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult check(Connection conn, long id) {
        String sql = "update settlement1 set status=1 where id = ?";
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult reset(Connection conn,long id) {
        String sql = "update settlement1 set status=0 where id = ?";
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult deduct(Connection conn,long id) {
        String sql = "update settlement1 set status=5 where id = ?";
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult confirm(Connection conn,long id) {
        String sql = "update settlement1 set status=6 where id = ?";
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }

    public static String exportBank(Connection conn, long id,String bank) {
        return null;
    }

    public static String getLogs(Connection conn, long id) {
        return null;
    }
}
