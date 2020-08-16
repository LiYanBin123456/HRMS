package service.rule;

import bean.rule.RuleMedicare;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.rule.RuleMedicareDao;
import database.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.List;

public class RuleMedicareService {
    //获取医保规则列表
    public static String getList(Connection conn, HttpServletRequest request){
        QueryParameter queryParameter = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult res = RuleMedicareDao.getList(conn, queryParameter);
        return JSONObject.toJSONString(res);
    }

    //获取指定医保规则信息
    public static String get(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        QueryConditions conditions = new QueryConditions();
        conditions.add("id", "=", id);
        DaoQueryResult res = RuleMedicareDao.get(conn, conditions);
        return JSONObject.toJSONString(res);
    }

    //修改医保规则信息
    public static String update(Connection conn, HttpServletRequest request) {
        RuleMedicare rule = JSON.parseObject(request.getParameter("rule"), RuleMedicare.class);
        DaoUpdateResult res = RuleMedicareDao.update(conn, rule);
        return JSONObject.toJSONString(res);
    }

    //添加医保规则信息
    public static String insert(Connection conn, HttpServletRequest request) {
        RuleMedicare rule = JSON.parseObject(request.getParameter("rule"), RuleMedicare.class);
        DaoUpdateResult res = RuleMedicareDao.insert(conn, rule);
        return JSONObject.toJSONString(res);
    }

    //删除医保规则信息
    public static String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        DaoUpdateResult res = RuleMedicareDao.delete(conn, conditions);
        return JSONObject.toJSONString(res);
    }

    //根据城市模糊查询获取医保规则信息
    public static String getLast(Connection conn, HttpServletRequest request) {
        String city = request.getParameter("city");
        QueryParameter param = new QueryParameter();
        param.addCondition("city","=",city);
        param.order.set(true,"start",false);
        param.pagination.set(true,1,1);
        DaoQueryListResult res1 = RuleMedicareDao.getList(conn, param);
        DaoQueryResult res2 = new DaoQueryResult();
        if(res1.success) {
            List<RuleMedicare> rules = (List<RuleMedicare>) res1.rows;
            if (rules.size() > 0) {
                res2.success = true;
                res2.data = rules.get(0);
                return JSONObject.toJSONString(res2);
            }
        }

        res2.success = false;
        res2.msg = "获取医保规则失败";
        return JSONObject.toJSONString(res2);
    }
}