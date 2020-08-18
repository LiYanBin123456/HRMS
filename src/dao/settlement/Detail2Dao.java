package dao.settlement;

import bean.settlement.Detail2;
import bean.settlement.ViewDetail2;
import database.DaoQueryListResult;
import database.DbUtil;
import database.QueryParameter;

import java.sql.Connection;
import java.util.List;

public class Detail2Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"view_detail2",param, ViewDetail2.class);
    }
    public static String update(Connection conn,  List<Detail2> details){
        return  null;
    }
    public static String importDetails(Connection conn,long id, List<Detail2> details){
        return  null;
    }
    public static String exportDetails(Connection conn,long id){
        return  null;
    }
}
