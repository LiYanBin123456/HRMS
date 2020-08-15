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

@WebServlet(name="RuleFundServlet",urlPatterns = "/ruleFund")//浏览器发出请求的地址
public class RuleFundService extends HttpServlet {
    private static RuleFundDao ruleFundDao = new RuleFundDao();
    private static QueryConditions queryConditions = new QueryConditions();
    private static QueryParameter queryParameter = new QueryParameter();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin","*");//允许所有域名访问
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result;
        Connection conn = RecruitConnUtil.getConnection();

        String op = request.getParameter("op");
        switch (op) {
            case "getList"://获取公积金规则列表
                result = getList(conn,request);
                break;
            case "get"://获取指定公积金规则信息
                result = get(conn,request);
                break;
            case "update"://修改公积金规则信息
                result = update(conn,request);
                break;
            case "insert"://添加公积金规则信息
                result = insert(conn,request);
                break;
            case "delete"://删除公积金规则信息
                result = delete(conn,request);
                break;
            case "getLast"://根据城市模糊查询获取公积金规则信息
                result = getLast(conn,request);
                break;
            default:
                result = "非法操作";
        }
        RecruitConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //获取公积金规则列表
    public String getList(Connection conn, HttpServletRequest request){
        queryParameter = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult res = ruleFundDao.getList(conn, queryParameter);
        return JSONObject.toJSONString(res);
    }

    //获取指定公积金规则信息
    public String get(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        queryConditions.add("id", "=", id);
        DaoQueryResult res = ruleFundDao.get(conn, queryConditions);
        return JSONObject.toJSONString(res);
    }

    //修改公积金规则信息
    public String update(Connection conn, HttpServletRequest request) {
        RuleFund ruleFund = JSON.parseObject(request.getParameter("ruleFund"), RuleFund.class);
        DaoUpdateResult res = ruleFundDao.update(conn, ruleFund);
        return JSONObject.toJSONString(res);
    }

    //添加公积金规则信息
    public String insert(Connection conn, HttpServletRequest request) {
        RuleFund ruleFund = JSON.parseObject(request.getParameter("ruleFund"), RuleFund.class);
        DaoUpdateResult res = ruleFundDao.insert(conn, ruleFund);
        return JSONObject.toJSONString(res);
    }

    //删除公积金规则信息
    public String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        queryConditions.add("id","=",id);
        DaoUpdateResult res = ruleFundDao.delete(conn, queryConditions);
        return JSONObject.toJSONString(res);
    }

    //根据城市模糊查询获取公积金规则信息
    private String getLast(Connection conn, HttpServletRequest request) {
        queryParameter = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        queryParameter.addCondition("city","like",queryParameter.conditions.extra);
        DaoQueryListResult res = ruleFundDao.getList(conn, queryParameter);
        return JSONObject.toJSONString(res);
    }
}
