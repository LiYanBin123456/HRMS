package servlet;


import bean.admin.Account;
import bean.settlement.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import dao.settlement.*;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;
import service.employee.SettingService;
import service.settlement.*;

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
            case "insert"://添加
                result = insert(conn, request);
                break;
            case "delete"://删除
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
            case "replaceDetails"://替换明细
                result = replaceDetails(conn, request);
                break;
            case "confirmDetails1"://确认商业保险新增
                result = confirmDetails1(conn, request);
                break;
            case "confirmDetails2"://确认商业保险替换
                result = confirmDetails2(conn, request);
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
            case "updateExtra"://修改结算单额外信息
                result = update(conn, request);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();

    }

    //修改普通结算单额外信息
    private String update(Connection conn, HttpServletRequest request) {
        Settlement1 settlement1 = JSON.parseObject(request.getParameter("settlement"),Settlement1.class);
        DaoUpdateResult result = Settlement1Dao.updateExtra(conn,settlement1);
        return JSONObject.toJSONString(result);
    }

    //保存并且计算结算单明细
    private String saveDetail(Connection conn, HttpServletRequest request) {
        int category = Integer.parseInt(request.getParameter("category"));//类别
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        long cid = Long.parseLong(request.getParameter("cid"));//合作单位id
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                result = Detail1Service.saveDetail(conn,sid,cid);
                break;
            case 1://小时工结算单明细
                result = Detail2Service.saveDetail(conn,sid);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    //保存并且计算结算单
    private String saveSettlement(Connection conn, HttpServletRequest request) {
        int category = Integer.parseInt(request.getParameter("category"));
        long sid = Long.parseLong(request.getParameter("sid"));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单
                result =Settlement1Service.saveSettlement(conn,sid);
                break;
            case 1://小时工结算单
                result =Settlement2Service.saveSettlement(conn,sid);
                break;
           /* case 2://商业保险结算单
                result =Settlement3Service.saveSettlement(conn,sid);
                break;*/
        }
        return JSONObject.toJSONString(result);
    }

    //获取列表
    private String getList(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
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

    //插入结算单
    private String insert(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));//结算单类型
        boolean needDetail = Boolean.parseBoolean(request.getParameter("needDetail"));//是否自动生成明细 0 不 1 自动生成
        boolean needCalculateSocial = Boolean.parseBoolean(request.getParameter("needCalculateSocial"));//是否自动生成明细 0 不 1 自动生成
        DaoUpdateResult result = null;
        HttpSession session = request.getSession();
        //获取管理员所属的公司id
        long rid = ((Account) session.getAttribute("account")).getRid();
        switch (category){
            case 0://普通结算单
                Settlement1 settlement1 = JSONObject.parseObject(request.getParameter("settlement"), Settlement1.class);
                settlement1.setDid(rid);
                settlement1.setNeedCalculateSocial(needCalculateSocial);
                result = Settlement1Service.insert(conn,settlement1,needDetail);
                break;
            case 1://小时工结算单
                Settlement2 settlement2 = JSONObject.parseObject(request.getParameter("settlement"), Settlement2.class);
                settlement2.setDid(rid);
                result = Settlement2Service.insert(conn,settlement2,needDetail);
                break;
            case 2://商业保险结算单
                Settlement3 settlement3 = JSONObject.parseObject(request.getParameter("settlement"), Settlement3.class);
                settlement3.setDid(rid);
                result = Settlement3Service.insert(conn,settlement3,needDetail);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    //删除结算单
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

    //另存为结算单
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

    //批量修改明细
    private String updateDetails(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        DaoUpdateResult result = null;
        switch (category){
            case 0://普通结算单明细
                List<ViewDetail1> detail1s = JSONArray.parseArray(request.getParameter("details"),ViewDetail1.class);
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

    //获取明细
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

    //导入明细
    private String importDetails(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));//结算单id
        DaoUpdateResult result = null;
        HttpSession session = request.getSession();
        long did = ((Account) session.getAttribute("account")).getRid();//当前操作的管理员所属公司id
        switch (category){
            case 0://普通结算单明细
                List<ViewDetail1> detail1s = JSONArray.parseArray(request.getParameter("details"),ViewDetail1.class);
                result = Detail1Service.importDetails(conn,id,detail1s,did);
                break;
            case 1://小时工结算单明细
                List<ViewDetail2> ViewDetail2s = JSONArray.parseArray(request.getParameter("details"),ViewDetail2.class);
                result = Detail2Service.importDetails(conn,id,ViewDetail2s,did);
                break;
            case 2://商业保险结算单明细
                List<Detail3> detail3s = JSONArray.parseArray(request.getParameter("details"),Detail3.class);
                result = Detail3Service.importDetails(conn,id,detail3s);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    //替换明细
    private String replaceDetails(Connection conn, HttpServletRequest request) {
        List<Detail3> member1 = JSONArray.parseArray(request.getParameter("member1"),Detail3.class);
        List<Detail3> member2 = JSONArray.parseArray(request.getParameter("member2"),Detail3.class);

        DaoUpdateResult res = Detail3Service.replaceDetails(conn,member1,member2);
        return JSONObject.toJSONString(res);
    }

    //确认商业保险新增
    private String confirmDetails1(Connection conn, HttpServletRequest request) {
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        byte day = Byte.parseByte(request.getParameter("day"));
        String[] ids = request.getParameterValues("ids[]");

        DaoUpdateResult res = Detail3Service.confirmDetails(conn,sid,ids,day);
        return JSONObject.toJSONString(res);
    }

    //确认商业保险替换
    private String confirmDetails2(Connection conn, HttpServletRequest request) {
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        byte day = Byte.parseByte(request.getParameter("day"));
        String[] ids1 = request.getParameterValues("ids1[]");
        String[] ids2 = request.getParameterValues("ids2[]");

        DaoUpdateResult res = Detail3Service.confirmDetails(conn,sid,ids1,ids2,day);
        return JSONObject.toJSONString(res);
    }

    //导出明细
    private String exportDetails(Connection conn, HttpServletRequest request) {
        return null;
    }

    //补缴
    private String backup(Connection conn, HttpServletRequest request) {
        String start = request.getParameter("start");//起始月份
        String end = request.getParameter("end");//结束月份
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        List<JSONObject> employees = JSONArray.parseArray(request.getParameter("employees"),JSONObject.class);
        DaoUpdateResult result = Settlement1Service.backup(start,end,sid,employees,conn);
        return JSONObject.toJSONString(result);
    }

    //补差
    private String makeup(Connection conn, HttpServletRequest request) {
        String start = request.getParameter("start");//起始月份
        String end = request.getParameter("end");//结束月份
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        String[] eids = request.getParameterValues("eids[]");//员工id数组

        DaoUpdateResult result = Settlement1Service.makeup(conn,eids,start,end,sid);
        return JSONObject.toJSONString(result);

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

    //确认发放
    private String confirm(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));//结算单id
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

    //删除明细
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

}
