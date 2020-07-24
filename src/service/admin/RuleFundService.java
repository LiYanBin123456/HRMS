package service.admin;

import bean.admin.RuleFund;
import dao.admin.RuleFundDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class RuleFundService {
    private RuleFundDao ruleFundDao = new RuleFundDao();

    public DaoQueryListResult getFundList(Connection conn, QueryParameter param){
        return ruleFundDao.getFundList(conn,param);
    }

    public DaoQueryResult getFund(Connection conn, long id) {
        return ruleFundDao.getFund(conn,id);
    }

    public DaoUpdateResult updateFund(Connection conn, RuleFund rule) {
        return ruleFundDao.updateFund(conn,rule);
    }

    public DaoUpdateResult insertFund(Connection conn, RuleFund rule) {
        return ruleFundDao.insertFund(conn,rule);
    }

    public DaoUpdateResult deleteFund(Connection conn, long id) {
        return  ruleFundDao.deleteFund(conn,id);
    }
}
