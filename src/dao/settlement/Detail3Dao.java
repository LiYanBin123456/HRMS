package dao.settlement;

import bean.settlement.Detail2;
import bean.settlement.Detail3;
import bean.settlement.ViewDetail3;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryParameter;

import java.sql.Connection;
import java.util.List;

public class Detail3Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"ViewDetail3",param, ViewDetail3.class);
    }
    public static DaoUpdateResult update(Connection conn, List<Detail3> details){
        return  null;
    }
    public static DaoUpdateResult importDetails(Connection conn,long id, List<Detail3> details){
        return  null;
    }
    public static DaoUpdateResult exportDetails(Connection conn,long id){
        return  null;
    }
}
