package servlet;

import com.alibaba.fastjson.JSONObject;
import database.ConnUtil;
import database.DaoUpdateResult;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.Connection;

@WebServlet(name = "FileServlet",urlPatterns = "/file")
@MultipartConfig
public class FileServlet extends HttpServlet {
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
            case "readXls"://读取xls数据反馈给前台
                result = readXls(conn,request);
                break;
            case "upload"://上传合同附件
                result = upload(conn,request);
                break;
            case "download"://下载合同复印件
                result = download(conn,request);
                break;
            case "uploadImg"://上传员工头像
                result = uploadImg(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    private String readXls(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String upload(Connection conn, HttpServletRequest request) throws IOException, ServletException {
        DaoUpdateResult result = null;
        String id = request.getParameter("id");
            //将文件上传到服务器并且以合同id命名
            String file = null;
            Part part = request.getPart("file");
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
                    result.msg = "合共插入成功";
                }
                else {
                    result.msg ="格式不正确";
                }
            }
            return JSONObject.toJSONString(result);
    }

    private String download(Connection conn, HttpServletRequest request) {
        return null;
    }

    private String uploadImg(Connection conn, HttpServletRequest request) {
        return null;
    }


}
