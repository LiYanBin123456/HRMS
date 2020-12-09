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
        String sql = "update detail3 set day=?,status=? where id = ?";
        Object [][]params = new Object[details.size()][];
        for (int i = 0; i < details.size(); i++) {
            params[i] = new Object[]{details.get(i).getDay(),details.get(i).getStatus(),details.get(i).getId()};
        }
        return DbUtil.batch(conn,sql,params);
    }

    public static DaoUpdateResult importDetails(Connection conn, List<Detail3> details){
        String sql = "insert detail3 (sid,eid,status) values (?,?,?)";
        Object [][]params = new Object[details.size()][];
        for (int i = 0; i < details.size(); i++) {
            params[i] = new Object[]{details.get(i).getSid(),details.get(i).getEid(),details.get(i).getStatus()};
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

    /**
     * 确认替换下
     * @param conn
     * @param ids1 被换下的
     * @param ids2 换上的
     * @param day 生效日
     * @return
     */
    public static DaoUpdateResult confirm(Connection conn, String[] ids1, String[] ids2, byte day) {
        String sql = "update detail3 set day=?,status=?,rid=? where id = ?";
        Object [][]params = new Object[ids1.length][];
        for (int i = 0; i < ids1.length; i++) {
            params[i] = new Object[]{day,Detail3.STATUS_CONFIRMED,ids1[i],ids2[i]};
        }
        return DbUtil.batch(conn,sql,params);
    }

    /**
     * 确认参保
     * @param conn
     * @param ids
     * @param day
     * @return
     */
    public static DaoUpdateResult confirm(Connection conn, String[] ids, byte day) {
        String sql = "update detail3 set day=?,status=? where id = ?";
        Object [][]params = new Object[ids.length][];
        for (int i = 0; i < ids.length; i++) {
            params[i] = new Object[]{day,Detail3.STATUS_CONFIRMED,ids[i]};
        }
        return DbUtil.batch(conn,sql,params);
    }
}
