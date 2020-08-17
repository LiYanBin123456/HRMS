package service.client;

import dao.client.ClientDao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class ClientService {
    private ClientDao clientDao = new ClientDao();
    //分配管理员
    public DaoUpdateResult allocate(Connection conn, Long aid, String[] cids, byte category) {
        return clientDao.allocate(conn,aid,cids,category);
    }

    public  static DaoQueryListResult getBalances(Connection conn, QueryParameter param){
       return ClientDao.getBalances(conn,param);
    }
}
