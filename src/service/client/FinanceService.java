package service.client;

import bean.client.Finance;
import dao.client.FinanceDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class FinanceService {
    //修改客户服务信息
    public static DaoUpdateResult update(Connection conn, Finance finance) {
        return FinanceDao.update(conn,finance);
    }

    //获取客户服务信息
    public static DaoQueryResult get(Connection conn, long cid,byte type) {
        return FinanceDao.get(conn,cid,type);
    }

    //获取客户服务信息
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return FinanceDao.getList(conn,param);
    }

    //添加客户服务信息
    public static DaoUpdateResult insert(Connection conn, Finance finance) {
        return FinanceDao.insert(conn,finance);
    }

    public static String arrive(Connection conn,long id,float balance){
       return  null;
    }
}
