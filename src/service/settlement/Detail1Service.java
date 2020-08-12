package service.settlement;

import bean.settlement.Detail1;
import database.QueryParameter;

import java.sql.Connection;
import java.util.List;

public class Detail1Service {
    private String getList(Connection conn, QueryParameter param){
        return null;
    }
    private String update(Connection conn,List<Detail1> details ){
        return  null;
    }
    private String importDetails(Connection conn,long id, List<Detail1> details){
        return  null;
    }
    private String exportDetails(Connection conn,long id, List<Detail1> details){
        return  null;
    }
    private String backup(Connection conn,Long id,String month){
       return null;
    }
    private String makeup(Connection conn,Long id,String month){
        return null;
    }
}
