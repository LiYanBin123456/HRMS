package service.settlement;

import bean.client.MapSalary;
import bean.employee.Employee;
import bean.settlement.ViewDetail1;
import dao.client.CooperationDao;
import dao.client.MapSalaryDao;
import dao.employee.EmployeeDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Detail2Dao;
import database.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SalaryService {

    /**
     * 查询工资
     * @param conn 数据库连接
     * @param cardId  身份证号码
     * @param month 月份,形如“2020-10-01”，日必须是01
     * @return 工资数据集合，如果不是小时工，还会通过extra附带工资定义集合
     */
    public static DaoQueryListResult getSalary(Connection conn, String cardId, String month) {
        DaoQueryListResult res = new DaoQueryListResult();

        QueryConditions c = new QueryConditions();
        c.add("cardId","=",cardId);
        Employee employee = (Employee) EmployeeDao.get(conn,c).data;
        if(employee == null){
            res.success = false;
            res.msg = "查无此人";
            return res;
        }

        QueryParameter p2 = new QueryParameter();
        p2.addCondition("cardId","=",cardId);
        p2.addCondition("month","=",month);
        if(employee.getCategory() == 3){
            res = Detail2Dao.getList(conn,p2);
        }else{
            res = Detail1Dao.getList(conn,p2);
            List<ViewDetail1> details = (List<ViewDetail1>) res.rows;
            if(details.size()>0){
                List<MapSalary> mapSalaries = new ArrayList<>();
                for(ViewDetail1 d:details){
                    MapSalary mapSalary = (MapSalary) MapSalaryDao.get(d.getSid(),month,conn).data;
                    mapSalaries.add(mapSalary);
                }
                res.extra = mapSalaries;
            }
        }
        return res;
    }
}
