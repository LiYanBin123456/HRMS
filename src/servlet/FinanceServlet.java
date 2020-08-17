package servlet;

import com.alibaba.fastjson.JSONObject;
import dao.finance.FinanceDao;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;

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

    //到帐确认
    private String arrive(Connection conn, HttpServletRequest request) {
        float balance = Float.parseFloat(request.getParameter("balance"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res = FinanceDao.arrive(conn, balance, id);
        return JSONObject.toJSONString(res);
    }

    //获取资金往来明细
    private String getTransactions(Connection conn, HttpServletRequest request) {
        long cid = Long.parseLong(request.getParameter("cid"));
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("cid","=",cid);
        DaoQueryListResult res = FinanceDao.getTransactions(conn,parameter);
        return JSONObject.toJSONString(res);
    }
}
