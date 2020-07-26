package dao.admin;


import bean.admin.RuleSocial;
import database.*;

import java.sql.Connection;

public class RuleSocialDao {

    /**
     * 获取社保列表
     *
     * @param conn  数据库连接
     * @param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryListResult getSocialRules(Connection conn, QueryParameter param) {
        return DbUtil.getList(conn, "rule_social", param, RuleSocial.class);
    }

    public DaoQueryResult getSocialRule(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id", "=", id);
        return DbUtil.get(conn, "rule_social", conditions, RuleSocial.class);

    }

    public DaoUpdateResult updateSocialRule(Connection conn, RuleSocial s) {
        String sql = "update rule_social set city = ?,start = ?,base=?,per1=?,per2=?,per3=?,extra=?,per4=?,per5=? where id=?";
        Object[] param = {s.getCity(), s.getStart(), s.getBase(), s.getPer1(), s.getPer2(), s.getPer3(), s.getExtra(),s.getPer4(),s.getPer5(), s.getId()};
        return DbUtil.update(conn, sql, param);
    }

    public DaoUpdateResult insertSocialRule(Connection conn, RuleSocial s) {
        String sql = "insert rule_social(city ,start ,base,per1,per2,per3,extra,per4,per5) values (?,?,?,?,?,?,?,?,?)";
        Object[] param = {s.getCity(), s.getStart(), s.getBase(), s.getPer1(), s.getPer2(), s.getPer3(), s.getExtra(),s.getPer4(),s.getPer5()};
        return DbUtil.insert(conn, sql, param);
    }

    public DaoUpdateResult deleteSocialRule(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id", "=", id);
        return DbUtil.delete(conn,"rule_social",conditions);
    }
}
