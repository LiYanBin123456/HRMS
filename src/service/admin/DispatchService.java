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

    public DaoUpdateResult deletePotential(Connection conn, long id,int type) {
        DaoUpdateResult res;
        res= dispatchDao.delete(conn,id);
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
