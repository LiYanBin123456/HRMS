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
    private DispatchDao dispatchDao = new DispatchDao();
    private FinanceDao financeDao = new FinanceDao();

    public  DaoQueryListResult getList(Connection conn, QueryParameter param){
        return dispatchDao.getList(conn,param);
    }

    public DaoQueryResult get(Connection conn, long id) {
        return dispatchDao.get(conn,id);
    }

    public DaoUpdateResult update(Connection conn, Dispatch dispatch) {
        return dispatchDao.update(conn, dispatch);
    }

    public DaoUpdateResult insert(Connection conn, Dispatch dispatch) {
        return dispatchDao.insert(conn, dispatch);
    }

    public DaoUpdateResult deletePot(Connection conn, long id,int type) {
        DaoUpdateResult res;
        res= dispatchDao.delete(conn,id);
        //删除潜在客户时，也要删除客户的财务服务信息表
        if(res.success){
            financeDao.delete(conn,id,type);
        }
        return res;
    }

    public DaoUpdateResult deleteCoop(Connection conn, long id) {
        //修改状态为潜在客户
        int status = 0;
        return dispatchDao.updateStatus(conn,id,status);
    }
}
