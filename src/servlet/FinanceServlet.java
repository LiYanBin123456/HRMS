package servlet;

import bean.log.Transaction;
import com.alibaba.fastjson.JSONObject;
import dao.TransactionDao;
import dao.finance.FinanceDao;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;

//资金管理servlet
@WebServlet(name = "FinanceServlet" ,urlPatterns = "/verify/finance")
public class FinanceServlet extends HttpServlet {
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
            case "arrive"://到帐确认
                result = arrive(conn,request);
                break;
            case "getTransactions"://获取资金往来明细
                result = getTransactions(conn,request);
                break;
            case "getTaxs"://获取个税申报列表
                result = getTaxs(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    private String getTaxs(Connection conn, HttpServletRequest request) {
        QueryParameter param = JSONObject.parseObject(request.getParameter("param"),QueryParameter.class);
        DaoQueryListResult result = FinanceDao.getTaxs(conn,param);
        return JSONObject.toJSONString(result);
    }

    //到帐确认
    private String arrive(Connection conn, HttpServletRequest request) {
        float balance = Float.parseFloat(request.getParameter("balance"));
        long id = Long.parseLong(request.getParameter("id"));
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);
        DaoUpdateResult res1 = FinanceDao.arrive(conn, balance, id);
        Transaction transaction = new Transaction();
        transaction.setCid(id);
        transaction.setComments("资金到账确认"+balance+"元。");
        transaction.setMoney(balance);
        transaction.setTime(new Date());
        //生成明细
        DaoUpdateResult res2 = TransactionDao.insert(conn,transaction);
        //事务处理
        if(res1.success && res2.success){
            ConnUtil.commit(conn);
            return JSONObject.toJSONString(res1);
        }else{
            ConnUtil.rollback(conn);
            JSONObject json = new JSONObject();
            json.put("success",false);
            json.put("msg","资金到账确认失败");
            return json.toJSONString();
        }
    }

    //获取资金往来明细
    private String getTransactions(Connection conn, HttpServletRequest request) {
        long cid = Long.parseLong(request.getParameter("cid"));
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("cid","=",cid);
        DaoQueryListResult res = FinanceDao.getTransactions(conn,parameter);
        return JSONObject.toJSONString(res);
    }
}
