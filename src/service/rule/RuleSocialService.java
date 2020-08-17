package service.rule;

import bean.rule.RuleSocial;
import dao.rule.RuleSocialDao;
import database.*;

import java.sql.Connection;

public class RuleSocialService{

    //获取社保规则列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter pram){

        return  RuleSocialDao.getList(conn,pram);
    }

    //获取指定社保规则信息
    public static DaoQueryResult get(Connection conn, long id) {

        return  RuleSocialDao.get(conn,id);
    }

    //修改社保规则信息
    public static DaoUpdateResult update(Connection conn, RuleSocial ruleSocial) {

        return  RuleSocialDao.update(conn,ruleSocial);
    }

    //添加社保规则信息
    public static DaoUpdateResult insert(Connection conn, RuleSocial ruleSocial) {

        return  RuleSocialDao.insert(conn,ruleSocial);
    }

    //删除社保规则信息
    public static DaoUpdateResult delete(Connection conn, long id) {

        return  RuleSocialDao.delete(conn,id);
    }

    //获取指定城市最新社保规则
    public static DaoQueryResult getLast(Connection conn, String city) {
        return RuleSocialDao.getLast(conn,city);
    }
}