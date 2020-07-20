package servlet.admin;

import bean.admin.Finance;
import com.alibaba.fastjson.JSONObject;
import database.*;
import service.admin.FinanceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(name = "FinanceServlet" ,urlPatterns = "/finance")
public class FinanceServlet extends HttpServlet {
    private FinanceService financeService = new FinanceService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");

        switch (op) {
            case "getFinance"://根据客户id获取财务信息
                result = getFinance(conn,request);
                break;
            case "updateFinance"://根据客户id修改财务信息
                result = updateFinance(conn,request);
                break;
            case "insertFinance"://根据客户id添加一个财务信息
                result = insertFinance(conn,request);
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

    private String getFinance(Connection conn, HttpServletRequest request) {
        long cid = Long.parseLong(request.getParameter("cid"));
        DaoQueryResult res =financeService.getFinance(conn,cid);
        return JSONObject.toJSONString(res);
    }

    private String updateFinance(Connection conn, HttpServletRequest request) {
        Finance finance = JSONObject.parseObject(request.getParameter("finance"), Finance.class);
        DaoUpdateResult res = financeService.updateFinance(conn,finance);
        return JSONObject.toJSONString(res);
    }

    private String insertFinance(Connection conn, HttpServletRequest request) {
        Finance finance = JSONObject.parseObject(request.getParameter("finance"), Finance.class);
        DaoUpdateResult res = financeService.insertFinance(conn,finance);
        return JSONObject.toJSONString(res);
    }
}
