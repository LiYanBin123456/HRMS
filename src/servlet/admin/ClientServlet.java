package servlet.admin;


import bean.admin.Client;
import com.alibaba.fastjson.*;
import dao.admin.A_ContractDao;
import database.*;
import service.admin.ClientService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

@WebServlet(urlPatterns = "/client")
public class ClientServlet extends HttpServlet {
    private ClientService clientService = new ClientService();
    private A_ContractDao contractDao = new A_ContractDao();
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
            case "deleteClient1"://删除潜在客户
                result = deleteClient1(conn,request);
                break;
            case "deleteClient2"://删除合作客户
                result = deleteClient2(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    /**
     * 删除流程
     * 先查出该用户所签订的合同文件名字，删除存储的文件
     * 在删除合同
     * 最后修改合作客户为潜在客户
     *
     * @param conn
     * @param request
     * @return
     */
    private String deleteClient2(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        List<String> list = contractDao.deleteContract(conn, id);
        if(list!=null){
           for(String filename:list){
               String url = request.getServletContext().getRealPath("/upload");
               File file = new File(url+"/"+filename+".jpg");
               if (file.exists()) {
                   file.delete();
                   System.out.println(filename+"文件删除成功!!");
               }else {
                   System.out.println("文件不存在!!");
               }
           }
        }
        DaoUpdateResult res = clientService.deleteClient2(conn,id);
        return JSONObject.toJSONString(res);
    }

    //删除潜在客户
    private String deleteClient1(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res = clientService.deleteClient1(conn,id);
        return JSONObject.toJSONString(res);
    }

    //获取客户列表
    private String getClients(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res =clientService.getClientList(conn,parameter);
        System.out.println(res);
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
