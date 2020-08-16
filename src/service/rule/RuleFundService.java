package service.rule;

import bean.rule.RuleFund;
import dao.rule.RuleFundDao;
import database.*;

import java.sql.Connection;

public class RuleFundService  {

    //获取公积金规则列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){

        return RuleFundDao.getList(conn,param);
    }

    //获取指定公积金规则信息
    public static DaoQueryResult get(Connection conn, long id) {

        return  RuleFundDao.get(conn,id);
    }

    //修改公积金规则信息
    public static DaoUpdateResult update(Connection conn, RuleFund ruleFund) {

        return  RuleFundDao.update(conn,ruleFund);
    }

    //添加公积金规则信息
    public static DaoUpdateResult insert(Connection conn, RuleFund ruleFund) {

        return  RuleFundDao.insert(conn,ruleFund);
    }

    //删除公积金规则信息
    public static DaoUpdateResult delete(Connection conn, long id) {

        return  RuleFundDao.delete(conn,id);
    }

    //获取指定城市最新公积金规则
    public static DaoQueryResult getLast(Connection conn, String city) {
        return  RuleFundDao.getLast(conn,city);
    }
}
