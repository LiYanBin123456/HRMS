package servlet;

import bean.admin.Account;
import bean.client.MapSalary;
import bean.employee.Employee;
import bean.settlement.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.admin.AccountDao;
import dao.client.CooperationDao;
import dao.client.DispatchDao;
import dao.client.MapSalaryDao;
import dao.employee.EmployeeDao;
import dao.insurance.InsuredDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Detail2Dao;
import dao.settlement.Detail4Dao;
import database.*;
import service.admin.AccountService;
import utills.DateUtil;
import utills.excel.Field;
import utills.excel.Scheme;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = "/count")
public class CountServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");
        switch (op) {
            case "getEmployees"://获取账号列表
                result = getEmployees(conn,request);
                break;
            case "getDetails"://获取账号列表
                result = getDetails(conn,request);
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

    private String getDetails(Connection conn, HttpServletRequest request) {
        String cardId = request.getParameter("cardId");
        String month =request.getParameter("month");
        byte category= Byte.parseByte(request.getParameter("category"));
        DetailResult detailResult = new DetailResult();
        QueryConditions conditions = new QueryConditions();
        conditions.add("cardId","=",cardId);
        Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;
        if(employee==null){
           detailResult.success=false;
           detailResult.msg="工资查询失败，请确认身份证号或者月份是否正确";
        }
        QueryParameter queryParameter = new QueryParameter();
        queryParameter.addCondition("cardId","=",cardId);
        queryParameter.addCondition("month","=",month);
        if(category==0){//普通结算单
            if(employee.getCategory()==Employee.CATEGORY_5){//小时工
                DaoQueryListResult result = Detail2Dao.getList(conn,queryParameter);
                if(result.success){
                    detailResult.success=true;
                    JSONObject object = new JSONObject();
                    object.put("name","姓名");
                    object.put("month","月份");
                    object.put("cardId","身份证号");
                    object.put("hours","工时");
                    object.put("price","单价");
                    object.put("food","餐补");
                    object.put("traffic","交通费");
                    object.put("accommodation","住宿费");
                    object.put("utilities","水电费");
                    object.put("insurance","保险费");
                    object.put("other1","其他1");
                    object.put("other2","其他2");
                    object.put("tax","个税");
                    object.put("payable","应付金额");
                    object.put("paid","实发金额");
                    detailResult.cols=object;
                    detailResult.details=result.rows;
                }else {
                    detailResult.success=false;
                    detailResult.msg="工资查询失败，请确认身份证号或者月份是否正确";
                }
            }else {//查询普通结算单
                DaoQueryListResult result = Detail1Dao.getList(conn,queryParameter);
                if(result.success){
                    MapSalary mapSalary = (MapSalary) MapSalaryDao.selectByMonth(employee.getCid(),conn, DateUtil.getLastDayofMonth(month)).data;
                    JSONObject object = new JSONObject();
                    object.put("name", "姓名");
                    object.put("cardId", "身份证号码");
                    object.put( "base", "基本工资");
                    //自定义工资项
                    if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0) {
                        List<MapSalary.SalaryItem> itemList = mapSalary.getItemList();//获取自定义工资项集合
                        for (int i = 0; i < itemList.size(); i++) {
                            object.put("f"+(i+1), itemList.get(i).getField());
                        }
                    }
                    object.put("pension1", "养老");
                    object.put("medicare1", "医疗");
                    object.put("disease1", "大病");
                    object.put("unemployment1", "失业");
                    object.put("injury", "工伤");
                    object.put("fund1", "公积金");
                    object.put("extra1", "核收补减");
                    object.put("comments1", "备注");
                    object.put("payable", "应发");
                    object.put("tax", "个税");
                    object.put("paid", "实发");
                    List<ViewDetail1> details = (List<ViewDetail1>) result.rows;
                    List<ViewDetail1> ds = new ArrayList<>();
                    if(details.size()>0){
                        for(ViewDetail1 v:details){
                            if(v.getStatus()==Detail1.STATUS_NORMAL ||v.getStatus()==Detail1.STATUS_MAKEUP){//只需要正常和补发的明细
                                ds.add(v);
                            }
                        }
                    }
                    detailResult.success=true;
                    detailResult.cols=object;
                    detailResult.details=ds;
                }else {
                    detailResult.success=false;
                    detailResult.msg="工资查询失败，请确认身份证号或者月份是否正确";
                }
            }
        }else {//查询特殊结算单
            DaoQueryListResult result = Detail4Dao.getList(conn,queryParameter);
            if(result.success){
                JSONObject object = new JSONObject();
                object.put("name", "姓名");
                object.put("cardId", "身份证号码");
                object.put("amount", "应发工资");
                object.put("tax", "个税");
                object.put("paid", "实发");
                detailResult.cols=object;
                detailResult.details=result.rows;
                detailResult.success=true;
            }else {
                detailResult.success=false;
                detailResult.msg="工资查询失败，请确认身份证号或者月份是否正确";
            }
        }
        return JSONObject.toJSONString(detailResult);
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
