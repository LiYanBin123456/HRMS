package servlet.client;


import bean.client.Dispatch;
import com.alibaba.fastjson.*;
import dao.admin.ContractDao;
import database.*;
import service.client.DispatchService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

@WebServlet(urlPatterns = "/client")
public class ClientServlet extends HttpServlet {
    private DispatchService dispatchService = new DispatchService();
   private ContractDao contractDao = new ContractDao();
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
            case "insert"://添加一个客户
                result = insert(conn,request);
                break;
            case "delete"://删除客户
                result = delete(conn,request);
                break;
            case "update"://修改一个客户
                result = update(conn,request);
                break;
            case "getList"://获取所有客户清单
                result = getList(conn,request);
                break;
            case "get"://获取一个客户清单
                result = get(conn,request);
                break;
            case "allocate"://修改管理员
                result = allocate(conn,request);
                break;
            case "insertSalaryDefine"://增加客户自定义工资
                result = insertSalaryDefine(conn,request);
                break;
            case "getLastSalaryDefine"://获取客户最新自定义工资
                result = getLastSalaryDefine(conn,request);
                break;
            case "getSalaryDefine"://获取客户自定义工资
                result = getSalaryDefine(conn,request);
                break;
            case "insertFinance"://增加客户服务信息
                result = insertFinance(conn,request);
                break;
            case "getFinance"://获取客户服务信息
                result = getFinance(conn,request);
                break;
            case "updateFinance"://修改客户服务信息
                result = updateFinance(conn,request);
                break;


}
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }




    //添加
    private String insert(Connection conn,HttpServletRequest request) {

        Dispatch dispatch = JSON.parseObject(request.getParameter("dispatch"), Dispatch.class);
        System.out.println(dispatch);
        //调用dao层的update方法
        DaoUpdateResult res = dispatchService.insertDispatch(conn, dispatch);

        return JSONObject.toJSONString(res);
    }
    //删除
    private String delete(Connection conn, HttpServletRequest request) {
        DaoUpdateResult res=new DaoUpdateResult();
        long id = Long.parseLong(request.getParameter("id"));
        byte status = Byte.parseByte(request.getParameter("status"));
        //判断客户状态
        if(status==1){
            //合作客户，删除合同，修改状态为潜在客户
            List<String> list = contractDao.deleteContract(conn, id);
            if(list!=null){
                for(String filename:list){
                    String url = request.getServletContext().getRealPath("/upload");
                    File file = new File(url+"/"+filename+".pdf");
                    if (file.exists()) {
                        file.delete();
                        System.out.println(filename+"文件删除成功!!");
                        res.msg+="合同文件删除成功!!";
                    }else {
                        System.out.println("文件不存在!!");
                    }
                }
            }
            res = dispatchService.deleteCooperation(conn,id);
        }else {
            //潜在客户，删除客户服务项目，删除客户
            int type = 0;
            res = dispatchService.deletePotential(conn,id,type);
        }
        return JSONObject.toJSONString(res);
    }

    //修改客户信息
    private String update(Connection conn,HttpServletRequest request) {

        Dispatch dispatch = JSON.parseObject(request.getParameter("dispatch"), Dispatch.class);
        System.out.println(dispatch);
        //调用dao层的update方法
        DaoUpdateResult res = dispatchService.updateDispatch(conn, dispatch);

        return JSONObject.toJSONString(res);
    }

    //获取客户列表
    private String getList(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res = dispatchService.getDispatchs(conn,parameter);
        System.out.println(res);
        return JSONObject.toJSONString(res);
    }

    //获取客户基本信息
    private String get(Connection conn,HttpServletRequest request){
        long id = Long.parseLong(request.getParameter("id"));
        System.out.println("客户id="+id);
        DaoQueryResult res = dispatchService.getDispatch(conn,id);
        return  JSONObject.toJSONString(res);
    }

    //分配管理员
    private String allocate(Connection conn, HttpServletRequest request) {
        return null;
    }

    //修改客户服务信息
    private String updateFinance(Connection conn, HttpServletRequest request) {
        return  null;
    }

    //获取客户服务信息
    private String getFinance(Connection conn, HttpServletRequest request) {
        return  null;
    }

    //增加客户服务信息
    private String insertFinance(Connection conn, HttpServletRequest request) {
        return  null;
    }

    //获取客户自定义工资信息
    private String getSalaryDefine(Connection conn, HttpServletRequest request) {
        return  null;
    }

    //获取客户最新自定义工资信息
    private String getLastSalaryDefine(Connection conn, HttpServletRequest request) {
        return  null;
    }

    //增加客户自定义工资信息
    private String insertSalaryDefine(Connection conn, HttpServletRequest request) {
        return  null;
    }


}
