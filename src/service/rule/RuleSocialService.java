package service.rule;

import bean.rule.RuleSocial;
import dao.rule.RuleSocialDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class RuleSocialService {
    private RuleSocialDao reluSocialDao = new RuleSocialDao();

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return null;
    }

    public DaoQueryResult get(Connection conn, long id) {
        return null;
    }

    public DaoUpdateResult update(Connection conn, RuleSocial rule) {
        return null;
    }

    public DaoUpdateResult insert(Connection conn, RuleSocial rule) {
        return null;
    }

    public DaoUpdateResult delete(Connection conn, long id) {
        return null;
    }
    private String getLast(Connection conn, String city) {
        return null;
    }
}
