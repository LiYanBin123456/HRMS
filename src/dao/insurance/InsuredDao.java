package dao.insurance;

import bean.employee.Employee;
import bean.employee.ViewEmployee;
import bean.insurance.Insured;
import database.*;

import java.sql.Connection;
import java.util.List;

public class InsuredDao {

    //增加
    public static DaoUpdateResult insert(Connection conn, Insured e) {
        String sql = "insert insured (cid,cardId,name,place,post,category) values (?,?,?,?,?,?)";
        Object []params = {e.getCid(),e.getCardId(),e.getName(),e.getPlace(),e.getPost(),e.getCategory()};
        return  DbUtil.insert(conn,sql,params);
    }

    //删除
    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"insured",conditions);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, Insured e) {
        String sql = "update insured set cardId=?,name=?,place=?,post=?,category=? where id=?";
        Object []params = {e.getCardId(),e.getName(),e.getPlace(),e.getPost(),e.getCategory(),e.getId()};
        return DbUtil.update(conn,sql,params);
    }

    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"insured",param,Insured.class);
    }
}
