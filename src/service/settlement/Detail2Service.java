package service.settlement;

import bean.settlement.Detail2;
import dao.settlement.Detail2Dao;
import dao.settlement.Detail3Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;
import java.util.List;

public class Detail2Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param, long id){
        param.conditions.add("sid","=",id);
        return Detail2Dao.getList(conn,param);
    }
    public static DaoUpdateResult update(Connection conn, List<Detail2> details){
        return Detail2Dao.update(conn,details);
    }
    public static DaoUpdateResult importDetails(Connection conn, long id, List<Detail2> details){
        //将结算单id赋值给结算单明细中的结算单id
        for(Detail2 detail :details){
            detail.setSid(id);
            System.out.println(detail);
        }
        return  Detail2Dao.importDetails(conn,details);
    }
    public static String exportDetails(Connection conn,long id){
        return  null;
    }

}
