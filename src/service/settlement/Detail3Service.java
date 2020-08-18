package service.settlement;

import bean.settlement.Detail3;
import dao.settlement.Detail3Dao;
import database.DaoQueryListResult;
import database.QueryParameter;

import java.sql.Connection;
import java.util.List;

public class Detail3Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return Detail3Dao.getList(conn,param);
    }
    public static String update(Connection conn,  List<Detail3> details){
        return  null;
    }
    public static String importDetails(Connection conn,long id, List<Detail3> details){
        return  null;
    }
    public static String exportDetails(Connection conn,long id){
        return  null;
    }
}
