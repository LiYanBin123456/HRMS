package service.admin;

import bean.admin.Finance;
import dao.admin.FinanceDao;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;

public class FinanceService {
    private FinanceDao financeDao = new FinanceDao();


    public DaoUpdateResult updateFinance(Connection conn, Finance finance) {
        return financeDao.updateFinance(conn,finance);
    }

    public DaoQueryResult getFinance(Connection conn, long cid,String type) {
        return financeDao.getFinance(conn,cid,type);
    }

    public DaoUpdateResult insertFinance(Connection conn, Finance finance) {
        return financeDao.insertFinance(conn,finance);
    }
}
