package service;

import bean.Client;
import dao.ClientDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class ClientService {
    private ClientDao clientDao = new ClientDao();


    public  DaoQueryListResult getClientList(Connection conn, QueryParameter param){
        return clientDao.getClientList(conn,param);
    }

    public DaoQueryResult getClient(Connection conn, long id) {
        return clientDao.getClient(conn,id);
    }

    public DaoUpdateResult updateClient(Connection conn, Client client) {
        return clientDao.updateClient(conn,client);
    }
}
