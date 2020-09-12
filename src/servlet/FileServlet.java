package servlet;

import com.alibaba.fastjson.JSONObject;
import database.ConnUtil;
import database.DaoUpdateResult;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.io.IOUtils;

import javax.management.relation.Role;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import utills.XlsUtil;

@WebServlet(name = "FileServlet",urlPatterns = "/verify/file")
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
            case "uploadImg"://上传员工头像
                result = uploadImg(conn,request);
                break;
            case "download"://下载合同复印件
                download(conn,request,response);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    /**
     * 读取excal中的数据返回前台
     * 1、获取excal文件
     * 2、上传到服务器
     * 3、获取服务器中的excal文件，读取数据
     * 4、删除该文件
     * @param conn
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     */
    private String readXls(Connection conn, HttpServletRequest request) throws IOException, ServletException {
        String str = null;
        Part part = request.getPart("file");
        String header = part.getHeader("content-disposition");
        //截取字符串获取文件名称
        String filename = header.substring(header.indexOf("filename")+10, header.length()-1);
        //获取后缀名
        String suffixName=filename.substring(filename.indexOf(".")+1);
        String s="xls";
        if(suffixName.equals(s)){//判断后缀，上传服务器
            InputStream put = part.getInputStream();
            //获取文件夹的真实路径
            String url = request.getServletContext().getRealPath("/excelFile");
            File uploadDir = new File(url);
            // 如果该文件夹不存在则创建
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            //建立对拷流
            FileOutputStream fos = new FileOutputStream(new File(url, filename));
            IOUtils.copy(put, fos);
            String path = url+"\\"+filename;
            System.out.println(path);
            put.close();
            fos.close();
            //删除临时文件
            part.delete();
            try {//获取服务器中文件，读取数据
                InputStream in = new FileInputStream(path);
                List<JSONObject> data = XlsUtil.read(in,"信息表","元数据");
                str = JSONObject.toJSONString(data);
                System.out.println(str);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //删除文件
            File fileName = new File(path);
            if(fileName.exists()){
                fileName.delete();
            }
        }else {
            str = "文件必须是xls格式";
        }
        return str;
    }

    private String upload(Connection conn, HttpServletRequest request) throws IOException, ServletException {
        DaoUpdateResult result = new DaoUpdateResult();
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
                    //获取文件夹的真实路径
                    String url = request.getServletContext().getRealPath("/contractFile");
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
                    result.msg = "合同插入成功";
                }
                else {
                    result.msg ="格式不正确";
                }
            }
            return JSONObject.toJSONString(result);
    }

    private String download(Connection conn, HttpServletRequest request,HttpServletResponse response) throws IOException {
       DaoUpdateResult result = new DaoUpdateResult();
        // 获得请求文件名
        String id = request.getParameter("id");
        String fileName = id+".pdf";
        // 设置文件MIME类型
        String mimeType = getServletContext().getMimeType(fileName);
        response.setContentType(mimeType);
        // 设置Content-Disposition
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        // 读取目标文件，通过response将目标文件写到客户端
        // 获取目标文件的绝对路径
        String fullFileName = getServletContext().getRealPath("/contractFile/" + fileName);
        File file = new File(fullFileName);
        if(file.exists()){
            // 创建输入输出流对象
            InputStream in = new FileInputStream(fullFileName);
            OutputStream out = response.getOutputStream();
            // 读写文件
            int b;
            while((b=in.read())!= -1) {
                out.write(b);
            }
            in.close();
            result.msg="正在下载";
        }
        else {
            result.msg = "文件不存在";
        }

        return JSONObject.toJSONString(result);
    }

    private String uploadImg(Connection conn, HttpServletRequest request) throws IOException, ServletException {
        String  msg = null;
        String id = request.getParameter("id");
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
            String jpg="jpg";
            String jpeg = "jpeg";
            if(suffixName.equals(jpg)||suffixName.equals(jpeg)){
                //获取文件流
                InputStream put = part.getInputStream();
                //获取文件夹的真实路径
                String url = request.getServletContext().getRealPath("/headImg");
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
                String img = getServletContext().getRealPath("/headImg/" + file);
                System.out.println(img);
                msg = "头像插入成功,地址："+img;
            }
            else {
                msg ="格式不正确";
            }
        }
        return JSONObject.toJSONString(msg);
    }

}
