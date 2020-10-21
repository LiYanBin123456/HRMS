package service.client;

import bean.client.MapSalary;
import dao.client.MapSalaryDao;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;
import java.sql.Date;
import java.util.Calendar;

public class MapSalaryService {
    //根据月份获取自定义工资
    public static DaoQueryResult get(long id, String month, Connection conn){
        return MapSalaryDao.get(id,month,conn);
    }

    //获取最新自定义工资
    public static DaoQueryResult getLast(long id, Connection conn){

        return MapSalaryDao.getLast(id,conn);
    }

    //添加自定义工资
    public static DaoUpdateResult insert(MapSalary mapSalary, Connection conn){
        if(MapSalaryDao.exist(mapSalary.getCid(),mapSalary.getDate(),conn).exist){
            return MapSalaryDao.update(mapSalary,conn);
        }
        return MapSalaryDao.insert(mapSalary,conn);
    }
}
