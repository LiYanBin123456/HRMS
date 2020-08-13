package service.client;

import bean.client.MapSalary;
import dao.client.MapSalaryDao;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;

public class MapSalaryService {
    private MapSalaryDao mapSalaryDao = new MapSalaryDao();
    //根据月份获取自定义工资
    public DaoQueryResult get(long id, String month, Connection conn){
        return mapSalaryDao.get(id,month,conn);
    }

    //获取最新自定义工资
    public DaoQueryResult getLast(long id, Connection conn){

        return mapSalaryDao.getLast(id,conn);
    }

    //添加自定义工资
    public DaoUpdateResult insert(MapSalary mapSalary, Connection conn){
        return mapSalaryDao.insert(mapSalary,conn);
    }
}
