package service.employee;

import bean.employee.EmployeeExtra;
import dao.employee.ExtraDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;
import java.sql.Date;

public class ExtraService {
    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return ExtraDao.getList(conn,param);
    }

    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        return ExtraDao.get(conn,id);
    }

    //修改
    public static DaoUpdateResult update(Connection conn,  EmployeeExtra extra) {
        return ExtraDao.update(conn,extra);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, EmployeeExtra extra) {
        DaoUpdateResult result = null;
        if(!ExtraDao.exist(conn,extra.getEid()).exist){
           result = ExtraDao.insert(conn,extra);
        }else {
            result.msg = "该员工的补充信息已存在，请勿重复添加";
        }
        return result;
    }

    //批量插入
    public static DaoUpdateResult insertBatch(Connection conn, String[] extras) {
        return  ExtraDao.insertBatch(conn,extras);
    }

    //离职或者退休
    public static DaoUpdateResult leave(Connection conn, long id, byte category, byte reason, Date date) {
        return  ExtraDao.leave(conn,id,category,reason,date);
    }

}
