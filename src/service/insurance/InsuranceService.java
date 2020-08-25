package service.insurance;

import bean.insurance.Insurance;
import dao.insurance.InsuranceDao;
import database.DaoUpdateResult;

import java.sql.Connection;
import java.util.List;

public class InsuranceService {
    public static DaoUpdateResult insertBatch(Connection conn, List<Insurance> insurances) {
       return InsuranceDao.insertBatch(conn,insurances);
    }
}
