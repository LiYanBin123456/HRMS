package service.employee;

import bean.employee.EnsureSetting;
import dao.employee.SettingDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

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
        DaoUpdateResult result = new DaoUpdateResult();
        if(!SettingDao.exist(conn,setting.getEid()).exist){
            result = SettingDao.insert(conn,setting);
        }else{
            result.msg = "该员工社保设置已存在，请勿重复添加";
        }
        return result;
    }



}
