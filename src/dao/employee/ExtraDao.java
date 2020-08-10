package dao.employee;

import bean.employee.EmployeeExtra;
import database.*;

import java.sql.Connection;
import java.util.List;

public class ExtraDao {
    //获取列表
    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"employee_extra",param,EmployeeExtra.class);
    }

    //获取详情
    public DaoQueryResult get(Connection conn, long id) {
        return null;
    }

    //修改
    public DaoUpdateResult update(Connection conn, EmployeeExtra extra) {
        return null;
    }

    //增加
    public DaoUpdateResult insert(Connection conn, EmployeeExtra extra) {
        return null;
    }

    //批量插入
    public DaoUpdateResult insertBatch(Connection conn, List<EmployeeExtra> extras) {
        return  null;
    }

    //离职或者退休
    public DaoUpdateResult leave(Connection conn, long id,byte category,byte leave) {
        return  null;
    }

}
