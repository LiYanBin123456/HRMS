package service.admin;

import bean.client.Dispatch;
import dao.admin.ClientDao;
import dao.admin.FinanceDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class ClientService {
    private ClientDao clientDao = new ClientDao();
    private FinanceDao financeDao = new FinanceDao();
    public  DaoQueryListResult getDispatchs(Connection conn, QueryParameter param){
        return clientDao.getDispatchs(conn,param);
    }

    public DaoQueryResult getDispatch(Connection conn, long id) {
        return clientDao.getDispatch(conn,id);
    }

    public DaoUpdateResult updateDispatch(Connection conn, Dispatch dispatch) {
        return clientDao.updateDispatch(conn, dispatch);
    }

    public DaoUpdateResult insertDispatch(Connection conn, Dispatch dispatch) {
        return clientDao.insertDispatch(conn, dispatch);
    }

    public DaoUpdateResult deletePotential(Connection conn, long id,int type) {
        DaoUpdateResult res;
        res= clientDao.deleteDispatch(conn,id);
        //删除潜在客户时，也要删除客户的财务服务信息表
        if(res.success){
            financeDao.delete(conn,id,type);
        }
        return res;
    }

    public DaoUpdateResult deleteCooperation(Connection conn, long id) {
        int status = 0;
        return clientDao.updateStatus(conn,id,status);
    }
}
