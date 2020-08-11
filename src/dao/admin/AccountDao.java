package dao.admin;

import bean.admin.Account;
import database.*;

import java.sql.Connection;

public class AccountDao {
    public DaoQueryResult isExist(Connection conn,String username){
        return null;
    }
    public String login(String username,String password){
        return null;
    }

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"account",param,Account.class);

    }

    public DaoQueryResult get(Connection conn, long id) {
        return null;
    }

    public DaoUpdateResult update(Connection conn, Account account) {
        return null;
    }

    public DaoUpdateResult insert(Connection conn, Account account) {
        return null;
    }

    public DaoUpdateResult delete(Connection conn, long id) {
        return  null;
    }

    public DaoUpdateResult permit(Connection conn, long id,byte permission) {
        return null;
    }
}
