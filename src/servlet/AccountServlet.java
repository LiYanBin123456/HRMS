package servlet;

import com.alibaba.fastjson.JSONObject;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.QueryParameter;
import service.admin.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

public class AccountServlet extends HttpServlet {
    private AccountService accountService = new AccountService();
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
            case "login"://登录
                result = login(conn,request);
            case "quit"://退出
                result = quit(conn,request);
            case "getList"://获取账号列表
                result = getList(conn,request);
            case "delete"://删除账号列表
                result = delete(conn,request);
            case "insert"://添加账号
                result = insert(conn,request);
            case "get"://获取账号详情
                result = get(conn,request);
            case "update"://修改账号
                result = update(conn,request);
            case "permit"://设置权限
                result = permit(conn,request);
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //登录
    private String login(Connection conn, HttpServletRequest request) {
        return null;
    }

    //退出
    private String quit(Connection conn, HttpServletRequest request) {
        return null;
    }

    //获取账号列表
    private String getList(Connection conn, HttpServletRequest request) {
        DaoQueryListResult res = new DaoQueryListResult();
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        res = accountService.getList(conn,parameter);
        return null;
    }

    //删除账号
    private String delete(Connection conn, HttpServletRequest request) {
        return null;
    }

    //插入账号
    private String insert(Connection conn, HttpServletRequest request) {
        return null;
    }

    //获取账号详情
    private String get(Connection conn, HttpServletRequest request) {
        return null;
    }

    //修改账号
    private String update(Connection conn, HttpServletRequest request) {
        return null;
    }

    //设置权限
    private String permit(Connection conn, HttpServletRequest request) {
        return null;
    }


}
