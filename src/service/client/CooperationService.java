package service.client;

import bean.client.Cooperation;
import database.QueryParameter;

import java.sql.Connection;

public class CooperationService {
    public String get(long id,Connection conn){
        return null;
    }

    public String getList(Connection conn, QueryParameter param){
        return null;
    }

    public String update(Connection conn,Cooperation cooperation){
        return null;
    }
    public String deletePot(long id,Connection conn){
        return null;
    }
    public String insert(Cooperation cooperation, Connection conn){
        return null;
    }
    public String updateStatus(long id, Connection conn,byte status){
        return null;
    }
}
