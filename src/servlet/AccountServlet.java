package servlet;

import bean.admin.Account;
import com.alibaba.fastjson.JSONObject;
import dao.admin.AccountDao;
import database.*;
import service.admin.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
@WebServlet(urlPatterns = "/verify/account")
public class AccountServlet extends HttpServlet {
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
                break;
            case "quit"://退出
                result = quit(request);
                break;
            case "getList"://获取账号列表
                result = getList(conn,request);
                break;
            case "delete"://删除账号列表
                result = delete(conn,request);
                break;
            case "insert"://添加账号
                result = insert(conn,request);
                break;
            case "insertAdmin"://添加管理员账号
                result = insertAdmin(conn,request);
                break;
            case "get"://获取账号详情
                result = get(conn,request);
                break;
            case "getAdmin"://获取管理员账号账号详情
                result = getAdmin(conn,request);
                break;
            case "update"://修改账号
                result = update(conn,request);
                break;
            case "permit"://设置权限
                result = permit(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //登录
    private String login(Connection conn, HttpServletRequest request)  {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        return AccountService.login(conn,username,password,request.getSession());
    }

    //退出
    private String quit(HttpServletRequest request) {
        request.getSession().invalidate();
        DaoUpdateResult res = new DaoUpdateResult();
        res.msg = "退出";
        res.success = true;
        return JSONObject.toJSONString(res);
    }

    //获取账号列表
    private String getList(Connection conn, HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        parameter.addCondition("role","=",user.getRole());
        parameter.addCondition("rid","=",user.getRid());
        //不能查询出自己
        parameter.addCondition("id","!=",user.getId());
        DaoQueryListResult res = AccountService.getList(conn,parameter);
        return JSONObject.toJSONString(res);
    }

    //删除账号
    private String delete(Connection conn, HttpServletRequest request) {
        long id= Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res = AccountService.delete(conn,id);
        return JSONObject.toJSONString(res);
    }

    //插入账号
    private String insert(Connection conn, HttpServletRequest request) {
        Account account =JSONObject.parseObject(request.getParameter("account"),Account.class);
        return AccountService.insert(conn,account,request.getSession());
    }

    //插入管理员账号
    private String insertAdmin(Connection conn, HttpServletRequest request) {
        Account account =JSONObject.parseObject(request.getParameter("account"),Account.class);
        return AccountService.insertAdmin(conn,account);
    }

    //获取账号详情
    private String get(Connection conn, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        return AccountService.get(conn,user.getId());
    }

    //获取管理员账号详情
    private String getAdmin(Connection conn, HttpServletRequest request) {
        Account account = JSONObject.parseObject(request.getParameter("account"),Account.class);
        return JSONObject.toJSONString(AccountService.getAdmin(conn,account));
    }

    //修改账号
    private String update(Connection conn, HttpServletRequest request) {
        Account account =JSONObject.parseObject(request.getParameter("account"),Account.class);
        DaoUpdateResult res =AccountService.update(conn,account);
        return JSONObject.toJSONString(res);
    }

    //设置权限
    private String permit(Connection conn, HttpServletRequest request) {
        long permission = Long.parseLong(request.getParameter("permission"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res =AccountService.permit(conn,id,permission);
        return JSONObject.toJSONString(res);
    }
}
