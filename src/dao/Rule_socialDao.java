package dao;

import bean.Notice;
import bean.Rule_social;
import database.DaoQueryListResult;
import database.DbUtil;
import database.QueryParameter;

import java.sql.Connection;

public class Rule_socialDao {

    /**
     * 获取社保列表
     * @param conn 数据库连接
     *@param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryListResult getRule_socialList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"rule_social",param, Rule_social.class);
    }
}
