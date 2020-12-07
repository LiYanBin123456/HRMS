package servlet;

import bean.admin.Account;
import bean.settlement.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.settlement.*;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;
import service.settlement.*;

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

//特殊结算单
@WebServlet(name = "Settlement0Servlet",urlPatterns = "/verify/settlement0")
public class Settlement0Servlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();
        String op = request.getParameter("op");
        switch (op) {
            case "getList"://获取所有客户清单
                result = getList(conn, request);
                break;
            case "insert"://添加
                result = insert(conn, request);
                break;
            case "delete"://删除
                result = delete(conn, request);
                break;
            case "updateDetails"://修改明细
                result = updateDetails(conn, request);
                break;
            case "getDetails"://获取明细列表
                result = getDetails(conn, request);
                break;
            case "importDetails"://导入明细
                result = importDetails(conn, request);
                break;
            case "exportDetails"://导出明细
                result = exportDetails(conn, request);
                break;
            case "commit"://提交
                result = commit(conn, request);
                break;
            case "check"://审核
                result = check(conn, request);
                break;
            case "reset"://重置
                result = reset(conn, request);
                break;
            case "deduct"://扣款
                result = deduct(conn, request);
                break;
            case "confirm"://确认到账
                result = confirm(conn, request);
                break;
            case "getLogs"://查询日志
                result = getLogs(conn, request);
                break;
            case "deleteDetail"://删除结算单明细
                result = deleteDetail(conn, request);
                break;
            case "saveSettlement"://保存结算单
                result = saveSettlement(conn, request);
                break;
            case "saveDetail"://保存结算单明细
                result = saveDetail(conn, request);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();

    }


    //保存并且计算结算单明细
    private String saveDetail(Connection conn, HttpServletRequest request) {

        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        DaoUpdateResult result;
        result = Detail0Service.saveDetail(conn,sid);
        return JSONObject.toJSONString(result);
    }

    //保存并且计算结算单
    private String saveSettlement(Connection conn, HttpServletRequest request) {
        long sid = Long.parseLong(request.getParameter("sid"));
        DaoUpdateResult result;
        result = Settlement0Service.saveSettlement(conn,sid);
        return JSONObject.toJSONString(result);
    }

    //获取列表
    private String getList(Connection conn, HttpServletRequest request) {
        QueryParameter param = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        if(user.getRole()==Account.ROLE_DISPATCH){
            if(user.isAdmin()) {
                param.addCondition("did", "=", user.getRid());
            }else {
                param.addCondition("aid", "=", user.getId());
            }
        }else if(user.getRole()==Account.ROLE_COOPERATION){
            param.addCondition("cid","=",user.getRid());
        }
        DaoQueryListResult result=Settlement0Service.getList(conn,param);
        return JSONObject.toJSONString(result);
    }

    //插入结算单
    private String insert(Connection conn, HttpServletRequest request) {
        byte type = Byte.parseByte(request.getParameter("type"));//是否自动生成明细 0 不 1 自动生成
        DaoUpdateResult result = null;
        HttpSession session = request.getSession();
        //获取管理员所属的公司id
        long rid = ((Account) session.getAttribute("account")).getRid();
        Settlement0 settlement0 = JSONObject.parseObject(request.getParameter("settlement"), Settlement0.class);
        settlement0.setDid(rid);
        result = Settlement0Service.insert(conn,settlement0,type);
        return JSONObject.toJSONString(result);
    }

    //删除结算单
    private String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult result = Settlement0Service.delete(conn,id);
        return JSONObject.toJSONString(result);
    }

    //批量修改明细
    private String updateDetails(Connection conn, HttpServletRequest request) {
        List<Detail0> details = JSONArray.parseArray(request.getParameter("details"),Detail0.class);
        DaoUpdateResult result = Detail0Service.update(conn,details);
        return JSONObject.toJSONString(result);
    }

    //获取明细
    private String getDetails(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        QueryParameter param = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult result = Detail0Service.getList(conn,param,id);
        return JSONObject.toJSONString(result);
    }

    //导入明细
    private String importDetails(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));//结算单id
        DaoUpdateResult result = null;
        HttpSession session = request.getSession();
        long did = ((Account) session.getAttribute("account")).getRid();//当前操作的管理员所属公司id
        List<ViewDetail0> details = JSONArray.parseArray(request.getParameter("details"),ViewDetail0.class);
        result = Detail0Service.importDetails(conn,id,details,did);

        return JSONObject.toJSONString(result);
    }

    //删除明细
    private String deleteDetail(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult result = Detail0Dao.delete(conn,id);
        return JSONObject.toJSONString(result);
    }

    //导出明细
    private String exportDetails(Connection conn, HttpServletRequest request) {
        return null;
    }

    //提交
    private String commit(Connection conn, HttpServletRequest request) {
        Account user = (Account) request.getSession().getAttribute("account");
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Settlement1Service.commit(conn, id, user);
                break;
            case 1://小时工结算单明细
                result = Settlement2Service.commit(conn, id, user);
                break;
            /*case 2://商业保险结算单明细
                result = Settlement3Service.commit(conn, id, user);
                break;*/
        }
        return JSONObject.toJSONString(result);
    }

    //审核
    private String check(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        byte type = Byte.parseByte(request.getParameter("type"));
        boolean pass = Boolean.parseBoolean(request.getParameter("pass"));
        String reason = request.getParameter("reason");
        Account user = (Account) request.getSession().getAttribute("account");
        DaoUpdateResult res = null;
        switch (category){
            case 0://普通结算单明细
                res = Settlement1Service.check(conn,id,type,pass,reason,user);
                break;
            case 1://小时工结算单明细
                res = Settlement2Service.check(conn,id,type,pass,reason,user);
                break;
            /*case 2://商业保险结算单明细
                res = Settlement3Service.check(conn,id,type,pass,reason,user);
                break;*/
        }
        return JSONObject.toJSONString(res);
    }

    //重置
    private String reset(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        Account user = (Account) request.getSession().getAttribute("account");
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Settlement1Service.reset(conn, id, user);
                break;
            case 1://小时工结算单明细
                result = Settlement2Service.reset(conn, id, user);
                break;
            /*case 2://商业保险结算单明细
                result = Settlement3Service.reset(conn, id, user);
                break;*/
        }
        return JSONObject.toJSONString(result);
    }

    //确认扣款
    private String deduct(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        Account user = (Account) request.getSession().getAttribute("account");
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Settlement1Service.deduct(conn,id,user);
                break;
            case 1://小时工结算单明细
                result = Settlement2Service.deduct(conn,id,user);
                break;
            /*case 2://商业保险结算单明细
                result = Settlement3Service.deduct(conn,id,user);
                break;*/
        }
        return JSONObject.toJSONString(result);
    }

    //确认到账
    private String confirm(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        Account user = (Account) request.getSession().getAttribute("account");
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Settlement1Service.confirm(conn, id, user);
                break;
            case 1://小时工结算单明细
                result = Settlement2Service.confirm(conn, id, user);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    //获取日志
    private String getLogs(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        QueryParameter param = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Settlement1Service.getLogs(conn,id,param);
                break;
            case 1://小时工结算单明细
                result = Settlement2Service.getLogs(conn,id,param);
                break;
            case 2://商业保险结算单明细
                result = Settlement3Service.getLogs(conn,id,param);
                break;
        }
        return JSONObject.toJSONString(result);
    }


}
