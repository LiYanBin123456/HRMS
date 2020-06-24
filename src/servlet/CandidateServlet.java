package servlet;

import bean.Candidate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.CandidateDAO;
import database.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet(urlPatterns = "/candidate")
public class CandidateServlet extends HttpServlet {
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
            case "updateCandidate"://修改求职者信息
                result = updateCandidate(conn,request);
                break;
            case "getCandidate"://获取求职者信息
                result = getCandidate(conn,request);
                break;
            case "getCandidates"://获取求职者列表
                result = getCandidates(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //修改求职者信息
    private String updateCandidate(Connection conn,HttpServletRequest request) {
        Candidate candidate = JSON.parseObject(request.getParameter("candidate"), Candidate.class);
        DaoUpdateResult res = CandidateDAO.update(conn, candidate);

        return JSONObject.toJSONString(res);
    }

    //获取求职者基本信息
    private String getCandidate(Connection conn,HttpServletRequest request){
        HttpSession session = request.getSession();
        long cid = (Long)session.getAttribute("cid");
        DaoQueryResult res = CandidateDAO.get(conn,cid);
        return  JSONObject.toJSONString(res);
    }

    //获取求职者列表
    private String getCandidates(Connection conn,HttpServletRequest request) {
        QueryParameter parameter = JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);

        DaoQueryListResult res = CandidateDAO.getList(conn, parameter);
        return JSONObject.toJSONString(res);
    }
}
