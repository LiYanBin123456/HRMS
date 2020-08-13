package service.client;

import dao.client.ClientDao;

import java.sql.Connection;

public class ClientService {
    private ClientDao clientDao = new ClientDao();
    //分配管理员
    public String allocate(Connection conn, Long aid, Object cids, byte category) {
        return clientDao.allocate(conn,aid,cids,category);
    }
}
