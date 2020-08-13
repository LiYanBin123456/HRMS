package service.client;

import bean.client.Supplier;
import dao.client.SupplierDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class SupplierService {
    private SupplierDao supplierDao = new SupplierDao();
    public DaoQueryResult get(long id, Connection conn){
        return supplierDao.get(conn,id);
    }

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return supplierDao.getList(conn,param);
    }

    public DaoUpdateResult update(Connection conn, Supplier supplier){
        return supplierDao.update(conn,supplier);
    }

    public DaoUpdateResult delete(long id, Connection conn){
        return supplierDao.delete(conn,id);
    }
    public DaoUpdateResult insert(Supplier supplier, Connection conn){
        return supplierDao.insert(conn,supplier);
    }

}
