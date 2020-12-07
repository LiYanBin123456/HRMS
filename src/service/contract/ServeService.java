package service.contract;

import bean.contract.Serve;
import com.sun.xml.internal.bind.v2.model.core.ID;
import dao.contract.ServeDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class ServeService {
    //添加合同服务项目
    public static DaoUpdateResult insert(Connection conn, Serve serve){
        return ServeDao.insert(conn,serve);
    }

    //获取合同服务项目
    public static DaoQueryResult get(Connection conn, String id){
        return ServeDao.get(conn, id);
    }

    //获取合作客户的所有合同服务项目
    public static DaoQueryListResult getList(Connection conn, QueryParameter parameter){
        return ServeDao.getList(conn,parameter);
    }

    //修改合同服务项目
    public static DaoUpdateResult update(Connection conn, Serve serve){
        return ServeDao.update(conn,serve);
    }
}
