package service.employee;

import bean.employee.EmployeeSetting;
import bean.employee.PayCard;
import dao.employee.SettingDao;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;

public class SettingService {
    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        return SettingDao.get(conn,id);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, EmployeeSetting setting,byte catrgory) {
        return SettingDao.update(conn,setting,catrgory);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, EmployeeSetting setting) {
        return SettingDao.insert(conn,setting);
    }

}
