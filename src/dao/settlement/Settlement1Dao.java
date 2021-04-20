package dao.settlement;

import bean.settlement.*;
import database.*;

import java.sql.Connection;

public class Settlement1Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            //根据地市模糊查询
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn, "view_settlement1", param, ViewSettlement1.class);
    }

    public static DaoUpdateResult insert(Connection conn, Settlement1 s) {
        String sql = "insert into settlement1 (did,cid,ccid,type,month,salary,social,medicare,fund,buss,manage,tax,free,extra,summary,status,flag,comments) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {s.getDid(),s.getCid(),s.getCcid(),s.getType(),s.getMonth(),s.getSalary(),s.getSocial(),s.getMedicare(),s.getFund(),s.getBuss(),s.getManage(),s.getTax(),s.getFree(),s.getExtra(),s.getSummary(),s.getStatus(),s.getFlag(),s.getComments()};
        return DbUtil.insert(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"settlement1",conditions);
    }

    /**
     * 修改结算单状态
     * @param conn
     * @param id
     * @param status
     * @return
     */
    public static DaoUpdateResult updateStatus(Connection conn,long id,byte status) {
        String sql = "update settlement1 set status=? where id = ?";
        Object []params = {status,id};
        return DbUtil.update(conn,sql,params);
    }

    //获取结算单详情
    public static DaoQueryResult get(Connection conn,long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"view_settlement1",conditions,ViewSettlement1.class);
    }

    /*public static DaoUpdateResult updateExtra(Connection conn, Settlement1 s) {
        String sql = "update settlement1 set extra=?,comments=? where id = ?";
        Object []params = {s.getExtra(),s.getComments(),s.getId()};
        return DbUtil.update(conn,sql,params);
    }*/

    //修改结算单
    public static DaoUpdateResult update(Connection conn, Settlement1 s) {
        String sql = "update settlement1 set did=?,cid=?,month=?,salary=?,social=?,medicare=?,fund=? ,buss=?,manage=?,tax=?,extra=?,free=?,summary=?,status=?,flag=?,comments=? where id = ?";
        Object []params = {s.getDid(),s.getCid(),s.getMonth(),s.getSalary(),s.getSocial(),s.getMedicare(),s.getFund(),s.getBuss(),s.getManage(),s.getTax(),s.getExtra(),s.getFree(),s.getSummary(),s.getStatus(),s.getFlag(),s.getComments(),s.getId()};
        return DbUtil.update(conn,sql,params);
    }
}
