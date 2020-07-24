package servlet.admin;

import bean.admin.Notice;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import database.*;
import service.admin.NoticeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(urlPatterns ="/notice")
public class NoticeServlet extends HttpServlet {
    private NoticeService noticeService = new NoticeService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");
        switch (op) {
            case "insert"://插入清单
                result = insert(conn,request);
                break;
        }
        switch (op) {
            case "delete"://删除所有公积金清单
                result = delete(conn,request);
                break;
        }
        switch (op) {
            case "update"://修改清单
                result = update(conn,request);
                break;
        }
        switch (op) {
            case "getList"://获取所有公积金清单
                result = getList(conn,request);
                break;
        }
        switch (op) {
            case "get"://获取一个清单
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

    //添加公告
    private String insert(Connection conn,HttpServletRequest request) {
        //获取前台传过来的求职者的信息封装成对象Client
        Notice notice = JSON.parseObject(request.getParameter("notice"), Notice.class);
        System.out.println(notice);

        DaoUpdateResult res = noticeService.insert(conn, notice);

        return JSONObject.toJSONString(res);
    }
    //删除公告
    private String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));

        DaoUpdateResult res = noticeService.delete(conn,id);
        return  JSONObject.toJSONString(res);
    }

    //修改公告
    private String update(Connection conn,HttpServletRequest request) {
        //获取前台传过来的求职者的信息封装成对象Client
        Notice notice = JSON.parseObject(request.getParameter("notice"), Notice.class);
        System.out.println(notice);

        DaoUpdateResult res = noticeService.update(conn, notice);

        return JSONObject.toJSONString(res);
    }

    //获取公告列表
    private String getList(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res =noticeService.getList(conn,parameter);
        return JSONObject.toJSONString(res);
    }

    //获取公告
    private String get(Connection conn,HttpServletRequest request){
        long id = Long.parseLong(request.getParameter("id"));
        System.out.println("客户id="+id);
        DaoQueryResult res = noticeService.get(conn,id);
        return  JSONObject.toJSONString(res);
    }




}
