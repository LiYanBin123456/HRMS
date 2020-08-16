package service.rule;

import bean.rule.RuleSocial;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.rule.RuleSocialDao;
import database.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.List;

public class RuleSocialService{

    //获取社保规则列表
    public static String getList(Connection conn, HttpServletRequest request){
        QueryParameter queryParameter = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult res = RuleSocialDao.getList(conn, queryParameter);
        return JSONObject.toJSONString(res);
    }

    //获取指定社保规则信息
    public static String get(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        QueryConditions conditions = new QueryConditions();
        conditions.add("id", "=", id);
        DaoQueryResult res = RuleSocialDao.get(conn, conditions);
        return JSONObject.toJSONString(res);
    }

    //修改社保规则信息
    public static String update(Connection conn, HttpServletRequest request) {
        RuleSocial ruleSocial = JSON.parseObject(request.getParameter("ruleSocial"), RuleSocial.class);
        DaoUpdateResult res = RuleSocialDao.update(conn, ruleSocial);
        return JSONObject.toJSONString(res);
    }

    //添加社保规则信息
    public static String insert(Connection conn, HttpServletRequest request) {
        RuleSocial ruleSocial = JSON.parseObject(request.getParameter("ruleSocial"), RuleSocial.class);
        DaoUpdateResult res = RuleSocialDao.insert(conn, ruleSocial);
        return JSONObject.toJSONString(res);
    }

    //删除社保规则信息
    public static String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        DaoUpdateResult res = RuleSocialDao.delete(conn, conditions);
        return JSONObject.toJSONString(res);
    }

    //根据城市模糊查询获取社保规则信息
    public static String getLast(Connection conn, HttpServletRequest request) {
        String city = request.getParameter("city");
        QueryParameter param = new QueryParameter();
        param.addCondition("city","=",city);
        param.order.set(true,"start",false);
        param.pagination.set(true,1,1);
        DaoQueryListResult res1 = RuleSocialDao.getList(conn, param);
        DaoQueryResult res2 = new DaoQueryResult();
        if(res1.success) {
            List<RuleSocial> rules = (List<RuleSocial>) res1.rows;
            if (rules.size() > 0) {
                res2.success = true;
                res2.data = rules.get(0);
                return JSONObject.toJSONString(res2);
            }
        }

        res2.success = false;
        res2.msg = "获取社保规则失败";
        return JSONObject.toJSONString(res2);
    }
}