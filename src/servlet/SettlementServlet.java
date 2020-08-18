package servlet;

import bean.settlement.Settlement1;
import bean.settlement.Settlement2;
import bean.settlement.Settlement3;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import dao.settlement.Settlement3Dao;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;

@WebServlet(name = "SettlementServlet",urlPatterns = "/settlement")
public class SettlementServlet extends HttpServlet {
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
            case "insert"://添加一个客户
                result = insert(conn, request);
                break;
            case "delete"://删除客户
                result = delete(conn, request);
                break;
            case "saveAs"://复制
                result = saveAs(conn, request);
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
            case "backup"://补缴
                result = backup(conn, request);
                break;
            case "makeup"://补差
                result = makeup(conn, request);
                break;
            case "commit"://提交
                result = commit(conn, request);
                break;
            case "check"://获取客户服务信息
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
            case "exportBank"://导出银行卡
                result = exportBank(conn, request);
                break;
            case "getLogs"://查询日志
                result = getLogs(conn, request);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();

    }

    private String getList(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        QueryParameter param = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult result = null;
        switch (category){
            case 0://普通结算单
                result = Settlement1Service.getList(conn,param);
                break;
            case 1://小时工结算单
                result = Settlement2Service.getList(conn,param);
                break;
            case 2://商业保险结算单
              result = Settlement3Service.getList(conn,param);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String insert(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单
                Settlement1 settlement1 = JSONObject.parseObject(request.getParameter("settlement"), Settlement1.class);
                result = Settlement1Service.insert(conn,settlement1);
                break;
            case 1://小时工结算单
                Settlement2 settlement2 = JSONObject.parseObject(request.getParameter("settlement"), Settlement2.class);
                result = Settlement2Service.insert(conn,settlement2);
                break;
            case 2://商业保险结算单
                Settlement3 settlement3 = JSONObject.parseObject(request.getParameter("settlement"), Settlement3.class);
                result = Settlement3Service.insert(conn,settlement3);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String delete(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单
                result = Settlement1Service.delete(conn,id);
                break;
            case 1://小时工结算单
                result = Settlement2Service.delete(conn,id);
                break;
            case 2://商业保险结算单
                result = Settlement3Service.delete(conn,id);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String saveAs(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        Date month = Date.valueOf(request.getParameter("month"));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单

                break;
            case 1://小时工结算单
                break;
            case 2://商业保险结算单
                result = Settlement3Service.saveAs(conn,id,month);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String updateDetails(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String getDetails(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id "));
        QueryParameter param = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Detail1Service.getList(conn,param,id);
                break;
            case 1://小时工结算单明细
                result = Detail2Service.getList(conn,param,id);
                break;
            case 2://商业保险结算单明细
                result = Detail3Service.getList(conn,param,id);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String importDetails(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String exportDetails(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String backup(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String makeup(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String commit(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id "));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                break;
            case 1://小时工结算单明细
                break;
            case 2://商业保险结算单明细
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String check(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id "));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                break;
            case 1://小时工结算单明细
                break;
            case 2://商业保险结算单明细
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String reset(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id "));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                break;
            case 1://小时工结算单明细
                break;
            case 2://商业保险结算单明细
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String deduct(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id "));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                break;
            case 1://小时工结算单明细
                break;
            case 2://商业保险结算单明细
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String confirm(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id "));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                break;
            case 1://小时工结算单明细
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String exportBank(Connection conn, HttpServletRequest request) {
     return null;
    }

    private String getLogs(Connection conn, HttpServletRequest request) {
     return null;
    }
}
