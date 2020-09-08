package dao.settlement;

import bean.settlement.Detail2;
import bean.settlement.Detail3;
import bean.settlement.ViewDetail3;
import database.*;

import java.sql.Connection;
import java.util.List;

public class Detail3Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"view_detail3",param, ViewDetail3.class);
    }
    public static DaoUpdateResult update(Connection conn, List<Detail3> details){
        String sql = "update detail3 set pid=?,month=?,place=?,price=? where id = ?";
        Object [][]params = new Object[details.size()][];
        for (int i = 0; i < details.size(); i++) {
            params[i] = new Object[]{details.get(i).getPid(),details.get(i).getMonth(),details.get(i).getPlace()
                    ,details.get(i).getPrice(),details.get(i).getId()};
        }
        return DbUtil.batch(conn,sql,params);
    }

    public static DaoUpdateResult importDetails(Connection conn, List<Detail3> details){
        String sql = "insert detail3 (sid,eid,pid,month,place,price) values (?,?,?,?,?,?)";
        Object [][]params = new Object[details.size()][];
        for (int i = 0; i < details.size(); i++) {
            params[i] = new Object[]{details.get(i).getSid(),details.get(i).getEid(),details.get(i).getPid(),details.get(i).getMonth(),details.get(i).getPlace()
            ,details.get(i).getPrice()};
        }
        return DbUtil.insertBatch(conn,sql,params);
    }
    public static DaoUpdateResult exportDetails(Connection conn,long id){
        return  null;
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"detail3",conditions);
    }
}
