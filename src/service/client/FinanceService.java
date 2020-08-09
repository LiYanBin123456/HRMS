package service.client;

import bean.client.Finance;
import dao.client.FinanceDao;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;

public class FinanceService {
    private FinanceDao financeDao = new FinanceDao();


    //修改客户服务信息
    public DaoUpdateResult update(Connection conn, Finance finance) {
        return financeDao.update(conn,finance);
    }

    //获取客户服务信息
    public DaoQueryResult get(Connection conn, long cid,String type) {
        return financeDao.get(conn,cid,type);
    }

    //添加客户服务信息
    public DaoUpdateResult insert(Connection conn, Finance finance) {
        return financeDao.insert(conn,finance);
    }
}
