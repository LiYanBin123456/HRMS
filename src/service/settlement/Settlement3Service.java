package service.settlement;

import bean.settlement.Settlement1;
import dao.settlement.Settlement3Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class Settlement3Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement3Dao.getList(conn,param);
    }

    public static DaoUpdateResult insert(Connection conn, Settlement1 settlement1) {
        return null;
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        return null;
    }

    public static DaoUpdateResult copy(Connection conn, long id,String month) {
        return null;
    }

}
