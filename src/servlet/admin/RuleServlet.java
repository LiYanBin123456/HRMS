package servlet.admin;

import bean.admin.Rule_fund;
import bean.admin.Rule_medical;
import bean.admin.Rule_social;
import com.alibaba.fastjson.JSONObject;
import database.*;
import service.admin.FundService;
import service.admin.Rule_medicalService;
import service.admin.Rule_socialService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(urlPatterns = "/rule")
public class RuleServlet extends HttpServlet {
    private Rule_socialService socialService = new Rule_socialService();
    private Rule_medicalService medicalService = new Rule_medicalService();
    private FundService fundService = new FundService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");

        switch (op) {
            case "getFunds"://获取所有公积金清单
                result = getFunds(conn,request);
                break;
            case "getFund"://获取一个公积金清单
                result = getFund(conn,request);
                break;
            case "updateFund"://修改公积金清单
                result = updateFund(conn,request);
                break;
            case "insertFund"://插入公积金清单
                result = insertFund(conn,request);
                break;
            case "deleteFund"://删除公积金清单
                result = deleteFund(conn,request);
                break;
            case "getMedicals"://获取所有公积金清单
                result = getMedicals(conn,request);
                break;
            case "getMedical"://获取一个公积金清单
                result = getMedical(conn,request);
                break;
            case "updateMedical"://修改公积金清单
                result = updateMedical(conn,request);
                break;
            case "insertMedical"://插入所有公积金清单
                result = insertMedical(conn,request);
                break;
            case "deleteMedical"://删除所有公积金清单
                result = deleteMedical(conn,request);
                break;
            case "getSocials"://获取所有公积金清单
                result = getSocials(conn,request);
                break;
            case "getSocial"://获取所有公积金清单
                result = getSocial(conn,request);
                break;
            case "updateSocial"://获取所有公积金清单
                result = updateSocial(conn,request);
                break;
            case "insertSocial"://获取所有公积金清单
                result = insertSocial(conn,request);
                break;
            case "deleteSocial"://获取所有公积金清单
                result = deleteSocial(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    //获取求职者列表
    private String getFunds(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res =fundService.getFundList(conn,parameter);
        return JSONObject.toJSONString(res);
    }

    private String getFund(Connection conn,HttpServletRequest request){
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res =fundService.getFund(conn,id);
        return JSONObject.toJSONString(res);
    }

    private String updateFund(Connection conn,HttpServletRequest request){

        Rule_fund fund = JSONObject.parseObject(request.getParameter("rule"), Rule_fund.class);
        System.out.println(fund.getStart());

        DaoUpdateResult res = fundService.updateFund(conn,fund);
        return JSONObject.toJSONString(res);
    }
    private String insertFund(Connection conn,HttpServletRequest request){
        Rule_fund fund = JSONObject.parseObject(request.getParameter("rule"), Rule_fund.class);
        DaoUpdateResult res=fundService.insertFund(conn,fund);
        return JSONObject.toJSONString(res);
    }

    private String deleteFund(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res= fundService.deleteFund(conn,id);
        return JSONObject.toJSONString(res);
    }

    private String getMedicals(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res =medicalService.getRule_medicalList(conn,parameter);
        System.out.println(res);
        return JSONObject.toJSONString(res);
    }

    private String getMedical(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res= medicalService.getMedical(conn,id);
        return JSONObject.toJSONString(res);
    }

    private String updateMedical(Connection conn, HttpServletRequest request) {
        Rule_medical medical = JSONObject.parseObject(request.getParameter("rule"), Rule_medical.class);
        DaoUpdateResult res = medicalService.updateMedical(conn,medical);
        return JSONObject.toJSONString(res);
    }

    private String insertMedical(Connection conn, HttpServletRequest request) {
        Rule_medical medical = JSONObject.parseObject(request.getParameter("rule"), Rule_medical.class);
        DaoUpdateResult res = medicalService.insertMedical(conn,medical);
        return JSONObject.toJSONString(res);
    }
    private String deleteMedical(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res= medicalService.deleteMedical(conn,id);
        return JSONObject.toJSONString(res);
    }
    private String getSocial(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res= socialService.getSocial(conn,id);
        return JSONObject.toJSONString(res);
    }
    private String getSocials(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        DaoQueryListResult res =socialService.getRule_socialList(conn,parameter);
        return JSONObject.toJSONString(res);
    }
    private String updateSocial(Connection conn, HttpServletRequest request) {
        Rule_social social = JSONObject.parseObject(request.getParameter("rule"), Rule_social.class);
        DaoUpdateResult res = socialService.updateSocial(conn,social);
        return JSONObject.toJSONString(res);
    }

    private String insertSocial(Connection conn, HttpServletRequest request) {
        Rule_social social = JSONObject.parseObject(request.getParameter("rule"), Rule_social.class);
        DaoUpdateResult res = socialService.insertSocial(conn,social);
        return JSONObject.toJSONString(res);
    }

    private String deleteSocial(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res= socialService.deleteSocial(conn,id);
        return JSONObject.toJSONString(res);
    }
}
