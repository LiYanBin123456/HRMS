package servlet;

import com.alibaba.fastjson.JSONObject;
import database.DaoUpdateResult;
import org.apache.commons.io.IOUtils;
import utills.XlsUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

        String op = request.getParameter("op");
        switch (op) {
            case "readXls"://读取xls数据反馈给前台
                result = readXls(request);
                break;
            case "upload"://上传合同附件
                result = upload(request);
                break;
            case "uploadImg"://上传员工头像
                result = uploadImg(request);
                break;
            case "existContract"://判断合同附件是否存在
                result = existContract(request);
                break;
            case "downloadContract"://下载合同复印件
                downloadContract(request,response);
                return;
        }

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
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     */
    private String readXls(HttpServletRequest request) throws IOException, ServletException {
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

    private String upload(HttpServletRequest request) throws IOException, ServletException {
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

    private String existContract(HttpServletRequest request) throws IOException {
        String id = request.getParameter("id");
        String fileName = id+".pdf";
        String fullFileName = getServletContext().getRealPath("/contractFile/" + fileName);
        File file = new File(fullFileName);

        JSONObject json = new JSONObject();
        json.put("success",true);
        json.put("exist",file.exists()?true:false);
        return json.toJSONString();
    }

    private void downloadContract(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String fileName = id+".pdf";
        String fullFileName = getServletContext().getRealPath("/contractFile/" + fileName);
        File file = new File(fullFileName);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);

        ServletOutputStream os = response.getOutputStream();
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        int size=0;
        byte[] buff = new byte[1024];
        while ((size=bis.read(buff))!=-1) {
            os.write(buff, 0, size);
        }

        os.flush();
        os.close();
        bis.close();
    }

    private String uploadImg(HttpServletRequest request) throws IOException, ServletException {
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
