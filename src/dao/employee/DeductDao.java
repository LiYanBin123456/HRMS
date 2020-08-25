package dao.employee;

import bean.employee.Deduct;
import bean.employee.ViewDeduct;
import database.*;

import java.sql.Connection;
import java.util.List;

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

    //获取个税扣除视图列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"view_deduct",param, ViewDeduct.class);
    }

    //删除
    public static DaoUpdateResult delete(Connection conn,long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.delete(conn,"deduct",conditions);
    }

    //判断是否已经存在
    public static DaoExistResult exist(Connection conn, long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
       return DbUtil.exist(conn,"deduct",conditions);
    }

    public static DaoUpdateResult importDeducts(Connection conn, List<Deduct> d) {
        String sql = "insert deduct (eid,deduct1,deduct2,deduct3,deduct4,deduct5,deduct6) values (?,?,?,?,?,?,?)";
        Object [][]params = new Object[d.size()][];
        for (int i = 0; i < d.size(); i++) {
            params[i] = new Object[]{d.get(i).getEid(),d.get(i).getDeduct1(),d.get(i).getDeduct2(),d.get(i).getDeduct3(),d.get(i).getDeduct4(),d.get(i).getDeduct5(),d.get(i).getDeduct6()};
        }
        return DbUtil.insertBatch(conn,sql,params);
    }
}
