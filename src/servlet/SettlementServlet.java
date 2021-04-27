package servlet;


import bean.admin.Account;
import bean.employee.Employee;
import bean.settlement.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.settlement.*;
import database.*;
import service.settlement.*;
import utills.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
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
//            case "fillup"://补缴
//                result = fillup(conn, request);
//                break;
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
            case "charge"://扣款
                result = charge(conn, request);
                break;
            case "payroll"://确认发放
                result = payroll(conn, request);
                break;
            case "getLogs"://查询日志
                result = getLogs(conn, request);
                break;
            case "deleteDetail"://删除结算单明细
                result = deleteDetail(conn, request);
                break;
            case "calculate"://计算结算单
                result = calculate(conn, request);
                break;
            case "calcDetail"://计算结算单明细
                result = calcDetail(conn, request);
                break;
            /*case "updateExtra"://修改结算单额外信息
                result = update(conn, request);
                break;*/
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();

    }

    /*//修改普通结算单额外信息
    private String update(Connection conn, HttpServletRequest request) {
        Settlement1 settlement1 = JSON.parseObject(request.getParameter("settlement"),Settlement1.class);
        DaoUpdateResult result = Settlement1Dao.updateExtra(conn,settlement1);
        return JSONObject.toJSONString(result);
    }*/

    //保存并且计算结算单明细
    private String calcDetail(Connection conn, HttpServletRequest request) {
        int category = Integer.parseInt(request.getParameter("category"));//结算单类别 1 普通结算单 2小时工结算单 3商业保险结算单 4 特殊结算单
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        long cid = Long.parseLong(request.getParameter("cid"));//合作单位id

        DaoUpdateResult result = null;
        switch (category){
            case 1://普通结算单
                return DetailService1.calcDetail(conn,sid,cid);
            case 2://小时工结算单
                return DetailService2.calcDetail(conn,sid);
            case 4://特殊结算单
                return DetailService4.calcDetail(conn,sid);
        }
        return JSONObject.toJSONString(result);
    }

    //保存并且计算结算单
    private String calculate(Connection conn, HttpServletRequest request) {
        int category = Integer.parseInt(request.getParameter("category"));
        long sid = Long.parseLong(request.getParameter("sid"));
        DaoUpdateResult result = null;
        switch (category){
            case 1://普通结算单
                result = SettlementService1.calculate(conn,sid);
                break;
            case 2://小时工结算单
                result = SettlementService2.saveSettlement(conn,sid);
                break;
            case 3://商业保险结算单
                result =SettlementService3.calculate(conn,sid);
                break;
            case 4://特殊结算单
                result = SettlementService4.calSettlement(conn,sid);
                break;
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
            case 1://普通结算单
                result = SettlementService1.getList(conn,param);
                break;
            case 2://小时工结算单
                result = SettlementService2.getList(conn,param);
                break;
            case 3://商业保险结算单
              result = SettlementService3.getList(conn,param);
                break;
            case 4://特殊结算单
              result = SettlementService4.getList(conn,param);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    //插入结算单
    private String insert(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));//结算单类型
        boolean needDetail = Boolean.parseBoolean(request.getParameter("needDetail"));//是否自动生成明细 0 不 1 自动生成
        boolean needCalculateSocial = Boolean.parseBoolean(request.getParameter("needCalculateSocial"));//是否计算社保

        DaoUpdateResult result = null;
        HttpSession session = request.getSession();
        //获取管理员所属的公司id
        long rid = ((Account) session.getAttribute("account")).getRid();
        switch (category){
            case 1://普通结算单
                Settlement1 settlement1 = JSONObject.parseObject(request.getParameter("settlement"), Settlement1.class);
                settlement1.setDid(rid);
                settlement1.setNeedCalculateSocial(needCalculateSocial);
                result = SettlementService1.insert(conn,settlement1,needDetail);
                break;
            case 2://小时工结算单
                Settlement2 settlement2 = JSONObject.parseObject(request.getParameter("settlement"), Settlement2.class);
                settlement2.setDid(rid);
                result = SettlementService2.insert(conn,settlement2,needDetail);
                break;
            case 3://商业保险结算单
                Settlement3 settlement3 = JSONObject.parseObject(request.getParameter("settlement"), Settlement3.class);
                settlement3.setDid(rid);
                result = SettlementService3.insert(conn,settlement3,needDetail);
                break;
            case 4:
                Settlement4 settlement4 = JSONObject.parseObject(request.getParameter("settlement"), Settlement4.class);
                byte employee_category= Byte.parseByte(request.getParameter("employee_category"));
                settlement4.setDid(rid);
                result = SettlementService4.insert(conn,settlement4,needDetail,employee_category);
        }
        return JSONObject.toJSONString(result);
    }

    //删除结算单
    private String delete(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult result = null;
        switch (category){
            case 1://普通结算单
                result = SettlementService1.delete(conn,id);
                break;
            case 2://小时工结算单
                result = SettlementService2.delete(conn,id);
                break;
            case 3://商业保险结算单
                result = SettlementService3.delete(conn,id);
                break;
            case 4://特殊结算单
                result = SettlementService4.delete(conn,id);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    //另存为结算单
    private String saveAs(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        String mont=request.getParameter("month");
        Date month = DateUtil.parse(mont+"-"+"01","yyyy-MM-dd");
        DaoUpdateResult result = null;
        switch (category){
            case 1://普通结算单
                result = SettlementService1.saveAs(conn,id,month);
                break;
            case 2://小时工结算单
                result = SettlementService2.saveAs(conn,id,month);
                break;
            case 3://商业保险结算单
                result = SettlementService3.saveAs(conn,id,month);
                break;
            case 4://特殊结算单
                result = SettlementService4.saveAs(conn,id,month);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    //批量修改明细
    private String updateDetails(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        DaoUpdateResult result = null;
        switch (category){
            case 1://普通结算单
                List<ViewDetail1> details1 = JSONArray.parseArray(request.getParameter("details"),ViewDetail1.class);
                result = DetailService1.update(conn,details1);
                break;
            case 2://小时工结算单
                List<Detail2> details2 = JSONArray.parseArray(request.getParameter("details"),Detail2.class);
                result = DetailService2.update(conn,details2);
                break;
            case 3://商业保险结算单
                List<Detail3> details3 = JSONArray.parseArray(request.getParameter("details"),Detail3.class);
                result = DetailService3.update(conn,details3);
                break;
            case 4://特殊结算单
                List<Detail4> details4 = JSONArray.parseArray(request.getParameter("details"),Detail4.class);
                result = DetailService4.update(conn,details4);
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
            case 1://普通结算单
                result = DetailService1.getList(conn,param,id);
                break;
            case 2://小时工结算单
                result = DetailService2.getList(conn,param,id);
                break;
            case 3://商业保险结算单
                result = DetailService3.getList(conn,param,id);
                break;
            case 4://特殊结算单
                result = DetailService4.getList(conn,param,id);
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
        try {
            switch (category){
                case 1://普通结算单
                    List<ViewDetail1> details1 = JSONArray.parseArray(request.getParameter("details"),ViewDetail1.class);
                    result = DetailService1.importDetails(conn,id,details1,did);
                    break;
                case 2://小时工结算单
                    List<ViewDetail2> details2 = JSONArray.parseArray(request.getParameter("details"),ViewDetail2.class);
                    result = DetailService2.importDetails(conn,id,details2,did);
                    break;
                case 3://商业保险结算单
                    List<Detail3> details3 = JSONArray.parseArray(request.getParameter("details"),Detail3.class);
                    result = DetailService3.importDetails(conn,id,details3);
                    break;
                case 4://特殊结算单
                    List<ViewDetail4> details4 = JSONArray.parseArray(request.getParameter("details"),ViewDetail4.class);
                    result = DetailService4.importDetails(conn,id,details4,did);
                    break;
            }
        } catch (Exception e) {
            return DaoResult.fail("数据有误，请仔细核对员工数据");
        }
        return JSONObject.toJSONString(result);
    }

    //替换明细
    private String replaceDetails(Connection conn, HttpServletRequest request) {
        List<Detail3> member1 = JSONArray.parseArray(request.getParameter("member1"),Detail3.class);
        List<Detail3> member2 = JSONArray.parseArray(request.getParameter("member2"),Detail3.class);

        DaoUpdateResult res = DetailService3.replaceDetails(conn,member1,member2);
        return JSONObject.toJSONString(res);
    }

    //确认商业保险新增
    private String confirmDetails1(Connection conn, HttpServletRequest request) {
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        byte day = Byte.parseByte(request.getParameter("day"));
        String[] ids = request.getParameterValues("ids[]");

        DaoUpdateResult res = DetailService3.confirmDetails(conn,sid,ids,day);
        return JSONObject.toJSONString(res);
    }

    //确认商业保险替换
    private String confirmDetails2(Connection conn, HttpServletRequest request) {
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        byte day = Byte.parseByte(request.getParameter("day"));
        String[] ids1 = request.getParameterValues("ids1[]");
        String[] ids2 = request.getParameterValues("ids2[]");

        DaoUpdateResult res = DetailService3.confirmDetails(conn,sid,ids1,ids2,day);
        return JSONObject.toJSONString(res);
    }

    //导出明细
    private String exportDetails(Connection conn, HttpServletRequest request) {
        return null;
    }

    //补缴
//    private String fillup(Connection conn, HttpServletRequest request) {
//        String start = request.getParameter("start");//起始月份
//        String end = request.getParameter("end");//结束月份
//        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
//        List<JSONObject> employees = JSONArray.parseArray(request.getParameter("employees"),JSONObject.class);
//        DaoUpdateResult result = SettlementService1.fillup(start,end,sid,employees,conn);
//        return JSONObject.toJSONString(result);
//    }

    //补差
    private String makeup(Connection conn, HttpServletRequest request) {
        String start = request.getParameter("start");//起始月份
        String end = request.getParameter("end");//结束月份
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        List<Employee> employees = JSONArray.parseArray(request.getParameter("employees"),Employee.class);

        DaoUpdateResult result = SettlementService1.makeup(conn,employees,start,end,sid);
        return JSONObject.toJSONString(result);

    }

    //提交
    private String commit(Connection conn, HttpServletRequest request) {
        Account user = (Account) request.getSession().getAttribute("account");
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult result = null;
        switch (category){
            case 1://普通结算单
                result = SettlementService1.commit(conn, id, user);
                break;
            case 2://小时工结算单
                result = SettlementService2.commit(conn, id, user);
                break;
            /*case 2://商业保险结算单
                result = Settlement3Service.commit(conn, id, user);
                break;*/
            case 4://特殊结算单
                result = SettlementService4.commit(conn, id, user);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    //审核
    private String check(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        byte level = Byte.parseByte(request.getParameter("level"));
        boolean pass = Boolean.parseBoolean(request.getParameter("pass"));
        String reason = request.getParameter("reason");
        Account user = (Account) request.getSession().getAttribute("account");
        DaoUpdateResult res = null;
        switch (category){
            case 1://普通结算单
                res = SettlementService1.check(conn,id,level,pass,reason,user);
                break;
            case 2://小时工结算单
                res = SettlementService2.check(conn,id,level,pass,reason,user);
                break;
            /*case 2://商业保险结算单
                res = Settlement3Service.check(conn,id,type,pass,reason,user);
                break;*/
            case 4://特殊结算单
                res = SettlementService4.check(conn,id,level,pass,reason,user);
                break;
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
            case 1://普通结算单
                result = SettlementService1.reset(conn, id, user);
                break;
            case 2://小时工结算单
                result = SettlementService2.reset(conn, id, user);
                break;
            /*case 2://商业保险结算单
                result = Settlement3Service.reset(conn, id, user);
                break;*/
            case 4://特殊结算单
                result = SettlementService4.reset(conn, id, user);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    //确认扣款
    private String charge(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));
        Account user = (Account) request.getSession().getAttribute("account");
        DaoUpdateResult result = null;
        switch (category){
            case 1://普通结算单
                result = SettlementService1.charge(conn,id,user);
                break;
            case 2://小时工结算单
                result = SettlementService2.charge(conn,id,user);
                break;
            /*case 2://商业保险结算单
                result = Settlement3Service.charge(conn,id,user);
                break;*/
            case 4://特殊结算单
                result = SettlementService4.charge(conn, id, user);
                break;
        }
        return JSONObject.toJSONString(result);
    }

    //确认发放
    private String payroll(Connection conn, HttpServletRequest request) {
        byte category = Byte.parseByte(request.getParameter("category"));
        long id = Long.parseLong(request.getParameter("id"));//结算单id
        Account user = (Account) request.getSession().getAttribute("account");
        DaoUpdateResult result = null;
        switch (category){
            case 1://普通结算单
                result = SettlementService1.payroll(conn, id, user);
                break;
            case 2://小时工结算单
                result = SettlementService2.payroll(conn, id, user);
                break;
            case 4://特殊结算单
                result = SettlementService4.payroll(conn, id, user);
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
            case 1://普通结算单
                result = SettlementService1.getLogs(conn,id,param);
                break;
            case 2://小时工结算单
                result = SettlementService2.getLogs(conn,id,param);
                break;
            case 3://商业保险结算单
                result = SettlementService3.getLogs(conn,id,param);
                break;
            case 4://特殊结算单
                result = SettlementService4.getLogs(conn,id,param);
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
            case 1://普通结算
                result = Detail1Dao.delete(conn,id);
                break;
            case 2://小时工结算
                result = Detail2Dao.delete(conn,id);
                break;
            case 3://商业结算
                result = Detail3Dao.delete(conn,id);
                break;
            case 4://特殊结算单
                result = Detail4Dao.delete(conn,id);
                break;
        }
        return JSONObject.toJSONString(result);
    }

}
