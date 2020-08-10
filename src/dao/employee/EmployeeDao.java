package dao.employee;

import bean.employee.Employee;
import database.*;

import java.sql.Connection;
import java.util.List;

public class EmployeeDao {
    //获取列表，通过视图查找
    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(name,contact)","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"employee",param,Employee.class);
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
