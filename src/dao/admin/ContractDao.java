package dao.admin;

import bean.admin.Contract;
import database.*;

import java.sql.Connection;

public class ContractDao {
    public DaoQueryListResult getContracts(Connection conn, QueryParameter parameter) {
        return DbUtil.getList(conn,"contract",parameter, Contract.class);
    }

    public  DaoQueryResult getContract(Connection conn, String id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"contract",conditions,Contract.class);
    }

    public DaoUpdateResult insertContract(Connection conn, Contract c) {
        String sql = "insert into contract (id,cid,sign,start,end,type,accessory,status) values (?,?,?,?,?,?,?,?)";
        Object []params = {c.getId(),c.getCid(),c.getSign(),c.getStart(),c.getEnd(),c.getType(),c.getAccessory(),c.getStatus()};
        return DbUtil.insert(conn,sql,params);

    }
}
