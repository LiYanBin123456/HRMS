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

@WebServlet(name = "SettlementServlet",urlPatterns = "/settlement")
public class SettlementServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");

        switch (op) {

            case "getList"://获取所有客户清单
                result = getList(conn, request);
                break;
            case "insert"://添加一个客户
                result = insert(conn, request);
                break;
            case "delete"://删除客户
                result = delete(conn, request);
                break;
            case "copy"://复制
                result = copy(conn, request);
                break;
            case "updateDetails"://修改明细
                result = updateDetails(conn, request);
                break;
            case "getDetails"://获取明细列表
                result = getDetails(conn, request);
                break;
            case "importDetails"://导入明细
                result = importDetails(conn, request);
                break;
            case "exportDetails"://导出明细
                result = exportDetails(conn, request);
                break;
            case "backup"://补缴
                result = backup(conn, request);
                break;
            case "makeup"://补差
                result = makeup(conn, request);
                break;
            case "commit"://提交
                result = commit(conn, request);
                break;
            case "check"://获取客户服务信息
                result = check(conn, request);
                break;
            case "reset"://重置
                result = reset(conn, request);
                break;
            case "deduct"://扣款
                result = deduct(conn, request);
                break;
            case "confirm"://确认到账
                result = confirm(conn, request);
                break;
            case "exportBank"://导出银行卡
                result = exportBank(conn, request);
                break;
            case "getLogs"://查询日志
                result = getLogs(conn, request);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();

    }

    private String getList(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String insert(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String delete(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String copy(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String updateDetails(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String getDetails(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String importDetails(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String exportDetails(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String backup(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String makeup(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String commit(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String check(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String reset(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String deduct(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String confirm(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String exportBank(Connection conn, HttpServletRequest request) {
     return null;
    }

    private String getLogs(Connection conn, HttpServletRequest request) {
     return null;
    }
}
