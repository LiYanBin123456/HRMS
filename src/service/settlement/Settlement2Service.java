package service.settlement;

import bean.settlement.Settlement2;
import dao.settlement.Settlement2Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class Settlement2Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement2Dao.getList(conn,param);
    }

    public static DaoUpdateResult insert(Connection conn, Settlement2 settlement2) {
        return Settlement2Dao.insert(conn,settlement2);
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        return Settlement2Dao.delete(conn,id);
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
