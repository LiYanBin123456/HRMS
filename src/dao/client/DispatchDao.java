package dao.client;




import bean.client.Dispatch;
import database.*;
import java.sql.Connection;

public class DispatchDao {
    /**
     * 获取派遣单位客户列表
     * @param conn 数据库连接
     *@param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(name,contact)","like",param.conditions.extra);
            System.out.println("param.conditions.extra:"+param.conditions.extra);
        }
        return DbUtil.getList(conn,"dispatch",param, Dispatch.class);
    }


    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return  DbUtil.get(conn,"dispatch",conditions, Dispatch.class);
    }


    public static DaoUpdateResult update(Connection conn, Dispatch c){
        String sql = "update dispatch set rid=?,name=?,nickname=?,address=?,contact=?,phone=?,wx=?,qq=?,mail=?,intro=? where id=?";
        Object []params = {c.getRid(),c.getName(),c.getNickname(),c.getAddress(), c.getContact(), c.getPhone(),c.getWx(),c.getQq(),c.getMail(),c.getIntro(),c.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    /**
     * 修改派遣单位管理员
     * @param conn
     * @param id 派遣单位id
     * @param aid 管理员id
     * @return
     */
    public static DaoUpdateResult updateAdmin(Connection conn, long id,long aid){
        String sql = "update dispatch set aid=? where id=?";
        Object []params = {aid,id};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult insert(Connection conn, Dispatch c) {
        String sql = "insert into dispatch (aid,rid,name,nickname,address,contact,phone,wx,qq,mail,intro,type) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {c.getAid(),c.getRid(),c.getName(),c.getNickname(),c.getAddress(), c.getContact(), c.getPhone(),c.getWx(),c.getQq(),c.getMail(),c.getIntro(),c.getType()};
        return DbUtil.insert(conn,sql,params);
    }

    //删除客户
    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"dispatch",conditions);
    }


    //修改客户状态 合作或者潜在 0_潜在，1_合作，2_流失客户
    public  static DaoUpdateResult updateType(Connection conn, long id,byte type){
        String sql = "update dispatch set type=? where id=?";
        Object []params = {type,id};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }


    public static DaoUpdateResult allocateAdmin(Connection conn, String[] cids, long aid) {
        String sql = "update dispatch set aid=? where id=?";
        Object [][]params = new Object[cids.length][];
        for (int i = 0; i < cids.length; i++) {
            params[i] = new Object[]{aid,cids[i]};
        }
        return DbUtil.batch(conn,sql,params);
    }
}
