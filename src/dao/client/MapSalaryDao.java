package dao.client;

import bean.client.MapSalary;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryConditions;

import java.sql.Connection;

public class MapSalaryDao {
    //根据月份获取自定义工资,也是查出这个月的最新自定义工资
    public static DaoQueryResult get(long id,String month,Connection conn){
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid", "=", id);
        conditions.add("date" ,"like",month+"%");
        String order = " order by date desc limit 1";
        return DbUtil.getLast(conn, "map_salary", conditions,MapSalary.class,order);
    }

    //获取最新自定义工资
    public static DaoQueryResult getLast(long id, Connection conn){
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid", "=", id);
        String order = " ORDER BY date DESC limit 1";
        return DbUtil.getLast(conn, "map_salary", conditions,MapSalary.class,order);
    }

    //添加自定义工资
    public static DaoUpdateResult insert(MapSalary m, Connection conn){
        String sql = "insert into map_salary (cid,items,date) values (?,?,now())";
        Object []params = {m.getCid(),m.getItems()};
        return DbUtil.insert(conn,sql,params);
    }
}
