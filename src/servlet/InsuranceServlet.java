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
import utills.XlsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.Connection;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


@WebServlet(name = "InsuranceServlet",urlPatterns = "/verify/insurance")
@MultipartConfig
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
            case "check"://校对参保单
                result = check(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //校对参保单
    private String check(Connection conn, HttpServletRequest request) throws IOException, ServletException {
        DaoUpdateResult result=null;
        byte category = Byte.parseByte(request.getParameter("category"));//判断是社保，医保，公积金
        byte type = Byte.parseByte(request.getParameter("status"));//判断社保的类型
        Part part = request.getPart("file");
        InputStream is=null;
        List<JSONObject> data;
        try {//获取part中的文件，读取数据
             is = part.getInputStream();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        switch (category){
            case 0://校对社保
                data = XlsUtil.readSocial(is,0);//读第一个sheet
                result = InsuranceService.checkSocial(conn,data,type);
                break;
            case 1://校对医保
                data = XlsUtil.readMedicare(is,0);
                result = InsuranceService.checkMedicare(conn,data);
                break;
            case 2://校对公积金
                data = XlsUtil.readFund(is,0);
                result = InsuranceService.checkFund(conn,data);
                break;
        }
        return  JSONObject.toJSONString(result);
    }

    //获取
    private String get(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        DaoQueryResult result = InsuranceDao.get(conn,conditions);
        return JSONObject.toJSONString(result);
    }

    //获取列表
    private String getList(Connection conn, HttpServletRequest request) {
        QueryParameter parameter =JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        return JSONObject.toJSONString(result);
    }

    //删除
    private String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult result = InsuranceDao.delete(conn,id);
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
