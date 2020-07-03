package service.admin;

import bean.admin.Fund;
import dao.admin.FundDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class FundService {
    private FundDao fundDao = new FundDao();

    public DaoQueryListResult getFundList(Connection conn, QueryParameter param){
        return fundDao.getFundList(conn,param);
    }

    public DaoQueryResult getFund(Connection conn, long id) {
        return fundDao.getFund(conn,id);
    }

    public DaoUpdateResult updateFund(Connection conn, Fund fund) {
        return fundDao.updateFund(conn,fund);
    }

    public DaoUpdateResult insertFund(Connection conn, Fund fund) {
        return fundDao.insertFund(conn,fund);
    }
}
