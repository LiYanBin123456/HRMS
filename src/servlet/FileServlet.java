package servlet;

import bean.admin.Account;
import bean.client.Items;
import bean.client.MapSalary;

import bean.employee.Employee;
import bean.employee.ViewEmployee;
import bean.settlement.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.client.MapSalaryDao;
import dao.employee.EmployeeDao;

import dao.finance.FinanceDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Detail2Dao;
import dao.settlement.Detail3Dao;
import database.*;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Label;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.io.IOUtils;
import utills.Calculate;
import utills.XlsUtil;
import utills.IDCardUtil;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sound.midi.Soundbank;
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
            case "upload"://上传文件
                result = upload(request);
                break;
            /*case "uploadImg"://上传员工头像
                result = uploadImg(request);
                break;*/
            case "readDeduct"://读取个税表中的数据
                result = readDeduct(request);
                break;
            case "exist"://判断文件是否存在
                result = exist(request);
                break;/*
            case "existModel"://判断合同附件是否存在
                result = existModel(request);
                break;*/
            case "download"://下载文件
                download(request,response);
                return;/*
            case "downloadModel"://下载模板
                downloadModel(request,response);
                return;*/
            case "downloadDetail1"://下载结算单明细模板
                downloadDetail1(conn,request,response);
                return;
            case "exportDetail1"://导出结算单明细
                    exportDetail1(conn,request,response);
                return;
            case "exportDetail2"://下载小时工结算单明细
                exportDetail2(conn,request,response);
                return;
            case "exportDetail3"://下载商业保险结算单明细
                exportDetail3(conn,request,response);
                return;
            case "exportTax"://导出个税申报表
                exportTax(conn,request,response);
                return;
            case "exportTaxEmployee"://导出个税申报名单
                exportTaxEmployee(conn,request,response);
                return;
            default:
                result = "{\"success\":false,\"msg\":\"参数错误\"}";
        }

        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    //导出个税申报名单表
    private void exportTaxEmployee(Connection conn, HttpServletRequest request, HttpServletResponse response)  {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=tax1.xls");

        //读取模板
        String fileName = "tax1.xls";
        String fullFileName = getServletContext().getRealPath("/excelFile/" + fileName);
        File file = new File(fullFileName);
        Workbook book = null;

        //查询出员工，条件限制先留着以后交流修改
        QueryParameter parameter = new QueryParameter();
        List<Employee> employeeList = JSONArray.parseArray(JSONObject.toJSONString(EmployeeDao.getList(conn,parameter).rows),Employee.class);
        try {
            //获取模板
            book = Workbook.getWorkbook(file);

            // jxl.Workbook 对象是只读的，所以如果要修改Excel，需要创建一个可读的副本，副本指向原Excel文件（即下面的new File(excelpath)）
            //WritableWorkbook如果直接createWorkbook模版文件会覆盖原有的文件
            WritableWorkbook workbook = Workbook.createWorkbook(response.getOutputStream(),book);
            WritableSheet sheet = workbook.getSheet(0);//获取第一个sheet
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int index = 1;

            for(Employee e:employeeList){
                sheet.addCell(new Label(0, index, ""));//工号
                sheet.addCell(new Label(1, index, e.getName()));//姓名
                sheet.addCell(new Label(2, index, "居民身份证"));//证件类型
                sheet.addCell(new Label(3, index,e.getCardId()));//证件号码
                sheet.addCell(new Label(4, index,"中国"));//国籍
                sheet.addCell(new Label(5, index,e.getCardId()==null?"":IDCardUtil.getSex(e.getCardId())));//性别
                sheet.addCell(new Label(6, index,e.getCardId()==null?"":IDCardUtil.getBirthday(e.getCardId())));//出生日期
                sheet.addCell(new Label(8, index, "雇员"));//任职受雇从业类型
                sheet.addCell(new Label(9, index,e.getPhone()));//手机号码
                index++;
            }
            workbook.write();
            workbook.close();
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        ConnUtil.closeConnection(conn);

    }

    //导出个税申报表
    private void exportTax(Connection conn, HttpServletRequest request, HttpServletResponse response)  {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=tax2.xls");

        //读取模板
        String fileName = "tax2.xls";
        String fullFileName = getServletContext().getRealPath("/excelFile/" + fileName);
        File file = new File(fullFileName);
        Workbook book = null;

        //查询出所有个税申报，但是目前还不清出需要那些条件限制
        QueryParameter parameter = new QueryParameter();
        List<ViewTax> viewTaxes = (List<ViewTax>) FinanceDao.getTaxs(conn,parameter).rows;
        try {
            //获取模板
            book = Workbook.getWorkbook(file);

            // jxl.Workbook 对象是只读的，所以如果要修改Excel，需要创建一个可读的副本，副本指向原Excel文件（即下面的new File(excelpath)）
            //WritableWorkbook如果直接createWorkbook模版文件会覆盖原有的文件
            WritableWorkbook workbook = Workbook.createWorkbook(response.getOutputStream(),book);
            WritableSheet sheet = workbook.getSheet(0);//获取第一个sheet

            int index = 1;
            for(ViewTax v:viewTaxes){
                sheet.addCell(new Label(0, index, ""));//工号
                sheet.addCell(new Label(1, index, v.getName()));//姓名
                sheet.addCell(new Label(2, index, "居民身份证"));//证件类型
                sheet.addCell(new Label(3, index, v.getCardId()));//证件号码
                sheet.addCell(new jxl.write.Number(4, index, v.getPayable()));//本期收入
                sheet.addCell(new jxl.write.Number(5, index, 0));//本期免税收入
                sheet.addCell(new jxl.write.Number(6, index, v.getPension1()));//基本养老保险费
                sheet.addCell(new jxl.write.Number(7, index, v.getMedicare1()));//基本医疗保险费
                sheet.addCell(new jxl.write.Number(8, index, v.getUnemployment1()));//失业保险费
                sheet.addCell(new jxl.write.Number(9, index, v.getFund1()));//住房公积金
                sheet.addCell(new jxl.write.Number(10, index, v.getDeduct1()));//累计子女教育
                sheet.addCell(new jxl.write.Number(11, index, v.getDeduct3()));//累计继续教育
                sheet.addCell(new jxl.write.Number(12, index, v.getDeduct5()));//累计住房贷款利息
                sheet.addCell(new jxl.write.Number(13, index, v.getDeduct6()));//累计住房租金
                sheet.addCell(new jxl.write.Number(14, index, v.getDeduct2()));//累计赡养老人
                sheet.addCell(new jxl.write.Number(15, index, 0));//企业(职业)年金
                sheet.addCell(new jxl.write.Number(16, index, 0));//商业健康保险
                sheet.addCell(new jxl.write.Number(17, index, 0));//税延养老保险
                sheet.addCell(new jxl.write.Number(18, index, 0));//其他
                sheet.addCell(new jxl.write.Number(19, index, 0));//准予扣除的捐赠额
                sheet.addCell(new jxl.write.Number(20, index, 0));//减免税额
                sheet.addCell(new Label(21, index, ""));//备注
                index++;
            }
            workbook.write();
            workbook.close();
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        ConnUtil.closeConnection(conn);
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
            case 3://员工模板
                fileName = "EmployeeContract"+".xls";
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
                fileName = "detail2.xls";
                break;
            case 1://商业保险结算单明细模板
                fileName = "detail3.xls";
                break;
            case 2://员工模板
                fileName = "employee.xls";
                break;
            case 3://员工合同模板
                fileName = "EmployeeContract.xls";
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


    //上传员工头像
    private String upload(HttpServletRequest request) throws IOException, ServletException {
        String id = request.getParameter("id");
        byte category = Byte.parseByte(request.getParameter("category"));
        Part part = request.getPart("file");
        JSONObject json = new JSONObject();
        if(part!=null){
            String header = part.getHeader("content-disposition");//获取文件的名称
            String headername = header.substring(header.indexOf("filename")+10, header.length()-1);//截取字符串获取文件名称
            String suffixName=headername.substring(headername.indexOf(".")+1);//获取文件名后缀

            String file = id+"."+suffixName;
            String folder = "";
            switch (category){
                case 0:
                    folder = request.getServletContext().getRealPath("/accessory/headImg");
                    break;
                case 1:
                    folder = request.getServletContext().getRealPath("/accessory/contract1");
                    break;
                case 2:
                    folder = request.getServletContext().getRealPath("/accessory/contract2");
                    break;
                case 3:
                    folder = request.getServletContext().getRealPath("/accessory/contract3");
                    break;
                case 4:
                    folder = request.getServletContext().getRealPath("/accessory/leave");
                    break;
            }
            FileOutputStream fos = new FileOutputStream(new File(folder, file));
            InputStream put = part.getInputStream();//获取文件流
            IOUtils.copy(put, fos);
            put.close();
            fos.close();
            //删除临时文件
            part.delete();
            json.put("success",true);
        }else{
            json.put("success",false);
            json.put("msg","上传文件错误");
        }
        return json.toJSONString();
    }

    //判断合同文件是否存在
    private String exist(HttpServletRequest request) throws IOException {
        byte category = Byte.parseByte(request.getParameter("category"));
        String id = request.getParameter("id");

        File file = getFile(category,id,request.getServletContext().getRealPath("/"));
        JSONObject json = new JSONObject();
        json.put("success",true);
        json.put("exist",file.exists()?true:false);
        return json.toJSONString();
    }

    private File getFile(byte category, String id, String root){
        String folder = "";
        String filename = "";
        switch (category){
            case 0:
                folder = "accessory/headImg";
                filename = id+".jpg";
                break;
            case 1:
                folder = "accessory/contract1";
                filename = id+".pdf";
                break;
            case 2:
                folder = "accessory/contract2";
                filename = id+".pdf";
                break;
            case 3:
                folder = "accessory/contract3";
                filename = id+".pdf";
                break;
            case 4:
                folder = "accessory/leave";
                filename = id+".jpg";
                break;
            case 5://小时工模板
                folder = "excelFile";
                filename = "detail2.xls";
                break;
            case 6://商业保险结算单明细模板
                folder = "excelFile";
                filename = "detail3.xls";
                break;
            case 7://员工模板
                folder = "excelFile";
                filename = "employee.xls";
                break;
            case 8://员工合同模板
                folder = "/excelFile";
                filename = "EmployeeContract.xls";
        }
        String fileName = String.format("%s/%s/%s",root,folder,filename);
        return new File(fileName);
    }

    //下载合同文件
    private void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte category = Byte.parseByte(request.getParameter("category"));
        String id = request.getParameter("id");

        File file = getFile(category,id,request.getServletContext().getRealPath("/"));

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename="+file.getName());

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

    //下载普通结算单明细模板
    private void downloadDetail1(Connection conn, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=detailModel.xls");

        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
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
        parameter.addCondition("did","=",user.getRid());
        parameter.addCondition("type","=",1);
        parameter.addCondition("category","!=",3);
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
            sheet1.addCell(new Label(0, 0, "员工姓名*",wcf));
            sheet1.addCell(new Label(1, 0, "身份证号码*",wcf));
            sheet1.addCell(new Label(2, 0, "基本工资*",wcf));
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
            sheet2.addCell(new Label(2, 4, "TRUE"));
            sheet2.addCell(new Label(2, 5, "TRUE"));
            sheet2.addCell(new Label(2, 6, "TRUE"));
            sheet2.addCell(new Label(2, 7, "TRUE"));
            sheet2.addCell(new Label(2, 8, "TRUE"));
            sheet2.addCell(new Label(2, 9, "TRUE"));
            sheet2.addCell(new Label(2, 10, "TRUE"));
            sheet2.addCell(new Label(2, 11, "TRUE"));
            sheet2.addCell(new Label(2, 12, "TRUE"));
            sheet2.addCell(new Label(2, 13, "TRUE"));
            sheet2.addCell(new Label(2, 14, "TRUE"));
            sheet2.addCell(new Label(2, 15, "TRUE"));
            sheet2.addCell(new Label(2, 16, "TRUE"));
            if(flag){
                int  c = 0;
                for(int i = 0;i<itemList.size();i++){
                    c = i+17;
                    sheet2.addCell(new Label(2, c, "TRUE"));
                }
                sheet2.addCell(new Label(2,c+1,  "TRUE"));
                sheet2.addCell(new Label(2,c+2,  "TRUE"));
            }else {
                sheet2.addCell(new Label(2,17,  "TRUE"));
                sheet2.addCell(new Label(2,18,  "TRUE"));
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
                sheet1.addCell(new Label(2, index, detail3.getPname()));
                sheet1.addCell(new Label(3, index, detail3.getPlace()));
                sheet1.addCell(new Label(4, index, detail3.getPost()));
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

    //读取个税专项扣除
    private String readDeduct(HttpServletRequest request)throws IOException, ServletException {
        String result = null;
        Part part = request.getPart("file");
        try {//获取part中的文件，读取数据
            InputStream is = part.getInputStream();
            List<JSONObject> data = XlsUtil.readDeduct(is,0);
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
}
