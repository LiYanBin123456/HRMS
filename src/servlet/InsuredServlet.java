package servlet;

import bean.admin.Account;
import bean.employee.Employee;
import bean.insurance.Insured;
import com.alibaba.fastjson.JSONObject;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;
import service.employee.EmployeeService;
import service.insurance.InsuredService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(name = "InsuredServlet",urlPatterns = "/verify/insured")
public class InsuredServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");
        switch (op) {
            case "getList"://获取列表
                result = getList(conn, request);
                break;
            case "insert"://添加
                result = insert(conn, request);
                break;
            case "delete"://删除员工
                result = delete(conn, request);
                break;
            case "update"://修改
                result = update(conn, request);
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

    //插入员工信息
    private String insert(Connection conn, HttpServletRequest request) {
        HttpSession session = request.getSession();
        /*Account user = (Account) session.getAttribute("account");
        if(user.getRole() == Account.ROLE_COOPERATION) {
            Insured insured = JSONObject.parseObject(request.getParameter("insured"), Insured.class);
            insured.setCid(user.getId());
            return InsuredService.insert(conn, insured);
        }
        return "";*/
        Insured insured = JSONObject.parseObject(request.getParameter("insured"), Insured.class);
        insured.setCid(2);
        return InsuredService.insert(conn, insured);
    }

    //删除
    private String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong((request.getParameter("id")));
        return InsuredService.delete(conn,id);
    }

    //修改员工信息
    private String update(Connection conn, HttpServletRequest request) {
        Insured insured = JSONObject.parseObject(request.getParameter("insured"), Insured.class);
        return InsuredService.update(conn,insured);
    }

    //获取员工列表
    private String getList(Connection conn, HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        return InsuredService.getList(conn,parameter);
    }

}
