package servlet;

import bean.rule.RuleFund;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import com.alibaba.fastjson.JSONObject;
import database.*;
import service.rule.RuleFundService;
import service.rule.RuleMedicareService;
import service.rule.RuleSocialService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(urlPatterns = "/rule")
public class RuleServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");
        String result;
        switch (op) {
            case "insert"://插入规则
                result = insert(conn,request);
                break;
            case "delete"://删除规则
                result = delete(conn,request);
                break;
            case "update"://修改规则
                result = update(conn,request);
                break;
            case "getList"://获取规则列表
                result = getList(conn,request);
                break;
            case "get"://获取规则详情
                result = get(conn,request);
                break;
            case "getLast"://获取最新规则详情
                result = getLast(conn,request);
                break;
            default:
                result = "{\"success\":false,\"msg\":\"参数错误\"}";
        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //插入规则
    private String insert(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        DaoUpdateResult res = null;
        switch (category){
            case 0:
                RuleMedicare ruleMedicare =JSONObject.parseObject(request.getParameter("rule"), RuleMedicare.class);
                res=RuleMedicareService.insert(conn,ruleMedicare);
                break;
            case 1:
                RuleSocial ruleSocial =JSONObject.parseObject(request.getParameter("rule"), RuleSocial.class);
                res=RuleSocialService.insert(conn,ruleSocial);
                break;
            case 2:
                RuleFund ruleFund =JSONObject.parseObject(request.getParameter("rule"), RuleFund.class);
                 res =RuleFundService.insert(conn,ruleFund);
                 break;
            default:
                res.success=false;
                res.msg="参数错误";
               break;
        }
        return  JSONObject.toJSONString(res);
    }

    //删除规则
    private String delete(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res = null;
        switch (category){
            case 0:
                res=RuleMedicareService.delete(conn,id);
                break;
            case 1:
                res=RuleSocialService.delete(conn,id);
                break;
            case 2:
                res =RuleFundService.delete(conn,id);
                break;
            default:
                res.success=false;
                res.msg="参数错误";
                break;
        }
        return  JSONObject.toJSONString(res);
    }

    //修改规则
    private String update(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        DaoUpdateResult res = null;
        switch (category){
            case 0:
                RuleMedicare ruleMedicare =JSONObject.parseObject(request.getParameter("rule"), RuleMedicare.class);
                res=RuleMedicareService.update(conn,ruleMedicare);
                break;
            case 1:
                RuleSocial ruleSocial =JSONObject.parseObject(request.getParameter("rule"), RuleSocial.class);
                res=RuleSocialService.update(conn,ruleSocial);
                break;
            case 2:
                RuleFund ruleFund =JSONObject.parseObject(request.getParameter("rule"), RuleFund.class);
                res =RuleFundService.update(conn,ruleFund);
                break;
            default:
                res.success=false;
                res.msg="参数错误";
                break;
        }
        return  JSONObject.toJSONString(res);
    }

    //获取规则列表
    private String getList(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        QueryParameter param =JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult res = null;
        switch (category){
            case 0:
                res=RuleMedicareService.getList(conn,param);
                break;
            case 1:
                res=RuleSocialService.getList(conn,param);
                break;
            case 2:
                res =RuleFundService.getList(conn,param);
                break;
            default:
                res.success=false;
                res.msg="参数错误";
                break;
        }
        return  JSONObject.toJSONString(res);
    }

    //获取规则详情
    private String get(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res = null;
        switch (category){
            case 0:
                res=RuleMedicareService.get(conn,id);
                break;
            case 1:
                res=RuleSocialService.get(conn,id);
                break;
            case 2:
                res =RuleFundService.get(conn,id);
                break;
            default:
                res.success=false;
                res.msg="参数错误";
                break;
        }
        return  JSONObject.toJSONString(res);
    }

    //获取最新规则详情
    private String getLast(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        String city = request.getParameter("city");
        DaoQueryResult res = null;
        switch (category){
            case 0:
                res=RuleMedicareService.getLast(conn,city);
                break;
            case 1:
                res=RuleSocialService.getLast(conn,city);
                break;
            case 2:
                res =RuleFundService.getLast(conn,city);
                break;
            default:
                res.success=false;
                res.msg="参数错误";
                break;
        }
        return  JSONObject.toJSONString(res);
    }



}
