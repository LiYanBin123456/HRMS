package service.admin;

import bean.admin.Rule_social;
import dao.admin.Rule_socialDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class Rule_socialService {
    private Rule_socialDao rule_socialDao = new Rule_socialDao();

    public DaoQueryListResult getRule_socialList(Connection conn, QueryParameter param){
        return rule_socialDao.getRule_socialList(conn,param);
    }

    public DaoQueryResult getSocial(Connection conn, long id) {
        return rule_socialDao.getSocial(conn,id);
    }

    public DaoUpdateResult updateSocial(Connection conn, Rule_social social) {
        return rule_socialDao.updateSocial(conn,social);
    }

    public DaoUpdateResult insertSocial(Connection conn, Rule_social social) {
        return rule_socialDao.insertSocial(conn,social);
    }

    public DaoUpdateResult deleteSocial(Connection conn, long id) {
        return rule_socialDao.deleteSocial(conn,id);
    }
}
