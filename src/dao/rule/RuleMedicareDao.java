package dao.rule;

import bean.rule.RuleMedicare;
import database.*;

import java.sql.Connection;

public class RuleMedicareDao {

    /**
     * 获取医保规则集合
     * @param conn 连接数据库
     * @param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn, "rule_medicare", param, RuleMedicare.class);
    }

    /**
     * 获取指定的医保规则
     * @param conn 连接数据库
     * @param conditions 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryResult get(Connection conn, QueryConditions conditions) {
        return DbUtil.get(conn, "rule_medicare", conditions, RuleMedicare.class);
    }

    /**
     * 修改医保规则
     * @param conn 连接数据库
     * @param rule 医保规则对象
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoUpdateResult update(Connection conn, RuleMedicare rule) {
        String sql = "update rule_medicare set city=?, start=?, base=?, per1=?, per2=?, per3=?, fin1=?, fin2=? where id=?";
        Object[] params = {rule.getCity(), rule.getStart(), rule.getBase(), rule.getPer1(), rule.getPer2(), rule.getPer3(), rule.getFin1(), rule.getFin2(), rule.getId()};
        return DbUtil.update(conn, sql, params);
    }

    /**
     * 添加医保规则
     * @param conn 连接数据库
     * @param rule 医保规则对象
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoUpdateResult insert(Connection conn, RuleMedicare rule) {
        String sql = "insert into rule_medicare (city , start, base, per1, per2, per3, fin1, fin2) values (?,?,?,?,?,?,?,?)";
        Object[] params = {rule.getCity(), rule.getStart(), rule.getBase(), rule.getPer1(), rule.getPer2(), rule.getPer3(), rule.getFin1(), rule.getFin2()};
        return DbUtil.insert(conn, sql, params);
    }

    /**
     * 删除指定医保规则
     * @param conn 连接数据库
     * @param queryConditions 删除参数
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoUpdateResult delete(Connection conn, QueryConditions queryConditions) {
        return DbUtil.delete(conn,"rule_medicare", queryConditions);
    }
}
