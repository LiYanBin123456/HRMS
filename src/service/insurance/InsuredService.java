package service.insurance;

import bean.insurance.Insured;
import com.alibaba.fastjson.JSONObject;
import dao.insurance.InsuredDao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import java.sql.Connection;

public class InsuredService {

    public static String insert(Connection conn, Insured insured) {
        DaoUpdateResult res = InsuredDao.insert(conn,insured);
        return JSONObject.toJSONString(res);
    }

    public static String delete(Connection conn, long id) {
        DaoUpdateResult res = InsuredDao.delete(conn,id);
        return JSONObject.toJSONString(res);
    }

    public static String update(Connection conn, Insured insured) {
        DaoUpdateResult res = InsuredDao.update(conn,insured);
        return JSONObject.toJSONString(res);
    }

    public static String getList(Connection conn, QueryParameter parameter) {
        DaoQueryListResult res = InsuredDao.getList(conn,parameter);
        return JSONObject.toJSONString(res);
    }
}
