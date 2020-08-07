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
    public  DaoQueryListResult getDispatchs(Connection conn, QueryParameter param){
        return dispatchDao.getDispatchs(conn,param);
    }

    public DaoQueryResult getDispatch(Connection conn, long id) {
        return dispatchDao.getDispatch(conn,id);
    }

    public DaoUpdateResult updateDispatch(Connection conn, Dispatch dispatch) {
        return dispatchDao.updateDispatch(conn, dispatch);
    }

    public DaoUpdateResult insertDispatch(Connection conn, Dispatch dispatch) {
        return dispatchDao.insertDispatch(conn, dispatch);
    }

    public DaoUpdateResult deletePotential(Connection conn, long id,int type) {
        DaoUpdateResult res;
        res= dispatchDao.deleteDispatch(conn,id);
        //删除潜在客户时，也要删除客户的财务服务信息表
        if(res.success){
            financeDao.delete(conn,id,type);
        }
        return res;
    }

    public DaoUpdateResult deleteCooperation(Connection conn, long id) {
        int status = 0;
        return dispatchDao.updateStatus(conn,id,status);
    }
}
