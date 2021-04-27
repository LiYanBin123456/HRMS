package dao.employee;

import bean.employee.Employee;
import bean.employee.EmployeeExtra;
import bean.employee.ViewEmployee;
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

    //判断是否存在
    public static DaoExistResult exist(Connection conn, QueryConditions conditions) {
        return DbUtil.exist(conn,"employee",conditions);
    }

    //获取详情
    public static DaoQueryResult get(Connection conn, QueryConditions conditions) {
        return DbUtil.get(conn,"employee",conditions,Employee.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, Employee e) {
        //需要判断外键是否为0，为0就需要转换成null
        String cid = e.getCid()==0?null:String.valueOf(e.getCid());
        String sql = "update employee set cid=?,cardId=?,name=?,phone=?,degree=?,type=?,entry=?,status=?,department=?,post=?,category=?,price=? where id=?";
        Object []params = {cid,e.getCardId(),e.getName(),e.getPhone(),e.getDegree(),e.getType(),e.getEntry(),e.getStatus(),e.getDepartment(),e.getPost(),e.getCategory(),e.getPrice(),e.getId()};
        return DbUtil.update(conn,sql,params);
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
    public static DaoUpdateResult insertBatch(Connection conn, List<Employee> es) {
        String sql = "insert employee (did,cid,cardId,name,phone,degree,type,entry,status,department,post,category,price) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object [][]params = new Object[es.size()][];
        for (int i = 0; i < es.size(); i++) {
            //需要判断外键是否为0，为0就需要转换成null
            String cid = es.get(i).getCid()==0?null:String.valueOf(es.get(i).getCid());

            params[i] = new Object[]{es.get(i).getDid(),cid,es.get(i).getCardId(),es.get(i).getName(),es.get(i).getPhone(),
                    es.get(i).getDegree(),es.get(i).getType(),es.get(i).getEntry(),es.get(i).getStatus(),es.get(i).getDepartment(),es.get(i).getPost(),
                    es.get(i).getCategory(),es.get(i).getPrice()
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
        String sql = String.format("update employee set  cid = %S ,type=? where id=?",null);
        Object []params = {category,id};
        return  DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult updateStatus(Connection conn, long id, byte status) {
        String sql = "update employee set status=? where id=?";
        Object []params = {status,id};
        return  DbUtil.update(conn,sql,params);
    }
}
