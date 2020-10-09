package service.admin;

import bean.admin.Account;
import com.alibaba.fastjson.JSONObject;
import dao.admin.AccountDao;
import database.*;

import javax.servlet.http.HttpSession;
import java.sql.Connection;

public class AccountService {
    public static String login(Connection conn,String username,String password,HttpSession session){
        Account account = (Account) AccountDao.get(conn,username).data;
        if(account == null) {
            return DaoResult.fail("请核对账号密码！");
        }
        JSONObject json = new JSONObject();
        if(account.getPassword().equals(password)){
            session.setAttribute("id", account.getId());
            session.setAttribute("nickname", account.getNickname());
            session.setAttribute("role", account.getRole());
            session.setAttribute("rid", account.getRid());
            session.setAttribute("permission", account.getPermission());
            json.put("success",true);
            json.put("role",account.getRole());
        }else {
            json.put("success",false);
            json.put("msg","密码错误");
        }
        return json.toJSONString();
    }

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return AccountDao.getList(conn,param);
    }

    public static String get(Connection conn, long id) {
        DaoQueryResult res = AccountDao.get(conn,id);
        Account account = (Account) res.data;
        account.setPassword("");
        return JSONObject.toJSONString(res);
    }

    public static DaoUpdateResult update(Connection conn, Account account) {
        return AccountDao.update(conn,account);
    }

    public static String insert(Connection conn, Account account) {
        DaoExistResult res1 = AccountDao.isExist(conn,account.getUsername());
        if(res1.exist){
            return DaoResult.fail("该账号已经存在");
        }
        DaoUpdateResult res2 = AccountDao.insert(conn,account);
        return JSONObject.toJSONString(res2);
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        return AccountDao.delete(conn,id);
    }

    public static DaoUpdateResult permit(Connection conn, long id,int permission) {
        return AccountDao.permit(conn,id,permission);
    }
}
