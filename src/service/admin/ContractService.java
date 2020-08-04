package service.admin;

import bean.contract.Contract;
import dao.admin.ClientDao;
import dao.admin.ContractDao;
import database.*;

import java.sql.Connection;

public class ContractService {
    ContractDao contractDao = new ContractDao();
    private ClientDao clientDao = new ClientDao();
    public DaoQueryListResult getList(Connection conn, QueryParameter parameter) {
     return contractDao.getList(conn,parameter);
    }

    public DaoQueryResult getLast(Connection conn, String id,String type) {
        return contractDao.getLast(conn,id,type);
    }

    public DaoUpdateResult insertCooperation(Connection conn, Contract contract) {
        return contractDao.insertContract(conn,contract);
    }

    public DaoUpdateResult insertPotential(Connection conn, Contract contract) {
        DaoUpdateResult result;
        int status = 1;//修改为合作状态
        String type = contract.getType();
        if(type=="A"){
            //修改派遣方客户的状态
             result = clientDao.updateStatus(conn, contract.getBid(), status);
            if(result.success){
                result.msg="修改为派遣方合作客户";
            }
        }else if(type=="C"){
            //修改合作单位客户的状态
            System.out.println("修改合作单位客户的状态");
        }


        return contractDao.insertContract(conn,contract);
    }
}
