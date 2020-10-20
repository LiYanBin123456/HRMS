package dao;

import bean.log.Log;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryParameter;

import java.sql.Connection;

public class LogDao {
    //插入日志
    public static DaoUpdateResult insert(Connection conn, Log l){
      String sql = "insert into log(sid,type,operator,time,content) values (?,?,?,?,?)";
        Object []params = {l.getSid(),l.getType(),l.getOperator(),l.getTime(),l.getContent()};
        return DbUtil.insert(conn,sql,params);
    }

    //获取所有日志
    public static DaoQueryListResult getList(Connection conn, long id, QueryParameter param){
        param.conditions.add("sid","=",id);
        return DbUtil.getList(conn,"log",param,Log.class);
    }
}
