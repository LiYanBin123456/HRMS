package dao.admin;

import bean.admin.Account;
import database.*;

import java.sql.Connection;

public class AccountDao {
    public static DaoExistResult isExist(Connection conn, String username){
        QueryConditions conditions = new QueryConditions();
        conditions.add("username","=",username);
        return DbUtil.exist(conn,"account",conditions);
    }


    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"account",param,Account.class);

    }

    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"account",conditions,Account.class);
    }

    public static DaoQueryResult get(Connection conn, String username) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("username","=",username);
        return DbUtil.get(conn,"account",conditions,Account.class);
    }

    //根据用户角色和rid来查询
    public static DaoQueryResult get(Connection conn, Account account) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("role","=",account.getRole());
        conditions.add("rid","=",account.getRid());
        return DbUtil.get(conn,"account",conditions,Account.class);
    }

    public static DaoUpdateResult update(Connection conn, Account a) {
        String sql = "update account set nickname=?,username=?,password=? where id=?";
        Object []params = {a.getNickname(),a.getUsername(),a.getPassword(),a.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult insert(Connection conn, Account a) {
        String sql = "insert account (nickname,username,password,role,rid,permission) values (?,?,?,?,?,?)";
        Object []params = {a.getNickname(),a.getUsername(),a.getPassword(),a.getRole(),a.getRid(),a.getPermission()};
        return  DbUtil.insert(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return  DbUtil.delete(conn,"account",conditions);
    }

    public static DaoUpdateResult permit(Connection conn, long id,long permission) {
        String sql = "update account set permission=? where id=?";
        Object []params = {permission,id};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }


}
