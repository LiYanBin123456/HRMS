package service.product;

import bean.insurance.Product;
import bean.rule.RuleFund;
import dao.product.ProductDao;
import database.*;

import java.sql.Connection;

public class ProductService {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return ProductDao.getList(conn,param);
    }

    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return ProductDao.get(conn,conditions);
    }

    public static DaoUpdateResult update(Connection conn, Product product) {
        return ProductDao.update(conn,product);
    }

    public static DaoUpdateResult insert(Connection conn, Product product) {
        return ProductDao.insert(conn,product);
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        return ProductDao.delete(conn,id);
    }
}
