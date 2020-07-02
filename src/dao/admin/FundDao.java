package dao.admin;

import bean.admin.Fund;
import database.DaoQueryListResult;
import database.DbUtil;
import database.QueryParameter;

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
}
