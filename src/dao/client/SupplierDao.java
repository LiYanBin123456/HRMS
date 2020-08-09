package dao.client;

import bean.client.Cooperation;
import bean.client.Dispatch;
import bean.client.Supplier;
import database.DaoQueryListResult;
import database.DbUtil;
import database.QueryParameter;

import java.sql.Connection;

public class SupplierDao {
    public String get(long id,Connection conn){
        return null;
    }

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"supplier",param, Supplier.class);
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
