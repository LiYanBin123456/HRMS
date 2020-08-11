package service.settlement;

import bean.settlement.Settlement1;
import database.QueryParameter;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

public class Settlement1Service {
    private String getList(Connection conn, QueryParameter param) {
        return null;
    }

    private String insert(Connection conn, Settlement1 settlement1) {
        return null;
    }

    private String delete(Connection conn, Long id) {
        return null;
    }

    private String copy(Connection conn, long id,String month) {
        return null;
    }

    private String commit(Connection conn, long id) {
        return null;
    }

    private String check(Connection conn, long id) {
        return null;
    }

    private String reset(Connection conn,long id) {
        return null;
    }

    private String deduct(Connection conn,long id) {
        return null;
    }

    private String confirm(Connection conn,long id) {
        return null;
    }

    private String exportBank(Connection conn, long id,String bank) {
        return null;
    }

    private String getLogs(Connection conn, long id) {
        return null;
    }
}
