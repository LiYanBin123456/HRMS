package dao.client;

import bean.client.Finance;
import bean.client.MapSalary;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;

public class ClientDao {
    public DaoUpdateResult updateFinance(Connection conn, Finance finance) {
        return null;
    }

    public DaoQueryResult getFinance(Connection conn, long cid, String type) {
        return null;
    }

    public DaoUpdateResult insertFinance(Connection conn, Finance finance) {
        return null;
    }

    public String getSalaryDefine(long id,Connection conn){
        return null;
    }
    public String getLastSalaryDefine(long id,Connection conn){
        return null;
    }
    public String insertSalaryDefine(MapSalary mapSalary, Connection conn){
        return null;
    }
}
