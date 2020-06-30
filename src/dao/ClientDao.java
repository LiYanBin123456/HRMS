package dao;



import bean.Client;
import database.*;
import java.sql.Connection;

public class ClientDao {
    /**
     * 获取客户列表
     * @param conn 数据库连接
     *@param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public  DaoQueryListResult getClientList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"client",param, Client.class);
    }
}
