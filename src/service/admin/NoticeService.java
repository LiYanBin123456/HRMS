package service.admin;


import bean.admin.Notice;
import dao.admin.NoticeDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class NoticeService {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return NoticeDao.getList(conn,param);
    }

    public static DaoQueryResult get(Connection conn, long id) {
        return NoticeDao.get(conn,id);
    }

    public static DaoUpdateResult update(Connection conn, Notice notice) {
        return NoticeDao.update(conn,notice);
    }

    public static DaoUpdateResult insert(Connection conn, Notice notice) {
        return NoticeDao.insert(conn,notice);
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        return  NoticeDao.delete(conn,id);
    }
}
