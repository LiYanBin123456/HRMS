package servlet;

import bean.admin.Account;
import bean.client.Items;
import bean.client.MapSalary;

import bean.contract.Serve;
import bean.contract.ViewContractCooperation;
import bean.employee.Employee;
import bean.employee.ViewDeduct;
import bean.employee.ViewEmployee;
import bean.settlement.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.client.FinanceDao;
import dao.client.MapSalaryDao;
import dao.contract.ContractDao;
import dao.contract.ServeDao;
import dao.employee.EmployeeDao;


import dao.settlement.*;
import database.*;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Label;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.io.IOUtils;
import service.employee.DeductService;
import service.fileService.FileService;
import utills.Calculate;
import utills.XlsUtil;
import utills.IDCardUtil;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static utills.Calculate.calculateManageAndTax2;
import static utills.IDCardUtil.getLastday_Month;

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
            case "download"://下载文件
                download(request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "readDeduct"://读取个税表中的数据
                result = readDeduct(request);
                break;
            case "readDeducts"://读取个税表中的数据
                result = readDeducts(conn,request);
                break;
            case "exist"://判断文件是否存在
                result = exist(request);
                break;
            case "downloadDetail1"://下载结算单明细模板
                downloadDetail1(conn,request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "exportDetail1"://导出结算单明细
                exportDetail1(conn,request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "exportDetail2"://导出小时工结算单明细
                exportDetail2(conn,request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "exportDetail3"://下载商业保险结算单明细
                exportDetail3(conn,request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "exportDetail4"://导出代缴社保结算单明细
                exportDetail4(conn,request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "exportTax"://导出个税申报表
                exportTax(conn,request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "exportTaxEmployee"://导出个税申报名单
                exportTaxEmployee(conn,request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "exportInsurance"://导出参保单
                exportInsurance(conn,request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "exportBank"://导出银行卡
                exportBank(conn, request,response);
                ConnUtil.closeConnection(conn);
                return;
            default:
                result = "{\"success\":false,\"msg\":\"参数错误\"}";
        }
        ConnUtil.closeConnection(conn);
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
                    System.out.println(data);
                    JSONObject json = new JSONObject();
                    json.put("success",true);
                    json.put("data",data);
                    result = json.toJSONString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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

    //下载文件
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

    //下载
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

        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        long cid = Long.parseLong(request.getParameter("cid"));//合作单位id
        byte type = Byte.parseByte(request.getParameter("type"));//0 派遣 1 外包  2代缴工资 3代缴社保
        String month = request.getParameter("month");//获取结算单月份
        month = getLastday_Month(month);//将月份变成该月最后一天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");

        //获取合作客户的自定义工资项
        MapSalary mapSalary = (MapSalary)MapSalaryDao.selectByMonth(cid,conn, Date.valueOf(month)).data;

        //根据条件找到派遣到该单位的员工列表，条件有cid，did，类型为外派或者派遣员工，用工性质不是小时工，在职
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("cid","=",cid);
        parameter.addCondition("did","=",user.getRid());
        parameter.addCondition("type","=",1);
        switch (type){
            case 0://派遣员工
                parameter.addCondition("category","=",1);
                break;
            case 1://外包员工
                parameter.addCondition("category","=",2);
                break;
            case 2://代缴工资
                parameter.addCondition("category","=",4);
                break;
            case 3://代发工资
                parameter.addCondition("category","=",5);
                break;
        }
        parameter.addCondition("status","=",0);
        List<ViewEmployee> employeeList = JSONArray.parseArray(JSONObject.toJSONString(EmployeeDao.getList(conn,parameter).rows),ViewEmployee.class);

        //获取结算单视图
        ViewSettlement1 vs = (ViewSettlement1) Settlement1Dao.get(conn,sid).data;
        //结算单类型
        byte types = vs.getType();
        String typeMsg="";
        switch (types){
            case 0:
                typeMsg = "派遣";
                break;
            case 1:
                typeMsg = "外包";
                break;
            case 2:
                typeMsg = "代发工资";
                break;
            case 3:
                typeMsg = "代缴社保";
                break;
        }

        //文件名
        String fileName=vs.getName()+(vs.getMonth()==null?"":sdf.format(vs.getMonth()))+typeMsg+"结算单明细模板";
        fileName = new String(fileName.getBytes(),"iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\""
                + fileName + ".xls\"");

        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("信息表", 0);
        WritableSheet sheet2 = book.createSheet("元数据", 1);

        try {
            sheet1.addCell(new Label(0, 0, "员工姓名*"));
            sheet1.addCell(new Label(1, 0, "身份证号码*"));
            sheet1.addCell(new Label(2, 0, "基本工资"));
            if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0){
                int  c = 0;
                List<Items> itemList = mapSalary.getItemList();
                for(int i = 0;i<itemList.size();i++){
                    c = i+3;
                    sheet1.addCell(new Label(c, 0, itemList.get(i).getField()));
                }
                sheet1.addCell(new Label(c+1, 0, "个人核收补减"));
                sheet1.addCell(new Label(c+2, 0, "备注1"));
                sheet1.addCell(new Label(c+3, 0, "单位核收补减"));
                sheet1.addCell(new Label(c+4, 0, "备注2"));
                sheet1.addCell(new Label(c+5, 0, "个人养老"));
                sheet1.addCell(new Label(c+6, 0, "个人医疗"));
                sheet1.addCell(new Label(c+7, 0, "个人失业"));
                sheet1.addCell(new Label(c+8, 0, "个人大病"));
                sheet1.addCell(new Label(c+9, 0, "个人公积金"));
                sheet1.addCell(new Label(c+10, 0, "单位养老"));
                sheet1.addCell(new Label(c+11, 0, "单位医疗"));
                sheet1.addCell(new Label(c+12, 0, "单位失业"));
                sheet1.addCell(new Label(c+13, 0, "单位工伤"));
                sheet1.addCell(new Label(c+14, 0, "单位大病"));
                sheet1.addCell(new Label(c+15, 0, "单位生育"));
                sheet1.addCell(new Label(c+16, 0, "单位公积金"));
                sheet1.addCell(new Label(c+17, 0, "个税"));
                sheet1.addCell(new Label(c+18, 0, "应付"));
                sheet1.addCell(new Label(c+19, 0, "实付"));
            }else {
                sheet1.addCell(new Label(3, 0, "个人核收补减"));
                sheet1.addCell(new Label(4, 0, "备注1"));
                sheet1.addCell(new Label(5, 0, "单位核收补减"));
                sheet1.addCell(new Label(6, 0, "备注2"));
                sheet1.addCell(new Label(7, 0, "个人养老"));
                sheet1.addCell(new Label(8, 0, "个人医疗"));
                sheet1.addCell(new Label(9, 0, "个人失业"));
                sheet1.addCell(new Label(10, 0, "个人大病"));
                sheet1.addCell(new Label(11, 0, "个人公积金"));
                sheet1.addCell(new Label(12, 0, "单位养老"));
                sheet1.addCell(new Label(13, 0, "单位医疗"));
                sheet1.addCell(new Label(14, 0, "单位失业"));
                sheet1.addCell(new Label(15, 0, "单位工伤"));
                sheet1.addCell(new Label(16, 0, "单位大病"));
                sheet1.addCell(new Label(17, 0, "单位生育"));
                sheet1.addCell(new Label(18, 0, "单位公积金"));
                sheet1.addCell(new Label(19, 0, "个税"));
                sheet1.addCell(new Label(20, 0, "应付"));
                sheet1.addCell(new Label(21, 0, "实付"));
            }
            int index = 1;
            for( ViewEmployee viewEmployee:employeeList){//根据员工生成明细，如果没有员工则不生成
                sheet1.addCell(new Label(0, index, viewEmployee.getName()));
                sheet1.addCell(new Label(1, index, viewEmployee.getCardId()));
                index++;
            }

            sheet2.addCell(new Label(0, 0, "字段名"));
            sheet2.addCell(new Label(0, 1, "name"));
            sheet2.addCell(new Label(0, 2, "cardId"));
            sheet2.addCell(new Label(0, 3, "base"));
            if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0){
                int  c = 0;
                List<Items> itemList = mapSalary.getItemList();
                for(int i = 0;i<itemList.size();i++){
                    c = i+4;
                    String name = "f"+(i+1);
                    sheet2.addCell(new Label(0, c, name));
                }
                sheet2.addCell(new Label(0, c+1, "extra1"));
                sheet2.addCell(new Label(0, c+2, "comments1"));
                sheet2.addCell(new Label(0, c+3, "extra2"));
                sheet2.addCell(new Label(0, c+4, "comments2"));
                sheet2.addCell(new Label(0, c+5, "pension1"));
                sheet2.addCell(new Label(0, c+6, "medicare1"));
                sheet2.addCell(new Label(0, c+7, "unemployment1"));
                sheet2.addCell(new Label(0, c+8, "disease1"));
                sheet2.addCell(new Label(0, c+9, "fund1"));
                sheet2.addCell(new Label(0, c+10, "pension2"));
                sheet2.addCell(new Label(0, c+11, "medicare2"));
                sheet2.addCell(new Label(0, c+12, "unemployment2"));
                sheet2.addCell(new Label(0, c+13, "injury"));
                sheet2.addCell(new Label(0, c+14, "disease2"));
                sheet2.addCell(new Label(0, c+15, "birth"));
                sheet2.addCell(new Label(0, c+16, "fund2"));
                sheet2.addCell(new Label(0, c+17, "tax"));
                sheet2.addCell(new Label(0, c+18,  "payable"));
                sheet2.addCell(new Label(0, c+19,  "paid"));
            }else {
                sheet2.addCell(new Label(0, 4, "extra1"));
                sheet2.addCell(new Label(0, 5, "comments1"));
                sheet2.addCell(new Label(0, 6, "extra2"));
                sheet2.addCell(new Label(0, 7, "comments2"));
                sheet2.addCell(new Label(0, 8, "pension1"));
                sheet2.addCell(new Label(0, 9, "medicare1"));
                sheet2.addCell(new Label(0, 10, "unemployment1"));
                sheet2.addCell(new Label(0, 11, "disease1"));
                sheet2.addCell(new Label(0, 12, "fund1"));
                sheet2.addCell(new Label(0, 13, "pension2"));
                sheet2.addCell(new Label(0, 14, "medicare2"));
                sheet2.addCell(new Label(0, 15, "unemployment2"));
                sheet2.addCell(new Label(0, 16, "injury"));
                sheet2.addCell(new Label(0, 17, "disease2"));
                sheet2.addCell(new Label(0, 18, "birth"));
                sheet2.addCell(new Label(0, 19, "fund2"));
                sheet2.addCell(new Label(0, 20, "tax"));
                sheet2.addCell(new Label(0, 21,  "payable"));
                sheet2.addCell(new Label(0, 22,  "paid"));
            }
            sheet2.addCell(new Label(1, 0, "类型"));
            sheet2.addCell(new Label(1, 1, "string"));
            sheet2.addCell(new Label(1, 2, "string"));
            sheet2.addCell(new Label(1, 3, "float"));
            if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0){
                int  c = 0;
                List<Items> itemList = mapSalary.getItemList();
                for(int i = 0;i<itemList.size();i++){
                    c = i+4;
                    sheet2.addCell(new Label(1, c, "float"));
                }
                sheet2.addCell(new Label(1, c+1, "float"));
                sheet2.addCell(new Label(1, c+2, "float"));
                sheet2.addCell(new Label(1, c+3, "float"));
                sheet2.addCell(new Label(1, c+4, "float"));
                sheet2.addCell(new Label(1, c+5, "float"));
                sheet2.addCell(new Label(1, c+6, "float"));
                sheet2.addCell(new Label(1, c+7, "float"));
                sheet2.addCell(new Label(1, c+8, "float"));
                sheet2.addCell(new Label(1, c+9, "float"));
                sheet2.addCell(new Label(1, c+10, "float"));
                sheet2.addCell(new Label(1, c+11, "float"));
                sheet2.addCell(new Label(1, c+12, "float"));
                sheet2.addCell(new Label(1, c+13, "float"));
                sheet2.addCell(new Label(1,c+14,  "float"));
                sheet2.addCell(new Label(1,c+15,  "float"));
                sheet2.addCell(new Label(1,c+16,  "float"));
                sheet2.addCell(new Label(1,c+17,  "float"));
                sheet2.addCell(new Label(1,c+18,  "float"));
                sheet2.addCell(new Label(1,c+19,  "float"));
            }else {
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
                sheet2.addCell(new Label(1, 17,  "float"));
                sheet2.addCell(new Label(1, 18,  "float"));
                sheet2.addCell(new Label(1, 19, "float"));
                sheet2.addCell(new Label(1, 20, "float"));
                sheet2.addCell(new Label(1, 21, "float"));
                sheet2.addCell(new Label(1, 22, "float"));
            }

            sheet2.addCell(new Label(2, 0, "是否允许为空"));
            sheet2.addCell(new Label(2, 1, "False"));
            sheet2.addCell(new Label(2, 2, "False"));
            sheet2.addCell(new Label(2, 3, "TRUE"));
            if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0){
                int  c = 0;
                List<Items> itemList = mapSalary.getItemList();
                for(int i = 0;i<itemList.size();i++){
                    c = i+4;
                    sheet2.addCell(new Label(2, c, "TRUE"));
                }
                sheet2.addCell(new Label(2,c+1,  "TRUE"));
                sheet2.addCell(new Label(2,c+2,  "TRUE"));
                sheet2.addCell(new Label(2, c+3, "TRUE"));
                sheet2.addCell(new Label(2, c+4, "TRUE"));
                sheet2.addCell(new Label(2, c+5, "TRUE"));
                sheet2.addCell(new Label(2, c+6, "TRUE"));
                sheet2.addCell(new Label(2, c+7, "TRUE"));
                sheet2.addCell(new Label(2, c+8, "TRUE"));
                sheet2.addCell(new Label(2, c+9, "TRUE"));
                sheet2.addCell(new Label(2, c+10, "TRUE"));
                sheet2.addCell(new Label(2, c+11, "TRUE"));
                sheet2.addCell(new Label(2, c+12, "TRUE"));
                sheet2.addCell(new Label(2, c+13, "TRUE"));
                sheet2.addCell(new Label(2, c+14, "TRUE"));
                sheet2.addCell(new Label(2, c+15, "TRUE"));
                sheet2.addCell(new Label(2, c+16, "TRUE"));
                sheet2.addCell(new Label(2, c+17, "TRUE"));
                sheet2.addCell(new Label(2, c+18, "TRUE"));
                sheet2.addCell(new Label(2, c+19, "TRUE"));
            }else {
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
                sheet2.addCell(new Label(2, 17, "TRUE"));
                sheet2.addCell(new Label(2, 18,  "TRUE"));
                sheet2.addCell(new Label(2, 19,  "TRUE"));
                sheet2.addCell(new Label(2, 20, "TRUE"));
                sheet2.addCell(new Label(2, 21, "TRUE"));
                sheet2.addCell(new Label(2, 22,  "TRUE"));
            }
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    //导出普通结算单明细
    private void exportDetail1(Connection conn, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName;
        String month = request.getParameter("month");//获取结算单月份
        month = getLastday_Month(month);//将月份变成该月最后一天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");

        long cid = Long.parseLong(request.getParameter("cid"));//获取合作客户的id
        //获取合作客户的自定义工资项
        MapSalary mapSalary = (MapSalary)MapSalaryDao.selectByMonth(cid,conn, Date.valueOf(month)).data;

        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        //获取结算单视图
        ViewSettlement1 vs = (ViewSettlement1) Settlement1Dao.get(conn,sid).data;
        //获取合作客户视图
        ViewContractCooperation vc = (ViewContractCooperation) ContractDao.getViewContractCoop(conn,vs.getCcid()).data;
        //获取结算单明细
        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List< ViewDetail1> detail1s = JSONArray.parseArray(rows, ViewDetail1.class);

        //结算单类型
        byte types = vs.getType();
        String typeMsg="";
        switch (types){
            case 0:
                typeMsg = "派遣";
                break;
            case 1:
                typeMsg = "外包";
                break;
            case 2:
                typeMsg = "代发工资";
                break;
            case 3:
                typeMsg = "代缴社保";
                break;
        }
        //文件名
        fileName=vs.getName()+(vs.getMonth()==null?"":sdf.format(vs.getMonth()))+typeMsg+"结算单";
        fileName = new String(fileName.getBytes(),"iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\""
                + fileName + ".xls\"");

        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("公司汇款明细", 0);
        WritableSheet sheet2 = book.createSheet("个人工资表", 1);

        try {
            sheet1.addCell(new Label(0, 0, vs.getName()+""+(vs.getMonth()==null?"":sdf.format(vs.getMonth()))+typeMsg+"工资汇款表"));

            sheet1.addCell(new Label(0, 1, "员工姓名"));
            sheet1.addCell(new Label(1, 1, "身份证号码"));
            sheet1.addCell(new Label(2, 1, "基本工资"));

            sheet2.addCell(new Label(0, 0, vs.getName()+""+(vs.getMonth()==null?"":sdf.format(vs.getMonth()))+typeMsg+"工资明细表"));

            sheet2.addCell(new Label(0, 1, "员工姓名"));
            sheet2.addCell(new Label(1, 1, "身份证号码"));
            sheet2.addCell(new Label(2, 1, "基本工资"));
            if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0){
                List<Items> itemList = mapSalary.getItemList();//获取自定义工资项集合
                int  c = 0;
                for(int i = 0;i<itemList.size();i++){
                    c = i+3;
                    sheet1.addCell(new Label(c, 1, itemList.get(i).getField()));
                    sheet2.addCell(new Label(c, 1, itemList.get(i).getField()));
                }
                sheet1.addCell(new Label(c+1, 1, "单位养老"));
                sheet1.addCell(new Label(c+2, 1, "单位医疗"));
                sheet1.addCell(new Label(c+3, 1, "单位大病"));
                sheet1.addCell(new Label(c+4, 1, "单位生育"));
                sheet1.addCell(new Label(c+5, 1, "单位失业"));
                sheet1.addCell(new Label(c+6, 1, "单位工伤"));
                sheet1.addCell(new Label(c+7, 1, "单位公积金"));
                sheet1.addCell(new Label(c+8, 1, "单位社保合计"));
                sheet1.addCell(new Label(c+9, 1, "国家减免"));
                sheet1.addCell(new Label(c+10, 1, "管理费"));
                sheet1.addCell(new Label(c+11,1, "核收补减"));
                sheet1.addCell(new Label(c+12,1, "税费"));
                sheet1.addCell(new Label(c+13,1, "汇款总额"));
                sheet1.addCell(new Label(c+14,1, "备注"));

                sheet2.addCell(new Label(c+1, 1, "个人养老"));
                sheet2.addCell(new Label(c+2, 1, "个人医疗"));
                sheet2.addCell(new Label(c+3, 1, "个人失业"));
                sheet2.addCell(new Label(c+4, 1, "个人大病"));
                sheet2.addCell(new Label(c+5, 1, "个人公积金"));
                sheet2.addCell(new Label(c+6, 1, "应发工资"));
                sheet2.addCell(new Label(c+7, 1, "个税"));
                sheet2.addCell(new Label(c+8, 1, "实发工资"));
            }else {
                sheet1.addCell(new Label(3, 1, "单位养老"));
                sheet1.addCell(new Label(4, 1, "单位医疗"));
                sheet1.addCell(new Label(5, 1, "单位大病"));
                sheet1.addCell(new Label(6, 1, "单位生育"));
                sheet1.addCell(new Label(7, 1, "单位失业"));
                sheet1.addCell(new Label(8, 1, "单位工伤"));
                sheet1.addCell(new Label(9, 1, "单位公积金"));
                sheet1.addCell(new Label(10, 1, "单位社保合计"));
                sheet1.addCell(new Label(11, 1, "国家减免"));
                sheet1.addCell(new Label(12, 1, "管理费"));
                sheet1.addCell(new Label(13, 1, "核收补减"));
                sheet1.addCell(new Label(14, 1, "税费"));
                sheet1.addCell(new Label(15, 1, "汇款总额"));
                sheet1.addCell(new Label(16, 1, "备注"));

                sheet2.addCell(new Label(3, 1, "个人养老"));
                sheet2.addCell(new Label(4, 1, "个人医疗"));
                sheet2.addCell(new Label(5, 1, "个人失业"));
                sheet2.addCell(new Label(6, 1, "个人大病"));
                sheet2.addCell(new Label(7, 1, "个人公积金"));
                sheet2.addCell(new Label(8, 1, "应发工资"));
                sheet2.addCell(new Label(9, 1, "个税"));
                sheet2.addCell(new Label(10, 1, "实发工资"));
            }
            int index = 2;
            int type = vc.getStype();//合同服务项目中的类型
            int category = vc.getCategory();//合同服务项目中的结算方式
            int invoice = vc.getInvoice();//合同基础信息中的发票类型
            float per = vc.getPer()/100;//税费比例（选择增值税专用发票（全额）需要用到）
            float val = vc.getValue();//结算值，根据结算方式的不同，代表的意义不同

            for( ViewDetail1 v:detail1s){
                float manage = 0;
                float tax2 = 0;

                sheet1.addCell(new Label(0, index, v.getName()));
                sheet1.addCell(new Label(1, index, v.getCardId()));
                sheet1.addCell(new jxl.write.Number(2, index, v.getBase()));

                sheet2.addCell(new Label(0, index, v.getName()));
                sheet2.addCell(new Label(1, index, v.getCardId()));
                sheet2.addCell(new jxl.write.Number(2, index, v.getBase()));
                float salary = 0;//自定义工资总和
                if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0) {//判断客户是否存在自定义字段
                    List<Items> itemList = mapSalary.getItemList();
                    int c2 = 0;
                    for (int i = 0; i < itemList.size(); i++) {
                        c2 = i + 3;
                        int in = i + 1;
                        String name = "getF" + in;
                        String value = null;
                        Method method;
                        try {//通过反射获取对应的值
                            method = v.getClass().getSuperclass().getMethod(name);
                            value = method.invoke(v).toString();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        if(itemList.get(i).getType()==0){//加项
                            salary+=Float.parseFloat(value);
                        }else {//减项
                            salary-=Float.parseFloat(value);
                        }

                        sheet1.addCell(new Label(c2, index, value));
                        sheet2.addCell(new Label(c2, index, value));
                    }
                    sheet1.addCell(new jxl.write.Number(c2+1, index, v.getPension2()));
                    sheet1.addCell(new jxl.write.Number(c2+2, index, v.getMedicare2()));
                    sheet1.addCell(new jxl.write.Number(c2+3, index, v.getDisease2()));
                    sheet1.addCell(new jxl.write.Number(c2+4, index, v.getBirth()));
                    sheet1.addCell(new jxl.write.Number(c2+5, index, v.getUnemployment2()));
                    sheet1.addCell(new jxl.write.Number(c2+6, index, v.getInjury()));
                    sheet1.addCell(new jxl.write.Number(c2+7, index, v.getFund2()));
                    //社保合计
                    float sum = v.getPension2()+v.getMedicare2()+v.getDisease2()+v.getBirth()+v.getUnemployment2()
                            +v.getInjury()+v.getFund2();
                    sheet1.addCell(new jxl.write.Number(c2+8, index, sum));
                    sheet1.addCell(new jxl.write.Number(c2+9, index, v.getFree()));
                    //计算管理费和税费
                    HashMap<String,Float> map2 = Calculate.calculateManageAndTax2(type,category,invoice,per,val,manage,tax2,v,salary);
                    manage =  map2.get("manage");
                    tax2 = map2.get("tax2");
                    //管理费
                    sheet1.addCell(new jxl.write.Number(c2+10, index, manage));
                    //核收补减
                    sheet1.addCell(new jxl.write.Number(c2+11, index, v.getExtra2()));
                    //税费
                    sheet1.addCell(new jxl.write.Number(c2+12, index,tax2));
                    //汇款总额 = 基本工资+自定义工资项+单位社保总额+管理费+税费+（单位）核收补减
                    float summary = v.getBase()+salary+sum-v.getFree()+manage+tax2+v.getExtra2();
                    sheet1.addCell(new jxl.write.Number(c2+13, index, summary));
                    sheet1.addCell(new Label(c2+14, index, v.getComments2()));


                    sheet2.addCell(new jxl.write.Number(c2+1, index, v.getPension1()));
                    sheet2.addCell(new jxl.write.Number(c2+2, index, v.getMedicare1()));
                    sheet2.addCell(new jxl.write.Number(c2+3, index, v.getUnemployment1()));
                    sheet2.addCell(new jxl.write.Number(c2+4, index, v.getDisease1()));
                    sheet2.addCell(new jxl.write.Number(c2+5, index, v.getFund1()));
                    sheet2.addCell(new jxl.write.Number(c2+6, index, v.getPayable()));
                    sheet2.addCell(new jxl.write.Number(c2+7, index, v.getTax()));
                    sheet2.addCell(new jxl.write.Number(c2+8, index, v.getPaid()));
                }else {
                    sheet1.addCell(new jxl.write.Number(3, index, v.getPension2()));
                    sheet1.addCell(new jxl.write.Number(4, index, v.getMedicare2()));
                    sheet1.addCell(new jxl.write.Number(5, index, v.getDisease2()));
                    sheet1.addCell(new jxl.write.Number(6, index, v.getBirth()));
                    sheet1.addCell(new jxl.write.Number(7, index, v.getUnemployment2()));
                    sheet1.addCell(new jxl.write.Number(8, index, v.getInjury()));
                    sheet1.addCell(new jxl.write.Number(9, index, v.getFund2()));
                    //社保合计
                    float sum = v.getPension2()+v.getMedicare2()+v.getDisease2()+v.getBirth()+v.getUnemployment2()
                            +v.getInjury()+v.getFund2();
                    sheet1.addCell(new jxl.write.Number(10, index, sum));
                    sheet1.addCell(new jxl.write.Number(11, index, v.getFree()));

                    //计算管理费和税费
                    HashMap<String,Float> map2 =calculateManageAndTax2(type,category,invoice,per,val,manage,tax2,v,salary);
                    manage = map2.get("manage");
                    tax2 = map2.get("tax2");
                    //管理费
                    sheet1.addCell(new jxl.write.Number(12, index, manage ));
                    //核收补减
                    sheet1.addCell(new jxl.write.Number(13, index, v.getExtra2()));
                    //税费
                    sheet1.addCell(new jxl.write.Number(14, index,tax2));
                    //汇款总额 = 基本工资+单位社保总额+管理费+税费+（单位）核收补减
                    float summary = v.getBase()+sum-v.getFree()+manage+tax2+v.getExtra2();
                    sheet1.addCell(new jxl.write.Number(15, index, summary));
                    //备注
                    sheet1.addCell(new Label(16, index,v.getComments2()));


                    sheet2.addCell(new jxl.write.Number(3, index, v.getPension1()));
                    sheet2.addCell(new jxl.write.Number(4, index, v.getMedicare1()));
                    sheet2.addCell(new jxl.write.Number(5, index, v.getUnemployment1()));
                    sheet2.addCell(new jxl.write.Number(6, index, v.getDisease1()));
                    sheet2.addCell(new jxl.write.Number(7, index, v.getFund1()));
                    sheet2.addCell(new jxl.write.Number(8, index, v.getPayable()));
                    sheet2.addCell(new jxl.write.Number( 9, index, v.getTax()));
                    sheet2.addCell(new jxl.write.Number( 10, index, v.getPaid()));
                }
                index++;
            }
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    //导出小时工结算的明细
    private void exportDetail2(Connection conn, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String fileName ;
        long sid = Long.parseLong(request.getParameter("id"));//结算单id
        //获取小时工结算单视图
        ViewSettlement2 vs = (ViewSettlement2) Settlement2Dao.get(conn,sid).data;

        //获取小时工明细
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        DaoQueryListResult result = Detail2Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewDetail2> detail2s = JSONArray.parseArray(rows, ViewDetail2.class);

        String cid = vs.getCcid();//获取合作客户的id
        Serve serve = (Serve) ServeDao.get(conn,cid).data;
        byte payer = serve.getPayer();//0 派遣单位发放工资  1 合作客户发放工资
        //公司的单价
        float price = vs.getPrice();
        if(payer==1){//合作客户发放工资  单价=公司单价-员工单价
              price =price - detail2s.get(0).getPrice();
        }

        //文件名
        fileName=vs.getName()+"小时工结算单";
        fileName = new String(fileName.getBytes(),"iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\""
                + fileName + ".xls\"");
        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("小时工汇款表", 0);
        WritableSheet sheet2 = book.createSheet("小时工结算单明细", 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        try {
            //表头
            sheet1.addCell(new Label(0, 0, vs.getName()+""+(vs.getMonth()==null?"":sdf.format(vs.getMonth()))+"小时工汇款表"));

            sheet1.addCell(new Label(0, 1, "员工姓名"));
            sheet1.addCell(new Label(1, 1, "身份证号码"));
            sheet1.addCell(new Label(2, 1, "工时"));
            sheet1.addCell(new Label(3, 1, "公司单价"));
            sheet1.addCell(new Label(4, 1, "餐费"));
            sheet1.addCell(new Label(5, 1, "交通费"));
            sheet1.addCell(new Label(6, 1, "住宿费"));
            sheet1.addCell(new Label(7, 1, "水电费"));
            sheet1.addCell(new Label(8, 1, "保险费"));
            sheet1.addCell(new Label(9, 1, "其他1"));
            sheet1.addCell(new Label(10, 1, "其他2"));
            sheet1.addCell(new Label(11, 1, "汇款总额"));

            //表头
            sheet2.addCell(new Label(0, 0, vs.getName()+""+(vs.getMonth()==null?"":sdf.format(vs.getMonth()))+"小时工明细表"));

            sheet2.addCell(new Label(0, 1, "员工姓名"));
            sheet2.addCell(new Label(1, 1, "身份证号码"));
            sheet2.addCell(new Label(2, 1, "工时"));
            sheet2.addCell(new Label(3, 1, "单价"));
            sheet2.addCell(new Label(4, 1, "餐费"));
            sheet2.addCell(new Label(5, 1, "交通费"));
            sheet2.addCell(new Label(6, 1, "住宿费"));
            sheet2.addCell(new Label(7, 1, "水电费"));
            sheet2.addCell(new Label(8, 1, "保险费"));
            sheet2.addCell(new Label(9, 1, "个税"));
            sheet2.addCell(new Label(10, 1, "其他1"));
            sheet2.addCell(new Label(11, 1, "其他2"));
            sheet2.addCell(new Label(12, 1, "应付"));
            sheet2.addCell(new Label(13, 1, "实付"));

            int index = 2;
            for(ViewDetail2 d:detail2s){
                sheet1.addCell(new Label(0, index, d.getName()));
                sheet1.addCell(new Label(1, index, d.getCardId()));
                sheet1.addCell(new jxl.write.Number(2, index, d.getHours()));
                sheet1.addCell(new jxl.write.Number(3, index, price));
                sheet1.addCell(new jxl.write.Number(4, index, d.getFood()));
                sheet1.addCell(new jxl.write.Number(5, index, d.getTraffic()));
                sheet1.addCell(new jxl.write.Number(6, index, d.getAccommodation()));
                sheet1.addCell(new jxl.write.Number(7, index, d.getUtilities()));
                sheet1.addCell(new jxl.write.Number(8, index, d.getInsurance()));
                sheet1.addCell(new jxl.write.Number(9, index, d.getOther1()));
                sheet1.addCell(new jxl.write.Number(10, index, d.getOther2()));
                //汇款总额 = 工时*公司单价-水电-餐费-住宿费-保险+其他1+其他2
                float sum = d.getHours()*price-d.getFood()- d.getTraffic()-d.getAccommodation()-d.getUtilities()- d.getInsurance()
                        +d.getOther1()+d.getOther2();
                sheet1.addCell(new jxl.write.Number(11, index, sum));


                sheet2.addCell(new Label(0, index, d.getName()));
                sheet2.addCell(new Label(1, index, d.getCardId()));
                sheet2.addCell(new jxl.write.Number(2, index, d.getHours()));
                sheet2.addCell(new jxl.write.Number(3, index, d.getPrice()));
                sheet2.addCell(new jxl.write.Number(4, index, d.getFood()));
                sheet2.addCell(new jxl.write.Number(5, index, d.getTraffic()));
                sheet2.addCell(new jxl.write.Number(6, index, d.getAccommodation()));
                sheet2.addCell(new jxl.write.Number(7, index, d.getUtilities()));
                sheet2.addCell(new jxl.write.Number(8, index, d.getInsurance()));
                sheet2.addCell(new jxl.write.Number(9, index, d.getTax()));
                sheet2.addCell(new jxl.write.Number(10, index, d.getOther1()));
                sheet2.addCell(new jxl.write.Number(11, index, d.getOther2()));
                sheet2.addCell(new jxl.write.Number(12, index, d.getPayable()));
                sheet2.addCell(new jxl.write.Number(13, index, d.getPaid()));
                index++;
            }
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        }
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
            sheet1.addCell(new Label(2, 0, "工作地点"));
            sheet1.addCell(new Label(3, 0, "工作岗位"));
            sheet1.addCell(new Label(4, 0, "人员类别"));


            int index = 1;
            for(ViewDetail3 detail3:detail3s){
                sheet1.addCell(new Label(0, index, detail3.getName()));
                sheet1.addCell(new Label(1, index, detail3.getCardId()));
                sheet1.addCell(new Label(2, index, detail3.getPlace()));
                sheet1.addCell(new Label(3, index, detail3.getPost()));
                sheet1.addCell(new jxl.write.Number(4, index, detail3.getCategory()));
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
    }

    //导出代缴社保结算单明细
    private void exportDetail4(Connection conn, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");

        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);

        //获取结算单视图
        ViewSettlement1 vs = (ViewSettlement1) Settlement1Dao.get(conn,sid).data;
        //获取合作客户视图
        ViewContractCooperation vc = (ViewContractCooperation) ContractDao.getViewContractCoop(conn,vs.getCcid()).data;
        //获取结算单明细
        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List< ViewDetail1> detail1s = JSONArray.parseArray(rows, ViewDetail1.class);

        //文件名
        fileName=vs.getName()+(vs.getMonth()==null?"":sdf.format(vs.getMonth()))+"代缴社保结算单";
        fileName = new String(fileName.getBytes(),"iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\""
                + fileName + ".xls\"");

        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("公司汇款明细", 0);

        try {
            sheet1.addCell(new Label(0, 0, vs.getName()+""+(vs.getMonth()==null?"":sdf.format(vs.getMonth()))+"代缴社保汇款表"));
            sheet1.addCell(new Label(0, 1, "员工姓名"));
            sheet1.addCell(new Label(1, 1, "身份证号码"));
            sheet1.addCell(new Label(2, 1, "单位养老"));
            sheet1.addCell(new Label(3, 1, "单位医疗"));
            sheet1.addCell(new Label(4, 1, "单位大病"));
            sheet1.addCell(new Label(5, 1, "单位生育"));
            sheet1.addCell(new Label(6, 1, "单位失业"));
            sheet1.addCell(new Label(7, 1, "单位工伤"));
            sheet1.addCell(new Label(8, 1, "单位公积金"));
            sheet1.addCell(new Label(9, 1, "单位社保合计"));
            sheet1.addCell(new Label(10, 1, "个人养老"));
            sheet1.addCell(new Label(11, 1, "个人医疗"));
            sheet1.addCell(new Label(12, 1, "个人失业"));
            sheet1.addCell(new Label(13, 1, "个人大病"));
            sheet1.addCell(new Label(14, 1, "个人公积金"));
            sheet1.addCell(new Label(15, 1, "个人社保合计"));
            sheet1.addCell(new Label(16, 1, "国家减免"));
            sheet1.addCell(new Label(17, 1, "管理费"));
            sheet1.addCell(new Label(18, 1, "核收补减"));
            sheet1.addCell(new Label(19, 1, "税费"));
            sheet1.addCell(new Label(20, 1, "汇款总额"));
            sheet1.addCell(new Label(21, 1, "备注"));


            int index = 2;
            int type = vc.getStype();//合同服务项目中的类型
            int category = vc.getCategory();//合同服务项目中的结算方式
            int invoice = vc.getInvoice();//合同基础信息中的发票类型
            float per = vc.getPer()/100;//税费比例（选择增值税专用发票（全额）需要用到）
            float val = vc.getValue();//结算值，根据结算方式的不同，代表的意义不同
            float manage = 0;
            float tax2 = 0;

            for( ViewDetail1 v:detail1s){
                sheet1.addCell(new Label(0, index, v.getName()));
                sheet1.addCell(new Label(1, index, v.getCardId()));
                sheet1.addCell(new jxl.write.Number(2, index, v.getPension2()));
                sheet1.addCell(new jxl.write.Number(3, index, v.getMedicare2()));
                sheet1.addCell(new jxl.write.Number(4, index, v.getDisease2()));
                sheet1.addCell(new jxl.write.Number(5, index, v.getBirth()));
                sheet1.addCell(new jxl.write.Number(6, index, v.getUnemployment2()));
                sheet1.addCell(new jxl.write.Number(7, index, v.getInjury()));
                sheet1.addCell(new jxl.write.Number(8, index, v.getFund2()));
                float sum1 = v.getPension2()+v.getMedicare2()+v.getDisease2()+v.getBirth()+v.getUnemployment2()
                        +v.getInjury()+v.getFund2();
                //单位社保合计
                sheet1.addCell(new jxl.write.Number(9, index, sum1));

                sheet1.addCell(new jxl.write.Number(10, index, v.getPension1()));
                sheet1.addCell(new jxl.write.Number(11, index, v.getMedicare1()));
                sheet1.addCell(new jxl.write.Number(12, index, v.getUnemployment1()));
                sheet1.addCell(new jxl.write.Number(13, index, v.getDisease1()));
                sheet1.addCell(new jxl.write.Number(14, index, v.getFund1()));
                float sum2 = v.getPension1()+v.getDisease1()+v.getUnemployment1()
                        +v.getMedicare1()+v.getFund1();
                sheet1.addCell(new jxl.write.Number(15, index, sum2));
                sheet1.addCell(new jxl.write.Number(16, index, v.getFree()));

                //因为代缴社保的管理费和税费计算需要整合个人五险一金，所以这里的数据需要加工
                manage+=sum2;
                tax2+=sum2;
                //计算管理费和税费
                HashMap<String,Float> map2 =calculateManageAndTax2(type,category,invoice,per,val,manage,tax2,v,0);
                //管理费
                sheet1.addCell(new jxl.write.Number(17, index,  map2.get("manage")));
                //核收补减
                sheet1.addCell(new jxl.write.Number(18, index, v.getExtra2()));
                //税费
                sheet1.addCell(new jxl.write.Number(19, index, map2.get("tax2")));
                //社保合计
                float sum = sum1+sum2;
                //汇款总额 = 社保总额+管理费+税费+（单位）核收补减
                float summary = sum-v.getFree()+ map2.get("manage")+map2.get("tax2")+v.getExtra2();
                sheet1.addCell(new jxl.write.Number(20, index, summary));
                //备注
                sheet1.addCell(new Label(21, index,v.getComments2()));
                index++;
            }
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        }
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
        return result;
    }

    //读取专项扣除数据
    private String readDeducts(Connection conn,HttpServletRequest request)throws IOException, ServletException {
        DaoQueryListResult result = new DaoQueryListResult();
        Part part = request.getPart("file");
        try {//获取part中的文件，读取数据
            InputStream is = part.getInputStream();
            List<ViewDeduct> data = XlsUtil.readDeducts(is);
            result=DeductService.addDeducts(conn,data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString(result);
    }
    //导出参保单
    private void exportInsurance(Connection conn, HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte category = Byte.parseByte(request.getParameter("category"));
        byte status = Byte.parseByte(request.getParameter("status"));
        switch (category){
            case 0://导出社保单
                if(status == 0){//导出新增
                    FileService.exportSocial1(conn,response);
                }else {//导出停保
                    FileService.exportSocial2(conn,response);
                }
                break;
            case 1://导出医保单
                if(status == 0){//导出续保
                    FileService.exportMedicare1(conn,response);
                }else {//导出停保
                    FileService.exportMedicare2(conn,response);
                }
                break;
            case 2://导出公积金
                String fileName = "exportFund.xls";
                String fullFileName = getServletContext().getRealPath("/excelFile/" + fileName);
                File file = new File(fullFileName);
                FileService.exportFund(conn,response,file);
                break;
        }
    }

    //导出银行
    private void exportBank(Connection conn, HttpServletRequest request,HttpServletResponse response) throws IOException {
        byte category = Byte.parseByte(request.getParameter("category"));
        long sid = Long.parseLong(request.getParameter("id"));
        String fileName;
        String fullFileName;
        File file;
        switch (category){
            case 0://招行
                fileName = "bank1.xls";
                fullFileName = getServletContext().getRealPath("/excelFile/" + fileName);
                file = new File(fullFileName);
                FileService.exportBank1(conn,sid,response,file);
                break;
            case 1://农行
                fileName = "bank2.xls";
                fullFileName = getServletContext().getRealPath("/excelFile/" + fileName);
                file = new File(fullFileName);
                FileService.exportBank2(conn,sid,response,file);
                break;
            case 2://浦发
                FileService.exportBank3(conn,sid,response);
                break;
            case 3://交通
                FileService.exportBank4(conn,sid,response);
                break;
        }
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
