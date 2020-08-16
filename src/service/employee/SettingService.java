package service.employee;

import bean.employee.EmployeeSetting;
import bean.employee.PayCard;
import dao.employee.SettingDao;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;

public class SettingService {
    private SettingDao settingDao = new SettingDao();
    //获取详情
    public DaoQueryResult get(Connection conn, long id) {
        return settingDao.get(conn,id);
    }

    //修改
    public DaoUpdateResult update(Connection conn, EmployeeSetting setting,byte catrgory) {
        return settingDao.update(conn,setting,catrgory);
    }

    //增加
    public DaoUpdateResult insert(Connection conn, EmployeeSetting setting) {
        return settingDao.insert(conn,setting);
    }

}
