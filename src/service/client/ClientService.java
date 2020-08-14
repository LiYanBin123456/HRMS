package service.client;

import dao.client.ClientDao;
import database.DaoUpdateResult;

import java.sql.Connection;

public class ClientService {
    private ClientDao clientDao = new ClientDao();
    //分配管理员
    public DaoUpdateResult allocate(Connection conn, Long aid, String[] cids, byte category) {
        return clientDao.allocate(conn,aid,cids,category);
    }
}
