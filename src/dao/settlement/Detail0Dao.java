package dao.settlement;

import bean.settlement.Detail0;
import bean.settlement.Detail1;
import bean.settlement.ViewDetail0;
import bean.settlement.ViewDetail1;
import database.*;

import java.sql.Connection;
import java.util.List;

public class Detail0Dao {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"view_detail0",param, ViewDetail0.class);
    }

    public static DaoUpdateResult update(Connection conn, List<Detail0> ds){
        String sql = "update detail0 set sid=?,eid=?,amount=?,tax=?,paid=? where id = ?";
        Object [][]params = new Object[ds.size()][];
        for (int i = 0; i < ds.size(); i++) {
            params[i] = new Object[]{ds.get(i).getSid(), ds.get(i).getEid(), ds.get(i).getAmount(), ds.get(i).getTax(), ds.get(i).getPaid(), ds.get(i).getId()};
        }
        return DbUtil.batch(conn,sql,params);
    }
    //获取明细
    public static DaoQueryResult get(Connection conn, QueryConditions conditions){
        return DbUtil.get(conn,"view_detail0",conditions, ViewDetail0.class);
    }

    public static DaoUpdateResult importDetails(Connection conn, List<Detail0> ds){
        String sql = "insert detail0 (sid,eid,amount,tax,paid) values (?,?,?,?,?)";
        Object [][]params = new Object[ds.size()][];
        for (int i = 0; i < ds.size(); i++) {
            params[i] = new Object[]{ds.get(i).getSid(), ds.get(i).getEid(), ds.get(i).getAmount(), ds.get(i).getTax(), ds.get(i).getPaid()};
        }
        return DbUtil.insertBatch(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"detail0",conditions);
    }

}
