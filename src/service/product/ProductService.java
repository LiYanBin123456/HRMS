package service.product;

import bean.insurance.Product;
import bean.rule.RuleFund;
import dao.product.ProductDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class ProductService {
    private ProductDao productDao = new ProductDao();
    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return productDao.getList(conn,param);
    }

    public DaoQueryResult get(Connection conn, long id) {
        return productDao.get(conn,id);
    }

    public DaoUpdateResult update(Connection conn, Product product) {
        return productDao.update(conn,product);
    }

    public DaoUpdateResult insert(Connection conn, Product product) {
        return productDao.insert(conn,product);
    }

    public DaoUpdateResult delete(Connection conn, long id) {
        return productDao.delete(conn,id);
    }
}
