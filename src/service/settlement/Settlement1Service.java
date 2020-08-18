package service.settlement;

import bean.settlement.Settlement1;
import dao.settlement.Settlement1Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class Settlement1Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement1Dao.getList(conn,param);
    }

    public static DaoUpdateResult insert(Connection conn, Settlement1 settlement1) {
        return Settlement1Dao.insert(conn,settlement1);
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        return null;
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
