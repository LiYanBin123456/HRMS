package dao.client;

import bean.client.Cooperation;
import bean.client.Dispatch;
import bean.client.Supplier;
import database.*;

import java.sql.Connection;

public class SupplierDao {
    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return  DbUtil.get(conn,"supplier",conditions, Supplier.class);
    }

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(name,contact)","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"supplier",param, Supplier.class);
    }

    public  static DaoUpdateResult update(Connection conn, Supplier c){
        String sql = "update supplier set did=?, rid=?,name=?,nickname=?,business=?,address=?,contact=?,phone=?,wx=?,qq=?,mail=?,intro=? where id=?";
        Object []params = {c.getDid(),c.getRid(),c.getName(),c.getNickname(),c.getBusiness(),c.getAddress(), c.getContact(), c.getPhone(),c.getWx(),c.getQq(),c.getMail(),c.getIntro(),c.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }


    public static DaoUpdateResult insert(Connection conn, Supplier c) {
        String sql = "insert into supplier (did,rid,name,nickname,business,address,contact,phone,wx,qq,mail,intro) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {c.getDid(),c.getRid(),c.getName(),c.getNickname(),c.getBusiness(),c.getAddress(), c.getContact(), c.getPhone(),c.getWx(),c.getQq(),c.getMail(),c.getIntro()};
        return DbUtil.insert(conn,sql,params);
    }

    //删除客户
    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"supplier",conditions);
    }

}
