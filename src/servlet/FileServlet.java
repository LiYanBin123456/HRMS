package servlet;

import bean.client.Items;
import bean.client.MapSalary;

import bean.employee.ViewEmployee;
import bean.settlement.Detail1;
import bean.settlement.ViewDetail1;
import bean.settlement.ViewDetail2;
import bean.settlement.ViewDetail3;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.client.MapSalaryDao;
import dao.employee.EmployeeDao;

import dao.settlement.Detail1Dao;
import dao.settlement.Detail2Dao;
import dao.settlement.Detail3Dao;
import database.*;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Label;
import org.apache.commons.io.IOUtils;
import utills.XlsUtil;
import utills.Calculate;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;
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
        Connection conn = ConnUtil.getConnection();

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
            case "readExcel"://读取xls数据反馈给前台
                result = readExcel(request);
                break;
            case "existContract"://判断合同附件是否存在
                result = existContract(request);
                break;
            case "existModel"://判断合同附件是否存在
                result = existModel(request);
                break;
            case "downloadContract"://下载合同复印件
                downloadContract(request,response);
                return;
            case "downloadModel"://下载模板
                downloadModel(request,response);
                return;
            case "downloadDetail1"://下载商业保险结算单明细
                downloadDetail1(conn,request,response);
                return;
            case "exportDetail1"://导出商业保险结算单明细
                    exportDetail1(conn,request,response);
                return;
            case "exportDetail2"://下载小时工结算单明细
                exportDetail2(conn,request,response);
                return;
            case "exportDetail3"://下载商业保险结算单明细
                exportDetail3(conn,request,response);
                return;
            default:
                result = "{\"success\":false,\"msg\":\"参数错误\"}";
        }

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }


    //判断模板是否存在
    private String existModel(HttpServletRequest request) throws IOException {
        int category = Integer.parseInt(request.getParameter("category"));
        String fileName=null;

        switch (category){
            case 0://小时工模板
                fileName = "detail2"+".xls";
                break;
            case 1://商业保险结算单明细模板
                fileName = "detail3"+".xls";
                break;
            case 2://员工模板
                fileName = "employee"+".xls";
                break;
        }
        String fullFileName = getServletContext().getRealPath("/excelFile/" + fileName);
        File file = new File(fullFileName);

        JSONObject json = new JSONObject();
        json.put("success",true);
        json.put("exist",file.exists()?true:false);
        return json.toJSONString();
    }

    //下载模板
    private void downloadModel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int category = Integer.parseInt(request.getParameter("category"));
        String fileName=null;
        switch (category){
            case 0://小时工模板
                fileName = "detail2"+".xls";
                break;
            case 1://商业保险结算单明细模板
                fileName = "detail3"+".xls";
                break;
            case 2://员工模板
                fileName = "employee"+".xls";
                break;
        }
        String fullFileName = getServletContext().getRealPath("/excelFile/" + fileName);
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

    //读取excel表
    private String readXls(HttpServletRequest request) throws IOException, ServletException {
        String result = null;
        Part part = request.getPart("file");
            try {//获取part中的文件，读取数据
                InputStream is = part.getInputStream();
                List<JSONObject> data = XlsUtil.read(is,"信息表","元数据");
                if(null == data){
                    result = "{\"success\":false,\"msg\":\"xls文件不符合要求，请下载模板再重新填写\"}";
                }else{
                    JSONObject json = new JSONObject();
                    json.put("success",true);
                    json.put("data",data);
                    result = json.toJSONString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        System.out.println(result);
        return result;
    }

    //上传合同文件
    private String upload(HttpServletRequest request) throws IOException, ServletException {
        DaoUpdateResult result = new DaoUpdateResult();
        String id = request.getParameter("id");
            //将文件上传到服务器并且以合同id命名
            String file;
            Part part = request.getPart("file");
            if(part!=null){//获取文件的名称
                String header = part.getHeader("content-disposition");
                String headerName = header.substring(header.indexOf("filename")+10, header.length()-1); //截取字符串获取文件名称
                String suffixName=headerName.substring(headerName.indexOf(".")+1); //获取文件名后缀
                String s="pdf";
                if(suffixName.equals(s)){
                    InputStream put = part.getInputStream();  //获取文件流
                    String url = request.getServletContext().getRealPath("/contractFile"); //获取文件夹的真实路径
                    File uploadDir = new File(url);
                    if (!uploadDir.exists()) { // 如果该文件夹不存在则创建
                        uploadDir.mkdirs();
                    }

                    file = id+"."+suffixName;
                    FileOutputStream fos = new FileOutputStream(new File(url, file));   //建立对拷流
                    IOUtils.copy(put, fos);
                    put.close();
                    fos.close();
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

    //判断合同文件是否存在
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

    //下载合同文件
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

    //上传员工头像
    private String uploadImg(HttpServletRequest request) throws IOException, ServletException {
        String  msg = null;
        String id = request.getParameter("id");
        String file ;
        Part part = request.getPart("file");
        if(part!=null){
            //获取文件的名称
            String header = part.getHeader("content-disposition");
            //截取字符串获取文件名称
            String headername = header.substring(header.indexOf("filename")+10, header.length()-1);
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

    //下载普通结算单明细
    private void downloadDetail1(Connection conn, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=detailModel.xls");

        HttpSession session = request.getSession();
        long did = (long) session.getAttribute("rid");//当前操作的管理员所属公司id
        long cid = Long.parseLong(request.getParameter("cid"));//合作单位id

        boolean flag=MapSalaryDao.exist(cid,conn).exist;//判断客户是否有自定义工资项
        List<Items> itemList = new ArrayList<>();
        if(flag){//如果有
            MapSalary mapSalary = (MapSalary) MapSalaryDao.getLast(cid, conn).data;
            itemList = mapSalary.getItemList();
        }

        //根据条件找到派遣到该单位的员工列表，条件有cid，did，类型为外派或者派遣员工，用工性质不是小时工，在职
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("cid","=",cid);
        parameter.addCondition("did","=",did);
        parameter.addCondition("type","=",1);
        parameter.addCondition("category","!=",2);
        parameter.addCondition("status","=",0);
        List<ViewEmployee> employeeList = JSONArray.parseArray(JSONObject.toJSONString(EmployeeDao.getList(conn,parameter).rows),ViewEmployee.class);

        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("信息表", 0);
        WritableSheet sheet2 = book.createSheet("元数据", 1);

        WritableCellFormat wcf = new WritableCellFormat();
        Color color = Color.decode("#d6dae0"); // 自定义的颜色
        book.setColourRGB(Colour.ORANGE, color.getRed(),
        color.getGreen(), color.getBlue());
        try {
            wcf.setBackground(Colour.ORANGE);
        } catch (WriteException e) {
            e.printStackTrace();
        }
        try {
            sheet1.addCell(new Label(0, 0, "员工姓名",wcf));
            sheet1.addCell(new Label(1, 0, "身份证号码",wcf));
            sheet1.addCell(new Label(2, 0, "基本工资",wcf));
            sheet1.addCell(new Label(3, 0, "个人养老",wcf));
            sheet1.addCell(new Label(4, 0, "个人医疗",wcf));
            sheet1.addCell(new Label(5, 0, "个人失业",wcf));
            sheet1.addCell(new Label(6, 0, "个人大病",wcf));
            sheet1.addCell(new Label(7, 0, "个人公积金",wcf));
            sheet1.addCell(new Label(8, 0, "单位养老",wcf));
            sheet1.addCell(new Label(9, 0, "单位医疗",wcf));
            sheet1.addCell(new Label(10, 0, "单位失业",wcf));
            sheet1.addCell(new Label(11, 0, "单位工伤",wcf));
            sheet1.addCell(new Label(12, 0, "单位大病",wcf));
            sheet1.addCell(new Label(13, 0, "单位生育",wcf));
            sheet1.addCell(new Label(14, 0, "单位公积金",wcf));
            sheet1.addCell(new Label(15, 0, "个税",wcf));
            if(flag){
                int  c = 0;
                for(int i = 0;i<itemList.size();i++){
                    c = i+16;
                    sheet1.addCell(new Label(c, 0, itemList.get(i).getField(),wcf));
                }
                sheet1.addCell(new Label(c+1, 0, "应付",wcf));
                sheet1.addCell(new Label(c+2, 0, "实付",wcf));
            }else {
                sheet1.addCell(new Label(16, 0, "应付",wcf));
                sheet1.addCell(new Label(17, 0, "实付",wcf));
            }
            int index = 1;
            for( ViewEmployee viewEmployee:employeeList){//根据员工生成明细，如果没有员工则不生成
                Detail1 detail = Calculate.calculateInsurance(viewEmployee.getId());

                sheet1.addCell(new Label(0, index, viewEmployee.getName()));
                sheet1.addCell(new Label(1, index, viewEmployee.getCardId()));
                sheet1.addCell(new jxl.write.Number(2, index, 0));
                sheet1.addCell(new jxl.write.Number(3, index, detail.getPension1()));
                sheet1.addCell(new jxl.write.Number(4, index, detail.getMedicare1()));
                sheet1.addCell(new jxl.write.Number(5, index, detail.getUnemployment1()));
                sheet1.addCell(new jxl.write.Number(6, index, detail.getDisease1()));
                sheet1.addCell(new jxl.write.Number(7, index, detail.getFund1()));
                sheet1.addCell(new jxl.write.Number(8, index, detail.getPension2()));
                sheet1.addCell(new jxl.write.Number(9, index, detail.getMedicare2()));
                sheet1.addCell(new jxl.write.Number(10, index, detail.getUnemployment2()));
                sheet1.addCell(new jxl.write.Number(11, index, detail.getInjury()));
                sheet1.addCell(new jxl.write.Number(12, index, detail.getDisease2()));
                sheet1.addCell(new jxl.write.Number(13, index, detail.getBirth()));
                sheet1.addCell(new jxl.write.Number(14, index, detail.getFund2()));
                sheet1.addCell(new jxl.write.Number(15, index, 0));
                if(flag) {//判断客户是否存在自定义字段
                     int c2 = 0;
                    for (int i = 0; i < itemList.size(); i++) {
                        c2 = i + 16;
                        sheet1.addCell(new jxl.write.Number(c2 , index, 0));
                    }
                    sheet1.addCell(new jxl.write.Number(c2 + 1, index, 0));
                    sheet1.addCell(new jxl.write.Number(c2 + 2, index, 0));
                }else {
                    sheet1.addCell(new jxl.write.Number( 15, index, 0));
                    sheet1.addCell(new jxl.write.Number( 16, index,0));
                }
                index++;
            }


            sheet2.addCell(new Label(0, 0, "字段名"));
            sheet2.addCell(new Label(0, 1, "name"));
            sheet2.addCell(new Label(0, 2, "cardId"));
            sheet2.addCell(new Label(0, 3, "base"));
            sheet2.addCell(new Label(0, 4, "pension1"));
            sheet2.addCell(new Label(0, 5, "medicare1"));
            sheet2.addCell(new Label(0, 6, "unemployment1"));
            sheet2.addCell(new Label(0, 7, "disease1"));
            sheet2.addCell(new Label(0, 8, "fund1"));
            sheet2.addCell(new Label(0, 9, "pension2"));
            sheet2.addCell(new Label(0, 10, "medicare2"));
            sheet2.addCell(new Label(0, 11, "unemployment2"));
            sheet2.addCell(new Label(0, 12, "injury"));
            sheet2.addCell(new Label(0, 13, "disease2"));
            sheet2.addCell(new Label(0, 14, "birth"));
            sheet2.addCell(new Label(0, 15, "fund2"));
            sheet2.addCell(new Label(0, 16, "tax"));
            if(flag){
                int  c = 0;
                for(int i = 0;i<itemList.size();i++){
                    c = i+17;
                    String name = "f"+(i+1);
                    sheet2.addCell(new Label(0, c, name));
                }
                sheet2.addCell(new Label(0,c+1,  "payable"));
                sheet2.addCell(new Label(0,c+2,  "paid"));
            }else {
                sheet2.addCell(new Label(0,17,  "payable"));
                sheet2.addCell(new Label(0,18,  "paid"));
            }

            sheet2.addCell(new Label(1, 0, "类型"));
            sheet2.addCell(new Label(1, 1, "string"));
            sheet2.addCell(new Label(1, 2, "string"));
            sheet2.addCell(new Label(1, 3, "float"));
            sheet2.addCell(new Label(1, 4, "float"));
            sheet2.addCell(new Label(1, 5, "float"));
            sheet2.addCell(new Label(1, 6, "float"));
            sheet2.addCell(new Label(1, 7, "float"));
            sheet2.addCell(new Label(1, 8, "float"));
            sheet2.addCell(new Label(1, 9, "float"));
            sheet2.addCell(new Label(1, 10, "float"));
            sheet2.addCell(new Label(1, 11, "float"));
            sheet2.addCell(new Label(1, 12, "float"));
            sheet2.addCell(new Label(1, 13, "float"));
            sheet2.addCell(new Label(1, 14, "float"));
            sheet2.addCell(new Label(1, 15, "float"));
            sheet2.addCell(new Label(1, 16, "float"));
            if(flag){
                int  c = 0;
                for(int i = 0;i<itemList.size();i++){
                    c = i+17;
                    sheet2.addCell(new Label(1, c, "float"));
                }
                sheet2.addCell(new Label(1,c+1,  "float"));
                sheet2.addCell(new Label(1,c+2,  "float"));
            }else {
                sheet2.addCell(new Label(1,17,  "float"));
                sheet2.addCell(new Label(1,18,  "float"));
            }


            sheet2.addCell(new Label(2, 0, "是否允许为空"));
            sheet2.addCell(new Label(2, 1, "False"));
            sheet2.addCell(new Label(2, 2, "False"));
            sheet2.addCell(new Label(2, 3, "False"));
            sheet2.addCell(new Label(2, 4, "False"));
            sheet2.addCell(new Label(2, 5, "False"));
            sheet2.addCell(new Label(2, 6, "False"));
            sheet2.addCell(new Label(2, 7, "False"));
            sheet2.addCell(new Label(2, 8, "False"));
            sheet2.addCell(new Label(2, 9, "False"));
            sheet2.addCell(new Label(2, 10, "False"));
            sheet2.addCell(new Label(2, 11, "False"));
            sheet2.addCell(new Label(2, 12, "False"));
            sheet2.addCell(new Label(2, 13, "False"));
            sheet2.addCell(new Label(2, 14, "False"));
            sheet2.addCell(new Label(2, 15, "False"));
            sheet2.addCell(new Label(2, 16, "False"));
            if(flag){
                int  c = 0;
                for(int i = 0;i<itemList.size();i++){
                    c = i+17;
                    sheet2.addCell(new Label(2, c, "False"));
                }
                sheet2.addCell(new Label(2,c+1,  "False"));
                sheet2.addCell(new Label(2,c+2,  "False"));
            }else {
                sheet2.addCell(new Label(2,17,  "False"));
                sheet2.addCell(new Label(2,18,  "False"));
            }
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        ConnUtil.closeConnection(conn);
    }

    //导出普通结算单明细
    private void exportDetail1(Connection conn, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=details1.xls");

        long cid = Long.parseLong(request.getParameter("cid"));//获取合作客户的id
        boolean flag=MapSalaryDao.exist(cid,conn).exist;//判断客户是否有自定义工资项

        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List< ViewDetail1> detail1s = JSONArray.parseArray(rows, ViewDetail1.class);
        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("结算单明细", 0);
        try {
            sheet1.addCell(new Label(0, 0, "员工姓名"));
            sheet1.addCell(new Label(1, 0, "身份证号码"));
            sheet1.addCell(new Label(2, 0, "基本工资"));
            sheet1.addCell(new Label(3, 0, "个人养老"));
            sheet1.addCell(new Label(4, 0, "个人医疗"));
            sheet1.addCell(new Label(5, 0, "个人失业"));
            sheet1.addCell(new Label(6, 0, "个人大病"));
            sheet1.addCell(new Label(7, 0, "个人公积金"));
            sheet1.addCell(new Label(8, 0, "单位养老"));
            sheet1.addCell(new Label(9, 0, "单位医疗"));
            sheet1.addCell(new Label(10, 0, "单位失业"));
            sheet1.addCell(new Label(11, 0, "单位工伤"));
            sheet1.addCell(new Label(12, 0, "单位大病"));
            sheet1.addCell(new Label(13, 0, "单位生育"));
            sheet1.addCell(new Label(14, 0, "单位公积金"));
            sheet1.addCell(new Label(15, 0, "个税"));
            if(flag){
                MapSalary mapSalary =  (MapSalary)MapSalaryDao.getLast(cid,conn).data;
                String map = mapSalary.getItems();
                String[] maps = map.split(";");//maps[{加班工资,1},{考勤扣款,0}];
                int  c = 0;
                for(int i = 0;i<maps.length;i++){
                    c = i+16;
                    sheet1.addCell(new Label(c, 0, maps[i]));
                }
                sheet1.addCell(new Label(c+1, 0, "应付"));
                sheet1.addCell(new Label(c+2, 0, "实付"));
            }else {
                sheet1.addCell(new Label(16, 0, "应付"));
                sheet1.addCell(new Label(17, 0, "实付"));
            }
            int index = 1;
            for( ViewDetail1 detail1:detail1s){
                sheet1.addCell(new Label(0, index, detail1.getName()));
                sheet1.addCell(new Label(1, index, detail1.getCardId()));
                sheet1.addCell(new jxl.write.Number(2, index, detail1.getBase()));
                sheet1.addCell(new jxl.write.Number(3, index, detail1.getPension1()));
                sheet1.addCell(new jxl.write.Number(4, index, detail1.getMedicare1()));
                sheet1.addCell(new jxl.write.Number(5, index, detail1.getUnemployment1()));
                sheet1.addCell(new jxl.write.Number(6, index, detail1.getDisease1()));
                sheet1.addCell(new jxl.write.Number(7, index, detail1.getFund1()));
                sheet1.addCell(new jxl.write.Number(8, index, detail1.getPension2()));
                sheet1.addCell(new jxl.write.Number(9, index, detail1.getMedicare2()));
                sheet1.addCell(new jxl.write.Number(10, index, detail1.getUnemployment2()));
                sheet1.addCell(new jxl.write.Number(11, index, detail1.getInjury()));
                sheet1.addCell(new jxl.write.Number(12, index, detail1.getDisease2()));
                sheet1.addCell(new jxl.write.Number(13, index, detail1.getBirth()));
                sheet1.addCell(new jxl.write.Number(14, index, detail1.getFund2()));
                sheet1.addCell(new jxl.write.Number(15, index, detail1.getTax()));
                if(flag) {//判断客户是否存在自定义字段
                    MapSalary mapSalary = (MapSalary) MapSalaryDao.getLast(cid, conn).data;
                    String map = mapSalary.getItems();
                    String[] maps = map.split(";");//maps[{加班工资,1},{考勤扣款,0}];
                    int c2 = 0;
                    for (int i = 0; i < maps.length; i++) {
                        c2 = i + 16;
                        int in = i + 1;
                        String name = "getF" + in;
                        String value = null;
                        Method method;
                        try {//通过反射获取对应的值
                            method = detail1.getClass().getSuperclass().getMethod(name);
                            value = method.invoke(detail1).toString();
                            System.out.println(value);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        sheet1.addCell(new Label(c2, index, value));
                    }
                    sheet1.addCell(new jxl.write.Number(c2 + 1, index, detail1.getTax()));
                    sheet1.addCell(new jxl.write.Number(c2 + 2, index, detail1.getTax()));
                }else {
                    sheet1.addCell(new jxl.write.Number( 15, index, detail1.getTax()));
                    sheet1.addCell(new jxl.write.Number( 16, index, detail1.getTax()));
                }
                index++;
            }
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        ConnUtil.closeConnection(conn);
    }

    //导出小时工结算的明细
    private void exportDetail2(Connection conn, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=details2.xls");

        long sid = Long.parseLong(request.getParameter("id"));//结算单id
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        DaoQueryListResult result = Detail2Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);

        List<ViewDetail2> detail2s = JSONArray.parseArray(rows, ViewDetail2.class);
        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("小时工结算单明细", 0);
        try {
            sheet1.addCell(new Label(0, 0, "员工姓名"));
            sheet1.addCell(new Label(1, 0, "身份证号码"));
            sheet1.addCell(new Label(2, 0, "工时"));
            sheet1.addCell(new Label(3, 0, "单价"));
            sheet1.addCell(new Label(4, 0, "餐费"));
            sheet1.addCell(new Label(5, 0, "交通费"));
            sheet1.addCell(new Label(6, 0, "住宿费"));
            sheet1.addCell(new Label(7, 0, "水电费"));
            sheet1.addCell(new Label(8, 0, "保险费"));
            sheet1.addCell(new Label(9, 0, "个税"));
            sheet1.addCell(new Label(10, 0, "其他1"));
            sheet1.addCell(new Label(11, 0, "其他2"));
            sheet1.addCell(new Label(12, 0, "应付"));
            sheet1.addCell(new Label(13, 0, "实付"));

            int index = 1;
            for(ViewDetail2 detail2:detail2s){
                sheet1.addCell(new Label(0, index, detail2.getName()));
                sheet1.addCell(new Label(1, index, detail2.getCardId()));
                sheet1.addCell(new jxl.write.Number(2, index, detail2.getHours()));
                sheet1.addCell(new jxl.write.Number(3, index, detail2.getPrice()));
                sheet1.addCell(new jxl.write.Number(4, index, detail2.getFood()));
                sheet1.addCell(new jxl.write.Number(5, index, detail2.getTraffic()));
                sheet1.addCell(new jxl.write.Number(6, index, detail2.getAccommodation()));
                sheet1.addCell(new jxl.write.Number(7, index, detail2.getUtilities()));
                sheet1.addCell(new jxl.write.Number(8, index, detail2.getInsurance()));
                sheet1.addCell(new jxl.write.Number(9, index, detail2.getTax()));
                sheet1.addCell(new jxl.write.Number(10, index, detail2.getOther1()));
                sheet1.addCell(new jxl.write.Number(11, index, detail2.getOther2()));
                sheet1.addCell(new jxl.write.Number(12, index, detail2.getPayable()));
                sheet1.addCell(new jxl.write.Number(13, index, detail2.getPaid()));
                index++;
            }
            //设置列宽
            sheet1.setColumnView(0,10);
            sheet1.setColumnView(1,20);
            sheet1.setColumnView(2,10);
            sheet1.setColumnView(3,10);
            sheet1.setColumnView(4,10);
            sheet1.setColumnView(5,10);
            sheet1.setColumnView(6,10);
            sheet1.setColumnView(7,10);
            sheet1.setColumnView(8,10);
            sheet1.setColumnView(9,10);
            sheet1.setColumnView(10,10);
            sheet1.setColumnView(11,10);
            sheet1.setColumnView(12,10);
            sheet1.setColumnView(13,15);
            sheet1.setColumnView(14,15);
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        ConnUtil.closeConnection(conn);
    }

    //导出商业保险结算单明细
    private void exportDetail3(Connection conn, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=details3.xls");

        long sid = Long.parseLong(request.getParameter("id"));//结算单id
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        DaoQueryListResult result = Detail3Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);

        List<ViewDetail3> detail3s = JSONArray.parseArray(rows, ViewDetail3.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("商业保险结算单明细", 0);
        try {
            sheet1.addCell(new Label(0, 0, "员工姓名"));
            sheet1.addCell(new Label(1, 0, "身份证号码"));
            sheet1.addCell(new Label(2, 0, "保险产品"));
            sheet1.addCell(new Label(3, 0, "工作地点"));
            sheet1.addCell(new Label(4, 0, "工作岗位"));
            sheet1.addCell(new Label(5, 0, "保费"));


            int index = 1;
            for(ViewDetail3 detail3:detail3s){
                sheet1.addCell(new Label(0, index, detail3.getCname()));
                sheet1.addCell(new Label(1, index, detail3.getCardId()));
                sheet1.addCell(new Label(1, index, detail3.getPname()));
                sheet1.addCell(new Label(1, index, detail3.getPlace()));
                sheet1.addCell(new Label(1, index, detail3.getPost()));
                sheet1.addCell(new jxl.write.Number(5, index, detail3.getPrice()));
                index++;
            }
            //设置列宽
            sheet1.setColumnView(0,10);
            sheet1.setColumnView(1,10);
            sheet1.setColumnView(2,10);
            sheet1.setColumnView(3,10);
            sheet1.setColumnView(4,10);
            sheet1.setColumnView(5,10);
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        ConnUtil.closeConnection(conn);
    }



    private String readExcel(HttpServletRequest request) {
        String result;
        try {
            Part part = getPart(request);
            InputStream is = part.getInputStream();
            List<JSONObject> data = XlsUtil.read(is,"信息表","元数据");
            if(null == data){
                result = "{\"success\":false,\"msg\":\"xls文件不符合要求，请下载模板再重新填写\"}";
            }else{
                JSONObject json = new JSONObject();
                json.put("success",true);
                json.put("data",data);
                result = json.toJSONString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"success\":false,\"msg\":\"读取Excel错误，请联系开发人员\"}";
        }

        return result;
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
