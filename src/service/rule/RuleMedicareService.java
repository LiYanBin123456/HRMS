package service.rule;

import bean.rule.RuleMedicare;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.rule.RuleMedicareDao;
import database.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(name="RuleMedicareService",urlPatterns = "/ruleMedical")//浏览器发出请求的地址
public class RuleMedicareService extends HttpServlet {
    private static RuleMedicareDao ruleMedicareDao = new RuleMedicareDao();
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
            case "getList"://获取医保规则列表
                result = getList(conn,request);
                break;
            case "get"://获取指定医保规则信息
                result = get(conn,request);
                break;
            case "update"://修改医保规则信息
                result = update(conn,request);
                break;
            case "insert"://添加医保规则信息
                result = insert(conn,request);
                break;
            case "delete"://删除医保规则信息
                result = delete(conn,request);
                break;
            case "getLast"://根据城市模糊查询获取医保规则信息
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

    //获取医保规则列表
    public String getList(Connection conn, HttpServletRequest request){
        queryParameter = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult res = ruleMedicareDao.getList(conn, queryParameter);
        return JSONObject.toJSONString(res);
    }

    //获取指定医保规则信息
    public String get(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        queryConditions.add("id", "=", id);
        DaoQueryResult res = ruleMedicareDao.get(conn, queryConditions);
        return JSONObject.toJSONString(res);
    }

    //修改医保规则信息
    public String update(Connection conn, HttpServletRequest request) {
        RuleMedicare ruleMedicare = JSON.parseObject(request.getParameter("ruleMedicare"), RuleMedicare.class);
        DaoUpdateResult res = ruleMedicareDao.update(conn, ruleMedicare);
        return JSONObject.toJSONString(res);
    }

    //添加医保规则信息
    public String insert(Connection conn, HttpServletRequest request) {
        RuleMedicare ruleMedicare = JSON.parseObject(request.getParameter("ruleMedicare"), RuleMedicare.class);
        DaoUpdateResult res = ruleMedicareDao.insert(conn, ruleMedicare);
        return JSONObject.toJSONString(res);
    }

    //删除医保规则信息
    public String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        queryConditions.add("id","=",id);
        DaoUpdateResult res = ruleMedicareDao.delete(conn, queryConditions);
        return JSONObject.toJSONString(res);
    }

    //根据城市模糊查询获取医保规则信息
    private String getLast(Connection conn, HttpServletRequest request) {
        queryParameter = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        queryParameter.addCondition("city","like",queryParameter.conditions.extra);
        DaoQueryListResult res = ruleMedicareDao.getList(conn, queryParameter);
        return JSONObject.toJSONString(res);
    }
}