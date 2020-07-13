package servlet.admin;

import bean.admin.Fund;
import com.alibaba.fastjson.JSONObject;
import database.*;
import service.admin.FundService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(urlPatterns = "/fund")
public class FundServlet extends HttpServlet {
    private FundService fundService = new FundService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");

        switch (op) {
            case "getFunds"://获取所有公积金清单
                result = getFunds(conn,request);
                break;
            case "getFund"://获取所有公积金清单
                result = getFund(conn,request);
                break;
            case "updateFund"://获取所有公积金清单
                result = updateFund(conn,request);
                break;
            case "insertFund"://获取所有公积金清单
                result = insertFund(conn,request);
                break;
            case "deleteFund"://获取所有公积金清单
                result = deleteFund(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    //获取求职者列表
    private String getFunds(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res =fundService.getFundList(conn,parameter);
        return JSONObject.toJSONString(res);
    }

    private String getFund(Connection conn,HttpServletRequest request){
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res =fundService.getFund(conn,id);
        return JSONObject.toJSONString(res);
    }

    private String updateFund(Connection conn,HttpServletRequest request){
        Fund fund = JSONObject.parseObject(request.getParameter("fund"), Fund.class);
        DaoUpdateResult res = fundService.updateFund(conn,fund);
        return JSONObject.toJSONString(res);
    }
    private String insertFund(Connection conn,HttpServletRequest request){
        Fund fund = JSONObject.parseObject(request.getParameter("fund"), Fund.class);
        DaoUpdateResult res=fundService.insertFund(conn,fund);
        return JSONObject.toJSONString(res);
    }

    private String deleteFund(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res= fundService.deleteFund(conn,id);
        return JSONObject.toJSONString(res);
    }
}
