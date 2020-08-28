package dao.settlement;

import bean.rule.RuleFund;
import bean.settlement.Settlement1;
import bean.settlement.Settlement3;
import bean.settlement.ViewSettlement3;
import database.*;

import java.sql.Connection;

public class Settlement3Dao {
    //获取结算单列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            //根据地市模糊查询
            param.addCondition("concat(cname,month,status)","like",param.conditions.extra);
        }
        return DbUtil.getList(conn, "view_settlement3", param, ViewSettlement3.class);

    }
    //获取结算单详情
    public static DaoQueryResult get(Connection conn,long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"settlement3",conditions,Settlement3.class);
    }
    //添加结算单
    public static DaoUpdateResult insert(Connection conn, Settlement3 s) {
        String sql = "insert into settlement3 (did,cid,pid,month,price,status,source) values (?,?,?,?,?,?,?)";
        Object []params = {s.getDid(),s.getCid(),s.getPid(),s.getMonth(),s.getPrice(),s.getStatus(),s.getSource()};
        return DbUtil.insert(conn,sql,params);
    }

    //删除结算单
    public static DaoUpdateResult delete(Connection conn, Long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"settlement3",conditions);
    }



    /**
     * 提交，将结算单的status 设置为 1
     * @param conn
     * @param id  要提交的结算单id
     * @return
     */
    public static DaoUpdateResult commit(Connection conn, long id) {
        String sql = "update settlement3 set status=1 where id = ?";
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }

    /**
     * 审核，修改结算单状态为几审
     * @param conn
     * @param id 要审核的结算单id
     * @param status  几审
     * @return
     */
    public static DaoUpdateResult check(Connection conn, long id,byte status) {
        String sql = String.format("update settlement3 set status=%s where id = ?",status);
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }

    /**
     * 重置结算单，将结算单的状态设置为0编辑状态
     * @param conn
     * @param id
     * @return
     */
    public static DaoUpdateResult reset(Connection conn,long id) {
        String sql = "update settlement3 set status=0 where id = ?";
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
        String sql = "update settlement3 set status=5 where id = ?";
        Object []params = {id};
        return DbUtil.update(conn,sql,params);
    }


}
