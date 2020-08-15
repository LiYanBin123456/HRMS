package service.admin;

import bean.admin.Account;
import bean.admin.Notice;
import dao.admin.AccountDao;
import database.*;

import java.sql.Connection;

public class AccountService {
    private AccountDao accountDao = new AccountDao();

    public DaoQueryResult login(Connection conn,String username){
        return accountDao.login(conn,username);
    }

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return accountDao.getList(conn,param);
    }

    public DaoQueryResult get(Connection conn, long id) {
        return accountDao.get(conn,id);
    }

    public DaoUpdateResult update(Connection conn, Account account) {
        return accountDao.update(conn,account);
    }

    public DaoUpdateResult insert(Connection conn, Account account) {
        return accountDao.insert(conn,account);
    }

    public DaoUpdateResult delete(Connection conn, long id) {
        return accountDao.delete(conn,id);
    }

    public DaoUpdateResult permit(Connection conn, long id,byte permission) {
        return accountDao.permit(conn,id,permission);
    }
}
