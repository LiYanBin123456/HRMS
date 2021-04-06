package dao.settlement;

import bean.settlement.Settlement4;
import bean.settlement.ViewSettlement4;
import database.*;

import java.sql.Connection;

public class Settlement4Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            //根据地市模糊查询
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn, "view_settlement4", param, ViewSettlement4.class);

    }

    public static DaoUpdateResult insert(Connection conn, Settlement4 s) {
        String sql = "insert into settlement4 (did,cid,type,month,amount,tax,status,paid) values (?,?,?,?,?,?,?,?)";
        Object []params = {s.getDid(),s.getCid(),s.getType(),s.getMonth(),s.getAmount(),s.getTax(),s.getStatus(),s.getPaid()};
        return DbUtil.insert(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"settlement4",conditions);

    }



    /**
     * 修改结算单状态
     * @param conn
     * @param id
     * @return
     */
    public static DaoUpdateResult updateStatus(Connection conn,long id,byte status) {
        String sql = "update settlement4 set status=? where id = ?";
        Object []params = {status,id};
        return DbUtil.update(conn,sql,params);
    }

    //获取结算单详情
    public static DaoQueryResult get(Connection conn,long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"view_settlement4",conditions,ViewSettlement4.class);
    }

    //修改结算单
    public static DaoUpdateResult update(Connection conn, Settlement4 s) {
        String sql = "update settlement4 set did=?,cid=?,type=?,month=?,amount=?,tax=?,status=?,paid=? where id = ?";
        Object []params = {s.getDid(),s.getCid(),s.getType(),s.getMonth(),s.getAmount(),s.getTax(),s.getStatus(),s.getPaid(),s.getId()};
        return DbUtil.update(conn,sql,params);
    }


}
