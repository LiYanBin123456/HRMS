package dao.product;

import bean.insurance.Product;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class ProductDao {

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return null;
    }

    public DaoQueryResult get(Connection conn, long id) {
        return null;
    }

    public DaoUpdateResult update(Connection conn, Product product) {
        return null;
    }

    public DaoUpdateResult insert(Connection conn, Product product) {
        return null;
    }

    public DaoUpdateResult delete(Connection conn, long id) {
        return null;
    }
}
