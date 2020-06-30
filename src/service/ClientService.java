package service;

import dao.ClientDao;
import database.DaoQueryListResult;
import database.QueryParameter;

import java.sql.Connection;

public class ClientService {
    private ClientDao clientDao = new ClientDao();


    public  DaoQueryListResult getClientList(Connection conn, QueryParameter param){
        return clientDao.getClientList(conn,param);
    }
}
