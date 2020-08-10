package dao.client;

import bean.client.Cooperation;

import database.DaoQueryListResult;
import database.DbUtil;
import database.QueryParameter;

import java.sql.Connection;

public class CooperationDao {

    public String get(long id,Connection conn){
        return null;
    }

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(name,contact)","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"cooperation",param, Cooperation.class);
    }

    public String update(Connection conn,Cooperation cooperation){
        return null;
    }

    public String delete(long id,Connection conn){
        return null;
    }

    public String insert(Cooperation cooperation, Connection conn){
        return null;
    }

    public String updateStatus(long id, Connection conn,byte status){
        return null;
    }
}
