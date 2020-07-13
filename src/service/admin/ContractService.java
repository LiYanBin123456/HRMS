package service.admin;

import bean.admin.Contract;
import dao.admin.ContractDao;
import database.*;
import utills.CreateGetNextId;

import java.sql.Connection;

public class ContractService {
    ContractDao contractDao = new ContractDao();
    public DaoQueryListResult getContracts(Connection conn, QueryParameter parameter) {
     return contractDao.getContracts(conn,parameter);
    }

    public DaoQueryResult getContract(Connection conn, String id) {
        return contractDao.getContract(conn,id);
    }

    public DaoUpdateResult insertContract(Connection conn, Contract contract) {
        //自定义自增id
        QueryConditions conditions = new QueryConditions();
        String type = contract.getType();
        System.out.println(type);
        type += "%";
        System.out.println(type);
        conditions.add("id","like",type);

        //通过模糊查找各种类型id的条数
        DaoQueryListResult counts = DbUtil.getCounts(conn, "contract", conditions);
        long total = counts.total;
        String id = CreateGetNextId.NextId(total, contract.getType());
        contract.setId(id);

        return contractDao.insertContract(conn,contract);
    }
}
