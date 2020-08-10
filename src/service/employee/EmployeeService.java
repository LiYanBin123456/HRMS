package service.employee;

import bean.admin.Account;
import bean.employee.Employee;
import dao.employee.EmployeeDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;
import java.util.List;

public class EmployeeService {
private EmployeeDao employeeDao = new EmployeeDao();
    //获取列表
    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return employeeDao.getList(conn,param);
    }

    //获取详情
    public DaoQueryResult get(Connection conn, long id) {
        return null;
    }

    //修改
    public DaoUpdateResult update(Connection conn, Employee employee) {
        return null;
    }

    //增加
    public DaoUpdateResult insert(Connection conn, Employee employee) {
        return null;
    }

    //删除
    public DaoUpdateResult delete(Connection conn, long id) {
        return  null;
    }

    //批量插入
    public DaoUpdateResult insertBatch(Connection conn, List<Employee> employees) {
        return  null;
    }

    //批量派遣
    public DaoUpdateResult dispatch(Connection conn, List eids,long cid) {
        return  null;
    }

    //雇用
    public DaoUpdateResult employ(Connection conn, long id,byte category) {
        return  null;
    }
}
