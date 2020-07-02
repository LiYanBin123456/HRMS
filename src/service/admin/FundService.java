package service.admin;

import dao.admin.FundDao;
import database.DaoQueryListResult;
import database.QueryParameter;

import java.sql.Connection;

public class FundService {
    private FundDao fundDao = new FundDao();

    public DaoQueryListResult getFundList(Connection conn, QueryParameter param){
        return fundDao.getFundList(conn,param);
    }
}
