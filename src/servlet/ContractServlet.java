package servlet;

import bean.contract.Contract;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.contract.ContractDao;
import database.*;
import org.apache.commons.io.IOUtils;
import service.contract.ContractService;
import utills.CreateGetNextId;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.Connection;


@WebServlet(urlPatterns = ("/contract"))
@MultipartConfig
public class ContractServlet extends HttpServlet {
    private ContractDao contractDao = new ContractDao();
    private ContractService contractService =new ContractService();
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

        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //插入合同
    private String insert(Connection conn, HttpServletRequest request)throws IOException, ServletException {
        DaoUpdateResult res ;
        Contract contract = JSON.parseObject(request.getParameter("contract"), Contract.class);

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
        res = contractService.insertPot(conn,contract);
        //先判断是否成功插入，否则会出现数据库插入失败，但是文件却已经上传的现象
        if(res.success){
            //将文件上传到服务器并且以合同id命名
            String file = null;
            Part part = request.getPart("file");
            System.out.println("part==="+part);
            System.out.println(part);
            if(part!=null){
                //获取文件的名称
                String header = part.getHeader("content-disposition");
                System.out.println(header);
                //截取字符串获取文件名称
                String headername = header.substring(header.indexOf("filename")+10, header.length()-1);
                System.out.println(headername);
                //获取文件名后缀
                String suffixName=headername.substring(headername.indexOf(".")+1);
                String s="pdf";
                if(suffixName.equals(s)){
                    //获取文件流
                    InputStream put = part.getInputStream();
                    //获取文件的真实路径
                    String url = request.getServletContext().getRealPath("/upload");

                    File uploadDir = new File(url);

                    // 如果该文件夹不存在则创建
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    file = id+"."+suffixName;

                    //建立对拷流
                    FileOutputStream fos = new FileOutputStream(new File(url, file));

                    IOUtils.copy(put, fos);
                    put.close();
                    fos.close();
                    //删除临时文件
                    part.delete();
                    res.msg="合同已上传";
                }
                else {
                    res.msg="不是pdf格式文件，不能上传";
                    //如果文件上传失败要删除掉该合同，以免前台重新插入时错误
                    contractDao.delete(conn,id);
                }
            }else {
                res.msg+= "合同文件未插入";
            }
        }

        return JSONObject.toJSONString(res);
    }

    //获取最新合同
    private String getLast(Connection conn, HttpServletRequest request) {

        String bid = (request.getParameter("bid"));
        System.out.println("客户id="+bid);
        String type = request.getParameter("type");
        DaoQueryResult res = contractService.getLast(conn,bid,type);
        return JSONObject.toJSONString(res);
    }

    //获取合同列表
    private String getList(Connection conn, HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        DaoQueryListResult res =contractService.getList(conn,parameter);
        return JSONObject.toJSONString(res);
    }

    //获取合同服务项目列表
    private String getServiceList(Connection conn, HttpServletRequest request) {
        return null;
    }

    //修改合同服务项目
    private String updateService(Connection conn, HttpServletRequest request) {
        return null;
    }

    //获取合同服务项目
    private String getService(Connection conn, HttpServletRequest request) {
        return null;
    }

    //添加合同服务项目
    private String insertService(Connection conn, HttpServletRequest request) {
        return null;
    }

    //删除合同
    private String delete(Connection conn, HttpServletRequest request) {
        return null;
    }

    //获取合同信息
    private String get(Connection conn, HttpServletRequest request) {
        return null;
    }

    //修改合同
    private String update(Connection conn, HttpServletRequest request) {
        return null;
    }


}
