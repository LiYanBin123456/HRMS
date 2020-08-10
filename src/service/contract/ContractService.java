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

    public DaoUpdateResult insertPot(Connection conn, Contract contract) {
        DaoUpdateResult result;
        int status = 1;//修改为合作状态
        String type = contract.getType();
        if(type=="A"){
            //修改派遣方客户的状态
             result = dispatchDao.updateStatus(conn, contract.getBid(), status);
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
