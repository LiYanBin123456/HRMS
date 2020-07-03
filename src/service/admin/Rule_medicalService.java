package service.admin;

import bean.admin.Rule_medical;
import dao.admin.Rule_medicalDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class Rule_medicalService {
    private Rule_medicalDao rule_medicalDao = new Rule_medicalDao();

    public DaoQueryListResult getRule_medicalList(Connection conn, QueryParameter param){
        return rule_medicalDao.getRule_medicalList(conn,param);
    }

    public DaoQueryResult getMedical(Connection conn, long id) {
        return rule_medicalDao.getMedical(conn,id);
    }

    public DaoUpdateResult updateMedical(Connection conn, Rule_medical medical) {
        return rule_medicalDao.updateMedical(conn,medical);
    }

    public DaoUpdateResult insertMedical(Connection conn, Rule_medical medical) {
        return rule_medicalDao.insertMedical(conn,medical);
    }
}
