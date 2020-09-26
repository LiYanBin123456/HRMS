package service.contract;

import bean.contract.Contract;
import dao.client.DispatchDao;
import dao.contract.ContractDao;
import database.*;

import java.sql.Connection;

public class ContractService {
    public static DaoQueryListResult getList(Connection conn, QueryParameter parameter, String type, long rid) {
     return ContractDao.getList(conn,parameter,type,rid);
    }

    public static DaoQueryResult getLast(Connection conn, long id,String type) {
        return ContractDao.getLast(conn,id,type);
    }

    public static DaoUpdateResult insert(Connection conn, Contract contract) {
        return ContractDao.insert(conn,contract);
    }

    public static DaoUpdateResult update(Connection conn, Contract contract) {
        return  ContractDao.update(conn,contract);
    }


    public static DaoQueryResult get(Connection conn,String id) {
        return ContractDao.get(conn,id);
    }

    public static DaoUpdateResult delete(Connection conn, String id) {
        return ContractDao.delete(conn,id);
    }
}
