package servlet.admin;

import bean.admin.Rule_medical;
import bean.admin.Rule_social;
import com.alibaba.fastjson.JSONObject;
import database.*;
import service.admin.Rule_socialService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(urlPatterns ="/rule_social")
public class Rule_socialServlet extends HttpServlet {
    private Rule_socialService socialService = new Rule_socialService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");

        switch (op) {
            case "getSocials"://获取所有公积金清单
                result = getSocials(conn,request);
                break;
            case "getSocial"://获取所有公积金清单
                result = getSocial(conn,request);
                break;
            case "updateSocial"://获取所有公积金清单
                result = updateSocial(conn,request);
                break;
            case "insertSocial"://获取所有公积金清单
                result = insertSocial(conn,request);
                break;
            case "deleteSocial"://获取所有公积金清单
                result = deleteSocial(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private String getSocials(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        DaoQueryListResult res =socialService.getRule_socialList(conn,parameter);
        return JSONObject.toJSONString(res);
    }

    private String getSocial(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res= socialService.getSocial(conn,id);
        return JSONObject.toJSONString(res);
    }

    private String updateSocial(Connection conn, HttpServletRequest request) {
        Rule_social social = JSONObject.parseObject(request.getParameter("rule_social"), Rule_social.class);
        DaoUpdateResult res = socialService.updateSocial(conn,social);
        return JSONObject.toJSONString(res);
    }

    private String insertSocial(Connection conn, HttpServletRequest request) {
        Rule_social social = JSONObject.parseObject(request.getParameter("rule_social"), Rule_social.class);
        DaoUpdateResult res = socialService.insertSocial(conn,social);
        return JSONObject.toJSONString(res);
    }

    private String deleteSocial(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res= socialService.deleteSocial(conn,id);
        return JSONObject.toJSONString(res);
    }
}
