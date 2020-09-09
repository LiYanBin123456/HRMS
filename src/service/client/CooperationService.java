package service.client;

import bean.client.Cooperation;
import dao.client.CooperationDao;
import dao.client.FinanceDao;
import database.DaoQueryListResult;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class CooperationService {
   //获取客户详情
    public static DaoQueryResult get(long id, Connection conn){
        return CooperationDao.get(conn,id);
    }

    //获取客户列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return CooperationDao.getList(conn,param);
    }

    //修改客户
    public static DaoUpdateResult update(Connection conn, Cooperation cooperation){
        return CooperationDao.update(conn,cooperation);
    }

    //添加客户
    public static DaoUpdateResult insert(Cooperation cooperation, Connection conn){
        return CooperationDao.insert(conn,cooperation);
    }

    //删除客户实质是修改状态
    public static DaoUpdateResult delete(Connection conn, long id, byte status) {
        //修改状态,合作客户修改为在客户，潜在客户修改为流失客户
        return CooperationDao.updateStatus(conn,id,status+1);
    }

    public static DaoUpdateResult allocateAdmin(Connection conn, String[] cids, long aid) {
        return CooperationDao.allocateAdmin(conn,cids,aid);
    }
}
