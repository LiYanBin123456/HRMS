package service.settlement;

import bean.settlement.Detail3;
import dao.settlement.Detail3Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;
import java.util.List;

public class Detail3Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param,long id){
        param.conditions.add("sid","=",id);
        return Detail3Dao.getList(conn,param);
    }
    public static DaoUpdateResult update(Connection conn, List<Detail3> details){
        return  Detail3Dao.update(conn,details);
    }
    public static DaoUpdateResult importDetails(Connection conn, long id, List<Detail3> details){
        //将结算单id赋值给结算单明细中的结算单id
        for(Detail3 detail :details){
            detail.setSid(id);
            System.out.println(detail);
        }
        return  Detail3Dao.importDetails(conn,details);
    }
    public static String exportDetails(Connection conn,long id){
        return  null;
    }
}
