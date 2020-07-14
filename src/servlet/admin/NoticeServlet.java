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
            case "getNotices"://获取所有公积金清单
                result = getNotices(conn,request);
                break;
        }
        switch (op) {
            case "getNotice"://获取一个清单
                result = getNotice(conn,request);
                break;
        }
        switch (op) {
            case "updateNotice"://修改清单
                result = updateNotice(conn,request);
                break;
        }
        switch (op) {
            case "insertNotice"://插入清单
                result = insertNotice(conn,request);
                break;
        }
        switch (op) {
            case "deleteNotice"://删除所有公积金清单
                result = deleteNotice(conn,request);
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

    //获取所有列表
    private String getNotices(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res =noticeService.getNoticeList(conn,parameter);
        return JSONObject.toJSONString(res);
    }

    //获取
    private String getNotice(Connection conn,HttpServletRequest request){
        long id = Long.parseLong(request.getParameter("id"));
        System.out.println("客户id="+id);
        DaoQueryResult res = noticeService.getNotice(conn,id);
        return  JSONObject.toJSONString(res);
    }

    //修改
    private String updateNotice(Connection conn,HttpServletRequest request) {
        //获取前台传过来的求职者的信息封装成对象Client
        Notice notice = JSON.parseObject(request.getParameter("notice"), Notice.class);
        System.out.println(notice);

        DaoUpdateResult res = noticeService.updateNotice(conn, notice);

        return JSONObject.toJSONString(res);
    }

    //添加
    private String insertNotice(Connection conn,HttpServletRequest request) {
        //获取前台传过来的求职者的信息封装成对象Client
        Notice notice = JSON.parseObject(request.getParameter("notice"), Notice.class);
        System.out.println(notice);

        DaoUpdateResult res = noticeService.insertNotice(conn, notice);

        return JSONObject.toJSONString(res);
    }

    private String deleteNotice(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));

        DaoUpdateResult res = noticeService.deleteNotice(conn,id);
        return  JSONObject.toJSONString(res);
    }
}
