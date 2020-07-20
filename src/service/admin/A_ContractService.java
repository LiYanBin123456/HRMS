package service.admin;

import bean.admin.a_contract;
import dao.admin.A_ContractDao;
import dao.admin.ClientDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class A_ContractService {
    private A_ContractDao contractDao = new A_ContractDao();
    private ClientDao clientDao = new ClientDao();
    public DaoUpdateResult insert(Connection conn, a_contract contract) {
        return contractDao.insert(conn,contract);
    }

    public DaoQueryResult getContract(Connection conn, String bid) {
        return  contractDao.getContract(conn,bid);
    }

    public DaoQueryListResult getContracts(Connection conn, QueryParameter parameter) {
        return  contractDao.getContracts(conn,parameter);
    }

    public DaoUpdateResult insert2(Connection conn, a_contract contract) {
        int status = 1;//修改为合作状态
        DaoUpdateResult result = clientDao.updateStatus(conn, contract.getBid(), status);
        if(result.success){
            result.msg="修改为合作客户";
        }
        return contractDao.insert(conn,contract);
    }
}
