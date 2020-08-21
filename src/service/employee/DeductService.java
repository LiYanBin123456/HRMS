package service.employee;

import bean.employee.Deduct;
import bean.employee.EnsureSetting;
import dao.employee.DeductDao;
import dao.employee.SettingDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

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
        return DeductDao.insert(conn,deduct);
    }

    //列表查询
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return  DeductDao.getList(conn,param);
    }

    //删除
    public static DaoUpdateResult delete(Connection conn,long id){
        return DeductDao.delete(conn,id);
    }
}
