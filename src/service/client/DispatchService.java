package service.client;

import bean.client.Dispatch;
import dao.client.DispatchDao;
import dao.client.FinanceDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class DispatchService {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DispatchDao.getList(conn,param);
    }

    public static DaoQueryResult get(Connection conn, long id) {
        return DispatchDao.get(conn,id);
    }

    public static DaoUpdateResult update(Connection conn, Dispatch dispatch) {
        return DispatchDao.update(conn, dispatch);
    }

    public static DaoUpdateResult insert(Connection conn, Dispatch dispatch) {
        return DispatchDao.insert(conn, dispatch);
    }

    //删除客户实质是修改状态
    public static DaoUpdateResult delete(Connection conn, long id, byte type) {
        //修改状态，合作客户修改为潜在客户，潜在客户修改为流失客户
        return DispatchDao.updateType(conn,id,(byte)(type+1));
    }

    public static DaoUpdateResult allocateAdmin(Connection conn, String[] cids, long aid) {
        return DispatchDao.allocateAdmin(conn,cids,aid);
    }
}
