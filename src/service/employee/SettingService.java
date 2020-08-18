package service.employee;

import bean.employee.EnsureSetting;
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
    public static DaoUpdateResult update(Connection conn, EnsureSetting setting) {
        return SettingDao.update(conn,setting);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, EnsureSetting setting) {
        return SettingDao.insert(conn,setting);
    }

}
