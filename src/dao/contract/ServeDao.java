package dao.contract;

import bean.admin.Notice;
import bean.contract.Contract;
import bean.contract.Serve;
import bean.contract.ViewServeCooperation;
import database.*;

import java.sql.Connection;

public class ServeDao {
    //添加合同服务项目
    public static DaoUpdateResult insert(Connection conn, Serve s){
        String sql = "insert into serve (cid,type,category,payment,settlement,receipt,pid,value,tax) values (?,?,?,?,?,?,?,?,?)";
        Object []params = {s.getCid(),s.getType(),s.getCategory(),s.getPayment(),s.getSettlement(),s.getReceipt(),s.getPid(),s.getValue(),s.getTax()};
        return DbUtil.insert(conn,sql,params);
    }
    //修改合同服务项目
    public static DaoUpdateResult update(Connection conn,Serve s){
        String sql = "update serve set type=?,category=?,payment=?,settlement=?,receipt=?,pid=?,value=?,tax=? where cid=?";
        Object []params = {s.getType(),s.getCategory(),s.getPayment(),s.getSettlement(),s.getReceipt(),s.getPid(),s.getValue(),s.getTax(),s.getCid()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }
    //获取合同服务项目
    public static DaoQueryResult get(Connection conn, String id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid", "=", id);
        return DbUtil.get(conn,"serve",conditions,Serve.class);
    }

    //获取合作客户的所有合同服务项目
    public static DaoQueryListResult getList(Connection conn, QueryParameter param,long id){
        return DbUtil.getList(conn,"view_serve_cooperation",param, ViewServeCooperation.class);
    }


}
