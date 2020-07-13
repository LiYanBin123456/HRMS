package service.admin;

import bean.admin.a_contract;
import dao.admin.A_ContractDao;
import database.DaoUpdateResult;

import java.sql.Connection;

public class A_ContractService {
    private A_ContractDao contractDao = new A_ContractDao();

    public DaoUpdateResult insert(Connection conn, a_contract contract) {
        return contractDao.insert(conn,contract);
    }
}
