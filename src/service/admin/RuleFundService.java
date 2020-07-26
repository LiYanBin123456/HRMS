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

    public DaoQueryListResult getFundRules(Connection conn, QueryParameter param){
        return ruleFundDao.getFundRules(conn,param);
    }

    public DaoQueryResult getFundRule(Connection conn, long id) {
        return ruleFundDao.getFundRule(conn,id);
    }

    public DaoUpdateResult updateFundRule(Connection conn, RuleFund rule) {
        return ruleFundDao.updateFundRule(conn,rule);
    }

    public DaoUpdateResult insertFundRule(Connection conn, RuleFund rule) {
        return ruleFundDao.insertFundRule(conn,rule);
    }

    public DaoUpdateResult deleteFundRule(Connection conn, long id) {
        return  ruleFundDao.deleteFundRule(conn,id);
    }
}
