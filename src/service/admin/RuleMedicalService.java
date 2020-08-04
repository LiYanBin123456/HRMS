package service.admin;

import bean.rule.RuleMedicare;
import dao.admin.RuleMedicalDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class RuleMedicalService {
    private RuleMedicalDao reluMedicalDao = new RuleMedicalDao();

    public DaoQueryListResult getMedicalRules(Connection conn, QueryParameter param){
        return reluMedicalDao.getMedicalRules(conn,param);
    }

    public DaoQueryResult getMedicalRule(Connection conn, long id) {
        return reluMedicalDao.getMedicalRule(conn,id);
    }

    public DaoUpdateResult updateMedicalRule(Connection conn, RuleMedicare rule) {
        return reluMedicalDao.updateMedicalRule(conn,rule);
    }

    public DaoUpdateResult insertMedicalRule(Connection conn, RuleMedicare rule) {
        return reluMedicalDao.insertMedicalRule(conn,rule);
    }

    public DaoUpdateResult deleteMedicalRule(Connection conn, long id) {
        return reluMedicalDao.deleteMedicalRule(conn,id);
    }
}
