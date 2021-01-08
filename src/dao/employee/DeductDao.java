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
            String sql = "update deduct set income=?,free=?,prepaid=?,deduct=?,deduct1=?,deduct2=?,deduct3=?,deduct4=?,deduct5=?,deduct6=? where eid=? ";
            Object []params = {d.getIncome(),d.getFree(),d.getPrepaid(),d.getDeduct(),d.getDeduct1(),d.getDeduct2(),d.getDeduct3(),d.getDeduct4(),d.getDeduct5(),d.getDeduct6(),d.getEid()};
        return  DbUtil.update(conn,sql,params);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn,  Deduct d) {
        String sql = "insert deduct (eid,income,free,prepaid,deduct,deduct1,deduct2,deduct3,deduct4,deduct5,deduct6) values (?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {d.getEid(),d.getIncome(),d.getFree(),d.getPrepaid(),d.getDeduct(),d.getDeduct1(),d.getDeduct2(),d.getDeduct3(),d.getDeduct4(),d.getDeduct5(),d.getDeduct6()};
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

    /**
     * 批量导入
     * @param conn
     * @param d
     * @return
     */
    public static DaoUpdateResult importDeducts(Connection conn, List<Deduct> d) {
        String sql = "insert deduct (eid,income,free,prepaid,deduct,deduct1,deduct2,deduct3,deduct4,deduct5,deduct6) values (?,?,?,?,?,?,?,?,?,?,?)";
        Object [][]params = new Object[d.size()][];
        for (int i = 0; i < d.size(); i++) {
            params[i] = new Object[]{d.get(i).getEid(),d.get(i).getIncome(),d.get(i).getFree(),d.get(i).getPrepaid(),d.get(i).getDeduct()
                    ,d.get(i).getDeduct1(),d.get(i).getDeduct2(),d.get(i).getDeduct3(),d.get(i).getDeduct4(),d.get(i).getDeduct5(),d.get(i).getDeduct6()};
        }
        return DbUtil.insertBatch(conn,sql,params);
    }

    /**
     * 批量修改员工个税
     * @param conn
     * @param d
     * @return
     */
    public static DaoUpdateResult updateDeducts(Connection conn, List<Deduct> d) {
        String sql = "update deduct set income=?,free=?,prepaid=?,deduct=?,deduct1=?,deduct2=?,deduct3=?,deduct4=?,deduct5=?,deduct6=? where eid=?";
        Object [][]params = new Object[d.size()][];
        for (int i = 0; i < d.size(); i++) {
            params[i] = new Object[]{d.get(i).getIncome(),d.get(i).getFree(),d.get(i).getPrepaid(),d.get(i).getDeduct()
                    ,d.get(i).getDeduct1(),d.get(i).getDeduct2(),d.get(i).getDeduct3(),d.get(i).getDeduct4(),d.get(i).getDeduct5(),d.get(i).getDeduct6(),d.get(i).getEid()};
        }
        return DbUtil.insertBatch(conn,sql,params);
    }

    public static DaoUpdateResult insertBatch(Connection conn, List<Deduct> p) {
        String sql = "insert deduct (eid,deduct,deduct1,deduct2,deduct3,deduct4,deduct5,deduct6,income,free,prepaid) values (?,?,?,?,?,?,?,?,?,?,?)";
        Object [][]params = new Object[p.size()][];
        for (int i = 0; i < p.size(); i++) {
            params[i] = new Object[]{
                    p.get(i).getEid(),p.get(i).getDeduct(),p.get(i).getDeduct1(),
                    p.get(i).getDeduct2(),p.get(i).getDeduct3(),p.get(i).getDeduct4(),
                    p.get(i).getDeduct5(),p.get(i).getDeduct6(),p.get(i).getIncome(),p.get(i).getFree(),p.get(i).getPrepaid()
            };
        }
        return DbUtil.insertBatch(conn,sql,params);
    }
}
