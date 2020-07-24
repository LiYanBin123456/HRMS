package service.admin;

import bean.admin.Finance;
import dao.admin.FinanceDao;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;

public class FinanceService {
    private FinanceDao financeDao = new FinanceDao();


    public DaoUpdateResult update(Connection conn, Finance finance) {
        return financeDao.update(conn,finance);
    }

    public DaoQueryResult get(Connection conn, long cid,String type) {
        return financeDao.get(conn,cid,type);
    }

    public DaoUpdateResult insert(Connection conn, Finance finance) {
        return financeDao.insert(conn,finance);
    }
}
