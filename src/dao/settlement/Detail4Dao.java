package dao.settlement;

import bean.settlement.Detail4;
import bean.settlement.Detail1;
import bean.settlement.ViewDetail4;
import bean.settlement.ViewDetail1;
import database.*;

import java.sql.Connection;
import java.util.List;

public class Detail4Dao {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"view_detail4",param, ViewDetail4.class);
    }

    public static DaoUpdateResult update(Connection conn, List<Detail4> ds){
        String sql = "update detail4 set sid=?,eid=?,amount=?,tax=?,paid=? where id = ?";
        Object [][]params = new Object[ds.size()][];
        for (int i = 0; i < ds.size(); i++) {
            params[i] = new Object[]{ds.get(i).getSid(), ds.get(i).getEid(), ds.get(i).getAmount(), ds.get(i).getTax(), ds.get(i).getPaid(), ds.get(i).getId()};
        }
        return DbUtil.batch(conn,sql,params);
    }
    //获取明细
    public static DaoQueryResult get(Connection conn, QueryConditions conditions){
        return DbUtil.get(conn,"view_detail4",conditions, ViewDetail4.class);
    }

    public static DaoUpdateResult importDetails(Connection conn, List<Detail4> ds){
        String sql = "insert detail4 (sid,eid,amount,tax,paid) values (?,?,?,?,?)";
        Object [][]params = new Object[ds.size()][];
        for (int i = 0; i < ds.size(); i++) {
            params[i] = new Object[]{ds.get(i).getSid(), ds.get(i).getEid(), ds.get(i).getAmount(), ds.get(i).getTax(), ds.get(i).getPaid()};
        }
        return DbUtil.insertBatch(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"detail4",conditions);
    }

}
