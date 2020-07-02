package dao.admin;

import bean.admin.Rule_medical;
import database.DaoQueryListResult;
import database.DbUtil;
import database.QueryParameter;

import java.sql.Connection;

public class Rule_medicalDao {

    /**
     * 获取医保列表
     * @param conn 数据库连接
     *@param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryListResult getRule_medicalList(Connection conn, QueryParameter param){
        //sql 自定义要查询的字段
        String sql = "id,city,`start`,base,concat(round(per1 *100,2),'%')AS per1,concat(round(per2 *100,2),'%')AS per2,concat(round(per3 *100,2),'%')AS per3,concat(round(per4 *100,2),'%')AS per4";
        return DbUtil.getList(conn,sql,"rule_medical",param, Rule_medical.class);
    }
}
