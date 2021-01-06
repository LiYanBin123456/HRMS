package dao.client;

import bean.client.MapSalary;
import bean.rule.RuleMedicare;
import database.*;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class MapSalaryDao {
    //根据月份获取自定义工资,也是查出这个月的最新自定义工资
    public static DaoQueryResult get(long id,String month,Connection conn){
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid", "=", id);
        conditions.add("date" ,"like",month+"%");
        String order = " order by date desc limit 1";
        return DbUtil.getLast(conn, "map_salary", conditions,MapSalary.class,order);
    }

    //判断是否存在
    public static DaoExistResult exist(long id, Connection conn){
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid", "=", id);
        return DbUtil.exist(conn, "map_salary", conditions);
    }

    //判断是否存在
    public static DaoExistResult exist(long id, Date date, Connection conn){
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid", "=", id);
        conditions.add("date", "=", date);
        return DbUtil.exist(conn, "map_salary", conditions);
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

    //修改自定义工资
    public static DaoUpdateResult update(MapSalary m, Connection conn){
        String sql = "update map_salary set items=? where cid=? and date=?";
        Object []params = {m.getItems(),m.getCid(),m.getDate()};
        return DbUtil.update(conn,sql,params);
    }

    //根据月份查询自定义工资项
    public  static DaoQueryResult selectByMonth(long id,Connection conn,Date month){
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("cid", "=", id);
        parameter.addCondition("date","<=",month);
        parameter.order.set(true,"date",false);
        parameter.pagination.set(true,1,1);
        DaoQueryListResult res1 = DbUtil.getList(conn,"map_salary",parameter,MapSalary.class);
        DaoQueryResult res2 = new DaoQueryResult();
        res2.success = true;

        if(!res1.success){
            res2.success = false;
            res2.msg = "数据库操作错误";
            return res2;
        }

        List<MapSalary> mapSalaries = (List<MapSalary>) res1.rows;
        if(mapSalaries.size() == 0){
            res2.data = null;
        }else {
            res2.data = mapSalaries.get(0);
        }
        return res2;
    }
}
