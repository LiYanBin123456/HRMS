package dao.rule;

import bean.rule.RuleFund;
import bean.rule.RuleSocial;
import database.*;

import java.sql.Connection;

public class RuleFundDao {
    /**
     * 获取公积金规则集合
     * @param conn 连接数据库
     * @param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            //根据地市模糊查询
            param.addCondition("city","like",param.conditions.extra);
        }
        return DbUtil.getList(conn, "rule_fund", param, RuleFund.class);
    }


    /**
     * 获取公积金规则
     * @param conn
     * @param id 指定id
     * @return
     */
    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id", "=", id);
        return DbUtil.get(conn, "rule_fund", conditions,RuleFund.class);
    }


    /**
     * 修改公积金规则信息
     * @param conn 连接数据库
     * @param rule 公积金规则对象
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoUpdateResult update(Connection conn, RuleFund rule) {
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
    public static DaoUpdateResult insert(Connection conn, RuleFund rule) {
        String sql = "insert into rule_fund (city, start, min, max, per1, per2) values (?,?,?,?,?,?)";
        Object[] params = {rule.getCity(), rule.getStart(), rule.getMin(), rule.getMax(), rule.getPer1(), rule.getPer2()};
        return DbUtil.insert(conn, sql, params);
    }


    /**
     * 删除公积金规则
     * @param conn
     * @param id 公积金id
     * @return
     */
    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id", "=", id);
        return DbUtil.delete(conn, "rule_fund", conditions);
    }

    /**
     * 根据地市获取最新规则，按照生效时间排序
     * @param conn
     * @param city 地市代码
     * @return
     */
    public static DaoQueryResult getLast(Connection conn ,String city){
        QueryConditions conditions = new QueryConditions();
        conditions.add("city", "=", city);
        String order = " ORDER BY start DESC limit 1";
        return DbUtil.getLast(conn, "rule_fund", conditions,RuleFund.class,order);
    }
}
