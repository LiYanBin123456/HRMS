package service.client;

import bean.client.Supplier;
import com.alibaba.fastjson.JSONObject;
import dao.client.SupplierDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class SupplierService {
    public static DaoQueryResult get(long id, Connection conn){
        return SupplierDao.get(conn,id);
    }

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return SupplierDao.getList(conn,param);
    }

    public static DaoUpdateResult update(Connection conn, Supplier supplier){
        return SupplierDao.update(conn,supplier);
    }

    public static DaoUpdateResult delete(long id, Connection conn){
        DaoUpdateResult  result = SupplierDao.updateStatus(conn,id,1);
        return result;
    }

    public static String insert(Supplier supplier, Connection conn){
        DaoUpdateResult  res = SupplierDao.insert(conn,supplier);
        return JSONObject.toJSONString(res);
    }

}
