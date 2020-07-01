package service;

import dao.NoticeDao;
import database.DaoQueryListResult;
import database.QueryParameter;

import java.sql.Connection;

public class NoticeService {
    private NoticeDao noticeDao = new NoticeDao();

    public DaoQueryListResult getNoticeList(Connection conn, QueryParameter param){
        return noticeDao.getNoticeList(conn,param);
    }
}
