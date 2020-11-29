package dao.settlement;

import bean.settlement.Settlement0;
import bean.settlement.Settlement1;
import bean.settlement.ViewSettlement1;
import database.*;

import java.sql.Connection;

public class Settlement0Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            //根据地市模糊查询
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn, "view_settlement0", param, ViewSettlement1.class);

    }

    public static DaoUpdateResult insert(Connection conn, Settlement0 s) {
        String sql = "insert into settlement0 (did,cid,type,month,amount,tax) values (?,?,?,?,?,?)";
        Object []params = {s.getDid(),s.getCid(),s.getType(),s.getMonth(),s.getAmount(),s.getTax()};
        return DbUtil.insert(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"settlement0",conditions);

    }
    /**
     * 提交，将结算单的status 设置为 1
     * @param conn
     * @param id  要提交的结算单id
     * @return
     */
    public static DaoUpdateResult commit(Connection conn, long id) {
        String sql = "update settlement0 set status=1 where id = ?";
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }

    /**
     * 审核，修改结算单状态为几审
     * @param conn
     * @param id 要审核的结算单id
     * @param type 0-初审;1-终审
     * @param result true-通过;false-不通过
     * @return
     */
    public static DaoUpdateResult check(Connection conn, long id,byte type,boolean result) {
        byte status;
        if(type==0){
            status = result?Settlement1.STATUS_CHECKED1:Settlement1.STATUS_EDITING;
        }else{
            status = result?Settlement1.STATUS_CHECKED2:Settlement1.STATUS_COMMITED;
        }
        String sql = "update settlement0 set status=? where id = ?";
        Object []params = {status,id};
        return DbUtil.update(conn,sql,params);
    }

    /**
     * 重置结算单，将结算单的状态设置为0编辑状态
     * @param conn
     * @param id
     * @return
     */
    public static DaoUpdateResult reset(Connection conn,long id) {
        String sql = "update settlement0 set status=0 where id = ?";
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }

    /**
     * 确认扣款 设置结算单为扣款
     * @param conn
     * @param id
     * @return
     */
    public static DaoUpdateResult deduct(Connection conn,long id) {
        String sql = "update settlement0 set status=5 where id = ?";
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }

    /**
     * 确认发放，修改结算单状态为发放
     * @param conn
     * @param id
     * @return
     */
    public static DaoUpdateResult confirm(Connection conn,long id) {
        String sql = "update settlement0 set status=6 where id = ?";
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }

    //获取结算单详情
    public static DaoQueryResult get(Connection conn,long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"view_settlement0",conditions,ViewSettlement1.class);
    }

    //修改结算单
    public static DaoUpdateResult update(Connection conn, Settlement0 s) {
        String sql = "update settlement1 set did=?,cid=?,type=?,month=?,amount=?,tax=? where id = ?";
        Object []params = {s.getDid(),s.getCid(),s.getType(),s.getMonth(),s.getAmount(),s.getTax(),s.getId()};
        return DbUtil.update(conn,sql,params);
    }


}
