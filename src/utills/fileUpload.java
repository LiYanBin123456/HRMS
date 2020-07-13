package utills;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

public class fileUpload {
    public void fileUpload(){

    }
    public String fileUpload(HttpServletRequest request, HttpServletResponse response){
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 设置上传文件保持路径
        String filepath = request.getServletContext().getRealPath("/upload");
        System.out.println(filepath);
        File uploadDir = new File(filepath);

        // 如果该文件夹不存在则创建
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 获取服务器默认的临时文件夹
        String temp = System.getProperty("java.io.tmpdir");
        File tempDir = new File(temp);

        // 如果该文件夹不存在，则创建
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        // 创建文件项目工厂对象
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置临时文件夹
        factory.setRepository(new File(temp));
        // 设置缓冲区大小为 1M
        factory.setSizeThreshold(1024 * 1024);
        ServletFileUpload serveltFileUpload = new ServletFileUpload(factory);

        // 解析文件上传请求，解析结果放在 List中
        try {
            List<FileItem> list = serveltFileUpload.parseRequest(request);
            for (FileItem item : list) {
                // 判断某项是否是普通的表单类型。
                String name = item.getFieldName();
                if (item.isFormField()) { //该表单项是普通类型
                    // 忽略简单form字段而不是上传域的文件域
                   continue;

                } else { // 否则文件域
                    // 获取上传文件名
                    String fileName = item.getName();
                    // 此时的文件名包含了完整的路径，得注意加工一下
                    int index = fileName.lastIndexOf("\\");
                    fileName = UUID.randomUUID()+ fileName.substring(index + 1, fileName.length());
                    System.out.println("完整的文件名：" + fileName);
                    String file = filepath+"\\"+fileName;
                    System.out.println("文件路径"+file);
                    request.setAttribute(name,fileName);
                    // 创建文件输入流
                    InputStream in = item.getInputStream();
                    // 创建一个指向 File 对象所表示的文件中写入数据的文件输出流
                    FileOutputStream outs = new FileOutputStream(new File(
                            filepath, fileName));
                    int len = 0;
                    // 创建字节数组 ，大小位 1024字节
                    byte[] buffer = new byte[1024];
                    out.println("上传文件名称：" + fileName + "<br/>");
                    out.println("上传文件大小：" + item.getSize() / 1024 + " KB（"
                            + item.getSize() + " 字节）<br/>");
                    System.out.println("上传文件大小：" + item.getSize() / 1024 + " KB（"
                            + item.getSize() + " 字节）<br/>");
                    while ((len = in.read(buffer)) != -1) {
                        // 字节数组从偏移量 0 到len 个字节写入文件输出流
                        outs.write(buffer, 0, len);
                    }
                    // 关闭输入流
                    in.close();
                    // 关闭输出流
                    outs.close();
                    return file;
                }

            }
        } catch (FileUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
           //e.printStackTrace();
        }
        return filepath;
    }

}
