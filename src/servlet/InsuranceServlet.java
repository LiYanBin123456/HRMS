package servlet;


import bean.admin.Account;
import bean.insurance.Insurance;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.insurance.InsuranceDao;
import database.*;
import service.insurance.InsuranceService;
import utills.excel.SchemeDefined;
import utills.excel.XlsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
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
            case "insertAndupdate"://插入参保单
                result = insertAndupdate(conn,request);
                break;
            case "insertBatch"://批量插入参保单
                result = insertBatch(conn,request);
                break;
            case "update"://修改参保单
                result = update(conn,request);
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
        byte type = Byte.parseByte(request.getParameter("status"));//判断校对的类型 0养老 1工伤 2失业 3医疗 4工伤
        Part part = request.getPart("file");
        InputStream is=null;
        JSONArray data;
        try {//获取part中的文件，读取数据
             is = part.getInputStream();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        switch (type){
            case 0://校对养老
            case 1://校对社保
            case 2://校对社保
                data = XlsUtil.read(is, SchemeDefined.SCHEME_SOCIAL,1);
                result = InsuranceService.checkSocial(conn,data,type);
                break;
            case 3://校对医保
                data = XlsUtil.read(is,SchemeDefined.SCHEME_MEDICAL,1);
                result = InsuranceService.checkMedicare(conn,data);
                break;
            case 4://校对公积金
                data = XlsUtil.read(is,SchemeDefined.SCHEME_FUND,1);
                result = InsuranceService.checkFund(conn,data);
                break;
        }
        return  JSONObject.toJSONString(result);
    }

    //获取
    private String get(Connection conn, HttpServletRequest request) {
        long eid = Long.parseLong(request.getParameter("eid"));
        DaoQueryListResult result = InsuranceDao.getList(conn,eid);
        return JSONObject.toJSONString(result);
    }

    //获取列表
    private String getList(Connection conn, HttpServletRequest request) {
        QueryParameter parameter =JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        Account user = (Account) request.getSession().getAttribute("account");

        parameter.addCondition("did", "=", user.getRid());
        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        return JSONObject.toJSONString(result);
    }

    //修改
    private String update(Connection conn, HttpServletRequest request) {
        List<Insurance> insurances = JSONArray.parseArray(request.getParameter("insurances"),Insurance.class);
        return InsuranceService.update(conn,insurances);
    }

    //批量插入
    private String insertAndupdate(Connection conn, HttpServletRequest request) {
        List<Insurance> insurances_insert = JSONArray.parseArray(request.getParameter("insurances_insert"),Insurance.class);
        List<Insurance> insurances_update = JSONArray.parseArray(request.getParameter("insurances_update"),Insurance.class);

        long eid = Long.parseLong(request.getParameter("eid"));
        return InsuranceService.insertAndupdate(conn,eid,insurances_insert,insurances_update);
    }

    //批量设置五险一金
    private String insertBatch(Connection conn, HttpServletRequest request) {
        List<Insurance> insurances = JSONArray.parseArray(request.getParameter("insurances"),Insurance.class);
        String[] eids = request.getParameterValues("eids[]");//获取员工的eids
        return InsuranceService.insertBatch(conn,eids,insurances);
    }

}
