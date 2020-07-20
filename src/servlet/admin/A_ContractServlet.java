package servlet.admin;

import bean.admin.a_contract;
import com.alibaba.fastjson.JSONObject;
import dao.admin.A_ContractDao;
import dao.admin.ContractDao;
import database.*;
import org.apache.commons.io.IOUtils;
import service.admin.A_ContractService;
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


@WebServlet(name = "A_ContractServlet",urlPatterns = "/a_contract")
@MultipartConfig
public class A_ContractServlet extends HttpServlet {
    private a_contract contract = new a_contract();
    private A_ContractDao contractDao=new A_ContractDao();
    private A_ContractService contractService = new A_ContractService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String result = "";
        Connection conn = ConnUtil.getConnection();

        String op = request.getParameter("op");

        switch (op) {
            case "getContracts"://根据客户获取历史合同清单
                result = getContracts(conn,request);
                break;
            case "getContract"://根据客户id获取合同
                result = getContract(conn,request);
                break;
            case "insertContract"://合作客户插入合同
                result = insertContract(conn,request);
                break;
            case "insertContract2"://潜在客户插入合同
                result = insertContract2(conn,request);
                break;
//            case "deleteContract"://删除合同
//                result = deleteContract(conn,request);
//                break;

        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    //删除文件
//    private String deleteContract(Connection conn, HttpServletRequest request) {
//        //获取文件的真实路径
//        String url = request.getServletContext().getRealPath("/upload");
//        File file = new File(url+"/A2020000007"+".jpg");
//
//        if (file.exists()) {
//            file.delete();
//            System.out.println("指定文件删除成功!!");
//        }
//        return null;
//    }
    //合作客户插入合同
    private String insertContract(Connection conn, HttpServletRequest request) throws IOException, ServletException {
        DaoUpdateResult res ;

        long bid = Long.parseLong(request.getParameter("bid"));
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        int status = Integer.parseInt(request.getParameter("status"));
        String intro = request.getParameter("intro");

        contract.setBid(bid);
        contract.setStart(start);
        contract.setEnd(end);
        contract.setStatus(status);
        contract.setIntro(intro);

        //自定义自增id
        QueryConditions conditions = new QueryConditions();
        String type ="A" ;
        System.out.println(type);
        type += "%";
        System.out.println(type);
        conditions.add("id","like",type);

        //通过模糊查找各种类型id的条数
        DaoQueryListResult counts = DbUtil.getCounts(conn, "a_contract", conditions);
        long total = counts.total;
        String id = CreateGetNextId.NextId(total, "A");
        contract.setId(id);

        res = contractService.insert(conn,contract);

        //先判断是否成功插入，否则会出现数据库插入失败，但是文件却已经上传的现象
        if(res.success){
        //将文件上传到服务器并且以合同id命名
            String file = null;
            Part part = request.getPart("file");
            if(part!=null){
                //获取文件的名称
                String header = part.getHeader("content-disposition");
                //截取字符串获取文件名称
                String headername = header.substring(header.indexOf("filename")+10, header.length()-1);
                //获取文件名后缀
                String suffixName=headername.substring(headername.indexOf(".")+1);
                System.out.println(suffixName);
                String s="pdf";
                if(suffixName.equals(s)){
                    System.out.println(1);
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
        return  JSONObject.toJSONString(res);
    }

    //潜在客户插入合同
    private String insertContract2(Connection conn, HttpServletRequest request) throws IOException, ServletException{
        DaoUpdateResult res ;

        long bid = Long.parseLong(request.getParameter("bid"));
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        int status = Integer.parseInt(request.getParameter("status"));
        String intro = request.getParameter("intro");

        contract.setBid(bid);
        contract.setStart(start);
        contract.setEnd(end);
        contract.setStatus(status);
        contract.setIntro(intro);

        //自定义自增id
        QueryConditions conditions = new QueryConditions();
        String type ="A" ;
        System.out.println(type);
        type += "%";
        System.out.println(type);
        conditions.add("id","like",type);

        //通过模糊查找各种类型id的条数
        DaoQueryListResult counts = DbUtil.getCounts(conn, "a_contract", conditions);
        long total = counts.total;
        String id = CreateGetNextId.NextId(total, "A");
        contract.setId(id);

        res = contractService.insert2(conn,contract);

        //先判断是否成功插入，否则会出现数据库插入失败，但是文件却已经上传的现象
        if(res.success){
            //将文件上传到服务器并且以合同id命名
            String file = null;
            Part part = request.getPart("file");
            if(part!=null){
                //获取文件的名称
                String header = part.getHeader("content-disposition");
                //截取字符串获取文件名称
                String headername = header.substring(header.indexOf("filename")+10, header.length()-1);
                //获取文件名后缀
                String suffixName=headername.substring(headername.indexOf(".")+1);
                String s="pdf";
                if(suffixName.equals(s)){
                    System.out.println(1);
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
                    res = contractDao.delete(conn,id);
                }
            }else {
                res.msg+= "合同文件未插入";
            }
        }
        return  JSONObject.toJSONString(res);
    }

    private String getContract(Connection conn, HttpServletRequest request) {

        String bid = (request.getParameter("bid"));
        System.out.println("客户id="+bid);
        DaoQueryResult res = contractService.getContract(conn,bid);
        if(res.data == null){
           res.msg="该客户暂时未签订合同";
        }else {
            res.msg="已经查询出该客户最新的合同";
        }
        return JSONObject.toJSONString(res);
    }


    private String getContracts(Connection conn, HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        DaoQueryListResult res =contractService.getContracts(conn,parameter);
        return JSONObject.toJSONString(res);
    }
}
