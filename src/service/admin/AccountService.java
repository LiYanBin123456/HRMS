package service.admin;

import bean.admin.Account;
import dao.admin.AccountDao;
import database.*;

import java.sql.Connection;

public class AccountService {
    public static DaoQueryResult login(Connection conn,String username){
        return AccountDao.login(conn,username);
    }

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return AccountDao.getList(conn,param);
    }

    public static DaoQueryResult get(Connection conn, long id) {
        return AccountDao.get(conn,id);
    }

    public static DaoUpdateResult update(Connection conn, Account account) {
        return AccountDao.update(conn,account);
    }

    public static DaoUpdateResult insert(Connection conn, Account account) {
        return AccountDao.insert(conn,account);
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        return AccountDao.delete(conn,id);
    }

    public static DaoUpdateResult permit(Connection conn, long id,int permission) {
        return AccountDao.permit(conn,id,permission);
    }
}
