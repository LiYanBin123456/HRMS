package servlet;

import bean.employee.Employee;
import bean.employee.EmployeeExtra;
import bean.insurance.Insurance;
import com.alibaba.fastjson.JSONObject;
import dao.employee.EmployeeDao;
import dao.employee.ExtraDao;
import dao.insurance.InsuranceDao;
import database.*;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;


@WebServlet(name = "InsuranceServlet",urlPatterns = "/insurance")
public class InsuranceServlet extends HttpServlet {
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
            case "insertBatch"://批量插入参保单
                result = insertBatch(conn,request);
                break;
            case "update"://修改参保单
                result = update(conn,request);
                break;
            case "delete"://删除参保单
                result = delete(conn,request);
                break;
            case "getList"://获取参保单列表
                result = getList(conn,request);
                break;
            case "get"://获取参保单
                result = get(conn,request);
                break;
            case "export"://导出参保单
                export(conn,request,response);
                return;
            case  "check"://校对参保单
                result = check(conn,request);
                break;
        }
        ConnUtil.closeConnection(conn);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //核对
    private String check(Connection conn, HttpServletRequest request) {
        return  null;
    }

    private void export(Connection conn, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=insurance.xls");
        long id = Long.parseLong((request.getParameter("id")));
        byte category = Byte.parseByte(request.getParameter("category"));
        byte status = Byte.parseByte(request.getParameter("status"));

        Employee employee = null;
        Insurance insurance = null;
        EmployeeExtra employeeExtra = null;

        //查询条件
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid", "=", id);
        conditions.add("type", "=", category);


        employee = (Employee) EmployeeDao.get(conn, id).data;
        employeeExtra = (EmployeeExtra) ExtraDao.get(conn, id).data;
        insurance = (Insurance) InsuranceDao.get(conn, conditions).data;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet sheet1 = book.createSheet("导出员工参保单", 0);
            //WritableSheet sheet2 = book.createSheet("明细", 1);
            try {
                sheet1.addCell(new Label(0, 0, "姓名"));
                sheet1.addCell(new Label(0, 1, "个人代码"));
                sheet1.addCell(new Label(0, 2, "证件号码"));
                sheet1.addCell(new Label(0, 3, "参保开始年月"));
                sheet1.addCell(new Label(0, 4, "月缴费工资"));
                sheet1.addCell(new Label(0, 5, "变更原因"));
                sheet1.addCell(new Label(0, 6, "用工形式"));
                sheet1.addCell(new Label(0, 7, "是否补收（不填表示否）"));
                sheet1.addCell(new Label(0, 8, "参加工作时间"));
                sheet1.addCell(new Label(0, 9, "联系电话"));
                sheet1.addCell(new Label(0, 10, "户口性质"));
                sheet1.addCell(new Label(0, 11, "户籍地址"));

                sheet1.addCell(new Label(1, 0, employee.getName()));
                sheet1.addCell(new Label(1, 1, insurance.getCode()));
                sheet1.addCell(new Label(1, 2, employee.getCardId()));
                sheet1.addCell(new Label(1, 3, sdf.format(insurance.getStart())));
                sheet1.addCell(new jxl.write.Number(1, 4, insurance.getMoney()));
                sheet1.addCell(new Label(1, 5, "正常参保登记"));
                sheet1.addCell(new Label(1, 6, "合同制"));
                sheet1.addCell(new Label(1, 7, "否"));
                sheet1.addCell(new Label(1, 8, employee.getPhone()));
                sheet1.addCell(new jxl.write.Number(1, 9, employeeExtra.getHousehold()));
                sheet1.addCell(new Label(1, 10, employeeExtra.getAddress()));

                //设置列宽
                sheet1.setColumnView(0, 10);
                sheet1.setColumnView(1, 30);

            /*sheet2.addCell(new Label(0,0,"报名编号"));
            sheet2.addCell(new Label(1,0,"报名时间"));
            sheet2.addCell(new Label(2,0,"身份证号"));
            sheet2.addCell(new Label(3,0,"姓名"));
            sheet2.addCell(new Label(4,0,"类型"));
            sheet2.addCell(new Label(5,0,"准操项目"));
            int index = 1;
            for(ViewApp app:apps){
                sheet2.addCell(new jxl.write.Number(0,index,app.getId()));
                sheet2.addCell(new Label(1,index,sdf.format(app.getDate())));
                sheet2.addCell(new Label(2,index,app.getCardId()));
                sheet2.addCell(new Label(3,index,app.getName()));
                sheet2.addCell(new Label(4,index,app.getdTypeString()));
                sheet2.addCell(new Label(5,index,Item.getItemText(app.getItem())));
                index++;
            }

            //设置列宽
            sheet2.setColumnView(0,8);
            sheet2.setColumnView(1,11);
            sheet2.setColumnView(2,19);
            sheet2.setColumnView(3,8);
            sheet2.setColumnView(4,8);
            sheet2.setColumnView(5,40);*/
                book.write();
                book.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        ConnUtil.closeConnection(conn);
    }



    //获取
    private String get(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        byte category = Byte.parseByte(request.getParameter("category"));
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        conditions.add("type","=",category);
        DaoQueryResult result = InsuranceDao.get(conn,conditions);
        return JSONObject.toJSONString(result);
    }

    //获取列表
    private String getList(Connection conn, HttpServletRequest request) {
        QueryParameter parameter =JSONObject.parseObject(request.getParameter("param"), QueryParameter.class);
        byte category = Byte.parseByte(request.getParameter("category"));
       DaoQueryListResult result = InsuranceDao.getList(conn,parameter,category);
        return JSONObject.toJSONString(result);
    }

    //删除
    private String delete(Connection conn, HttpServletRequest request) {
        long id = Long.parseLong(request.getParameter("id"));
        byte category = Byte.parseByte(request.getParameter("category"));
        DaoUpdateResult result = InsuranceDao.delete(conn,id,category);
        return JSONObject.toJSONString(result);
    }

    //修改
    private String update(Connection conn, HttpServletRequest request) {
        Insurance insurance =JSONObject.parseObject(request.getParameter("insurance"), Insurance.class);
        DaoUpdateResult result = InsuranceDao.update(conn,insurance);
        return JSONObject.toJSONString(result);
    }

    //批量插入
    private String insertBatch(Connection conn, HttpServletRequest request) {
      return  null;
    }


}
