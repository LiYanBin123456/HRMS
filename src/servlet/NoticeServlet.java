package servlet;

import com.alibaba.fastjson.JSONObject;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.QueryParameter;
import service.NoticeService;

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
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    //
    private String getNotices(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res =noticeService.getNoticeList(conn,parameter);
        return JSONObject.toJSONString(res);
    }
}
