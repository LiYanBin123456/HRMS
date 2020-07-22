package service.admin;

import bean.admin.Dispatch;
import dao.admin.DispatchDao;
import dao.admin.FinanceDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class DispatchService {
    private DispatchDao dispatchDao = new DispatchDao();
    private FinanceDao financeDao = new FinanceDao();
    public  DaoQueryListResult getClientList(Connection conn, QueryParameter param){
        return dispatchDao.getClientList(conn,param);
    }

    public DaoQueryResult getClient(Connection conn, long id) {
        return dispatchDao.getClient(conn,id);
    }

    public DaoUpdateResult updateClient(Connection conn, Dispatch dispatch) {
        return dispatchDao.updateClient(conn, dispatch);
    }

    public DaoUpdateResult insertClient(Connection conn, Dispatch dispatch) {
        return dispatchDao.insertClient(conn, dispatch);
    }

    public DaoUpdateResult deleteClient1(Connection conn, long id) {
        DaoUpdateResult res;
        res= dispatchDao.deleteClient1(conn,id);
        //删除潜在客户时，也要删除客户的财务服务信息表
        if(res.success){
            financeDao.deleteFinance(conn,id);
        }
        return res;
    }

    public DaoUpdateResult deleteClient2(Connection conn, long id) {
        int status = 0;
        return dispatchDao.updateStatus(conn,id,status);
    }
}
