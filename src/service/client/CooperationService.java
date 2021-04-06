package service.client;

import bean.admin.Account;
import bean.client.Cooperation;
import com.alibaba.fastjson.JSONObject;
import dao.admin.AccountDao;
import dao.client.CooperationDao;
import dao.client.DispatchDao;
import dao.client.FinanceDao;
import database.*;

import java.sql.Connection;

public class CooperationService {
   //获取客户详情
    public static DaoQueryResult get(long id, Connection conn){
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return CooperationDao.get(conn,conditions);
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
    public static String insert(Cooperation cooperation, Connection conn){
        ConnUtil.closeAutoCommit(conn);
        Account account = new Account();
        account.setUsername(cooperation.getNickname());
        account.setNickname(cooperation.getNickname());
        account.setRole(Account.ROLE_COOPERATION);
        account.setPassword(cooperation.getPhone());
        account.setRid(cooperation.getId());
        account.setPermission(2251805182394367L);
        DaoUpdateResult res1 = CooperationDao.insert(conn,cooperation);
        DaoUpdateResult res2 = AccountDao.insert(conn,account);
        if(res1.success && res2.success){
            ConnUtil.commit(conn);
            return JSONObject.toJSONString(res1);
        }else{
            ConnUtil.rollback(conn);
            return DaoResult.fail("数据库操作错误");
        }
    }

    //删除客户实质是修改状态
    public static DaoUpdateResult delete(Connection conn, long id, byte type) {
        //修改状态,合作客户修改为在客户，潜在客户修改为流失客户
        return CooperationDao.updateType(conn,id,(byte)(type+1));
    }

    public static DaoUpdateResult allocateAdmin(Connection conn, String[] cids, long aid) {
        return CooperationDao.allocateAdmin(conn,cids,aid);
    }
}
