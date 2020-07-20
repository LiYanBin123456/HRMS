package servlet;


import java.io.File;
import java.io.*;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.UUID;

import org.apache.commons.io.IOUtils;


@WebServlet(urlPatterns= "/FileUpLoad")
@MultipartConfig
public class FileUpLoad extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");	//设置编码
        response.setContentType("text/html;charset=utf-8");
        String name = request.getParameter("name");
        System.out.println(name);
        Part part = request.getPart("file1");
        //获取文件的名称
        String header = part.getHeader("content-disposition");
        //截取字符串获取文件名称
        String headername = header.substring(header.indexOf("filename")+10, header.length()-1);
        //获取文件流
        InputStream put = part.getInputStream();
        //获取文件的真实路径
        //String url = this.getServletContext().getRealPath("/upload");
        String url = request.getServletContext().getRealPath("/upload");
        //String realPath = request.getSession().getServletContext().getRealPath("/upload");
        System.out.println(url+"==");
        File uploadDir = new File(url);

        // 如果该文件夹不存在则创建
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        UUID uuid = UUID.randomUUID();
        String filename = uuid+headername;
        System.out.println(filename);

        //建立对拷流
        FileOutputStream fos = new FileOutputStream(new File(url, filename));

        IOUtils.copy(put, fos);
        put.close();
        fos.close();

        //删除临时文件
        part.delete();


//        PrintWriter out = null;
//        try {
//            out = response.getWriter();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 设置上传文件保持路径
//        String filepath = request.getServletContext().getRealPath("/upload");
//        System.out.println(filepath);
//        File uploadDir = new File(filepath);
//
//        // 如果该文件夹不存在则创建
//        if (!uploadDir.exists()) {
//            uploadDir.mkdirs();
//        }
//
//        // 获取服务器默认的临时文件夹
//        String temp = System.getProperty("java.io.tmpdir");
//        File tempDir = new File(temp);
//
//        // 如果该文件夹不存在，则创建
//        if (!tempDir.exists()) {
//            tempDir.mkdirs();
//        }
//        // 创建文件项目工厂对象
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        // 设置临时文件夹
//        factory.setRepository(new File(temp));
//        // 设置缓冲区大小为 1M
//        factory.setSizeThreshold(1024 * 1024);
//        ServletFileUpload serveltFileUpload = new ServletFileUpload(factory);
//
//        // 解析文件上传请求，解析结果放在 List中
//        try {
//            List<FileItem> list = serveltFileUpload.parseRequest(request);
//            for (FileItem item : list) {
//                //获取表单的属性名字
//                String name = item.getFieldName();
//
//                //如果获取的 表单信息是普通的 文本 信息
//                if (item.isFormField()) {
//
//                    //获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
//                    String value = item.getString("UTF-8");
//                    System.out.println(value);
//                    request.setAttribute(name, value);
//                } else {
//
//                // 获取上传文件名
//                String fileName = item.getName();
//                // 此时的文件名包含了完整的路径，得注意加工一下
//                int index = fileName.lastIndexOf("\\");
//                fileName = UUID.randomUUID() + fileName.substring(index + 1, fileName.length());
//                System.out.println("完整的文件名：" + fileName);
//                String file = filepath + "\\" + fileName;
//                System.out.println("文件路径" + file);
//                request.setAttribute(name, fileName);
//                // 创建文件输入流
//                InputStream in = item.getInputStream();
//                // 创建一个指向 File 对象所表示的文件中写入数据的文件输出流
//                FileOutputStream outs = new FileOutputStream(new File(
//                        filepath, fileName));
//                int len = 0;
//                // 创建字节数组 ，大小位 1024字节
//                byte[] buffer = new byte[1024];
//                out.println("上传文件名称：" + fileName + "<br/>");
//                out.println("上传文件大小：" + item.getSize() / 1024 + " KB（"
//                        + item.getSize() + " 字节）<br/>");
//                System.out.println("上传文件大小：" + item.getSize() / 1024 + " KB（"
//                        + item.getSize() + " 字节）<br/>");
//                while ((len = in.read(buffer)) != -1) {
//                    // 字节数组从偏移量 0 到len 个字节写入文件输出流
//                    outs.write(buffer, 0, len);
//                }
//                // 关闭输入流
//                in.close();
//                // 关闭输出流
//                outs.close();
//
//            }
//         }
//        } catch (FileUploadException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        catch (Exception e) {
//            // TODO Auto-generated catch block
//            //e.printStackTrace();
//        }
        request.getRequestDispatcher("admin/filedemo.jsp").forward(request, response);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
