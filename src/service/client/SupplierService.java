package service.client;

import bean.client.Cooperation;
import dao.client.SupplierDao;
import database.DaoQueryListResult;
import database.QueryParameter;

import java.sql.Connection;

public class SupplierService {
    private SupplierDao supplierDao = new SupplierDao();
    public String get(long id,Connection conn){
        return null;
    }

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return supplierDao.getList(conn,param);
    }

    public String update(Connection conn,Cooperation cooperation){
        return null;
    }
    public String delete(long id,Connection conn){
        return null;
    }
    public String insert(Cooperation cooperation, Connection conn){
        return null;
    }

}
