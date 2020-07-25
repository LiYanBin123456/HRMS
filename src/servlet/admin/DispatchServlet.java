package servlet.admin;


import bean.admin.Dispatch;
import com.alibaba.fastjson.*;
import dao.admin.ContractDao;
import database.*;
import service.admin.DispatchService;

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

@WebServlet(urlPatterns = "/dispatch")
public class DispatchServlet extends HttpServlet {
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
        DaoUpdateResult res = dispatchService.insert(conn, dispatch);

        return JSONObject.toJSONString(res);
    }
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
        DaoUpdateResult res = dispatchService.update(conn, dispatch);

        return JSONObject.toJSONString(res);
    }

    //获取客户列表
    private String getList(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res = dispatchService.getList(conn,parameter);
        System.out.println(res);
        return JSONObject.toJSONString(res);
    }

    //获取客户基本信息
    private String get(Connection conn,HttpServletRequest request){
        long id = Long.parseLong(request.getParameter("id"));
        System.out.println("客户id="+id);
        DaoQueryResult res = dispatchService.get(conn,id);
        return  JSONObject.toJSONString(res);
    }




}
