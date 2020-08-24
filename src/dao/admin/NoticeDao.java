package dao.admin;

import bean.admin.Notice;
import database.*;

import java.sql.Connection;

public class NoticeDao {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("title","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"notice",param, Notice.class);
    }

    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"notice",conditions,Notice.class);
    }

    public static DaoUpdateResult update(Connection conn, Notice n) {
        String sql = "update notice set title=?,brief=?,content=?,publisher=?,date=? where id=?";
        Object []params = {n.getTitle(),n.getBrief(),n.getContent(),n.getPublisher(),n.getDate(),n.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult insert(Connection conn, Notice n) {
        String sql = "insert notice (title,brief,content,publisher,date) values (?,?,?,?,?)";
        Object []params = {n.getTitle(),n.getBrief(),n.getContent(),n.getPublisher(),n.getDate()};
        return  DbUtil.insert(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"notice",conditions);
    }
}
