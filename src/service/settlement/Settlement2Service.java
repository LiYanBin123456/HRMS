package service.settlement;

import bean.settlement.Settlement1;
import bean.settlement.Settlement2;
import database.QueryParameter;

import java.sql.Connection;

public class Settlement2Service {
    public static String getList(Connection conn, QueryParameter param) {
        return null;
    }

    public static String insert(Connection conn, Settlement2 settlement2) {
        return null;
    }

    public static String delete(Connection conn, Long id) {
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
