package service.contract;

import bean.contract.Serve;
import database.QueryParameter;

import java.sql.Connection;

public class ServeService {
    //添加合同服务项目
    public String insert(Connection conn, Serve serve){
        return null;
    }

    //获取合同服务项目
    public String get(Connection conn,Long cid){
        return null;
    }

    //获取合作客户的所有合同服务项目
    public String getList(Connection conn, QueryParameter parameter){
        return null;
    }

    //修改合同服务项目
    public String update(Connection conn,Serve serve){
        return null;
    }
}
