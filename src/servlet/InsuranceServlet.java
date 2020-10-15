package servlet;


import bean.insurance.Insurance;
import bean.insurance.ViewInsurance;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import dao.insurance.InsuranceDao;
import database.*;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import service.insurance.InsuranceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


@WebServlet(name = "InsuranceServlet",urlPatterns = "/verify/insurance")
public class InsuranceServlet extends HttpServlet {
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
            case "insertBatch"://批量插入参保单
                result = insertBatch(conn,request);
                break;
            case "update"://修改参保单
                result = update(conn,request);
                break;
            case "delete"://删除参保单
                result = delete(conn,request);
                break;
            case "getList"://获取参保单列表
                result = getList(conn,request);
                break;
            case "get"://获取参保单
                result = get(conn,request);
                break;
            case  "check"://校对参保单
                result = check(conn,request);
                break;
            case "export"://导出参保单
                export(conn,request,response);
                return;
        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //核对
    private String check(Connection conn, HttpServletRequest request) {
        return  null;
    }

    //导出
    private void export(Connection conn, HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte category = Byte.parseByte(request.getParameter("category"));
        byte status = Byte.parseByte(request.getParameter("status"));
        switch (category){
            case 0://导出社保单
                if(status == 0){//导出新增
                   InsuranceService.exportSocial1(conn,response);
                }else {//导出停保
                   InsuranceService.exportSocial2(conn,response);
                }
                break;
            case 1://导出医保单
                if(status == 0){//导出续保
                    InsuranceService.exportMedicare1(conn,response);
                }else {//导出停保
                    InsuranceService.exportMedicare2(conn,response);
                }
                break;
            case 2://导出公积金
                break;
        }
    }

    //获取
    private String get(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        byte category = Byte.parseByte(request.getParameter("category"));
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        conditions.add("type","=",category);
        DaoQueryResult result = InsuranceDao.get(conn,conditions);
        return JSONObject.toJSONString(result);
    }

    //获取列表
    private String getList(Connection conn, HttpServletRequest request) {
        QueryParameter parameter =JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        byte category = Byte.parseByte(request.getParameter("category"));
       DaoQueryListResult result = InsuranceDao.getList(conn,parameter,category);
        return JSONObject.toJSONString(result);
    }

    //删除
    private String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        byte category = Byte.parseByte(request.getParameter("category"));
        DaoUpdateResult result = InsuranceDao.delete(conn,id,category);
        return JSONObject.toJSONString(result);
    }

    //修改
    private String update(Connection conn, HttpServletRequest request) {
        Insurance insurance =JSONObject.parseObject(request.getParameter("insurance"), Insurance.class);
        DaoUpdateResult result = InsuranceDao.update(conn,insurance);
        return JSONObject.toJSONString(result);
    }

    //批量插入
    private String insertBatch(Connection conn, HttpServletRequest request) {
        List<Insurance> insurances = JSONArray.parseArray(request.getParameter("insurances"),Insurance.class);
        DaoUpdateResult result = InsuranceService.insertBatch(conn,insurances);
        return  JSONObject.toJSONString(result);
    }

}
