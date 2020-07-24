package service.admin;

import bean.admin.RuleMedical;
import dao.admin.RuleMedicalDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class RuleMedicalService {
    private RuleMedicalDao reluMedicalDao = new RuleMedicalDao();

    public DaoQueryListResult getRule_medicalList(Connection conn, QueryParameter param){
        return reluMedicalDao.getRule_medicalList(conn,param);
    }

    public DaoQueryResult getMedical(Connection conn, long id) {
        return reluMedicalDao.getMedical(conn,id);
    }

    public DaoUpdateResult updateMedical(Connection conn, RuleMedical rule) {
        return reluMedicalDao.updateMedical(conn,rule);
    }

    public DaoUpdateResult insertMedical(Connection conn, RuleMedical rule) {
        return reluMedicalDao.insertMedical(conn,rule);
    }

    public DaoUpdateResult deleteMedical(Connection conn, long id) {
        return reluMedicalDao.deleteMedical(conn,id);
    }
}
