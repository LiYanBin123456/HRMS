package dao.admin;

import bean.admin.a_contract;
import database.*;

import java.sql.Connection;
import java.util.List;

public class A_ContractDao {
    //插入合同
    public DaoUpdateResult insert(Connection conn, a_contract c) {
        String sql = "insert into a_contract (id,cid,start,end,status,intro) values (?,?,?,?,?,?)";
        Object[] params = {c.getId(), c.getCid(), c.getStart(), c.getEnd(), c.getStatus(), c.getIntro()};
        return DbUtil.insert(conn, sql, params);
    }
    //根据客户id获取合同
    public DaoQueryResult getContract(Connection conn, String cid) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid", "=", cid);
        return DbUtil.getLast(conn, "a_contract", conditions, a_contract.class);
    }

    //根据查询条件获取所有合同
    public DaoQueryListResult getContracts(Connection conn, QueryParameter parameter) {
        return DbUtil.getList(conn, "a_contract", parameter, a_contract.class);
    }

    //删除合作客户时，将该客户的所有合同删除,还要删除对应合同的文件
    public List<String> deleteContract(Connection conn, long id) {
        QueryParameter parameter = new QueryParameter();
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid", "=", id);

        String sql = "id";
        parameter.conditions = conditions;
        List<String> column = DbUtil.getColumns(conn, sql,"a_contract",parameter);

        //查询完所有要删除的合同文件名字之后，在删除合同
        DbUtil.delete(conn,"a_contract",conditions);
        return column;
    }

    public static void main(String[] args) {
        Connection conn = ConnUtil.getConnection();
        A_ContractDao contractDao = new A_ContractDao();
//        DaoQueryListResult daoQueryListResult = contractDao.deleteContract(conn, 2);
//        System.out.println(daoQueryListResult.rows);
        contractDao.deleteContract(conn,3);
    }
}

