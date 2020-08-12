package service.rule;

import bean.rule.RuleMedicare;
import dao.rule.RuleMedicareDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class RuleMedicareService {
    private RuleMedicareDao reluMedicalDao = new RuleMedicareDao();

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return null;
    }

    public DaoQueryResult get(Connection conn, long id) {
        return null;
    }

    public DaoUpdateResult update(Connection conn, RuleMedicare rule) {
        return null;
    }

    public DaoUpdateResult insert(Connection conn, RuleMedicare rule) {
        return null;
    }

    public DaoUpdateResult delete(Connection conn, long id) {
        return null;
    }
}
