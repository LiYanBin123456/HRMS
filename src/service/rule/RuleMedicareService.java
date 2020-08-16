package service.rule;

import bean.rule.RuleMedicare;
import dao.rule.RuleMedicareDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;


import java.sql.Connection;

public class RuleMedicareService {
    //获取医保规则列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return  RuleMedicareDao.getList(conn,param);
    }

    //获取指定医保规则信息
    public static DaoQueryResult get(Connection conn, long id) {
        return  RuleMedicareDao.get(conn,id);
    }

    //修改医保规则信息
    public static DaoUpdateResult update(Connection conn, RuleMedicare ruleMedicare) {
        return  RuleMedicareDao.update(conn,ruleMedicare);
    }

    //添加医保规则信息
    public static DaoUpdateResult insert(Connection conn, RuleMedicare ruleMedicare) {
        return  RuleMedicareDao.insert(conn,ruleMedicare);
    }

    //删除医保规则信息
    public static DaoUpdateResult delete(Connection conn, long id) {
        return  RuleMedicareDao.delete(conn,id);
    }

    //获取指定城市最新医保规则
    public static DaoQueryResult getLast(Connection conn, String city) {
        return RuleMedicareDao.getLast(conn,city);
    }
}