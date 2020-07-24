package service.admin;


import bean.admin.Notice;
import dao.admin.NoticeDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class NoticeService {
    private NoticeDao noticeDao = new NoticeDao();

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return noticeDao.getList(conn,param);
    }

    public DaoQueryResult get(Connection conn, long id) {
        return noticeDao.get(conn,id);
    }

    public DaoUpdateResult update(Connection conn, Notice notice) {
        return noticeDao.update(conn,notice);
    }

    public DaoUpdateResult insert(Connection conn, Notice notice) {
        return noticeDao.insert(conn,notice);
    }

    public DaoUpdateResult delete(Connection conn, long id) {
        return  noticeDao.delete(conn,id);
    }
}
