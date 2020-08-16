package service.rule;

import bean.rule.RuleFund;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.rule.RuleFundDao;
import database.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

public class RuleFundService  {

    //获取公积金规则列表
    public static String getList(Connection conn, HttpServletRequest request){
        QueryParameter queryParameter = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult res = RuleFundDao.getList(conn, queryParameter);
        return JSONObject.toJSONString(res);
    }

    //获取指定公积金规则信息
    public static String get(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        QueryConditions queryConditions = new QueryConditions();
        queryConditions.add("id", "=", id);
        DaoQueryResult res = RuleFundDao.get(conn, queryConditions);
        return JSONObject.toJSONString(res);
    }

    //修改公积金规则信息
    public static String update(Connection conn, HttpServletRequest request) {
        RuleFund rule = JSON.parseObject(request.getParameter("rule"), RuleFund.class);
        DaoUpdateResult res = RuleFundDao.update(conn, rule);
        return JSONObject.toJSONString(res);
    }

    //添加公积金规则信息
    public static String insert(Connection conn, HttpServletRequest request) {
        RuleFund rule = JSON.parseObject(request.getParameter("rule"), RuleFund.class);
        DaoUpdateResult res = RuleFundDao.insert(conn, rule);
        return JSONObject.toJSONString(res);
    }

    //删除公积金规则信息
    public static String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        QueryConditions queryConditions = new QueryConditions();
        queryConditions.add("id","=",id);
        DaoUpdateResult res = RuleFundDao.delete(conn, queryConditions);
        return JSONObject.toJSONString(res);
    }

    //获取指定城市最新公积金规则
    public static String getLast(Connection conn, HttpServletRequest request) {
        String city = request.getParameter("city");
        QueryParameter param = new QueryParameter();
        param.addCondition("city","=",city);
        param.order.set(true,"start",false);
        param.pagination.set(true,1,1);
        DaoQueryListResult res1 = RuleFundDao.getList(conn, param);
        DaoQueryResult res2 = new DaoQueryResult();
        if(res1.success) {
            List<RuleFund> rules = (List<RuleFund>) res1.rows;
            if (rules.size() > 0) {
                res2.success = true;
                res2.data = rules.get(0);
                return JSONObject.toJSONString(res2);
            }
        }

        res2.success = false;
        res2.msg = "获取公积金规则失败";
        return JSONObject.toJSONString(res2);
    }
}
