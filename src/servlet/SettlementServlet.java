package servlet;


import bean.settlement.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import dao.settlement.Detail1Dao;
import dao.settlement.Detail2Dao;
import dao.settlement.Detail3Dao;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "SettlementServlet",urlPatterns = "/verify/settlement")
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
            case "deleteDetail"://删除结算单明细
                result = deleteDetail(conn, request);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();

    }

    //删除结算单明细
    private String deleteDetail(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult result = null;
        switch (category){
            case 0://删除普通结算明细
                result = Detail1Dao.delete(conn,id);
                break;
            case 1://删除小时工结算明细
                result = Detail2Dao.delete(conn,id);
                break;
            case 2://删除商业结算明细
                result = Detail3Dao.delete(conn,id);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String getList(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        QueryParameter param = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult result = null;
        HttpSession session = request.getSession();
        byte role = (byte) session.getAttribute("role");
        long rid = (long) session.getAttribute("rid");
        switch (category){
            case 0://普通结算单
                if(role==1){
                    param.addCondition("did","=",rid);
                }else if(role==2){
                    param.addCondition("cid","=",rid);
                }
                result = Settlement1Service.getList(conn,param);
                break;
            case 1://小时工结算单
                if(role==1){
                    param.addCondition("did","=",rid);
                }else if(role==2){
                    param.addCondition("cid","=",rid);
                }
                result = Settlement2Service.getList(conn,param);
                break;
            case 2://商业保险结算单
                if(role==1){
                    param.addCondition("did","=",rid);
                }else if(role==2){
                    param.addCondition("cid","=",rid);
                }
              result = Settlement3Service.getList(conn,param);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String insert(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        DaoUpdateResult result = null;
        HttpSession session = request.getSession();
        //获取管理员所属的公司i
        long rid = (long) session.getAttribute("rid");
        switch (category){
            case 0://普通结算单
                Settlement1 settlement1 = JSONObject.parseObject(request.getParameter("settlement"), Settlement1.class);
                settlement1.setDid(rid);
                result = Settlement1Service.insert(conn,settlement1);
                break;
            case 1://小时工结算单
                Settlement2 settlement2 = JSONObject.parseObject(request.getParameter("settlement"), Settlement2.class);
                settlement2.setDid(rid);
                result = Settlement2Service.insert(conn,settlement2);
                break;
            case 2://商业保险结算单
                Settlement3 settlement3 = JSONObject.parseObject(request.getParameter("settlement"), Settlement3.class);
                settlement3.setDid(rid);
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
        String mont=request.getParameter("month");
        Date month = Date.valueOf(mont+"-"+"01");
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单
                result = Settlement1Service.saveAs(conn,id,month);
                break;
            case 1://小时工结算单
                result = Settlement2Service.saveAs(conn,id,month);
                break;
            case 2://商业保险结算单
                result = Settlement3Service.saveAs(conn,id,month);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String updateDetails(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                List<Detail1> detail1s = JSONArray.parseArray(request.getParameter("details"),Detail1.class);
                result = Detail1Service.update(conn,detail1s);
                break;
            case 1://小时工结算单明细
                List<Detail2> detail2s = JSONArray.parseArray(request.getParameter("details"),Detail2.class);
                result = Detail2Service.update(conn,detail2s);
                break;
            case 2://商业保险结算单明细
                List<Detail3> detail3s = JSONArray.parseArray(request.getParameter("details"),Detail3.class);
                result = Detail3Service.update(conn,detail3s);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String getDetails(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
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
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult result = null;
        HttpSession session = request.getSession();
        long did = (long) session.getAttribute("rid");//当前操作的管理员所属公司id
        switch (category){
            case 0://普通结算单明细
                List<Detail1> detail1s = JSONArray.parseArray(request.getParameter("details"),Detail1.class);
                result = Detail1Service.importDetails(conn,id,detail1s,did);
                break;
            case 1://小时工结算单明细
                List<ViewDetail2> ViewDetail2s = JSONArray.parseArray(request.getParameter("details"),ViewDetail2.class);
                result = Detail2Service.importDetails(conn,id,ViewDetail2s,did);
                break;
            case 2://商业保险结算单明细
                List<ViewDetail3> viewDetail3s = JSONArray.parseArray(request.getParameter("details"),ViewDetail3.class);
                result = Detail3Service.importDetails(conn,id,viewDetail3s,did);
                break;
        }
        return JSONObject.toJSONString(result);
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
        Long aid = (Long) request.getSession().getAttribute("id");
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Settlement1Service.commit(conn, id, aid);
                break;
            case 1://小时工结算单明细
                result = Settlement2Service.commit(conn, id, aid);
                break;
            case 2://商业保险结算单明细
                result = Settlement3Service.commit(conn, id, aid);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String check(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        Long aid = (Long) request.getSession().getAttribute("id");
        byte status = Byte.parseByte(request.getParameter("status"));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Settlement1Service.check(conn,id,aid,status);
                break;
            case 1://小时工结算单明细
                result = Settlement2Service.check(conn,id,aid,status);
                break;
            case 2://商业保险结算单明细
                result = Settlement3Service.check(conn,id,aid,status);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String reset(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        Long aid = (Long) request.getSession().getAttribute("id");
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Settlement1Service.reset(conn, id, aid);
                break;
            case 1://小时工结算单明细
                result = Settlement2Service.reset(conn, id, aid);
                break;
            case 2://商业保险结算单明细
                result = Settlement3Service.reset(conn, id, aid);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String deduct(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        Long aid = (Long) request.getSession().getAttribute("id");
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Settlement1Service.deduct(conn,id,aid);
                break;
            case 1://小时工结算单明细
                result = Settlement2Service.deduct(conn,id,aid);
                break;
            case 2://商业保险结算单明细
                result = Settlement3Service.deduct(conn,id,aid);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String confirm(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        Long aid = (Long) request.getSession().getAttribute("id");
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Settlement1Service.confirm(conn, id, aid);
                break;
            case 1://小时工结算单明细
                result = Settlement2Service.confirm(conn, id, aid);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    private String exportBank(Connection conn, HttpServletRequest request) {
     return null;
    }

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

    //下载小时工模板
    private String DownLoadHour(Connection conn, HttpServletRequest request) {
        return null;
    }
}
