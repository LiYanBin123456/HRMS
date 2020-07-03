package dao.admin;




import bean.admin.Client;
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


    public DaoQueryResult getClient(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return  DbUtil.get(conn,"client",conditions, Client.class);
    }

    /**
     * 修改客户信息
     * @param conn 数据库连接
     * @param c 求职者信息
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoUpdateResult updateClient(Connection conn, Client c){
        String sql = "update client set name=?,address=?,contact=?,phone=?,wx=?,qq=?,intro=? where id=?";
        Object []params = {c.getName(),c.getAddress(), c.getContact(), c.getPhone(),c.getWx(),c.getQq(),c.getIntro(),c.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    /**
     * 插入潜在客户
     * @param conn
     * @param c
     * @return
     */
    public DaoUpdateResult insertClient(Connection conn, Client c) {
        String sql = "insert into client (name,address,contact,phone,wx,qq,intro,status,type) values (?,?,?,?,?,?,?,?,?)";
        Object []params = {c.getName(),c.getAddress(), c.getContact(), c.getPhone(),c.getWx(),c.getQq(),c.getIntro(),0,0};
        return DbUtil.insert(conn,sql,params);
    }
}
