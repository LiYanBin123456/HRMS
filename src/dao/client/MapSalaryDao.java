package dao.client;

import bean.client.MapSalary;

import java.sql.Connection;

public class MapSalaryDao {
    //根据月份获取自定义工资
    public String get(long id,String month,Connection conn){
        return null;
    }

    //获取最新自定义工资
    public String getLast(long id,Connection conn){
        return null;
    }

    //添加自定义工资
    public String insert(MapSalary mapSalary, Connection conn){
        return null;
    }
}
