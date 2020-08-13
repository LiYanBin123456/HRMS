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
    private ServeDao serveDao = new ServeDao();
    //添加合同服务项目
    public DaoUpdateResult insert(Connection conn, Serve serve){
        return serveDao.insert(conn,serve);
    }

    //获取合同服务项目
    public DaoQueryResult get(Connection conn, String id){
        return serveDao.get(conn, id);
    }

    //获取合作客户的所有合同服务项目
    public DaoQueryListResult getList(Connection conn, QueryParameter parameter, long id){
        return serveDao.getList(conn,parameter,id);
    }

    //修改合同服务项目
    public DaoUpdateResult update(Connection conn, Serve serve){
        return serveDao.update(conn,serve);
    }
}
