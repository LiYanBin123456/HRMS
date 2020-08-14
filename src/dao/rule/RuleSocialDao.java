package dao.rule;


import bean.rule.RuleSocial;
import database.*;

import java.sql.Connection;

public class RuleSocialDao {

    /**
     * 获取社保规则集合
     * @param conn 连接数据库
     * @param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn, "rule_social", param, RuleSocial.class);
    }

    /**
     * 获取指定的社保规则
     * @param conn 连接数据库
     * @param conditions 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryResult get(Connection conn, QueryConditions conditions) {
        return DbUtil.get(conn, "rule_social", conditions, RuleSocial.class);

    }

    /**
     * 修改社保规则
     * @param conn 连接数据库
     * @param rule 社保规则对象
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoUpdateResult update(Connection conn, RuleSocial rule) {
        String sql = "update rule_social set city=?, start=?, base=?, per1=?, per2=?, per3=?, extra=?, per4=?, per5=? where id=?";
        Object[] params = {rule.getCity(), rule.getStart(), rule.getBase(), rule.getPer1(), rule.getPer2(), rule.getPer3(), rule.getExtra(), rule.getPer4(), rule.getPer5(), rule.getId()};
        return DbUtil.update(conn, sql, params);
    }

    /**
     * 添加医社保规则
     * @param conn 连接数据库
     * @param rule 社保规则对象
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoUpdateResult insert(Connection conn, RuleSocial rule) {
        String sql = "insert into rule_social (city , start, base, per1, per2, per3, extra, per4, per5) values (?,?,?,?,?,?,?,?,?)";
        Object[] params = {rule.getCity(), rule.getStart(), rule.getBase(), rule.getPer1(), rule.getPer2(), rule.getPer3(), rule.getExtra(), rule.getPer4(), rule.getPer5()};
        return DbUtil.update(conn, sql, params);
    }

    /**
     * 删除指定社保规则
     * @param conn 连接数据库
     * @param queryConditions 删除参数
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoUpdateResult delete(Connection conn, QueryConditions queryConditions) {
        return DbUtil.delete(conn,"rule_social", queryConditions);
    }
}
