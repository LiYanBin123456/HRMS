package dao.employee;

import bean.employee.Employee;
import bean.employee.ViewEmployee;
import bean.insurance.Product;
import database.*;

import java.sql.Connection;
import java.util.List;

public class EmployeeDao {
    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(name,cname)","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"view_employee",param,ViewEmployee.class);
    }

    //获取详情
    public static DaoQueryResult get(Connection conn, QueryConditions conditions) {
        return DbUtil.get(conn,"view_employee",conditions,ViewEmployee.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, ViewEmployee e) {
        DaoUpdateResult result;
        //需要判断外键是否为0，为0就需要转换成null
        String cid = e.getCid()==0?null:String.valueOf(e.getCid());
        String sql = "update employee set cid=?,cardId=?,name=?,phone=?,degree=?,type=?,entry=?,status=?,department=?,post=?,category=?,price=? where id=?";
        Object []params = {cid,e.getCardId(),e.getName(),e.getPhone(),e.getDegree(),e.getType(),e.getEntry(),e.getStatus(),e.getDepartment(),e.getPost(),e.getCategory(),e.getPrice(),e.getId()};
        result = DbUtil.update(conn,sql,params);
        if(result.success){//修改员工信息成功后修改员工补充信息中的档案编号，学校，主修
            String sql2 = "update employee_extra set rid=?,school=?,major=? where eid=?";
            Object []params2 = {e.getRid(),e.getSchool(),e.getMajor(),e.getId()};
            DbUtil.update(conn,sql2,params2);
        }
        return result;
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, Employee e) {
        //需要判断外键是否为0，为0就需要转换成null
        String cid = e.getCid()==0?null:String.valueOf(e.getCid());
        String sql = "insert employee (did,cid,cardId,name,phone,degree,type,entry,status,department,post,category,price) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {e.getDid(),cid,e.getCardId(),e.getName(),e.getPhone(),e.getDegree(),e.getType(),e.getEntry(),e.getStatus(),e.getDepartment(),e.getPost(),e.getCategory(),e.getPrice()};
        return  DbUtil.insert(conn,sql,params);
    }

    //删除
    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"employee",conditions);
    }

    //移入人才库
    public static DaoUpdateResult remove(Connection conn, long id) {
        String sql = String.format("update employee set type = %S where id = ?",2);
        Object []params = {id};
        return  DbUtil.update(conn,sql,params);

    }

    //批量插入
    public static DaoUpdateResult insertBatch(Connection conn, List<Employee> employees) {
        String sql = "insert employee (did,cid,cardId,name,phone,degree,type,entry,status,department,post,category,price) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object [][]params = new Object[employees.size()][];
        for (int i = 0; i < employees.size(); i++) {
            params[i] = new Object[]{employees.get(i).getDid(),employees.get(i).getCid(),employees.get(i).getCardId(),employees.get(i).getName(),employees.get(i).getPhone(),
                    employees.get(i).getDegree(),employees.get(i).getType(),employees.get(i).getEntry(),employees.get(i).getStatus(),employees.get(i).getDepartment(),employees.get(i).getPost(),
                    employees.get(i).getCategory(),employees.get(i).getPrice()
            };
        }
        return DbUtil.insertBatch(conn,sql,params);
    }

    //批量派遣
    public static DaoUpdateResult dispatch(Connection conn, String[] eids,long cid) {
        String sql = String.format("update employee set cid = %S where id = ?",cid);
        Object [][]params = new Object[eids.length][];
        for (int i = 0; i < eids.length; i++) {
            params[i] = new Object[]{eids[i]};
        }
        return DbUtil.batch(conn,sql,params);
    }

    //雇用
    public static DaoUpdateResult employ(Connection conn, long id,byte category) {
        String sql = "update employee set type=? where id=?";
        Object []params = {category,id};
        return  DbUtil.update(conn,sql,params);
    }

}
