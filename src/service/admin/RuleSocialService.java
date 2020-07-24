package service.admin;

import bean.admin.RuleSocial;
import dao.admin.RuleSocialDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class RuleSocialService {
    private RuleSocialDao reluSocialDao = new RuleSocialDao();

    public DaoQueryListResult getRule_socialList(Connection conn, QueryParameter param){
        return reluSocialDao.getRule_socialList(conn,param);
    }

    public DaoQueryResult getSocial(Connection conn, long id) {
        return reluSocialDao.getSocial(conn,id);
    }

    public DaoUpdateResult updateSocial(Connection conn, RuleSocial rule) {
        return reluSocialDao.updateSocial(conn,rule);
    }

    public DaoUpdateResult insertSocial(Connection conn, RuleSocial rule) {
        return reluSocialDao.insertSocial(conn,rule);
    }

    public DaoUpdateResult deleteSocial(Connection conn, long id) {
        return reluSocialDao.deleteSocial(conn,id);
    }
}
