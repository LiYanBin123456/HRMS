package service.employee;

import bean.employee.Deduct;
import bean.employee.EnsureSetting;
import dao.employee.DeductDao;
import dao.employee.SettingDao;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;

//个人专项扣除service层
public class DeductService {
    public static DaoQueryResult get(Connection conn, long id) {
        return DeductDao.get(conn,id);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, Deduct deduct) {
        return DeductDao.update(conn,deduct);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, Deduct deduct) {
        return DeductDao.update(conn,deduct);
    }
}
