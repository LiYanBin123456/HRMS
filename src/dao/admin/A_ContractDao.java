package dao.admin;

import bean.admin.a_contract;
import database.DaoUpdateResult;
import database.DbUtil;

import java.sql.Connection;

public class A_ContractDao {
    public DaoUpdateResult insert(Connection conn, a_contract c) {
        String sql = "insert into a_contract (id,cid,start,end,status,intro) values (?,?,?,?,?,?)";
        Object []params = {c.getId(),c.getCid(),c.getStart(),c.getEnd(),c.getStatus(),c.getIntro()};
        return DbUtil.insert(conn,sql,params);
    }
}
