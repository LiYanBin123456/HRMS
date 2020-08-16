package service.employee;


import bean.employee.Employee;
import dao.employee.EmployeeDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;


public class EmployeeService {
private EmployeeDao employeeDao = new EmployeeDao();
    //获取列表
    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return employeeDao.getList(conn,param);
    }

    //获取详情
    public DaoQueryResult get(Connection conn, long id) {
        return employeeDao.get(conn,id);
    }

    //修改
    public DaoUpdateResult update(Connection conn, Employee employee) {
        return employeeDao.update(conn,employee);
    }

    //增加
    public DaoUpdateResult insert(Connection conn, Employee employee) {
        return employeeDao.insert(conn,employee);
    }

    //删除
    public DaoUpdateResult delete(Connection conn, long id) {
        return  employeeDao.delete(conn,id);
    }

    //批量插入
    public DaoUpdateResult insertBatch(Connection conn, String[] employees) {
        return  employeeDao.insertBatch(conn,employees);
    }

    //批量派遣
    public DaoUpdateResult dispatch(Connection conn, String[] eids,long cid) {
        return  employeeDao.dispatch(conn,eids,cid);
    }

    //雇用
    public DaoUpdateResult employ(Connection conn, long id,byte category) {
        return  employeeDao.employ(conn,id,category);
    }
}
