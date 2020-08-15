package dao.admin;

import bean.admin.Account;
import database.*;

import java.sql.Connection;

public class AccountDao {
    public DaoExistResult isExist(Connection conn, String username){
        QueryConditions conditions = new QueryConditions();
        conditions.add("username","=",username);
        return DbUtil.exist(conn,"account",conditions);
    }

    public DaoQueryResult login(Connection conn,String username){
        DaoQueryResult res ;
        QueryConditions conditions = new QueryConditions();
        conditions.add("username","=",username);
        if(isExist(conn,username).exist){
           res = DbUtil.get(conn,"account",conditions,Account.class);
        }else {
            res = DbUtil.get(conn,"account",conditions,Account.class);
            res.msg="用户不存在";
        }
        return res;
    }

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"account",param,Account.class);

    }

    public DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"account",conditions,Account.class);
    }

    public DaoUpdateResult update(Connection conn, Account a) {
        String sql = "update account set nickname=?,username=?,password=?,role=?,rid=?,permission=? where id=?";
        Object []params = {a.getNickname(),a.getUsername(),a.getPassword(),a.getRole(),a.getRid(),a.getPermission(),a.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    public DaoUpdateResult insert(Connection conn, Account a) {
        String sql = "insert account (nickname,username,password,role,rid,permission) values (?,?,?,?,?,?)";
        Object []params = {a.getNickname(),a.getPassword(),a.getRole(),a.getRid(),a.getPermission()};
        return  DbUtil.insert(conn,sql,params);
    }

    public DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return  DbUtil.delete(conn,"account",conditions);
    }

    public DaoUpdateResult permit(Connection conn, long id,byte permission) {
        String sql = "update account set permission=? where id=?";
        Object []params = {permission,id};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }
}
