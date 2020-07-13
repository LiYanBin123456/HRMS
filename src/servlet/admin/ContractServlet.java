package servlet.admin;

import bean.admin.Contract;
import com.alibaba.fastjson.JSONObject;
import database.*;
import org.apache.commons.io.IOUtils;
import service.admin.ContractService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.Connection;
import java.util.UUID;

@WebServlet(urlPatterns = ("/contract"))
@MultipartConfig
public class ContractServlet extends HttpServlet {
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
            case "getContracts"://获取所有合同清单
                result = getContracts(conn,request);
                break;
            case "getContract"://根据客户id获取合同
                result = getContract(conn,request);
                break;
            case "insertContract"://插入合同
                result = insertContract(conn,request);
                break;

        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //插入
    private String insertContract(Connection conn, HttpServletRequest request) throws IOException, ServletException {
        DaoUpdateResult res ;


        long cid = Long.parseLong(request.getParameter("cid"));
        String sign = request.getParameter("sign");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        String type = request.getParameter("type");
        int status = Integer.parseInt(request.getParameter("status"));
        String accessory = null;
        Part part = request.getPart("accessory");
        if(part!=null){
            //获取文件的名称
            String header = part.getHeader("content-disposition");
            //截取字符串获取文件名称
            String headername = header.substring(header.indexOf("filename")+10, header.length()-1);
            //获取文件流
            InputStream put = part.getInputStream();
            //获取文件的真实路径
            String url = request.getServletContext().getRealPath("/upload");

            File uploadDir = new File(url);

            // 如果该文件夹不存在则创建
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            UUID uuid = UUID.randomUUID();
            accessory = uuid+headername;

            //建立对拷流
            FileOutputStream fos = new FileOutputStream(new File(url, accessory));

            IOUtils.copy(put, fos);
            put.close();
            fos.close();
            //删除临时文件
            part.delete();
        }else {
             accessory = null;
        }
        accessory = "//upload//"+accessory;
        System.out.println(accessory);
        Contract contract = new Contract(cid,sign,start,end,type,accessory,status);

        res = contractService.insertContract(conn,contract);

        return JSONObject.toJSONString(res);
    }


    private String getContract(Connection conn, HttpServletRequest request) {

        String cid = (request.getParameter("cid"));
        System.out.println("客户id="+cid);
        DaoQueryResult res = contractService.getContract(conn,cid);
        return JSONObject.toJSONString(res);
    }

    private String getContracts(Connection conn, HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        DaoQueryListResult res =contractService.getContracts(conn,parameter);
        return JSONObject.toJSONString(res);
    }
}
