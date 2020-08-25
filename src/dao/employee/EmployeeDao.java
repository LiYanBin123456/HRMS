package dao.employee;

import bean.employee.Employee;
import bean.insurance.Product;
import database.*;

import java.sql.Connection;
import java.util.List;

public class EmployeeDao {
    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"employee",param,Employee.class);
    }

    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"employee",conditions,Employee.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, Employee e) {
        String sql = "update employee set did=?,cid=?,cardId=?,name=?,phone=?,degree=?,type=?,entry=?,status=?,department=?,post=?,category=?,price=? where id=?";
        Object []params = {e.getDid(),e.getCid(),e.getCardId(),e.getName(),e.getPhone(),e.getDegree(),e.getType(),e.getEntry(),e.getStatus(),e.getDepartment(),e.getPost(),e.getCategory(),e.getPrice(),e.getId()};
        return  DbUtil.update(conn,sql,params);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, Employee e) {
        String sql = "insert employee (did,cid,cardId,name,phone,degree,type,entry,status,department,post,category,price) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {e.getDid(),e.getCid(),e.getCardId(),e.getName(),e.getPhone(),e.getDegree(),e.getType(),e.getEntry(),e.getStatus(),e.getDepartment(),e.getPost(),e.getCategory(),e.getPrice()};
        return  DbUtil.insert(conn,sql,params);
    }

    //删除
    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"employee",conditions);
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
