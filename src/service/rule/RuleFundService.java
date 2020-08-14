package service.rule;

import bean.rule.RuleFund;
import dao.rule.RuleFundDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

public class RuleFundService {
    private RuleFundDao ruleFundDao = new RuleFundDao();

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return null;
    }

    public DaoQueryResult get(Connection conn, long id) {

        return null;
    }

    public DaoUpdateResult update(Connection conn, RuleFund rule) {
        return null;
    }

    public DaoUpdateResult insert(Connection conn, RuleFund rule) {
        return null;
    }

    public DaoUpdateResult delete(Connection conn, long id) {
        return null;
    }

    private String getLast(Connection conn, String city) {
        return null;
    }
}
