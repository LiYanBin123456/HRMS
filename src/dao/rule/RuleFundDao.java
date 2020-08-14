package dao.rule;

import bean.rule.RuleFund;
import database.*;

import java.sql.Connection;

public class RuleFundDao {
    /**
     * 获取公积金规则集合
     * @param conn 连接数据库
     * @param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn, "rule_fund", param, RuleFund.class);
    }

    /**
     * 获取指定的公积金规则
     * @param conn 连接数据库
     * @param conditions 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryResult get(Connection conn, QueryConditions conditions) {
        return DbUtil.get(conn, "rule_fund", conditions, RuleFund.class);
    }


    /**
     * 修改公积金规则信息
     * @param conn 连接数据库
     * @param rule 公积金规则对象
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoUpdateResult update(Connection conn, RuleFund rule) {
        String sql = "update rule_fund set city=?, start=?, min=?, max=?, per1=?, per2=? where id=?";
        Object[] parmas = {rule.getCity(), rule.getStart(), rule.getMin(), rule.getMax(), rule.getPer1(), rule.getPer2(), rule.getId()};
        return DbUtil.update(conn, sql, parmas);
    }

    /**
     * 添加公积金规则信息
     * @param conn 连接数据库
     * @param rule 公积金规则信息对象
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoUpdateResult insert(Connection conn, RuleFund rule) {
        String sql = "insert into rule_fund (city, start, min, max, per1, per2) values (?,?,?,?,?,?)";
        Object[] params = {rule.getCity(), rule.getStart(), rule.getMin(), rule.getMax(), rule.getPer1(), rule.getPer2()};
        return DbUtil.insert(conn, sql, params);
    }

    /**
     * 删除指定公积金规则信息
     * @param conn 连接数据库
     * @param conditions 删除参数
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     *
     */
    public DaoUpdateResult delete(Connection conn, QueryConditions conditions) {
        return DbUtil.delete(conn,"rule_fund", conditions);
    }
}
