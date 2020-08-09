package service.admin;

import bean.admin.Account;
import bean.admin.Notice;
import dao.admin.AccountDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class AccountService {
    private AccountDao accountDao = new AccountDao();

    public String login(String username,String password){
        return null;
    }
    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return accountDao.getList(conn,param);
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
