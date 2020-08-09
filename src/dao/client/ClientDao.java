package dao.client;

import bean.client.Finance;
import bean.client.MapSalary;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.awt.*;
import java.sql.Connection;

public class ClientDao {

    //分配管理员
    private String allocate(Connection conn,Long aid,List cids) {
        return null;
    }

    //修改客户服务信息
    public DaoUpdateResult updateFinance(Connection conn, Finance finance) {
        return null;
    }

}
