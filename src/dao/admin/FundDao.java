package dao.admin;

import bean.admin.Fund;
import database.*;

import java.sql.Connection;

public class FundDao {
    /**
     * 获取公积金列表
     * @param conn 数据库连接
     *@param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryListResult getFundList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"fund",param, Fund.class);
    }

    /**
     * 按id查询公积金
     * @param conn
     * @param id
     * @return
     */
    public DaoQueryResult getFund(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"fund",conditions,Fund.class);
    }

    public DaoUpdateResult updateFund(Connection conn, Fund f) {
        String sql = "update fund set city=?,start=?,base=?,min=?,max=?,per1=?,per2=? where id=?";
        Object []params = { f.getCity(),f.getStart(),f.getBase(),f.getMin(),f.getMax(),f.getPer1(),f.getPer2(),f.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    public DaoUpdateResult insertFund(Connection conn, Fund f) {
        String sql = "insert into fund(city,start,base,min,max,per1,per2) values (?,?,?,?,?,?,?)";
        Object []params = {f.getCity(),f.getStart(),f.getBase(),f.getMin(),f.getMax(),f.getPer1(),f.getPer2()};
        return  DbUtil.insert(conn,sql,params);
    }
}
