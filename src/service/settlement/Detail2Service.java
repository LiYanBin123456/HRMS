package service.settlement;

import bean.settlement.Detail2;
import dao.settlement.Detail2Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import javax.xml.soap.Detail;
import java.sql.Connection;
import java.util.List;

public class Detail2Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param, long id){
        param.conditions.add("sid","=",id);
        return Detail2Dao.getList(conn,param);
    }
    public static String update(Connection conn, List<Detail2> details){
        return  null;
    }
    public static String importDetails(Connection conn,long id, List<Detail2> details){
        return  null;
    }
    public static String exportDetails(Connection conn,long id){
        return  null;
    }

}
