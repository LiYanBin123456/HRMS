package service.admin;

import bean.admin.Client;
import bean.admin.Notice;
import dao.admin.NoticeDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class NoticeService {
    private NoticeDao noticeDao = new NoticeDao();

    public DaoQueryListResult getNoticeList(Connection conn, QueryParameter param){
        return noticeDao.getNoticeList(conn,param);
    }

    public DaoQueryResult getNotice(Connection conn, long id) {
        return noticeDao.getNotice(conn,id);
    }

    public DaoUpdateResult updateNotice(Connection conn, Notice notice) {
        return noticeDao.updateNotice(conn,notice);
    }

    public DaoUpdateResult insertNotice(Connection conn, Notice notice) {
        return noticeDao.insertNotice(conn,notice);
    }
}
