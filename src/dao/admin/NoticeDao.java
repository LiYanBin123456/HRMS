package dao.admin;

import bean.admin.Notice;
import database.*;

import java.sql.Connection;

public class NoticeDao {
    /**
     * 获取公告列表
     * @param conn 数据库连接
     *@param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryListResult getNoticeList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"notice",param, Notice.class);
    }

    /**
     * 根据id查找公告
     * @param conn
     * @param id
     * @return
     */
    public DaoQueryResult getNotice(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"notice",conditions,Notice.class);
    }

    /**
     * 修改
     * @param conn
     * @param n
     * @return
     */
    public DaoUpdateResult updateNotice(Connection conn, Notice n) {
        String sql = "update notice set title=?,brief=?,content=?,publisher=?,date=? where id=?";
        Object []params = {n.getTitle(),n.getBrief(),n.getContent(),n.getPublisher(),n.getDate(),n.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    /**
     * 插入
     * @param conn
     * @param n
     * @return
     */
    public DaoUpdateResult insertNotice(Connection conn, Notice n) {
        String sql = "insert notice (title,brief,content,publisher,date) values (?,?,?,?,?)";
        Object []params = {n.getTitle(),n.getBrief(),n.getContent(),n.getPublisher(),n.getDate()};
        return  DbUtil.insert(conn,sql,params);
    }
}
