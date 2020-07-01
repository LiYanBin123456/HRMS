package service;

import dao.Rule_socialDao;
import database.DaoQueryListResult;
import database.QueryParameter;

import java.sql.Connection;

public class Rule_socialService {
    private Rule_socialDao rule_socialDao = new Rule_socialDao();

    public DaoQueryListResult getRule_socialList(Connection conn, QueryParameter param){
        return rule_socialDao.getRule_socialList(conn,param);
    }
}
