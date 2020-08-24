package dao.employee;

import bean.employee.EmployeeExtra;
import database.*;

import java.sql.Connection;
import java.sql.Date;

public class ExtraDao {
    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"employee_extra",param,EmployeeExtra.class);
    }

    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.get(conn,"employee_extra",conditions,EmployeeExtra.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, EmployeeExtra e) {
        String sql = "update employee_extra set rid=?,school=?,major=?,household=?,address=?,date1=?,date2=?,reason=? where eid=?";
        Object []params = {e.getRid(),e.getSchool(),e.getMajor(),e.getHousehold(),e.getAddress(),e.getDate1(),e.getDate2(),e.getReason(),e.getEid()};
        return  DbUtil.update(conn,sql,params);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, EmployeeExtra e) {
        String sql = "insert employee_extra (eid,rid,school,major,household,address,date1,date2,reason) values (?,?,?,?,?,?,?,?,?)";
        Object []params = {e.getEid(),e.getRid(),e.getSchool(),e.getMajor(),e.getHousehold(),e.getAddress(),e.getDate1(),e.getDate2(),e.getReason()};
        return  DbUtil.insert(conn,sql,params);
    }

    //批量插入
    public static DaoUpdateResult insertBatch(Connection conn,String[] extras) {
        String sql = "insert employee_extra (eid,rid,school,major,household,address,date1,date2,reason) values (?,?,?,?,?,?,?,?,?)";

        return  null;
    }

    //离职或者退休
    public static DaoUpdateResult leave(Connection conn, long id, byte category, byte reason, Date date) {
        DaoUpdateResult res ;
        //修改员工状态
        String sql = "update employee set type = ? where id = ?";
        Object []params = {category,id};
        res =  DbUtil.update(conn,sql,params);
        if(res.success){//先修改员工信息，修改完毕之后再修改员工补充信息中的离职或者退休
            if(category==0){//离职
                String sql1 = "update employee_extra set date1 = ?, reason=? where eid = ?";
                Object []params1 = {date,reason,id};
                res =  DbUtil.update(conn,sql1,params1);
            }else {//退休
                String sql1 = "update employee_extra set date2 = ? where eid = ?";
                Object []params1 = {date,id};
                res =  DbUtil.update(conn,sql1,params1);
            }
        }

        return res;
    }

    //判断是否已经存在
    public static DaoExistResult exist(Connection conn, long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.exist(conn,"employee_extra",conditions);
    }

}
