package dao.employee;

import bean.employee.Deduct;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryConditions;

import java.sql.Connection;

//个人专项扣除dao层
public class DeductDao {
    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.get(conn,"deduct",conditions,Deduct.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, Deduct d) {
            String sql = "update deduct set deduct1=?,deduct2=?,deduct3=?,deduct4=?,deduct5=?,deduct6=? where eid=? ";
            Object []params = {d.getDeduct1(),d.getDeduct2(),d.getDeduct3(),d.getDeduct4(),d.getDeduct5(),d.getDeduct6(),d.getEid()};
        return  DbUtil.update(conn,sql,params);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn,  Deduct d) {
        String sql = "insert deduct (eid,deduct1,deduct2,deduct3,deduct4,deduct5,deduct6) values (?,?,?,?,?,?,?)";
        Object []params = {d.getEid(),d.getDeduct1(),d.getDeduct2(),d.getDeduct3(),d.getDeduct4(),d.getDeduct5(),d.getDeduct6()};
        return  DbUtil.insert(conn,sql,params);
    }
}
