package dao.settlement;


import bean.settlement.Settlement2;
import bean.settlement.ViewSettlement2;
import database.*;

import java.sql.Connection;

public class Settlement2Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            //根据地市模糊查询
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn, "view_settlement2", param, ViewSettlement2.class);

    }
    //获取结算单详情
    public static DaoQueryResult get(Connection conn,long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"view_settlement2",conditions,ViewSettlement2.class);
    }
    public static DaoUpdateResult insert(Connection conn, Settlement2 s) {
        String sql = "insert into settlement2 (did,cid,ccid,month,hours,price,traffic,extra,summary,status,flag) values (?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {s.getDid(),s.getCid(),s.getCcid(),s.getMonth(),s.getHours(),s.getPrice(),s.getTraffic(),s.getExtra(),s.getSummary(),s.getStatus(),s.getFlag()};
        return DbUtil.insert(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"settlement2",conditions);
    }

    /**
     * 修改结算单状态
     * @param conn
     * @param id
     * @return
     */
    public static DaoUpdateResult updateStatus(Connection conn,long id, byte status) {
        String sql = "update settlement2 set status=? where id = ?";
        Object []params = {status,id};
        return DbUtil.update(conn,sql,params);
    }

    public static String exportBank(Connection conn, long id,String bank) {
        return null;
    }

    public static String saveAs(Connection conn, long id,String month) {
        return null;
    }

    //修改结算单
    public static DaoUpdateResult update(Connection conn, Settlement2 settlement2) {
        String sql = "update settlement2 set did=?,cid=?,month=?,hours=?,price=?,traffic=?,extra=?,summary=?,status=?,flag=?  where id = ?";
        Object []params = {settlement2.getDid(),settlement2.getCid(),settlement2.getMonth(),settlement2.getHours(),settlement2.getPrice()
        ,settlement2.getTraffic(),settlement2.getExtra(),settlement2.getSummary(),settlement2.getStatus(),settlement2.getFlag(),settlement2.getId()};
        return DbUtil.update(conn,sql,params);
    }
}
