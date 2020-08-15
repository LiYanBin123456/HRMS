package servlet;

import bean.employee.Employee;
import bean.employee.EmployeeExtra;
import com.alibaba.fastjson.JSONObject;
import database.*;
import service.employee.EmployeeService;
import service.employee.ExtraService;
import service.employee.PayCardService;
import service.employee.SettingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;

@WebServlet(name = "EmployeeServlet",urlPatterns = "/employee")
public class EmployeeServlet extends HttpServlet {
    private EmployeeService employeeService = new EmployeeService();
    private ExtraService extraService = new ExtraService();
    private SettingService settingService = new SettingService();
    private PayCardService payCardService = new PayCardService();
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
            case "getList"://获取列表
                result = getList(conn,request);
                break;
            case "insert"://添加
                result = insert(conn,request);
                break;
            case "get"://获取详情
                result = get(conn,request);
                break;
            case "update"://修改
                result = update(conn,request);
                break;
            case "leave"://离职或者退休
                result = leave(conn,request);
                break;
            case "delete"://删除员工
                result = delete(conn,request);
                break;
            case "insertBatch"://批量插入
                result = insertBatch(conn,request);
                break;
            case "insertSetting"://插入社保
                result = insertSetting(conn,request);
                break;
            case "updateSetting"://修改社保和个税
                result = updateSetting(conn,request);
                break;
            case "getSetting"://获取社保和个税
                result = getSetting(conn,request);
                break;
            case "insertCard"://添加工资卡
                result = insertCard(conn,request);
                break;
            case "getCard"://获取工资卡信息
                result = getCard(conn,request);
                break;
            case "updateCard"://修改工资卡信息
                result = updateCard(conn,request);
                break;
            case "dispatch"://批量派遣
                result = dispatch(conn,request);
                break;
            case "employ"://雇用
                result = employ(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //获取员工列表
    private String getList(Connection conn, HttpServletRequest request) {
        DaoQueryListResult res;
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        byte category = Byte.parseByte(request.getParameter("category"));
        if(category==0){
            res =employeeService.getList(conn,parameter);
        }else {
            res = extraService.getList(conn,parameter);
        }
        return JSONObject.toJSONString(res);
    }

    //插入员工信息
    private String insert(Connection conn, HttpServletRequest request) {
        DaoUpdateResult res = null;
        byte category = Byte.parseByte(request.getParameter("category"));
        if(category==0){
            Employee employee =JSONObject.parseObject(request.getParameter("employee"), Employee.class);
            res= employeeService.insert(conn,employee);
        }else {
            EmployeeExtra employeeExtra =JSONObject.parseObject(request.getParameter("employee"), EmployeeExtra.class);
            res = extraService.insert(conn,employeeExtra);
        }
        return JSONObject.toJSONString(res);
    }
    //获取员工信息
    private String get(Connection conn, HttpServletRequest request) {
        DaoQueryResult res = null;
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong((request.getParameter("id")));
        if(category==0){
           res = employeeService.get(conn,id);
        }else {
           res = extraService.get(conn,id);
        }
        return JSONObject.toJSONString(res);
    }

    //修改员工信息
    private String update(Connection conn, HttpServletRequest request) {
        DaoUpdateResult res = null;
        byte category = Byte.parseByte(request.getParameter("category"));
        if(category==0){
            Employee employee =JSONObject.parseObject(request.getParameter("employee"), Employee.class);
            res= employeeService.update(conn,employee);
        }else {
            EmployeeExtra employeeExtra =JSONObject.parseObject(request.getParameter("employee"), EmployeeExtra.class);
            res = extraService.update(conn,employeeExtra);
        }
        return JSONObject.toJSONString(res);
    }

    //离职或者退休
    private String leave(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong((request.getParameter("id")));
        Date date = Date.valueOf(request.getParameter("date"));
        byte leave = Byte.parseByte(request.getParameter("leave"));
        DaoUpdateResult res =extraService.leave(conn,id,category,leave,date);
        return JSONObject.toJSONString(res);
    }

    //删除
    private String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong((request.getParameter("id")));
        DaoUpdateResult   res = employeeService.delete(conn,id);
        return JSONObject.toJSONString(res);
    }

    //批量插入
    private String insertBatch(Connection conn, HttpServletRequest request) {
        return  null;
    }

    //插入社保和个税
    private String insertSetting(Connection conn, HttpServletRequest request) {
        return  null;
    }

    //修改社保和个税
    private String updateSetting(Connection conn, HttpServletRequest request) {
        return  null;
    }

    //获取社保和个税
    private String getSetting(Connection conn, HttpServletRequest request) {
        return  null;
    }
    //插入员工工资卡
    private String insertCard(Connection conn, HttpServletRequest request) {
        return  null;
    }
    //获取员工工资卡
    private String getCard(Connection conn, HttpServletRequest request) {
        return  null;
    }
    //更改员工工资卡
    private String updateCard(Connection conn, HttpServletRequest request) {
        return  null;
    }
    //批量派遣员工
    private String dispatch(Connection conn, HttpServletRequest request) {
        return  null;
    }
    //雇用
    private String employ(Connection conn, HttpServletRequest request) {
        return  null;
    }


}
