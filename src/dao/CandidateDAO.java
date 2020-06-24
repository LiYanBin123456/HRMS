package dao;

import bean.Candidate;
import database.*;

import java.sql.Connection;

public class CandidateDAO {
    /**
     * 添加求职者信息
     * @param conn 数据库连接
     * @param c 求职者信息
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoUpdateResult insert(Connection conn, Candidate c){
        String sql = "insert into candidate(id,name,sex,birthday,degree,phone,years,modtime)values(?,?,?,?,?,?,?,now())";
        Object []params = {c.getId(),c.getName(),c.getSex(),c.getBirthday(),c.getDegree(),c.getPhone(),c.getYears()};
        return DbUtil.update(conn,sql,params);
    }

    /**
     * 修改求职者信息
     * @param conn 数据库连接
     * @param c 求职者信息
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoUpdateResult update(Connection conn, Candidate c){
        String sql = "update candidate set name=?,sex=?,birthday=?,origin=?,address=?,degree=?,phone=?,years=?,mail=?,prepare=?,state=? where id=?";
        Object []params = {c.getName(),c.getSex(), c.getBirthday(), c.getOrigin(),c.getAddress(),c.getDegree(),c.getPhone(),c.getYears(),c.getMail(),c.getPrepare(),c.getState(),c.getId()};
        return DbUtil.update(conn,sql,params);
    }

    /**
     * 查询简历
     * @param conn 数据库连接
     * @param cid 求职者编号
     * @return 查询结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoQueryResult get(Connection conn, long cid){
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",cid);
        return  DbUtil.get(conn,"candidate",conditions, Candidate.class);
    }

    /**
    * 获取求职者列表
    * @param conn 数据库连接
     *@param param 查询参数
    * @return 检索结果，格式："{success:true,msg:"",effects:1}"
    */
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"candidate",param, Candidate.class);
    }
}
