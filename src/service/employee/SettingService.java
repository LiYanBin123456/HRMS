package service.employee;

import bean.employee.EmployeeSetting;
import bean.employee.PayCard;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;

public class SettingService {

    //获取详情
    public DaoQueryResult get(Connection conn, long id) {
        return null;
    }

    //修改
    public DaoUpdateResult update(Connection conn, EmployeeSetting setting,byte catrgory) {
        return null;
    }

    //增加
    public DaoUpdateResult insert(Connection conn, EmployeeSetting setting) {
        return null;
    }

}
