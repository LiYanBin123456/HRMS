package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet(name = "DownLoadServlet",urlPatterns = "/downLoadServlet")
public class DownLoadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        // 获得请求文件名
        String fileName = request.getParameter("fileName");

        // 设置文件MIME类型
        String mimeType = getServletContext().getMimeType(fileName);
        response.setContentType(mimeType);

        // 设置Content-Disposition
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);

        // 读取目标文件，通过response将目标文件写到客户端
        // 获取目标文件的绝对路径
        String fullFileName = getServletContext().getRealPath("/upload/" + fileName);
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


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
