package dao.settlement;

import bean.settlement.Settlement1;
import bean.settlement.Settlement2;
import bean.settlement.ViewSettlement1;
import bean.settlement.ViewSettlement2;
import database.*;

import java.sql.Connection;

public class Settlement2Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            //根据地市模糊查询
            param.addCondition("concat(name,month,status)","like",param.conditions.extra);
        }
        return DbUtil.getList(conn, "view_settlement2", param, ViewSettlement2.class);

    }

    public static String insert(Connection conn, Settlement2 s) {
        return  null;
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"settlement2",conditions);
    }

    public static String copy(Connection conn, long id,String month) {
        return null;
    }

    public static String commit(Connection conn, long id) {
        return null;
    }

    public static String check(Connection conn, long id) {
        return null;
    }

    public static String reset(Connection conn,long id) {
        return null;
    }

    public static String deduct(Connection conn,long id) {
        return null;
    }

    public static String confirm(Connection conn,long id) {
        return null;
    }

    public static String exportBank(Connection conn, long id,String bank) {
        return null;
    }

    public static String getLogs(Connection conn, long id) {
        return null;
    }
}
