package servlet;


import bean.client.*;
import com.alibaba.fastjson.*;
import dao.contract.ContractDao;
import database.*;
import service.client.*;

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
import java.sql.Date;
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


}
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //添加
    private String insert(Connection conn,HttpServletRequest request) {
        DaoUpdateResult res = null;
        byte category = Byte.parseByte(request.getParameter("category"));
        HttpSession session = request.getSession();
        long rid = (long) session.getAttribute("rid");
        switch (category) {
            case 0://派遣方客户
                Dispatch dispatch = JSON.parseObject(request.getParameter("client"), Dispatch.class);
                res = DispatchService.insert(conn, dispatch);
                break;
            case 1://合作单位客户
               Cooperation cooperation= JSON.parseObject(request.getParameter("client"), Cooperation.class);
               cooperation.setDid(rid);
               res = CooperationService.insert(cooperation,conn);
               break;
            case 2://派遣单位客户
                Supplier supplier= JSON.parseObject(request.getParameter("client"), Supplier.class);
                supplier.setDid(rid);
                res = SupplierService.insert(supplier,conn);
                break;
        }
        return JSONObject.toJSONString(res);
    }
    //删除
    private String delete(Connection conn, HttpServletRequest request) {
        DaoUpdateResult res=new DaoUpdateResult();
        long id = Long.parseLong(request.getParameter("id"));
        byte status = Byte.parseByte(request.getParameter("status"));
        byte category = Byte.parseByte(request.getParameter("category"));
        switch (category) {
            case 0://派遣方客户
               if(status==2){//删除流失客户，删除合同和附件，删除服务信息,删除客户 A代表平台与派遣方的合同
                   List<String> list = ContractDao.deleteContract(conn, id,"A");
                   if(list!=null){
                       if(list!=null){
                           //调用自定义方法 删除服务器中的合同附件
                           String msg =deleteContract(list,request);
                           res.msg +=msg;
                       }
                   }
                   res = DispatchService.deletePot(conn,id,0);
               }else{//修改客户状态
                   res = DispatchService.deleteCoop(conn,id,status);
               }
                break;
            case 1://合作单位客户
                if(status==2){//删除流失客户，删除合同和附件，删除服务信息,删除客户 B代表派遣方与合作客户的合同
                    List<String> list = ContractDao.deleteContract(conn, id,"B");
                    if(list!=null){
                        //调用自定义方法 删除服务器中的合同附件
                        String msg =deleteContract(list,request);
                        res.msg +=msg;
                    }
                    res = CooperationService.deletePot(conn,id,1);
                }else{//修改客户状态
                    res = CooperationService.deleteCoop(conn,id,status);
                }
                break;
            case 2://供应商单位客户
                   res = SupplierService.delete(id,conn,status);
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
                System.out.println("前台传来的数据："+dispatch);
                res = DispatchService.update(conn, dispatch);
                break;
            case 1://合作单位客户
                Cooperation cooperation= JSON.parseObject(request.getParameter("client"), Cooperation.class);
                System.out.println("前台传来的数据："+cooperation);
                res = CooperationService.update(conn,cooperation);
                break;
            case 2://派遣单位客户
                Supplier supplier= JSON.parseObject(request.getParameter("client"), Supplier.class);
                System.out.println("前台传来的数据："+supplier);
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
        long rid = (long) session.getAttribute("rid");
        byte category= Byte.parseByte(request.getParameter("category"));
        if(category==0){
            //派遣方客户
            res = DispatchService.getList(conn,parameter);
        }else if(category==1){
            //合作单位客户
            parameter.addCondition("did","=",rid);
            res = CooperationService.getList(conn,parameter);
        }else {
            //供应商客户
            parameter.addCondition("did","=",rid);
            res = SupplierService.getList(conn,parameter);
        }

        return JSONObject.toJSONString(res);
    }

    //获取客户列表
    private String getAllocating(Connection conn,HttpServletRequest request) {
        DaoQueryListResult res = new DaoQueryListResult();
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("aid","=",0);
        byte category= Byte.parseByte(request.getParameter("category"));
        if(category==0){//派遣方客户
            res = DispatchService.getList(conn,parameter);
        }else if(category==1){//合作单位客户
            res = CooperationService.getList(conn,parameter);
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
        System.out.println("前台传来的数据："+finance);
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
        DaoQueryListResult result =FinanceService.getList(conn,parameter);
        return JSONObject.toJSONString(result);
    }

    //增加客户服务信息
    private String insertFinance(Connection conn, HttpServletRequest request) {
        Finance finance =JSONObject.parseObject(request.getParameter("finance"),Finance.class);
        System.out.println("前台传来的数据："+finance);
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
                System.out.println(filename+"文件删除成功!!");

            }else {
                msg = "文件不存在或者已删除";
            }
        }
        return msg;
    }
}
