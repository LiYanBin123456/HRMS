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
            case "insertFundRule"://插入公积金清单
                result = insertFundRule(conn,request);
                break;
            case "deleteFundRule"://删除公积金清单
                result = deleteFundRule(conn,request);
                break;
            case "updateFundRule"://修改公积金清单
                result = updateFundRule(conn,request);
                break;
            case "getFundRules"://获取所有公积金清单
                result = getFundRules(conn,request);
                break;
            case "getFundRule"://获取一个公积金清单
                result = getFundRule(conn,request);
                break;
            case "insertMedicalRule":
                result = insertMedicalRule(conn,request);
                break;
            case "deleteMedicalRule":
                result = deleteMedical(conn,request);
                break;
            case "updateMedicalRule":
                result = updateMedical(conn,request);
                break;
            case "getMedicalRules":
                result = getMedicalRules(conn,request);
                break;
            case "getMedicalRule":
                result = getMedicalRule(conn,request);
                break;
            case "insertSocialRule":
                result = insertSocialRule(conn,request);
                break;
            case "deleteSocialRule":
                result = deleteSocialRule(conn,request);
                break;
            case "updateSocialRule":
                result = updateSocialRule(conn,request);
                break;
            case "getSocialRules":
                result = getSocialRules(conn,request);
                break;
            case "getSocialRule":
                result = getSocialRule(conn,request);
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
    private String insertFundRule(Connection conn,HttpServletRequest request){
        RuleFund rule = JSONObject.parseObject(request.getParameter("rule"), RuleFund.class);
        DaoUpdateResult res= ruleFundService.insertFundRule(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //删除公积金规则
    private String deleteFundRule(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res= ruleFundService.deleteFundRule(conn,id);
        return JSONObject.toJSONString(res);
    }
    //修改公积金规则
    private String updateFundRule(Connection conn,HttpServletRequest request){

        RuleFund rule = JSONObject.parseObject(request.getParameter("rule"), RuleFund.class);
        DaoUpdateResult res = ruleFundService.updateFundRule(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //获取公积金规则列表
    private String getFundRules(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res = ruleFundService.getFundRules(conn,parameter);
        return JSONObject.toJSONString(res);
    }
    //获取公积金规则
    private String getFundRule(Connection conn,HttpServletRequest request){
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res = ruleFundService.getFundRule(conn,id);
        return JSONObject.toJSONString(res);
    }




    //增加医保规则
    private String insertMedicalRule(Connection conn, HttpServletRequest request) {
        RuleMedical rule = JSONObject.parseObject(request.getParameter("rule"), RuleMedical.class);
        DaoUpdateResult res = medicalService.insertMedicalRule(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //删除医保规则
    private String deleteMedical(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res= medicalService.deleteMedicalRule(conn,id);
        return JSONObject.toJSONString(res);
    }
    //修改医保规则
    private String updateMedical(Connection conn, HttpServletRequest request) {
        RuleMedical rule = JSONObject.parseObject(request.getParameter("rule"), RuleMedical.class);
        DaoUpdateResult res = medicalService.updateMedicalRule(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //获取所有医保规则
    private String getMedicalRules(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res =medicalService.getMedicalRules(conn,parameter);
        System.out.println(res);
        return JSONObject.toJSONString(res);
    }
    //获取医保规则
    private String getMedicalRule(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res= medicalService.getMedicalRule(conn,id);
        return JSONObject.toJSONString(res);
    }


    //增加社保规则
    private String insertSocialRule(Connection conn, HttpServletRequest request) {
        RuleSocial rule = JSONObject.parseObject(request.getParameter("rule"), RuleSocial.class);
        DaoUpdateResult res = socialService.insertSocialRule(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //删除社保规则
    private String deleteSocialRule(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res= socialService.deleteSocialRule(conn,id);
        return JSONObject.toJSONString(res);
    }
    //修改社保规则
    private String updateSocialRule(Connection conn, HttpServletRequest request) {
        RuleSocial rule = JSONObject.parseObject(request.getParameter("rule"), RuleSocial.class);
        DaoUpdateResult res = socialService.updateSocialRule(conn,rule);
        return JSONObject.toJSONString(res);
    }
    //获取社保规则
    private String getSocialRule(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res= socialService.getSocialRule(conn,id);
        return JSONObject.toJSONString(res);
    }
    //获取所有社保规则列表
    private String getSocialRules(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        DaoQueryListResult res =socialService.getSocialRules(conn,parameter);
        return JSONObject.toJSONString(res);
    }

}
