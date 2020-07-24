package servlet.admin;

import bean.admin.RuleFund;
import bean.admin.RuleMedical;
import bean.admin.RuleSocial;
import com.alibaba.fastjson.JSONObject;
import database.*;
import service.admin.RuleFundService;
import service.admin.RuleMedicalService;
import service.admin.RuleSocialService;

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
    private RuleSocialService socialService = new RuleSocialService();
    private RuleMedicalService medicalService = new RuleMedicalService();
    private RuleFundService ruleFundService = new RuleFundService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");

        switch (op) {
            case "insertFund"://插入公积金清单
                result = insertFund(conn,request);
                break;
            case "deleteFund"://删除公积金清单
                result = deleteFund(conn,request);
                break;
            case "updateFund"://修改公积金清单
                result = updateFund(conn,request);
                break;
            case "getFunds"://获取所有公积金清单
                result = getFunds(conn,request);
                break;
            case "getFund"://获取一个公积金清单
                result = getFund(conn,request);
                break;
            case "insertMedical":
                result = insertMedical(conn,request);
                break;
            case "deleteMedical":
                result = deleteMedical(conn,request);
                break;
            case "updateMedical":
                result = updateMedical(conn,request);
                break;
            case "getMedicals":
                result = getMedicals(conn,request);
                break;
            case "getMedical":
                result = getMedical(conn,request);
                break;
            case "insertSocial":
                result = insertSocial(conn,request);
                break;
            case "deleteSocial":
                result = deleteSocial(conn,request);
                break;
            case "updateSocial":
                result = updateSocial(conn,request);
                break;
            case "getSocials":
                result = getSocials(conn,request);
                break;
            case "getSocial":
                result = getSocial(conn,request);
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

    //插入公积金规则
    private String insertFund(Connection conn,HttpServletRequest request){
        RuleFund rule = JSONObject.parseObject(request.getParameter("rule"), RuleFund.class);
        DaoUpdateResult res= ruleFundService.insertFund(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //删除公积金规则
    private String deleteFund(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res= ruleFundService.deleteFund(conn,id);
        return JSONObject.toJSONString(res);
    }
    //修改公积金规则
    private String updateFund(Connection conn,HttpServletRequest request){

        RuleFund rule = JSONObject.parseObject(request.getParameter("rule"), RuleFund.class);
        DaoUpdateResult res = ruleFundService.updateFund(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //获取公积金规则列表
    private String getFunds(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res = ruleFundService.getFundList(conn,parameter);
        return JSONObject.toJSONString(res);
    }
    //获取公积金规则
    private String getFund(Connection conn,HttpServletRequest request){
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res = ruleFundService.getFund(conn,id);
        return JSONObject.toJSONString(res);
    }




    //增加医保规则
    private String insertMedical(Connection conn, HttpServletRequest request) {
        RuleMedical rule = JSONObject.parseObject(request.getParameter("rule"), RuleMedical.class);
        DaoUpdateResult res = medicalService.insertMedical(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //删除医保规则
    private String deleteMedical(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res= medicalService.deleteMedical(conn,id);
        return JSONObject.toJSONString(res);
    }
    //修改医保规则
    private String updateMedical(Connection conn, HttpServletRequest request) {
        RuleMedical rule = JSONObject.parseObject(request.getParameter("rule"), RuleMedical.class);
        DaoUpdateResult res = medicalService.updateMedical(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //获取所有医保规则
    private String getMedicals(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res =medicalService.getRule_medicalList(conn,parameter);
        System.out.println(res);
        return JSONObject.toJSONString(res);
    }
    //获取医保规则
    private String getMedical(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res= medicalService.getMedical(conn,id);
        return JSONObject.toJSONString(res);
    }


    //增加社保规则
    private String insertSocial(Connection conn, HttpServletRequest request) {
        RuleSocial rule = JSONObject.parseObject(request.getParameter("rule"), RuleSocial.class);
        DaoUpdateResult res = socialService.insertSocial(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //删除社保规则
    private String deleteSocial(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res= socialService.deleteSocial(conn,id);
        return JSONObject.toJSONString(res);
    }
    //修改社保规则
    private String updateSocial(Connection conn, HttpServletRequest request) {
        RuleSocial rule = JSONObject.parseObject(request.getParameter("rule"), RuleSocial.class);
        DaoUpdateResult res = socialService.updateSocial(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //获取社保规则
    private String getSocial(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res= socialService.getSocial(conn,id);
        return JSONObject.toJSONString(res);
    }
    //获取所有社保规则列表
    private String getSocials(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        DaoQueryListResult res =socialService.getRule_socialList(conn,parameter);
        return JSONObject.toJSONString(res);
    }

}
