package service.admin;

import dao.admin.Rule_medicalDao;
import database.DaoQueryListResult;
import database.QueryParameter;

import java.sql.Connection;

public class Rule_medicalService {
    private Rule_medicalDao rule_medicalDao = new Rule_medicalDao();

    public DaoQueryListResult getRule_medicalList(Connection conn, QueryParameter param){
        return rule_medicalDao.getRule_medicalList(conn,param);
    }
}
