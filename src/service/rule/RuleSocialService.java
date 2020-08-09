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

    public DaoQueryListResult getSocialRules(Connection conn, QueryParameter param){
        return reluSocialDao.getSocialRules(conn,param);
    }

    public DaoQueryResult getSocialRule(Connection conn, long id) {
        return reluSocialDao.getSocialRule(conn,id);
    }

    public DaoUpdateResult updateSocialRule(Connection conn, RuleSocial rule) {
        return reluSocialDao.updateSocialRule(conn,rule);
    }

    public DaoUpdateResult insertSocialRule(Connection conn, RuleSocial rule) {
        return reluSocialDao.insertSocialRule(conn,rule);
    }

    public DaoUpdateResult deleteSocialRule(Connection conn, long id) {
        return reluSocialDao.deleteSocialRule(conn,id);
    }
}
