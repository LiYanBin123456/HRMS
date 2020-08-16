package servlet;

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



    private String insert(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        switch (category){
            case 0:
                return RuleMedicareService.insert(conn,request);
            case 1:
                return RuleSocialService.insert(conn,request);
            case 2:
                return RuleFundService.insert(conn,request);
            default:
                return "{\"success\":false,\"msg\":\"参数错误\"}";
        }
    }

    private String delete(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        switch (category){
            case 0:
                return RuleMedicareService.delete(conn,request);
            case 1:
                return RuleSocialService.delete(conn,request);
            case 2:
                return RuleFundService.delete(conn,request);
            default:
                return "{\"success\":false,\"msg\":\"参数错误\"}";
        }
    }

    private String update(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        switch (category){
            case 0:
                return RuleMedicareService.update(conn,request);
            case 1:
                return RuleSocialService.update(conn,request);
            case 2:
                return RuleFundService.update(conn,request);
            default:
                return "{\"success\":false,\"msg\":\"参数错误\"}";
        }
    }

    private String getList(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        switch (category){
            case 0:
                return RuleMedicareService.getList(conn,request);
            case 1:
                return RuleSocialService.getList(conn,request);
            case 2:
                return RuleFundService.getList(conn,request);
            default:
                return "{\"success\":false,\"msg\":\"参数错误\"}";
        }
    }

    private String get(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        switch (category){
            case 0:
                return RuleMedicareService.get(conn,request);
            case 1:
                return RuleSocialService.get(conn,request);
            case 2:
                return RuleFundService.get(conn,request);
            default:
                return "{\"success\":false,\"msg\":\"参数错误\"}";
        }
    }

    private String getLast(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        switch (category){
            case 0:
                return RuleMedicareService.getLast(conn,request);
            case 1:
                return RuleSocialService.getLast(conn,request);
            case 2:
                return RuleFundService.getLast(conn,request);
            default:
                return "{\"success\":false,\"msg\":\"参数错误\"}";
        }
    }



}
