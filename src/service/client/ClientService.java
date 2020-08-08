package service.client;

import bean.client.Finance;
import bean.client.MapSalary;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;

public class ClientService {
    //修改服务信息
    public DaoUpdateResult updateFinance(Connection conn, Finance finance) {
        return null;
    }

    //获取服务信息
    public DaoQueryResult getFinance(Connection conn, long cid, String type) {
        return null;
    }

    //添加服务信息
    public DaoUpdateResult insertFinance(Connection conn, Finance finance) {
        return null;
    }

    //根据月份获取自定义工资
    public String getSalaryDefine(long id,String month,Connection conn){
        return null;
    }

    //获取最新自定义工资
    public String getLastSalaryDefine(long id,Connection conn){
        return null;
    }

    //添加自定义工资
    public String insertSalaryDefine(MapSalary mapSalary, Connection conn){
        return null;
    }
}
