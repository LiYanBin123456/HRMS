package service.admin;

import bean.contract.Contract;
import bean.contract.Serve;
import dao.client.DispatchDao;
import dao.admin.ContractDao;
import database.*;

import java.sql.Connection;

public class ContractService {
    ContractDao contractDao = new ContractDao();
    private DispatchDao dispatchDao = new DispatchDao();
    public DaoQueryListResult getList(Connection conn, QueryParameter parameter) {
     return contractDao.getList(conn,parameter);
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

    //添加合同服务项目
    public String insertService(Connection conn, Serve serve){
        return null;
    }

    //获取合同服务项目
    public String getService(Connection conn,Long cid){
        return null;
    }

    //获取合作客户的所有合同服务项目
    public String getServiceList(Connection conn,QueryParameter parameter){
        return null;
    }

    //修改合同服务项目
    public String updateService(Connection conn,Serve serve){
        return null;
    }
}
