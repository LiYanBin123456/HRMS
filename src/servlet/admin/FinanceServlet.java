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
            case "insert"://根据客户id添加一个财务信息
                result = insert(conn,request);
                break;
            case "update"://根据客户id修改财务信息
                result = update(conn,request);
                break;
            case "get"://根据客户id获取财务信息
                result = get(conn,request);
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

    //增加公司服务项目
    private String insert(Connection conn, HttpServletRequest request) {
        Finance finance = JSONObject.parseObject(request.getParameter("finance"), Finance.class);
        DaoUpdateResult res = financeService.insert(conn,finance);
        return JSONObject.toJSONString(res);
    }
    //修改公司服务项目
    private String update(Connection conn, HttpServletRequest request) {
        Finance finance = JSONObject.parseObject(request.getParameter("finance"), Finance.class);
        DaoUpdateResult res = financeService.update(conn,finance);
        return JSONObject.toJSONString(res);
    }
    //获取公司服务项目
    private String get(Connection conn, HttpServletRequest request) {
        long cid = Long.parseLong(request.getParameter("cid"));
        String type = request.getParameter("type");
        DaoQueryResult res =financeService.get(conn,cid,type);
        return JSONObject.toJSONString(res);
    }




}
