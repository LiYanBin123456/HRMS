package service.contract;

import bean.contract.Contract;
import dao.client.DispatchDao;
import dao.contract.ContractDao;
import database.*;

import java.sql.Connection;

public class ContractService {
    ContractDao contractDao = new ContractDao();
    private DispatchDao dispatchDao = new DispatchDao();
    public DaoQueryListResult getList(Connection conn, QueryParameter parameter,String type) {
     return contractDao.getList(conn,parameter,type);
    }

    public DaoQueryResult getLast(Connection conn, String id,String type) {
        return contractDao.getLast(conn,id,type);
    }

    public DaoUpdateResult insert(Connection conn, Contract contract) {
        return contractDao.insert(conn,contract);
    }

    public DaoUpdateResult update(Connection conn, Contract contract) {
        return  contractDao.update(conn,contract);
    }


    public DaoQueryResult get(Connection conn,String id) {
        return contractDao.get(conn,id);
    }

    public DaoUpdateResult delete(Connection conn, String id) {
        return contractDao.delete(conn,id);
    }
}
