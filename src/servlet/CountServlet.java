package servlet;

import bean.admin.Account;
import com.alibaba.fastjson.JSONObject;
import dao.admin.AccountDao;
import dao.client.CooperationDao;
import dao.client.DispatchDao;
import dao.employee.EmployeeDao;
import dao.insurance.InsuredDao;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;
import service.admin.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(urlPatterns = "/count")
public class CountServlet extends HttpServlet {
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
            case "getEmployees"://获取账号列表
                result = getEmployees(conn,request);
                break;
            case "getClients"://获取账号列表
                result = getClients(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //获取所有员工的列表
    private String getEmployees(Connection conn, HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        parameter.pagination.set(true);
        DaoQueryListResult res1 = EmployeeDao.getList(conn,parameter);
        DaoQueryListResult res2 = InsuredDao.getList(conn,parameter);
        DaoQueryListResult res3 = new DaoQueryListResult();
        if(res1.success&&res2.success){
           res3.success=true;
           res3.total = res2.total+res1.total;
           res3.msg="";
        }else {
            res3.success=false;
            res3.msg="数据库操作错误";
        }
        return JSONObject.toJSONString(res3);
    }

    //获取所有的客户单位
    private String getClients(Connection conn, HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        parameter.pagination.set(true);
        DaoQueryListResult res1 = CooperationDao.getList(conn,parameter);
        DaoQueryListResult res2 = DispatchDao.getList(conn,parameter);
        DaoQueryListResult res3 = new DaoQueryListResult();
        if(res1.success&&res2.success){
            res3.success=true;
            res3.total = res2.total+res1.total;
            res3.msg="";
        }else {
            res3.success=false;
            res3.msg="数据库操作错误";
        }
        return JSONObject.toJSONString(res3);
    }

}
