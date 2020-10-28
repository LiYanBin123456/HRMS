package servlet;

import bean.admin.Account;
import bean.employee.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import dao.employee.ExtraDao;
import database.*;
import service.employee.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.StringTokenizer;

@WebServlet(name = "EmployeeServlet",urlPatterns = "/verify/employee")
public class EmployeeServlet extends HttpServlet {
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
            case "insertExtra"://添加补充信息
                result = insertExtra(conn,request);
                break;
            case "get"://获取详情
                result = get(conn,request);
                break;
            case "getExtra"://获取详情
                result = getExtra(conn,request);
                break;
            case "update"://修改
                result = update(conn,request);
                break;
            case "updateExtra"://修改补充信息
                result = updateExtra(conn,request);
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
            case "insertEnsureSetting"://插入社保设置信息
                result = insertEnsureSetting(conn,request);
                break;
            case "updateEnsureSetting"://修改社保设置信息
                result = updateEnsureSetting(conn,request);
                break;
            case "getEnsureSetting"://获取社保设置信息
                result = getEnsureSetting(conn,request);
                break;
            case "insertDeduct"://插入个税扣除信息
                result = insertDeduct(conn,request);
                break;
            case "updateDeduct"://修改个税扣除信息
                result = updateDeduct(conn,request);
                break;
            case "getDeduct"://获取个税信息
                result = getDeduct(conn,request);
                break;
            case "importDeducts"://导入个税集合
                result = importDeducts(conn,request);
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
            case "getDeducts"://获取个税设置列表
                result = getDeducts(conn,request);
                break;
            case "deleteDeduct"://删除个税设置
                result = deleteDeduct(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //插入员工信息
    private String insert(Connection conn, HttpServletRequest request) {
        Employee employee = JSONObject.parseObject(request.getParameter("employee"), Employee.class);
        Account user = (Account) request.getSession().getAttribute("account");
        employee.setDid(user.getRid());
        return EmployeeService.insert(conn, employee);
    }

    //插入员工补充信息
    private String insertExtra(Connection conn, HttpServletRequest request) {
        EmployeeExtra employeeExtra =JSONObject.parseObject(request.getParameter("extra"), EmployeeExtra.class);
        DaoUpdateResult res = ExtraService.insert(conn,employeeExtra);
        return JSONObject.toJSONString(res);
    }

    //插入社保设置信息
    private String insertEnsureSetting(Connection conn, HttpServletRequest request) {
        EnsureSetting setting = JSONObject.parseObject(request.getParameter("setting"),EnsureSetting.class);
        DaoUpdateResult result = SettingService.insert(conn, setting);
        return  JSONObject.toJSONString(result);
    }

    //插入个税扣除
    private String insertDeduct(Connection conn, HttpServletRequest request) {
        Deduct deduct = JSONObject.parseObject(request.getParameter("deduct"), Deduct.class);
        System.out.println("前台传过来的数据"+deduct);
        DaoUpdateResult result = DeductService.insert(conn, deduct);
        return  JSONObject.toJSONString(result);
    }

    //插入员工工资卡
    private String insertCard(Connection conn, HttpServletRequest request) {
        PayCard payCard = JSONObject.parseObject(request.getParameter("payCard"), PayCard.class);
        DaoUpdateResult res = PayCardService.insert(conn,payCard);
        return  JSONObject.toJSONString(res);
    }

    //删除
    private String deleteDeduct(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult result = DeductService.delete(conn,id);
        return JSONObject.toJSONString(result);
    }

    //修改员工信息
    private String update(Connection conn, HttpServletRequest request) {
        Employee employee = JSONObject.parseObject(request.getParameter("employee"), Employee.class);
        DaoUpdateResult res = EmployeeService.update(conn,employee);
        return JSONObject.toJSONString(res);
    }

    //修改员工补充信息
    private String updateExtra(Connection conn, HttpServletRequest request) {
        EmployeeExtra extra = JSONObject.parseObject(request.getParameter("extra"), EmployeeExtra.class);
        DaoUpdateResult res = ExtraService.update(conn,extra);
        return JSONObject.toJSONString(res);
    }

    //修改社保设置信息
    private String updateEnsureSetting(Connection conn, HttpServletRequest request) {
        EnsureSetting setting = JSONObject.parseObject(request.getParameter("setting"),EnsureSetting.class);
        DaoUpdateResult result = SettingService.update(conn, setting);
        return  JSONObject.toJSONString(result);
    }

    //修改个税扣除
    private String updateDeduct(Connection conn, HttpServletRequest request) {
        Deduct deduct = JSONObject.parseObject(request.getParameter("deduct"), Deduct.class);
        DaoUpdateResult result = DeductService.update(conn, deduct);
        return  JSONObject.toJSONString(result);
    }

    //更改员工工资卡
    private String updateCard(Connection conn, HttpServletRequest request) {
        PayCard payCard = JSONObject.parseObject(request.getParameter("payCard"), PayCard.class);
        DaoUpdateResult res = PayCardService.update(conn,payCard);
        return  JSONObject.toJSONString(res);
    }

    //导入个税专项扣除
    private String getDeducts(Connection conn, HttpServletRequest request) {
        DaoQueryListResult res;
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        res = DeductService.getList(conn, parameter);
        return  JSONObject.toJSONString(res);
    }

    //获取社保设置信息
    private String getEnsureSetting(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult result = SettingService.get(conn,id);
        return  JSONObject.toJSONString(result);
    }

    //获取个税扣除
    private String getDeduct(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult result = DeductService.get(conn,id);
        return  JSONObject.toJSONString(result);
    }

    //导入个税扣除
    private String importDeducts(Connection conn, HttpServletRequest request) {
        List<ViewDeduct> deducts = JSONArray.parseArray(request.getParameter("deducts"),ViewDeduct.class);
        DaoUpdateResult result = DeductService.importDeducts(conn,deducts);
        return  JSONObject.toJSONString(result);
    }

    //获取员工列表
    private String getList(Connection conn, HttpServletRequest request) {
        DaoQueryListResult res;
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        byte type = Byte.parseByte(request.getParameter("type"));
        parameter.addCondition("type","=",type);
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        switch (type){
            case 0://内部员工
            case 2://人才库
                if(user.getRole()==1) {
                    parameter.addCondition("did", "=", user.getRid());//获取派遣单位的内部员工或者人才库
                }
                break;
            case 1://派遣员工
                if(user.getRole()==1){//派遣方管理员
                    if(user.isAdmin()) {
                        parameter.addCondition("did", "=", user.getRid());
                    }else{
                        parameter.addCondition("aid", "=", user.getId());
                    }
                }else if(user.getRole() == 2) {//合作方管理员
                    parameter.addCondition("cid","=",user.getRid());
                }
                break;
            default:
                return "";
        }

        res = EmployeeService.getList(conn,parameter);
        return JSONObject.toJSONString(res);
    }

    //获取员工信息
    private String get(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong((request.getParameter("id")));
        DaoQueryResult res = EmployeeService.get(conn,id);
        return JSONObject.toJSONString(res);
    }

    //获取员工补充信息
    private String getExtra(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong((request.getParameter("id")));
        DaoQueryResult res = ExtraService.get(conn,id);
        return JSONObject.toJSONString(res);
    }

    //离职或者退休
    private String leave(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong((request.getParameter("id")));
        Date date = Date.valueOf(request.getParameter("date"));
        byte reason = Byte.parseByte(request.getParameter("reason"));
        return EmployeeService.leave(conn,id,category,reason,date);
    }

    //删除
    private String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong((request.getParameter("id")));
        byte category = Byte.parseByte(request.getParameter("category"));
        DaoUpdateResult res = EmployeeService.delete(conn,id,category);
        return JSONObject.toJSONString(res);
    }

    //批量插入
    private String insertBatch(Connection conn, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        List<JSONObject> viewEmployees = JSONArray.parseArray(request.getParameter("employees"),JSONObject.class);

        DaoUpdateResult res = EmployeeService.insertBatch(conn,viewEmployees,user.getRid());
        return  JSONObject.toJSONString(res);
    }

    //获取员工工资卡
    private String getCard(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res = PayCardService.get(conn, id);
        return  JSONObject.toJSONString(res);
    }

    //批量派遣员工
    private String dispatch(Connection conn, HttpServletRequest request) {
        long cid = Long.parseLong(request.getParameter("cid"));
        String[] eids = request.getParameterValues("eids[]");
        DaoUpdateResult res = EmployeeService.dispatch(conn,eids,cid );
        return  JSONObject.toJSONString(res);
    }

    //雇用
    private String employ(Connection conn, HttpServletRequest request) {
        Byte category = Byte.valueOf(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res = EmployeeService.employ(conn, id, category);
        return  JSONObject.toJSONString(res);
    }


}


