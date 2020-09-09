package servlet;


import bean.insurance.Insurance;
import bean.insurance.ViewInsurance;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import dao.insurance.InsuranceDao;
import database.*;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import service.insurance.InsuranceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


@WebServlet(name = "InsuranceServlet",urlPatterns = "/verify/insurance")
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
            case  "check"://校对参保单
                result = check(conn,request);
                break;
            case "export"://导出参保单
                export(conn,request,response);
                return;
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
        byte category = Byte.parseByte(request.getParameter("category"));
        byte status = Byte.parseByte(request.getParameter("status"));

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("type", "=", category);
        parameter.addCondition("status","=",status);
        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        System.out.println(rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet sheet1 = book.createSheet("导出员工参保单", 0);
            try {
                sheet1.addCell(new Label(0, 0, "姓名"));
                sheet1.addCell(new Label(1, 0, "个人代码"));
                sheet1.addCell(new Label(2, 0, "证件号码"));
                sheet1.addCell(new Label(3, 0, "参保开始年月"));
                sheet1.addCell(new Label(4, 0, "月缴费工资"));
                sheet1.addCell(new Label(5, 0, "变更原因"));
                sheet1.addCell(new Label(6, 0, "用工形式"));
                sheet1.addCell(new Label(7, 0, "是否补收（不填表示否）"));
                sheet1.addCell(new Label(8, 0, "参加工作时间"));
                sheet1.addCell(new Label(9, 0, "联系电话"));
                sheet1.addCell(new Label(10, 0, "户口性质"));
                sheet1.addCell(new Label(11, 0, "户籍地址"));
            int index = 1;
            for(ViewInsurance Insurance:insurances){
                //转化户口性质
                String houseHold =  houseHold(Insurance.getHousehold());
                //比较时间
                String msg = compareDate(Insurance.getEntry());
                sheet1.addCell(new Label(0, index, Insurance.getName()));
                sheet1.addCell(new Label(1, index, Insurance.getCode()));
                sheet1.addCell(new Label(2, index, Insurance.getCardId()));
                sheet1.addCell(new Label(3, index, sdf.format(Insurance.getStart())));
                sheet1.addCell(new jxl.write.Number(4, index, Insurance.getMoney()));
                sheet1.addCell(new Label(5, index, "正常参保登记"));
                sheet1.addCell(new Label(6, index, "合同制"));
                sheet1.addCell(new Label(7, index, msg));
                sheet1.addCell(new Label(8, index, sdf.format(Insurance.getEntry())));
                sheet1.addCell(new Label(9, index, Insurance.getPhone()));
                sheet1.addCell(new Label(10, index, houseHold));
                sheet1.addCell(new Label(11, index, Insurance.getAddress()));
                index++;
            }
            //设置列宽
            sheet1.setColumnView(0,8);
            sheet1.setColumnView(1,11);
            sheet1.setColumnView(2,19);
            sheet1.setColumnView(3,8);
            sheet1.setColumnView(4,8);
            sheet1.setColumnView(5,40);
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
        List<Insurance> insurances = JSONArray.parseArray(request.getParameter("insurances"),Insurance.class);
        DaoUpdateResult result = InsuranceService.insertBatch(conn,insurances);
        return  JSONObject.toJSONString(result);
    }


    //转换用户性质
    public static String houseHold(byte h){
        String houseHold = null;
       switch (h){
           case 0:
               houseHold = "外地城镇";
               break;
           case 1:
               houseHold = "本地城镇";
               break;
           case 2:
               houseHold = "外地农村";
               break;
           case 3:
               houseHold = "城镇";
               break;
           case 4:
               houseHold = "农村";
               break;
           case 5:
               houseHold = "港澳台";
               break;
           case 6:
               houseHold = "外籍";
               break;
       }
       return houseHold;
    }

    /**
     * 和现在年月比较时间
     * @param date
     * @return 年月相等返回 “否”，年月不等返回“是”
     */
    public static String compareDate(Date date){
        //获取当前时间
         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
         String now = df.format(new Date(System.currentTimeMillis()));
         String time =df.format(date);
         String msg = null;
        try {
            java.util.Date date1 = df.parse(now);
            java.util.Date date2 = df.parse(time);
            if(date1.compareTo(date2)!=0){
               msg = "是";
            }else{
                msg = "否";
            }
            System.out.println(date1.compareTo(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
