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

    //删除潜在客户
    public static DaoUpdateResult deletePot(Connection conn, long id,int type) {
        DaoUpdateResult res;
        res= CooperationDao.delete(conn,id);
        //删除潜在客户时，也要删除客户的财务服务信息表
        if(res.success){
            FinanceDao.delete(conn,id,type);
        }
        return res;
    }

    //添加客户
    public static DaoUpdateResult insert(Cooperation cooperation, Connection conn){
        return CooperationDao.insert(conn,cooperation);
    }

    //删除合作客户
    public static DaoUpdateResult deleteCoop(Connection conn, long id) {
        //修改状态为潜在客户
        int status = 0;
        return CooperationDao.updateStatus(conn,id,status);
    }

    public static DaoUpdateResult allocateAdmin(Connection conn, String[] cids, long aid) {
        return CooperationDao.allocateAdmin(conn,cids,aid);
    }
}
