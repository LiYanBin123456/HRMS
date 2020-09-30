package servlet;

import bean.contract.Contract;
import bean.contract.Serve;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.contract.ContractDao;
import database.*;
import org.apache.commons.io.IOUtils;
import service.contract.ContractService;
import service.contract.ServeService;
import utills.CreateGetNextId;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.Connection;


@WebServlet(urlPatterns = ("/verify/contract"))
@MultipartConfig
public class ContractServlet extends HttpServlet {
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
            case "insert"://客户插入合同
                result = insert(conn,request);
                break;
            case "getLast"://获取最新合同
                result = getLast(conn,request);
                break;
            case "getList"://获取合同列表
                result = getList(conn,request);
                break;
            case "update"://修改合同
                result = update(conn,request);
                break;
            case "get"://获取合同
                result = get(conn,request);
                break;
            case "delete"://删除合同
                result = delete(conn,request);
                break;
            case "insertService"://添加合同服务项目
                result = insertService(conn,request);
                break;
            case "getService"://获取合同服务项目
                result = getService(conn,request);
                break;
            case "updateService"://获取合同列表
                result = updateService(conn,request);
                break;
            case "getServiceList"://获取合同服务项目列表
                result = getServiceList(conn,request);
                break;
            case "getContracts"://获取合同视图
                result = getContracts(conn,request);
                break;

        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //根据合作客户和服务项目类型获取派遣方与合作方的合同
    private String getContracts(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));//合作单位id
        int type = Integer.parseInt(request.getParameter("type"));//类型 0_劳务外包结算单，1_人事代理，2_小时工结算单，3_商业保险结算单
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        parameter.addCondition("did","=",id);
        parameter.addCondition("stype","=",type);
        HttpSession session = request.getSession();
        long rid = (long) session.getAttribute("rid");
        DaoQueryListResult res = ContractService.getList(conn,parameter,"B",rid);
        return JSONObject.toJSONString(res);
    }

    //插入合同
    private String insert(Connection conn, HttpServletRequest request)throws IOException, ServletException {
        DaoUpdateResult res ;
        Contract contract = JSON.parseObject(request.getParameter("contract"), Contract.class);
        System.out.println("前台传来的数据："+contract);
        //自定义自增id
        QueryConditions conditions = new QueryConditions();
        String type = contract.getType();
        conditions.add("type", "=", type);
        //查寻出数据库中类型为type的最后一条合同的id
        String id = DbUtil.getLast(conn, "contract", conditions);
        System.out.println("===="+id);
        if(id!=null){
            //合同id+1
            id = CreateGetNextId.NextId(id,contract.getType());
        }else {
            //id=null表示还没有该类型的合同
            id = CreateGetNextId.NextId(0, contract.getType());
        }
        contract.setId(id);
        System.out.println(contract);
        HttpSession session = request.getSession();
        long rid = (long) session.getAttribute("rid");
        contract.setAid(rid);
        res = ContractService.insert(conn,contract);
        res.extra = id;
        System.out.println(res);
        return JSONObject.toJSONString(res);
    }

    //获取最新合同
    private String getLast(Connection conn, HttpServletRequest request) {
        long bid = Long.parseLong(request.getParameter("id"));
        System.out.println("客户id="+bid);
        String type = request.getParameter("type");
        DaoQueryResult res = ContractService.getLast(conn,bid,type);
        return JSONObject.toJSONString(res);
    }

    //获取合同列表
    private String getList(Connection conn, HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        String type=request.getParameter("type");
        HttpSession session = request.getSession();
        long rid = (long) session.getAttribute("rid");
        DaoQueryListResult res = ContractService.getList(conn,parameter,type,rid);
        return JSONObject.toJSONString(res);
    }

    //删除合同
    private String delete(Connection conn, HttpServletRequest request) {
        String id = request.getParameter("id");
        DaoUpdateResult res = ContractService.delete(conn,id);
        return JSONObject.toJSONString(res);
    }

    //获取合同信息
    private String get(Connection conn, HttpServletRequest request) {
        String id = request.getParameter("id");
        DaoQueryResult res = ContractService.get(conn,id);
        return JSONObject.toJSONString(res);
    }

    //修改合同
    private String update(Connection conn, HttpServletRequest request) {
        Contract contract = JSON.parseObject(request.getParameter("contract"), Contract.class);
        System.out.println("前台传来的数据："+contract);
        DaoUpdateResult res = ContractService.update(conn,contract);
        return JSONObject.toJSONString(res);
    }

    //获取当前合作单位的有效服务项目列表
    private String getServiceList(Connection conn, HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        long id= Long.parseLong(request.getParameter("id"));
        DaoQueryListResult res = ServeService.getList(conn,parameter,id);
        return JSONObject.toJSONString(res);
    }

    //修改合同服务项目
    private String updateService(Connection conn, HttpServletRequest request) {
        Serve serve  = JSON.parseObject(request.getParameter("serve"), Serve.class);
        System.out.println("前台传来的数据："+serve);
        DaoUpdateResult res = ServeService.update(conn,serve);
        return JSONObject.toJSONString(res);
    }

    //获取合同服务项目
    private String getService(Connection conn, HttpServletRequest request) {
        String id = request.getParameter("id");
        DaoQueryResult res = ServeService.get(conn,id);
        return JSONObject.toJSONString(res);
    }

    //添加合同服务项目
    private String insertService(Connection conn, HttpServletRequest request) {
        Serve serve  = JSON.parseObject(request.getParameter("serve"), Serve.class);
        System.out.println("前台传来的数据："+serve);
        DaoUpdateResult res = ServeService.insert(conn,serve);
        return JSONObject.toJSONString(res);
    }

}
