package dao.settlement;

import bean.settlement.Detail1;
import bean.settlement.ViewDetail1;
import database.DaoQueryListResult;
import database.DbUtil;
import database.QueryParameter;


import java.sql.Connection;
import java.util.List;

public class Detail1Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"view_detail1",param, ViewDetail1.class);
    }
    public static String update(Connection conn, List<Detail1> details){
        return  null;
    }
    public static String importDetails(Connection conn,long id, List<Detail1> details){
        return  null;
    }
    public static String exportDetails(Connection conn,long id){
        return  null;
    }
    public static String backup(Connection conn,Long id,String month){
        return null;
    }
    public static String makeup(Connection conn,Long id,String month){
        return null;
    }
}
