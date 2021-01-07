package servlet;

import com.alibaba.fastjson.JSONObject;
import database.ConnUtil;
import database.DaoQueryListResult;
import service.settlement.SalaryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(name = "SalaryServlet",urlPatterns = "/salary")
public class SalaryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin","*");//允许跨域访问，招聘系统需要查工资
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();
        String op = request.getParameter("op");

        switch (op) {
            case "getSalary"://修改结算单额外信息
                result = getSalary(conn, request);
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

    //查询工资
    private String getSalary(Connection conn, HttpServletRequest request) {
        String cardId = request.getParameter("cardId");
        String month = request.getParameter("month");
        DaoQueryListResult result = SalaryService.getSalary(conn,cardId,month);
        return JSONObject.toJSONString(result);
    }
}
