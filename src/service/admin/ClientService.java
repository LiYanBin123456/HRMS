package service.admin;

import bean.admin.Client;
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
    public  DaoQueryListResult getClientList(Connection conn, QueryParameter param){
        return clientDao.getClientList(conn,param);
    }

    public DaoQueryResult getClient(Connection conn, long id) {
        return clientDao.getClient(conn,id);
    }

    public DaoUpdateResult updateClient(Connection conn, Client client) {
        return clientDao.updateClient(conn,client);
    }

    public DaoUpdateResult insertClient(Connection conn, Client client) {
        return clientDao.insertClient(conn,client);
    }

    public DaoUpdateResult deleteClient1(Connection conn, long id) {
        DaoUpdateResult res;
        res= clientDao.deleteClient1(conn,id);
        //删除潜在客户时，也要删除客户的财务服务信息表
        if(res.success){
            financeDao.deleteFinance(conn,id);
        }
        return res;
    }

    public DaoUpdateResult deleteClient2(Connection conn, long id) {

        int status = 0;
        return clientDao.updateStatus(conn,id,status);
    }
}
