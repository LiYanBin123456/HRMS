package dao.employee;

import bean.employee.Employee;
import bean.insurance.Product;
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
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"employee",conditions,Employee.class);
    }

    //修改
    public DaoUpdateResult update(Connection conn, Employee e) {
        String sql = "update employee set did=?,cid=?,cardId=?,name=?,phone=?,degree=?,type=?,entry=?,status=?,department=?,post=?,category=?,price=? where id=?";
        Object []params = {e.getDid(),e.getCid(),e.getCardId(),e.getName(),e.getPhone(),e.getDegree(),e.getType(),e.getEntry(),e.getStatus(),e.getDepartment(),e.getPost(),e.getCategory(),e.getPrice(),e.getId()};
        return  DbUtil.update(conn,sql,params);
    }

    //增加
    public DaoUpdateResult insert(Connection conn, Employee e) {
        String sql = "insert employee (did,cid,cardId,name,phone,degree,type,entry,status,department,post,category,price) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {e.getDid(),e.getCid(),e.getCardId(),e.getName(),e.getPhone(),e.getDegree(),e.getType(),e.getEntry(),e.getStatus(),e.getDepartment(),e.getPost(),e.getCategory(),e.getPrice()};
        return  DbUtil.insert(conn,sql,params);
    }

    //删除
    public DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"employee",conditions);
    }

    //批量插入
    public DaoUpdateResult insertBatch(Connection conn, String[] employees) {
        String sql = "insert employee (did,cid,cardId,name,phone,degree,type,entry,status,department,post,category,price) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object [][]params = new Object[employees.length][];
        for (int i = 0; i < employees.length; i++) {
            params[i] = new Object[]{employees[i]};
        }
        return  DbUtil.batch(conn,sql,params);
    }

    //批量派遣
    public DaoUpdateResult dispatch(Connection conn, String[] eids,long cid) {
        String sql = String.format("update employee set cid = %S where id = ?",cid);
        Object [][]params = new Object[eids.length][];
        for (int i = 0; i < eids.length; i++) {
            params[i] = new Object[]{eids[i]};
        }
        return DbUtil.batch(conn,sql,params);
    }

    //雇用
    public DaoUpdateResult employ(Connection conn, long id,byte category) {
        String sql = "update employee set type=? where id=?";
        Object []params = {category,id};
        return  DbUtil.update(conn,sql,params);
    }

}
