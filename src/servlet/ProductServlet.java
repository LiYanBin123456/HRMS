package servlet;

import bean.insurance.Product;
import com.alibaba.fastjson.JSONObject;
import database.*;
import service.product.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(name = "ProductServlet",urlPatterns = "/verify/product")
public class ProductServlet extends HttpServlet {
    private ProductService productService = new ProductService();
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
            case "insert"://插入清单
                result = insert(conn,request);
                break;
            case "update"://修改清单
                result = update(conn,request);
                break;
            case "delete"://删除所有清单
                result = delete(conn,request);
                break;
            case "get"://获取一个清单
                result = get(conn,request);
                break;
            case "getList"://获取所有公积金清单
                result = getList(conn,request);
                break;

        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }
    //插入保险产品
    private String insert(Connection conn, HttpServletRequest request) {
        Product product = JSONObject.parseObject(request.getParameter("product"),Product.class);
        HttpSession session = request.getSession();
        long did = (long) session.getAttribute("rid");//当前操作的管理员所属公司id
        product.setDid(did);
        DaoUpdateResult res = ProductService.insert(conn,product);
        return JSONObject.toJSONString(res);
    }

    //修改保险产品
    private String update(Connection conn, HttpServletRequest request) {
        Product product = JSONObject.parseObject(request.getParameter("product"),Product.class);
        DaoUpdateResult res = ProductService.update(conn,product);
        return JSONObject.toJSONString(res);
    }

    //删除保险产品
    private String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoUpdateResult res = ProductService.delete(conn,id);
        return JSONObject.toJSONString(res);
    }

    //获取保险产品
    private String get(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        DaoQueryResult res = ProductService.get(conn,id);
        return JSONObject.toJSONString(res);
    }

    //获取保险产品列表
    private String getList(Connection conn, HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        HttpSession session = request.getSession();
        long rid = (long) session.getAttribute("rid");
        parameter.addCondition("did","=",rid);
        DaoQueryListResult res = ProductService.getList(conn,parameter);
        return JSONObject.toJSONString(res);
    }


}
