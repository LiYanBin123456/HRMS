package dao.settlement;

import bean.settlement.Detail1;
import database.QueryParameter;


import java.sql.Connection;
import java.util.List;

public class Detail1Dao {
    public static String getList(Connection conn, QueryParameter param){
        return null;
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
