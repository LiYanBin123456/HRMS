package servlet;

import bean.admin.Account;
import com.alibaba.fastjson.JSONObject;
import database.*;
import service.admin.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
@WebServlet(urlPatterns = "/account")
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
                result = login(conn,request,response);
                break;
            case "quit"://退出
                result = quit(conn,request);
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
            case "get"://获取账号详情
                result = get(conn,request);
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
    private String login(Connection conn, HttpServletRequest request,HttpServletResponse response)  {
        DaoQueryResult res;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        res = AccountService.login(conn,username);
        Account account = (Account) res.data;
        if(account!=null){
            if(account.getPassword().equals(password)){
                res.msg ="账号密码正确";
                res.success=true;
                request.getSession().setAttribute("id", account.getId());
                request.getSession().setAttribute("nickname", account.getNickname());
                request.getSession().setAttribute("role", account.getRole());
                request.getSession().setAttribute("rid", account.getRid());
                request.getSession().setAttribute("permission", account.getPermission());
                return JSONObject.toJSONString(res);
            }else {
                res.msg="密码错误";
            }
        }
        res.success=false;
        return JSONObject.toJSONString(res);
    }

    //退出
    private String quit(Connection conn, HttpServletRequest request) {
        request.getSession().invalidate();
        DaoUpdateResult res=null;
        res.msg+="退出";
        res.success = true;
        return JSONObject.toJSONString(res);
    }

    //获取账号列表
    private String getList(Connection conn, HttpServletRequest request) {
        DaoQueryListResult res;
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        res = AccountService.getList(conn,parameter);
        System.out.println(JSONObject.toJSONString(res));
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
        System.out.println("前台传过来的参数："+account);
        DaoUpdateResult res = AccountService.insert(conn,account);
        return JSONObject.toJSONString(res);
    }

    //获取账号详情
    private String get(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res = AccountService.get(conn,id);
        return JSONObject.toJSONString(res);
    }

    //修改账号
    private String update(Connection conn, HttpServletRequest request) {
        Account account =JSONObject.parseObject(request.getParameter("account"),Account.class);
        DaoUpdateResult res =AccountService.update(conn,account);
        return JSONObject.toJSONString(res);
    }

    //设置权限
    private String permit(Connection conn, HttpServletRequest request) {
        int permission = Integer.parseInt(request.getParameter("permission"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res =AccountService.permit(conn,id,permission);
        return JSONObject.toJSONString(res);
    }


}
