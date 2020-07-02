package servlet.admin;

import bean.admin.Rule_medical;
import com.alibaba.fastjson.JSONObject;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.QueryParameter;
import service.admin.Rule_medicalService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(urlPatterns ="/rule_medical")
public class Rule_medicalServlet extends HttpServlet {
    private Rule_medicalService medicalService = new Rule_medicalService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");

        switch (op) {
            case "getMedicals"://获取所有公积金清单
                result = getMedicals(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private String getMedicals(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res =medicalService.getRule_medicalList(conn,parameter);
        System.out.println(res);
        return JSONObject.toJSONString(res);
    }
}
