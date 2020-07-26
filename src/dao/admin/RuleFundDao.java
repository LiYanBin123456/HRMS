package dao.admin;

import bean.admin.RuleFund;
import database.*;

import java.sql.Connection;

public class RuleFundDao {
    /**
     * 获取公积金列表
     * @param conn 数据库连接
     *@param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryListResult getFundRules(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"rule_fund",param, RuleFund.class);
    }
    /**
     * 按id查询公积金
     * @param conn
     * @param id
     * @return
     */
    public DaoQueryResult getFundRule(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"rule_fund",conditions,RuleFund.class);
    }

    public DaoUpdateResult deleteFundRule(Connection conn, long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"rule_fund",conditions);
    }

    public DaoUpdateResult updateFundRule(Connection conn, RuleFund f) {
        String sql = "update rule_fund set city=?,start=?,base=?,min=?,max=?,per1=?,per2=? where id=?";
        Object []params = { f.getCity(),f.getStart(),f.getBase(),f.getMin(),f.getMax(),f.getPer1(),f.getPer2(),f.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    public DaoUpdateResult insertFundRule(Connection conn, RuleFund f) {
        String sql = "insert into rule_fund(city,start,base,min,max,per1,per2) values (?,?,?,?,?,?,?)";
        Object []params = {f.getCity(),f.getStart(),f.getBase(),f.getMin(),f.getMax(),f.getPer1(),f.getPer2()};
        return  DbUtil.insert(conn,sql,params);
    }
}
