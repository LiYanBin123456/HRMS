package servlet;


import bean.admin.Account;
import bean.client.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.client.CooperationDao;
import dao.client.DispatchDao;
import dao.client.MapSalaryDao;
import dao.client.SupplierDao;
import database.*;
import service.client.*;
import utills.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = "/verify/client")
public class ClientServlet extends HttpServlet {
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
            case "insert"://添加一个客户
                result = insert(conn,request);
                break;
            case "delete"://删除客户
                result = delete(conn,request);
                break;
            case "update"://修改一个客户
                result = update(conn,request);
                break;
            case "getList"://获取所有客户清单
                result = getList(conn,request);
                break;
            case "get"://获取一个客户清单
                result = get(conn,request);
                break;
            case "getAllocating"://获取待分配管理员的客户清单
                result = getAllocating(conn,request);
                break;
            case "getAllocated"://获取待分配管理员的客户清单
                result = getAllocated(conn,request);
                break;
            case "allocate"://修改管理员
                result = allocate(conn,request);
                break;
            case "insertSalaryDefine"://增加客户自定义工资
                result = insertSalaryDefine(conn,request);
                break;
            case "getLastSalaryDefine"://获取客户最新自定义工资
                result = getLastSalaryDefine(conn,request);
                break;
            case "getSalaryDefineByMonth"://获取客户最新自定义工资
                result = getSalaryDefineByMonth(conn,request);
                break;
            case "getSalaryDefine"://获取客户自定义工资
                result = getSalaryDefine(conn,request);
                break;
            case "insertFinance"://增加客户服务信息
                result = insertFinance(conn,request);
                break;
            case "getFinance"://获取客户服务信息
                result = getFinance(conn,request);
                break;
            case "updateFinance"://修改客户服务信息
                result = updateFinance(conn,request);
                break;
            case "getFinances"://修改客户服务信息
                result = getFinances(conn,request);
                break;
            case "updateType"://修改客户类型
                result = updateType(conn,request);
                break;
}
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //根据日期获取公司自定义工资
    private String getSalaryDefineByMonth(Connection conn, HttpServletRequest request) {
        String month = request.getParameter("month");
        long cid = Long.parseLong(request.getParameter("id"));
        DaoQueryResult result = MapSalaryDao.selectByMonth(cid,conn,DateUtil.getLastDayofMonth(month));
        return JSONObject.toJSONString(result);
    }

    //修改客户状态
    private String updateType(Connection conn, HttpServletRequest request) {
        int category = Integer.parseInt(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));//要修改的客户id
        byte type = Byte.parseByte(request.getParameter("type"));//修改的类型 0 合作 1 潜在 2 流失
        DaoUpdateResult result = null;
        switch (category){
            case 0://管理单位客户
                result = DispatchDao.updateType(conn,id, type);
                break;
            case 1://合作单位客户
                result = CooperationDao.updateType(conn,id, type);
                break;
            case 2://合作单位客户
                result = SupplierDao.updateStatus(conn,id, type);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    //添加
    private String insert(Connection conn,HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        switch (category) {
            case 0://派遣单位客户
                Dispatch dispatch = JSON.parseObject(request.getParameter("client"), Dispatch.class);
                dispatch.setAid(user.getId());
                return DispatchService.insert(conn, dispatch);
            case 1://合作单位客户
               Cooperation cooperation= JSON.parseObject(request.getParameter("client"), Cooperation.class);
               cooperation.setDid(user.getRid());
               cooperation.setAid(user.getId());
               return CooperationService.insert(cooperation,conn);
            case 2://供应商客户
                Supplier supplier= JSON.parseObject(request.getParameter("client"), Supplier.class);
                supplier.setDid(user.getRid());
                return SupplierService.insert(supplier,conn);
        }
        return "";
    }

    //删除
    private String delete(Connection conn, HttpServletRequest request) {
        DaoUpdateResult res=new DaoUpdateResult();
        long id = Long.parseLong(request.getParameter("id"));
        byte category = Byte.parseByte(request.getParameter("category"));
        byte type = Byte.parseByte(request.getParameter("type"));
        switch (category) {
            case 0://派遣方客户
                   res = DispatchService.delete(conn,id,type);
                break;
            case 1://合作单位客户
                    res = CooperationService.delete(conn,id,type);
                break;
            case 2://供应商单位客户
                   res = SupplierService.delete(id,conn);
                break;
        }
        return JSONObject.toJSONString(res);
    }

    //修改客户信息
    private String update(Connection conn,HttpServletRequest request) {
        DaoUpdateResult res=null;
        byte category = Byte.parseByte(request.getParameter("category"));
        switch (category) {
            case 0://派遣方客户
                Dispatch dispatch = JSON.parseObject(request.getParameter("client"), Dispatch.class);
                res = DispatchService.update(conn, dispatch);
                break;
            case 1://合作单位客户
                Cooperation cooperation= JSON.parseObject(request.getParameter("client"), Cooperation.class);
                res = CooperationService.update(conn,cooperation);
                break;
            case 2://派遣单位客户
                Supplier supplier= JSON.parseObject(request.getParameter("client"), Supplier.class);
                res = SupplierService.update(conn,supplier);
                break;
        }

        return JSONObject.toJSONString(res);
    }

    //获取客户列表
    private String getList(Connection conn,HttpServletRequest request) {
        DaoQueryListResult res ;
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        byte category= Byte.parseByte(request.getParameter("category"));
        if(category==0){//派遣单位客户
//            if(!user.isAdmin()) {
//                parameter.addCondition("aid", "=", user.getId());
//            }
            res = DispatchService.getList(conn,parameter);
        }else if(category==1){//合作单位客户
            if(user.isAdmin()) {
                parameter.addCondition("did", "=", user.getRid());
            }else {
                parameter.addCondition("aid", "=", user.getId());
            }
            res = CooperationService.getList(conn,parameter);
        }else {
            //供应商客户
            parameter.addCondition("did","=",user.getRid());
            res = SupplierService.getList(conn,parameter);
        }

        return JSONObject.toJSONString(res);
    }

    //获取客户列表
    private String getAllocating(Connection conn,HttpServletRequest request) {
        DaoQueryListResult res = new DaoQueryListResult();
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("type","=",0);//合作客户
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        byte category= Byte.parseByte(request.getParameter("category"));
        if(category==0){//派遣方客户
            if(!user.isAdmin()) {
                parameter.addCondition("aid", "=", user.getId());
            }
            res = DispatchService.getList(conn,parameter);
        }else if(category==1){//合作单位客户
            if(user.isAdmin()) {
                parameter.addCondition("did", "=", user.getRid());
            }else {
                parameter.addCondition("aid", "=", user.getId());
            }
            res = CooperationService.getList(conn,parameter);
        }else {
            //供应商客户
            parameter.addCondition("did","=",user.getRid());
        }
        return JSONObject.toJSONString(res);
    }

    //获取客户列表
    private String getAllocated(Connection conn,HttpServletRequest request) {
        DaoQueryListResult res = new DaoQueryListResult();
        byte category= Byte.parseByte(request.getParameter("category"));
        long aid = Long.parseLong(request.getParameter("aid"));
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("aid","=",aid);
        parameter.addCondition("type","=",0);//合作客户
        if(category==0){//派遣方客户
            res = DispatchService.getList(conn,parameter);
        }else if(category==1){//合作单位客户
            res = CooperationService.getList(conn,parameter);
        }
        return JSONObject.toJSONString(res);
    }

    //获取客户基本信息
    private String get(Connection conn,HttpServletRequest request){
        DaoQueryResult res = null;
        long id = Long.parseLong(request.getParameter("id"));
        byte category = Byte.parseByte(request.getParameter("category"));
        switch (category) {
            case 0://派遣方客户
                res = DispatchService.get(conn, id);
                break;
            case 1://合作单位客户
                res = CooperationService.get(id,conn);
                break;
            case 2://派遣单位客户
                res = SupplierService.get(id,conn);
                break;
        }
        return  JSONObject.toJSONString(res);
    }

    //分配管理员
    private String allocate(Connection conn, HttpServletRequest request) {
        DaoUpdateResult res = null;
        long aid = Long.parseLong(request.getParameter("aid"));
        String[] cids = request.getParameterValues("cids[]");
        byte category = Byte.parseByte(request.getParameter("category"));
        switch(category){
            case 0:
                res = DispatchService.allocateAdmin(conn,cids,aid);
                break;
            case 1:
                res = CooperationService.allocateAdmin(conn,cids,aid);
        }
        return  JSONObject.toJSONString(res);
    }

    //修改客户服务信息
    private String updateFinance(Connection conn, HttpServletRequest request) {
        Finance finance =JSONObject.parseObject(request.getParameter("finance"),Finance.class);
        DaoUpdateResult res = FinanceService.update(conn,finance);
        return JSONObject.toJSONString(res);
    }

    //获取客户服务信息
    private String getFinance(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        byte category = Byte.parseByte(request.getParameter("category"));
        DaoQueryResult res = FinanceService.get(conn,id,category);
        return  JSONObject.toJSONString(res);
    }

    //获取合作单位和余额
    private String getFinances(Connection conn, HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        Account user = (Account) request.getSession().getAttribute("account");
        if(user.isAdmin()) {
            parameter.addCondition("did", "=", user.getRid());
        }else {
            parameter.addCondition("aid", "=", user.getId());
        }
        DaoQueryListResult result =FinanceService.getList(conn,parameter);
        return JSONObject.toJSONString(result);
    }

    //增加客户服务信息
    private String insertFinance(Connection conn, HttpServletRequest request) {
        Finance finance =JSONObject.parseObject(request.getParameter("finance"),Finance.class);
        DaoUpdateResult res = FinanceService.insert(conn,finance);
        return JSONObject.toJSONString(res);
    }

    //根据月份获取客户自定义工资信息
    private String getSalaryDefine(Connection conn, HttpServletRequest request) {
        String month = request.getParameter("month");
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res = MapSalaryService.get(id,month,conn);
        return  JSONObject.toJSONString(res);
    }

    //获取客户最新自定义工资信息
    private String getLastSalaryDefine(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res = MapSalaryService.getLast(id,conn);
        return  JSONObject.toJSONString(res);
    }

    //增加客户自定义工资信息
    private String insertSalaryDefine(Connection conn, HttpServletRequest request) {
        MapSalary mapSalary =JSONObject.parseObject(request.getParameter("mapSalary"),MapSalary.class);
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        mapSalary.setDate(date);
        DaoUpdateResult res= MapSalaryService.insert(mapSalary,conn);
        return  JSONObject.toJSONString(res);
    }

    //自定义方法 删除服务器中合同文件
    private String deleteContract(List<String>list ,HttpServletRequest request){
        String msg=null;
        for(String filename:list){
            String url = request.getServletContext().getRealPath("/upload");
            File file = new File(url+"/"+filename+".pdf");
            if (file.exists()) {
                file.delete();
                msg = "合同文件删除成功!!";
            }else {
                msg = "文件不存在或者已删除";
            }
        }
        return msg;
    }
}
