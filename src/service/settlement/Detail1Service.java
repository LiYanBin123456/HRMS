package service.settlement;

import bean.settlement.Detail1;
import dao.settlement.Detail1Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;
import java.util.List;

public class Detail1Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param, long id){
        param.conditions.add("sid","=",id);
        return Detail1Dao.getList(conn,param);
    }
    public static DaoUpdateResult update(Connection conn, List<Detail1> details ){
        return  Detail1Dao.update(conn,details);
    }
    public static DaoUpdateResult importDetails(Connection conn, long id, List<Detail1> details){
        //将结算单id赋值给结算单明细中的结算单id
        for(Detail1 detail :details){
              detail.setSid(id);
        }
        return  Detail1Dao.importDetails(conn,details);
    }
    public static String exportDetails(Connection conn,long id, List<Detail1> details){
        return  null;
    }
    public static String backup(Connection conn,Long id,String month){
       return null;
    }
    public static String makeup(Connection conn,Long id,String month){
        return null;
    }
}
