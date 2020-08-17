package service.employee;


import bean.employee.Employee;
import dao.employee.EmployeeDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;


public class EmployeeService {
    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return EmployeeDao.getList(conn,param);
    }

    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        return EmployeeDao.get(conn,id);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, Employee employee) {
        return EmployeeDao.update(conn,employee);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, Employee employee) {
        return EmployeeDao.insert(conn,employee);
    }

    //删除
    public static DaoUpdateResult delete(Connection conn, long id) {
        return  EmployeeDao.delete(conn,id);
    }

    //批量插入
    public static DaoUpdateResult insertBatch(Connection conn, String[] employees) {
        return  EmployeeDao.insertBatch(conn,employees);
    }

    //批量派遣
    public static DaoUpdateResult dispatch(Connection conn, String[] eids,long cid) {
        return  EmployeeDao.dispatch(conn,eids,cid);
    }

    //雇用
    public static DaoUpdateResult employ(Connection conn, long id,byte category) {
        return  EmployeeDao.employ(conn,id,category);
    }
}
