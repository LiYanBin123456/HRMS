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

    //删除流失客户
    public static DaoUpdateResult deletePot(Connection conn, long id,int type) {
        DaoUpdateResult res;
        res= DispatchDao.delete(conn,id);
        //删除客户时，也要删除客户的财务服务信息表
        if(res.success){
            FinanceDao.delete(conn,id,type);
        }
        return res;
    }

    public static DaoUpdateResult deleteCoop(Connection conn, long id, byte status) {
        //修改状态，合作客户修改韦潜在客户，潜在客户修改韦流失客户
        return DispatchDao.updateStatus(conn,id,status+1);
    }

    public static DaoUpdateResult allocateAdmin(Connection conn, String[] cids, long aid) {
        return DispatchDao.allocateAdmin(conn,cids,aid);
    }
}
