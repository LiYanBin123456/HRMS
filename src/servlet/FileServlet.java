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
import javax.smartcardio.ResponseAPDU;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import sun.font.TrueTypeFont;
import utills.AccessoryUtil;
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
                ConnUtil.closeConnection(conn);
                return;
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
                    result.success = true;
                }
                else {
                    result.msg ="格式不正确";
                    result.success = false;
                }
            }
            return JSONObject.toJSONString(result);
    }

    private void download(Connection conn, HttpServletRequest request,HttpServletResponse response) throws IOException {
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
        System.out.println(file);
        if(file.exists()){
            // 创建输入输出流对象
            InputStream in = new FileInputStream(fullFileName);
            OutputStream out = response.getOutputStream();
            // 读写文件
            int b;
            while((b=in.read())!= -1) {
                out.write(b);
            }
            out.close();
            in.close();
        }
        else {
            result.msg="文件不存在，请确认是否已经插入了合同";
            result.success=true;

            PrintWriter outs = response.getWriter();
            outs.print(JSONObject.toJSONString(result));
            outs.flush();
            outs.close();
        }
    }

    private String uploadImg(Connection conn, HttpServletRequest request) throws IOException, ServletException {
        String  msg = null;
        String id = request.getParameter("id");
        String file ;
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


    private Part getPart(HttpServletRequest request){
        Collection<Part> parts = null;
        try {
            parts = request.getParts();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        Part part = (Part) parts.toArray()[1];//第二个才是我们需要的，应该是uploadify对Servlet3支持还不够好
        return part;
    }

    /**
     * 获取上传的文件名
     * @param part
     * @return
     */
    private String getFileName(Part part) {
        String header = part.getHeader("content-disposition");
        /**
         * String[] tempArr1 = header.split(";");代码执行完之后，在不同的浏览器下，tempArr1数组里面的内容稍有区别
         * 火狐或者google浏览器下：tempArr1={form-data,name="file",filename="snmp4j--api.zip"}
         * IE浏览器下：tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"}
         */
        String[] tempArr1 = header.split(";");
        /**
         *火狐或者google浏览器下：tempArr2={filename,"snmp4j--api.zip"}
         *IE浏览器下：tempArr2={filename,"E:\snmp4j--api.zip"}
         */
        String[] tempArr2 = tempArr1[2].split("=");
        //获取文件名，兼容各种浏览器的写法
        String fileName = tempArr2[1].substring(tempArr2[1].lastIndexOf("\\")+1).replaceAll("\"", "");
        return fileName;
    }

    private void exportAccessory(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String []str = request.getParameter("stuIds").split(",");
        List<String> cardIds = new ArrayList<>(Arrays.asList(str));
        byte category = Byte.parseByte(request.getParameter("category"));
        String folder = request.getServletContext().getRealPath("/accessory");

        //List<File> files = AccessoryUtil.getAccessoryFiles(cardIds,category,folder);
       // AccessoryUtil.zipDownload(response,files);
    }

}
