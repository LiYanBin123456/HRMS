package dao.product;

import bean.insurance.Product;
import database.*;

import java.sql.Connection;

public class ProductDao {


    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"product",param, Product.class);
    }

    public static DaoExistResult exist(Connection conn, QueryConditions conditions) {
        return DbUtil.exist(conn,"product",conditions);
    }

    public static DaoQueryResult get(Connection conn,  QueryConditions conditions) {
        return DbUtil.get(conn,"product",conditions,Product.class);
    }

    public static DaoUpdateResult update(Connection conn, Product p) {
        String sql = "update product set name=?,fin1=?,fin2=?,allowance=? ,period=?,allow=?,min=?,max=?,intro=? where id=?";
        Object []params = {p.getName(),p.getFin1(),p.getFin2(),p.getAllowance(),p.getPeriod(),p.getAllow(),p.getMin(),p.getMax(),p.getIntro(),p.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult insert(Connection conn,  Product p) {
        String did = p.getDid()==0?null:String.valueOf(p.getDid());
        String sql = "insert product (did,name,fin1,fin2,allowance,period,allow,min,max,intro) values (?,?,?,?,?,?,?,?,?,?)";
        Object []params = {did,p.getName(),p.getFin1(),p.getFin2(),p.getAllowance(),p.getPeriod(),p.getAllow(),p.getMin(),p.getMax(),p.getIntro()};
        return  DbUtil.insert(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"product",conditions);
    }
}
