package service.client;

import bean.admin.Account;
import bean.client.Dispatch;
import com.alibaba.fastjson.JSONObject;
import dao.admin.AccountDao;
import dao.client.DispatchDao;
import dao.client.FinanceDao;
import database.*;

import java.sql.Connection;

public class DispatchService {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DispatchDao.getList(conn,param);
    }

    public static DaoQueryResult get(Connection conn, long id) {
        return DispatchDao.get(conn,id);
    }

    public static DaoUpdateResult update(Connection conn, Dispatch dispatch) {
        return DispatchDao.update(conn, dispatch);
    }

    public static String insert(Connection conn, Dispatch dispatch) {
        ConnUtil.closeAutoCommit(conn);
//        Account account = new Account();
//        account.setUsername(dispatch.getNickname());
//        account.setNickname(dispatch.getNickname());
//        account.setRole(Account.ROLE_DISPATCH);
//        account.setPassword(dispatch.getPhone());
//        account.setRid(dispatch.getId());
//        account.setPermission(2251832025939967L);
        DaoUpdateResult res1 = DispatchDao.insert(conn,dispatch);
//        DaoUpdateResult res2 = AccountDao.insert(conn,account);
        if(res1.success){
            ConnUtil.commit(conn);
            return JSONObject.toJSONString(res1);
        }else{
            ConnUtil.rollback(conn);
            return DaoResult.fail("数据库操作错误");
        }
    }

    //删除客户实质是修改状态
    public static DaoUpdateResult delete(Connection conn, long id, byte type) {
        //修改状态，合作客户修改为潜在客户，潜在客户修改为流失客户
        return DispatchDao.updateType(conn,id,(byte)(type+1));
    }

    public static DaoUpdateResult allocateAdmin(Connection conn, String[] cids, long aid) {
        return DispatchDao.allocateAdmin(conn,cids,aid);
    }
}
