package service.client;

import bean.client.Cooperation;
import database.QueryParameter;

import java.sql.Connection;

public class CooperationService {
   //获取客户详情
    public String get(long id,Connection conn){
        return null;
    }

    //获取客户列表
    public String getList(Connection conn, QueryParameter param){
        return null;
    }

    //修改客户
    public String update(Connection conn,Cooperation cooperation){
        return null;
    }

    //删除潜在客户
    public String deletePot(long id,Connection conn){
        return null;
    }

    //添加客户
    public String insert(Cooperation cooperation, Connection conn){
        return null;
    }

    //删除合作客户
    public String deleteCoop(long id, Connection conn,byte status){
        return null;
    }
}
