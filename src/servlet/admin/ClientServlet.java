package servlet.admin;


import bean.admin.Dispatch;
import com.alibaba.fastjson.*;
import dao.admin.ContractDao;
import database.*;
import service.admin.ClientService;

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
    private ClientService clientService = new ClientService();
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
            case "insertDispatch"://添加一个客户
                result = insertDispatch(conn,request);
                break;
            case "deleteDispatch"://删除客户
                result = deleteDispatch(conn,request);
                break;
            case "updateDispatch"://修改一个客户
                result = updateDispatch(conn,request);
                break;
            case "getDispatchs"://获取所有客户清单
                result = getDispatchs(conn,request);
                break;
            case "getDispatch"://获取一个客户清单
                result = getDispatch(conn,request);
                break;
}
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //添加
    private String insertDispatch(Connection conn,HttpServletRequest request) {

        Dispatch dispatch = JSON.parseObject(request.getParameter("dispatch"), Dispatch.class);
        System.out.println(dispatch);
        //调用dao层的update方法
        DaoUpdateResult res = clientService.insertDispatch(conn, dispatch);

        return JSONObject.toJSONString(res);
    }
    private String deleteDispatch(Connection conn, HttpServletRequest request) {
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
            res = clientService.deleteCooperation(conn,id);
        }else {
            //潜在客户，删除客户服务项目，删除客户
            int type = 0;
            res = clientService.deletePotential(conn,id,type);
        }
        return JSONObject.toJSONString(res);
    }

    //修改客户信息
    private String updateDispatch(Connection conn,HttpServletRequest request) {

        Dispatch dispatch = JSON.parseObject(request.getParameter("dispatch"), Dispatch.class);
        System.out.println(dispatch);
        //调用dao层的update方法
        DaoUpdateResult res = clientService.updateDispatch(conn, dispatch);

        return JSONObject.toJSONString(res);
    }

    //获取客户列表
    private String getDispatchs(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res = clientService.getDispatchs(conn,parameter);
        System.out.println(res);
        return JSONObject.toJSONString(res);
    }

    //获取客户基本信息
    private String getDispatch(Connection conn,HttpServletRequest request){
        long id = Long.parseLong(request.getParameter("id"));
        System.out.println("客户id="+id);
        DaoQueryResult res = clientService.getDispatch(conn,id);
        return  JSONObject.toJSONString(res);
    }




}
