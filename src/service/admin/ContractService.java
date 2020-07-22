package service.admin;

import bean.admin.Contract;
import dao.admin.DispatchDao;
import dao.admin.ContractDao;
import database.*;

import java.sql.Connection;

public class ContractService {
    ContractDao contractDao = new ContractDao();
    private DispatchDao dispatchDao = new DispatchDao();
    public DaoQueryListResult getContracts(Connection conn, QueryParameter parameter) {
     return contractDao.getContracts(conn,parameter);
    }

    public DaoQueryResult getContract(Connection conn, String id,String type) {
        return contractDao.getContract(conn,id,type);
    }

    public DaoUpdateResult insertContract(Connection conn, Contract contract) {
        return contractDao.insertContract(conn,contract);
    }

    public DaoUpdateResult insertContract2(Connection conn, Contract contract) {
        int status = 1;//修改为合作状态
        DaoUpdateResult result = dispatchDao.updateStatus(conn, contract.getBid(), status);
        if(result.success){
            result.msg="修改为合作客户";
        }
        return contractDao.insertContract(conn,contract);
    }
}
