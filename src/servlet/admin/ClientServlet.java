package servlet.admin;


import bean.admin.Client;
import com.alibaba.fastjson.*;
import database.*;
import service.admin.ClientService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(urlPatterns = "/client")
public class ClientServlet extends HttpServlet {
    private ClientService clientService = new ClientService();
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
            case "getClients"://获取所有客户清单
                result = getClients(conn,request);
                break;
            case "getClient"://获取一个客户清单
                result = getClient(conn,request);
                break;
            case "updateClient"://修改一个客户
                result = updateClient(conn,request);
                break;
            case "insertClient"://添加一个客户
                result = insertClient(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //获取客户列表
    private String getClients(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res =clientService.getClientList(conn,parameter);
        return JSONObject.toJSONString(res);
    }

    //获取客户基本信息
    private String getClient(Connection conn,HttpServletRequest request){
        long id = Long.parseLong(request.getParameter("id"));
        System.out.println("客户id="+id);
        DaoQueryResult res = clientService.getClient(conn,id);
        return  JSONObject.toJSONString(res);
    }

    //修改客户信息
    private String updateClient(Connection conn,HttpServletRequest request) {
        //获取前台传过来的求职者的信息封装成对象Client
        Client client = JSON.parseObject(request.getParameter("client"), Client.class);
        System.out.println(client);
        //调用dao层的update方法
        DaoUpdateResult res = clientService.updateClient(conn, client);

        return JSONObject.toJSONString(res);
    }

    //添加
    private String insertClient(Connection conn,HttpServletRequest request) {
        //获取前台传过来的求职者的信息封装成对象Client
        Client client = JSON.parseObject(request.getParameter("client"), Client.class);
        System.out.println(client);
        //调用dao层的update方法
        DaoUpdateResult res = clientService.insertClient(conn, client);

        return JSONObject.toJSONString(res);
    }
}
