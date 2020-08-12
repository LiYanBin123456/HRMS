package servlet;

import database.ConnUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

//资金管理servlet
@WebServlet(name = "FinanceServlet" ,urlPatterns = "/finance")
public class FinanceServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");
        switch (op) {
            case "arrive"://到帐确认
                result = arrive(conn,request);
                break;
            case "getTransactions"://获取资金往来明细
                result = getTransactions(conn,request);
                break;

        }

        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    private String arrive(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String getTransactions(Connection conn, HttpServletRequest request) {
        return null;
    }




}
