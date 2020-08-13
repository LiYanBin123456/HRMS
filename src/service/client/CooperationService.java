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
    private CooperationDao cooperationDao = new CooperationDao();
    private FinanceDao financeDao = new FinanceDao();
   //获取客户详情
    public DaoQueryResult get(long id, Connection conn){
        return cooperationDao.get(conn,id);
    }

    //获取客户列表
    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        return cooperationDao.getList(conn,param);
    }

    //修改客户
    public DaoUpdateResult update(Connection conn, Cooperation cooperation){
        return cooperationDao.update(conn,cooperation);
    }

    //删除潜在客户
    public DaoUpdateResult deletePot(Connection conn, long id,int type) {
        DaoUpdateResult res;
        res= cooperationDao.delete(conn,id);
        //删除潜在客户时，也要删除客户的财务服务信息表
        if(res.success){
            financeDao.delete(conn,id,type);
        }
        return res;
    }

    //添加客户
    public DaoUpdateResult insert(Cooperation cooperation, Connection conn){
        return cooperationDao.insert(conn,cooperation);
    }

    //删除合作客户
    public DaoUpdateResult deleteCoop(Connection conn, long id) {
        //修改状态为潜在客户
        int status = 0;
        return cooperationDao.updateStatus(conn,id,status);
    }
}
