package servlet;

import bean.admin.Account;
import bean.client.MapSalary;
import bean.contract.Serve;
import bean.contract.ViewContractCooperation;
import bean.employee.Employee;
import bean.employee.ViewDeduct;
import bean.employee.ViewEmployee;
import bean.settlement.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.client.FinanceDao;
import dao.client.MapSalaryDao;
import dao.contract.ContractDao;
import dao.contract.ServeDao;
import dao.employee.EmployeeDao;
import dao.settlement.*;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.io.IOUtils;
import service.employee.DeductService;
import service.fileService.FileService;
import utills.DateUtil;
import utills.IDCardUtil;
import utills.excel.Field;
import utills.excel.Scheme;
import utills.excel.SchemeDefined;
import utills.excel.XlsUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "FileServlet",urlPatterns = "/verify/file")
@MultipartConfig
public class FileServlet extends HttpServlet {
    public static void main(String[] args) {
        Connection conn = ConnUtil.getConnection();
        long sid =1;//结算单id
        //获取小时工明细
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid", "=", sid);
        DaoQueryListResult result = Detail2Dao.getList(conn, parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewDetail2> details = JSONArray.parseArray(rows, ViewDetail2.class);

        try {
            File file = new File("d:\\test2.xls");

            Scheme scheme = new Scheme();
            scheme.addField(new Field(0, "name", "员工姓名", Field.STRING, 100));
            scheme.addField(new Field(1, "cardId", "身份证号码", Field.STRING, 100));
            scheme.addField(new Field(2, "hours", "工时", Field.INT, 100));
            scheme.addField(new Field(3, "price", "公司单价", Field.FLOAT, 100));
            scheme.addField(new Field(4, "food", "餐费", Field.FLOAT, 100));
            scheme.addField(new Field(5, "traffic", "交通费", Field.FLOAT, 100));
            scheme.addField(new Field(6, "accommodation", "住宿费", Field.FLOAT, 100));
            scheme.addField(new Field(7, "utilities", "水电费", Field.FLOAT, 100));
            scheme.addField(new Field(8, "insurance", "保险费", Field.FLOAT, 100));
            scheme.addField(new Field(9, "other1", "其他1", Field.FLOAT, 100));
            scheme.addField(new Field(10, "other2", "其他2", Field.FLOAT, 100));

            //写数据测试
            FileOutputStream os = new FileOutputStream(file);
            String sheetNames = "test1";
            JSONArray data=JSONArray.parseArray(JSON.toJSONString(details));
            //   XlsUtil.write(os, sheetNames, scheme, data);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
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
            case "initDeducts"://初始化个税专项扣除累计
                result = initDeducts(request);
                break;
            case "readDeducts"://读取个税表中的数据
                result = readDeducts(conn,request);
                break;
            case "exist"://判断文件是否存在
                result = exist(request);
                break;
            case "downloadTemplateDetail4"://下载结算单明细模板
                downloadTemplateDetail4(conn,request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "downloadTemplateDetail1"://下载结算单明细模板
                downloadTemplateDetail1(conn,request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "downloadTemplateDetail2"://下载小时工结算单明细模板
                downloadTemplateDetail2(conn,request,response);
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
            case "exportDetail14"://导出代缴社保结算单明细
                exportDetail14(conn,request,response);
                ConnUtil.closeConnection(conn);
                return;
            case "exportDetail4"://导出特殊结算单明细
                //exportDetail4(conn,request,response);
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
    private void exportTaxEmployee(Connection conn, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=tax1.xls");

        //读取模板
        String template = getServletContext().getRealPath("/excelFile/tax1.xls");

        //查询出员工，条件限制先留着以后交流修改
        QueryParameter parameter = new QueryParameter();
        List<Employee> employees = (List<Employee>) EmployeeDao.getList(conn,parameter).rows;
        JSONArray data = JSONArray.parseArray(JSONObject.toJSONString(employees));
        for(int i=0; i<data.size(); i++){
            JSONObject o = (JSONObject) data.get(i);
            o.put("cardType","居民身份证");
            o.put("nation","中国");
            o.put("type","雇员");
            String cardId = o.getString("cardId");
            if(cardId!=null && IDCardUtil.isValid(cardId)) {
                o.put("sex", IDCardUtil.getSex(cardId));
                o.put("birthday", IDCardUtil.getBirthday(cardId));
            }else{
                o.put("sex", "-");
                o.put("birthday", "-");
            }
        }
        XlsUtil.write(response.getOutputStream(),template, SchemeDefined.SCHEME_TAX1,data);
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
                JSONArray data= XlsUtil.read(is,"信息表","元数据");
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

    //下载特殊结算单模板
    private void downloadTemplateDetail4(Connection conn, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        long cid = Long.parseLong(request.getParameter("cid"));//合作单位id

        //获取名单准备数据
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("cid","=",cid);
        parameter.addCondition("did","=",user.getRid());
        parameter.addCondition("type","=",1);
        parameter.addCondition("status","=",0);
        List<ViewEmployee> employees = (List<ViewEmployee>) EmployeeDao.getList(conn,parameter).rows;
        JSONArray data = JSONArray.parseArray(JSON.toJSONString(employees));

        //准备目标文件和模板文件
        String cname =employees.size()>0?employees.get(0).getCname():"";
        String filename=cname+"特殊结算单模板";
        filename = new String(filename.getBytes(),"iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\""+ filename + ".xls\"");
        String template = getServletContext().getRealPath("/excelFile/detail4.xls");

        //写Excel
        XlsUtil.write(response.getOutputStream(),template,SchemeDefined.SCHEME_DETAIL_EXPORT, data);
    }

    //下载普通结算单明细模板
    private void downloadTemplateDetail1(Connection conn, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        long cid = Long.parseLong(request.getParameter("cid"));//合作单位id
        String month = request.getParameter("month");//获取结算单月份

        //获取结算单视图
        ViewSettlement1 settlement = (ViewSettlement1) Settlement1Dao.get(conn,sid).data;
        String typeMsg="派遣";
        byte category = 1;//外派员工类别
        switch (settlement.getType()){
            case Settlement1.TYPE_1:
                typeMsg = "派遣";
                category = 1;
                break;
            case Settlement1.TYPE_2:
                typeMsg = "外包";
                category = 2;
                break;
            case Settlement1.TYPE_3:
                typeMsg = "代发工资";
                category = 3;
                break;
            case Settlement1.TYPE_4:
                typeMsg = "代缴社保";
                category = 4;
                break;
        }
        //根据条件找到派遣到该单位的员工列表，条件有cid，did，类型为外派或者派遣员工，用工性质不是小时工，在职
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("cid","=",cid);
        parameter.addCondition("did","=",user.getRid());
        parameter.addCondition("type","=",1);
        parameter.addCondition("category","=",category);
        parameter.addCondition("status","=",0);
        List<ViewEmployee> employees = (List<ViewEmployee>)EmployeeDao.getList(conn,parameter).rows;

        //文件名
        String fileName=settlement.getName()+(settlement.getMonth()==null?"":DateUtil.format(settlement.getMonth(),"yyyy年MM月"))+typeMsg+"结算单明细模板";
        fileName = new String(fileName.getBytes(),"iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\""
                + fileName + ".xls\"");

        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("信息表", 0);
        WritableSheet sheet2 = book.createSheet("元数据", 1);

        //获取合作客户的自定义工资项
        MapSalary mapSalary = (MapSalary)MapSalaryDao.selectByMonth(cid,conn, DateUtil.getLastDayofMonth(month)).data;

        try {
            sheet1.addCell(new Label(0, 0, "员工姓名*"));
            sheet1.addCell(new Label(1, 0, "身份证号码*"));
            sheet1.setColumnView(1,20);
            sheet1.addCell(new Label(2, 0, "基本工资"));
            int  col = 3;
            if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0){
                List<MapSalary.SalaryItem> itemList = mapSalary.getItemList();
                for(int i = 0;i<itemList.size();i++){
                    sheet1.addCell(new Label(col++, 0, itemList.get(i).getField()));
                }
            }
            sheet1.addCell(new Label(col+0, 0, "个人核收补减"));
            sheet1.addCell(new Label(col+1, 0, "备注1"));
            sheet1.addCell(new Label(col+2, 0, "单位核收补减"));
            sheet1.addCell(new Label(col+3, 0, "备注2"));
            sheet1.addCell(new Label(col+4, 0, "个人养老"));
            sheet1.addCell(new Label(col+5, 0, "个人医疗"));
            sheet1.addCell(new Label(col+6, 0, "个人失业"));
            sheet1.addCell(new Label(col+7, 0, "个人大病"));
            sheet1.addCell(new Label(col+8, 0, "个人公积金"));
            sheet1.addCell(new Label(col+9, 0, "单位养老"));
            sheet1.addCell(new Label(col+10, 0, "单位医疗"));
            sheet1.addCell(new Label(col+11, 0, "单位失业"));
            sheet1.addCell(new Label(col+12, 0, "单位工伤"));
            sheet1.addCell(new Label(col+13, 0, "单位大病"));
            sheet1.addCell(new Label(col+14, 0, "单位生育"));
            sheet1.addCell(new Label(col+15, 0, "单位公积金"));
            sheet1.addCell(new Label(col+16, 0, "个税"));
            sheet1.addCell(new Label(col+17, 0, "应付"));
            sheet1.addCell(new Label(col+18, 0, "实付"));
            sheet1.addCell(new Label(col+19, 0, "是否为自定义工资条（0否1是）*"));
            sheet1.setColumnView(col+19,20);
            int index = 1;
            for( ViewEmployee viewEmployee:employees){//根据员工生成明细，如果没有员工则不生成
                sheet1.addCell(new Label(0, index, viewEmployee.getName()));
                sheet1.addCell(new Label(1, index, viewEmployee.getCardId()));
                if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0){
                    List<MapSalary.SalaryItem> itemList = mapSalary.getItemList();
                    int c = itemList.size()+2;
                    sheet1.addCell(new Label(c+20, index,  "0"));

                }else {
                    sheet1.addCell(new Label(22, index,  "0"));
                }
                index++;
            }

            sheet2.addCell(new Label(0, 0, "字段名"));
            sheet2.addCell(new Label(0, 1, "name"));
            sheet2.addCell(new Label(0, 2, "cardId"));
            sheet2.addCell(new Label(0, 3, "base"));
            int  row = 4;
            if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0) {
                List<MapSalary.SalaryItem> itemList = mapSalary.getItemList();
                for (int i = 0; i < itemList.size(); i++) {
                    String name = "f" + (i + 1);
                    sheet2.addCell(new Label(0, row++, name));
                }
            }
            sheet2.addCell(new Label(0, row++, "extra1"));
            sheet2.addCell(new Label(0, row++, "comments1"));
            sheet2.addCell(new Label(0, row++, "extra2"));
            sheet2.addCell(new Label(0, row++, "comments2"));
            sheet2.addCell(new Label(0, row++, "pension1"));
            sheet2.addCell(new Label(0, row++, "medicare1"));
            sheet2.addCell(new Label(0, row++, "unemployment1"));
            sheet2.addCell(new Label(0, row++, "disease1"));
            sheet2.addCell(new Label(0, row++, "fund1"));
            sheet2.addCell(new Label(0, row++, "pension2"));
            sheet2.addCell(new Label(0, row++, "medicare2"));
            sheet2.addCell(new Label(0, row++, "unemployment2"));
            sheet2.addCell(new Label(0, row++, "injury"));
            sheet2.addCell(new Label(0, row++, "disease2"));
            sheet2.addCell(new Label(0, row++, "birth"));
            sheet2.addCell(new Label(0, row++, "fund2"));
            sheet2.addCell(new Label(0, row++, "tax"));
            sheet2.addCell(new Label(0, row++,  "payable"));
            sheet2.addCell(new Label(0, row++,  "paid"));
            sheet2.addCell(new Label(0, row++,  "status"));

            sheet2.addCell(new Label(1, 0, "类型"));
            sheet2.addCell(new Label(1, 1, "string"));
            sheet2.addCell(new Label(1, 2, "string"));
            sheet2.addCell(new Label(1, 3, "float"));
            if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0){
                int  c = 0;
                List<MapSalary.SalaryItem> itemList = mapSalary.getItemList();
                for(int i = 0;i<itemList.size();i++){
                    c = i+4;
                    sheet2.addCell(new Label(1, c, "float"));
                }
                sheet2.addCell(new Label(1, c+1, "float"));
                sheet2.addCell(new Label(1, c+2, "string"));
                sheet2.addCell(new Label(1, c+3, "float"));
                sheet2.addCell(new Label(1, c+4, "string"));
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
                sheet2.addCell(new Label(1,c+20,  "float"));
            }else {
                sheet2.addCell(new Label(1, 3, "float"));
                sheet2.addCell(new Label(1, 4, "String"));
                sheet2.addCell(new Label(1, 5, "float"));
                sheet2.addCell(new Label(1, 6, "String"));
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
                sheet2.addCell(new Label(1, 23, "float"));
            }

            sheet2.addCell(new Label(2, 0, "是否允许为空"));
            sheet2.addCell(new Label(2, 1, "False"));
            sheet2.addCell(new Label(2, 2, "False"));
            sheet2.addCell(new Label(2, 3, "TRUE"));
            if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0){
                int  c = 0;
                List<MapSalary.SalaryItem> itemList = mapSalary.getItemList();
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
                sheet2.addCell(new Label(2, c+20, "False"));
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
                sheet2.addCell(new Label(2, 23,  "False"));
            }
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    //下载小时工结算明细模板
    private void downloadTemplateDetail2(Connection conn, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        long cid = Long.parseLong(request.getParameter("cid"));//合作单位id

        //获取名单准备数据
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("cid","=",cid);
        parameter.addCondition("did","=",user.getRid());
        parameter.addCondition("type","=",1);
        parameter.addCondition("category","=",Employee.CATEGORY_5);//小时工
        parameter.addCondition("status","=",0);
        List<ViewEmployee> employees = (List<ViewEmployee>) EmployeeDao.getList(conn,parameter).rows;
        JSONArray data = JSONArray.parseArray(JSON.toJSONString(employees));


        //准备目标文件和模板文件
        String filename=employees.size()>0?employees.get(0).getCname():""+"小时工结算单明细模板";
        filename = new String(filename.getBytes(),"iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\""+ filename + ".xls\"");
        String template = getServletContext().getRealPath("/excelFile/detail2.xls");

        //写Excel
        XlsUtil.write(response.getOutputStream(),template,SchemeDefined.SCHEME_DETAIL_EXPORT, data);
    }

    //导出特殊结算单的明细
//    private void exportDetail4(Connection conn, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
//        String fileName;
//        long sid = Long.parseLong(request.getParameter("id"));//结算单id
//        //获取特殊结算单位视图
//        ViewSettlement4 vs = (ViewSettlement4) Settlement4Dao.get(conn, sid).data;
//
//        //获取特殊结算单明细
//        QueryParameter parameter = new QueryParameter();
//        parameter.addCondition("sid", "=", sid);
//        List<ViewDetail4> details = (List<ViewDetail4>) Detail4Dao.getList(conn, parameter).rows;
//        JSONArray data = JSONArray.parseArray(JSON.toJSONString(details));
//
//        //文件名
//        fileName = vs.getName()+(vs.getMonth()==null?"":DateUtil.format(vs.getMonth(),"yyyy-MM") + "特殊结算单");
//        fileName = new String(fileName.getBytes(), "iso-8859-1");
//        response.setContentType("APPLICATION/OCTET-STREAM");
//        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xls\"");
//
//        String title1 =vs.getName()+""+(vs.getMonth()==null?"":DateUtil.format(vs.getMonth(),"yyyy-MM")+"特殊工资汇款表");
//        String title2 =vs.getName()+""+(vs.getMonth()==null?"":DateUtil.format(vs.getMonth(),"yyyy-MM")+"特殊工资明细表");
//        String[] titles ={title1,title2};
//        String[] sheetNames = {"汇款表", "明细表"};
//        Scheme[] schemes = {SchemeDefined.SCHEME_DETAIL4_1, SchemeDefined.SCHEME_DETAIL4_2};
//        JSONArray[] datas = {data, data};
//        XlsUtil.write(response.getOutputStream(), sheetNames,titles, schemes, datas);
//    }

    //导出普通结算单明细
    private void exportDetail1(Connection conn, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String month = request.getParameter("month");//获取结算单月份
        long cid = Long.parseLong(request.getParameter("cid"));//获取合作客户的id
        //获取合作客户的自定义工资项
        MapSalary mapSalary = (MapSalary)MapSalaryDao.selectByMonth(cid,conn, DateUtil.getLastDayofMonth(month)).data;

        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        //获取结算单视图
        ViewSettlement1 vs = (ViewSettlement1) Settlement1Dao.get(conn,sid).data;
        //获取合同视图
        ViewContractCooperation vc = (ViewContractCooperation) ContractDao.getViewContractCoop(conn,vs.getCcid()).data;
        //获取结算单明细
        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List< ViewDetail1> details = JSONArray.parseArray(rows, ViewDetail1.class);

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
        String fileName = vs.getName()+(vs.getMonth()==null?"":DateUtil.format(vs.getMonth(),"yyyy-MM-dd"))+typeMsg+"结算单";
        fileName = new String(fileName.getBytes(),"iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\""
                + fileName + ".xls\"");
        Scheme scheme1 = new Scheme();
        scheme1.addField(new Field(0, "name", "员工姓名", Field.STRING, 100));
        scheme1.addField(new Field(1, "cardId", "身份证号码", Field.STRING, 300));
        scheme1.addField(new Field(2, "base", "基本工资", Field.FLOAT, 100));
        //自定义工资项
        int c1 = 3;
        if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0) {
            List<MapSalary.SalaryItem> itemList = mapSalary.getItemList();//获取自定义工资项集合
            for (int i = 0; i < itemList.size(); i++) {
                scheme1.addField(new Field(c1, "f"+(i+1), itemList.get(i).getField(), Field.FLOAT, 100));
                c1++;
            }
        }
        scheme1.addField(new Field(c1, "pension2", "单位养老", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+1, "medicare2", "单位医疗", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+2, "disease2", "单位大病", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+3, "birth", "单位生育", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+4, "unemployment2", "单位失业", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+5, "injury", "单位工伤", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+6, "fund2", "单位公积金", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+7, "total2", "单位社保合计", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+8, "manage", "管理费", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+9, "extra2", "核收补减", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+10, "tax2", "税费", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+11, "summary", "汇款总额", Field.FLOAT, 100));
        scheme1.addField(new Field(c1+12, "comments", "备注", Field.STRING, 100));

        Scheme scheme2 = new Scheme();
        scheme2.addField(new Field(0, "name", "员工姓名", Field.STRING, 100));
        scheme2.addField(new Field(1, "cardId", "身份证号码", Field.STRING, 300));
        scheme2.addField(new Field(2, "base", "基本工资", Field.FLOAT, 100));

        //自定义工资项
        int c2 = 3;
        if(mapSalary!=null&&mapSalary.getItems()!=null&&mapSalary.getItems().length()>0) {
            List<MapSalary.SalaryItem> itemList = mapSalary.getItemList();//获取自定义工资项集合
            for (int i = 0; i < itemList.size(); i++) {
                scheme2.addField(new Field(c2, "f"+(i+1), itemList.get(i).getField(), Field.FLOAT, 100));
                c2++;
            }
        }
        scheme2.addField(new Field(c2+0, "pension1", "个人养老", Field.FLOAT, 100));
        scheme2.addField(new Field(c2+1, "medicare1", "个人医疗", Field.FLOAT, 100));
        scheme2.addField(new Field(c2+2, "disease1", "个人大病", Field.FLOAT, 100));
        scheme2.addField(new Field(c2+3, "fund1", "个人公积金", Field.FLOAT, 100));
        scheme2.addField(new Field(c2+4, "payable", "税前工资", Field.FLOAT, 100));
        scheme2.addField(new Field(c2+5, "tax", "个税", Field.FLOAT, 100));
        scheme2.addField(new Field(c2+6, "paid", "实发工资", Field.FLOAT, 100));
        Settlement1 settlement = new Settlement1();
        for(ViewDetail1 d:details){
            //计算自定义工资项总和
            d.sumDefinedSalaryItem(mapSalary);
            //计算管理费和税费
            settlement.calcManageAndTax(vc,d);//计算管理费和税费
            d.setManage(settlement.getManage());
            d.setTax2(settlement.getTax());
            //汇款总额 = 基本工资+自定义工资项+单位社保总额+管理费+税费+（单位）核收补减
            float summary = d.getPayable()+d.getTotalDepartment()+d.getManage()+d.getTax2()+d.getExtra2();
            d.setSummary(summary);
            d.setTotal2(d.getTotalDepartment()+d.getFund2());
        }

        JSONArray data = JSONArray.parseArray(JSON.toJSONString(details));

        String title1 =vs.getName()+""+(vs.getMonth()==null?"":DateUtil.format(vs.getMonth(),"yyyy-MM")+"汇款表");
        String title2 =vs.getName()+""+(vs.getMonth()==null?"":DateUtil.format(vs.getMonth(),"yyyy-MM")+"明细表");
        String[] titles ={title1,title2};
        String[] sheetNames = {"汇款表", "明细表"};
        Scheme[] schemes = {scheme1, scheme2};
        JSONArray[] datas = {data, data};
        XlsUtil.write(response.getOutputStream(), sheetNames,titles, schemes, datas);
    }

    //导出小时工结算的明细
    private void exportDetail2(Connection conn, HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String fileName;
        long sid = Long.parseLong(request.getParameter("id"));//结算单id
        //获取小时工结算单视图
        ViewSettlement2 vs = (ViewSettlement2) Settlement2Dao.get(conn, sid).data;

        //获取小时工明细
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid", "=", sid);
        DaoQueryListResult result = Detail2Dao.getList(conn, parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewDetail2> details = JSONArray.parseArray(rows, ViewDetail2.class);

        String cid = vs.getCcid();//获取合作客户的id
        Serve serve = (Serve) ServeDao.get(conn, cid).data;
        byte payer = serve.getPayer();//0 派遣单位发放工资  1 合作客户发放工资
        //公司的单价
        float price = vs.getPrice();
        if (payer == 1) {//合作客户发放工资  单价=公司单价-员工单价
            price = price - details.get(0).getPrice();
        }
        for (ViewDetail2 d : details) {//计算汇款明细，就是合作方客户需要给派遣方汇款多少钱
           d.setSum(d.total(price));
        }
        //文件名
        fileName = vs.getName()+(vs.getMonth()==null?"":DateUtil.format(vs.getMonth(),"yyyy-MM") + "小时工结算单");
        fileName = new String(fileName.getBytes(), "iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\""
                + fileName + ".xls\"");

        JSONArray data = JSONArray.parseArray(JSON.toJSONString(details));

        String title1 =vs.getName()+""+(vs.getMonth()==null?"":DateUtil.format(vs.getMonth(),"yyyy-MM")+"小时工工资汇款表");
        String title2 =vs.getName()+""+(vs.getMonth()==null?"":DateUtil.format(vs.getMonth(),"yyyy-MM")+"小时工工资明细表");
        String[] titles ={title1,title2};
        String[] sheetNames = {"小时工汇款表", "小时工明细表"};
        Scheme[] schemes = {SchemeDefined.SCHEME_DETAIL2_PAID, SchemeDefined.SCHEME_DETAIL2_DETAIL};
        JSONArray[] datas = {data, data};
        XlsUtil.write(response.getOutputStream(), sheetNames,titles, schemes, datas);

    }

    //导出代缴社保结算单明细
    private void exportDetail14(Connection conn, HttpServletRequest request, HttpServletResponse response) throws IOException {
        long sid = Long.parseLong(request.getParameter("sid"));//结算单id
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);

        //获取结算单视图
        ViewSettlement1 vs = (ViewSettlement1) Settlement1Dao.get(conn,sid).data;
        //获取合作客户合同
        ViewContractCooperation vc = (ViewContractCooperation) ContractDao.getViewContractCoop(conn,vs.getCcid()).data;
        //获取结算单明细
        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List< ViewDetail1> details = JSONArray.parseArray(rows, ViewDetail1.class);

        //文件名
        String fileName = vs.getName()+(vs.getMonth()==null?"":DateUtil.format(vs.getMonth(),"yyyy年MM月"))+"代缴社保结算单";
        fileName = new String(fileName.getBytes(),"iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\""
                + fileName + ".xls\"");

        Scheme scheme = new Scheme();
        scheme.addField(new Field(0, "name", "员工姓名", Field.STRING, 100));
        scheme.addField(new Field(1, "cardId", "身份证号码", Field.STRING, 300));
        scheme.addField(new Field(2, "pension2", "单位养老", Field.FLOAT, 100));
        scheme.addField(new Field(3, "medicare2", "单位医疗", Field.FLOAT, 100));
        scheme.addField(new Field(4, "disease2", "单位大病", Field.FLOAT, 100));
        scheme.addField(new Field(5, "unemployment2", "单位失业", Field.FLOAT, 100));
        scheme.addField(new Field(6, "birth", "单位生育", Field.FLOAT, 100));
        scheme.addField(new Field(7, "injury", "单位工伤", Field.FLOAT, 100));
        scheme.addField(new Field(8, "fund2", "单位公积金", Field.FLOAT, 100));
        scheme.addField(new Field(9, "total2", "单位社保合计", Field.FLOAT, 100));scheme.addField(new Field(3, "pension2", "单位养老", Field.FLOAT, 100));
        scheme.addField(new Field(10, "pension1", "个人养老", Field.FLOAT, 100));
        scheme.addField(new Field(11, "medicare1", "个人医疗", Field.FLOAT, 100));
        scheme.addField(new Field(12, "disease1", "个人大病", Field.FLOAT, 100));
        scheme.addField(new Field(13, "unemployment1", "个人失业", Field.FLOAT, 100));
        scheme.addField(new Field(14, "fund1", "个人公积金", Field.FLOAT, 100));
        scheme.addField(new Field(15, "total1", "个人社保合计", Field.FLOAT, 100));
        scheme.addField(new Field(16, "manage", "管理费", Field.FLOAT, 100));
        scheme.addField(new Field(17, "extra2", "核收补减", Field.FLOAT, 100));
        scheme.addField(new Field(18, "tax2", "税费", Field.FLOAT, 100));
        scheme.addField(new Field(19, "summary", "汇款总额", Field.FLOAT, 100));
        scheme.addField(new Field(20, "comments", "备注", Field.STRING, 100));
        Settlement1 settlement = new Settlement1();
       for(ViewDetail1 v:details){
        //计算管理费和税费
        settlement.calcManageAndTax(vc,v);//计算管理费和税费
        v.setManage(settlement.getManage());
        v.setTax2(settlement.getTax());
        //汇款总额 = 社保总额+管理费+税费+（单位）核收补减
        float summary = v.getTotalDepartment()+v.getTotalPerson()+v.getFund1()+v.getFund2()+v.getManage()+v.getTax2()+v.getExtra2();
        v.setSummary(summary);

        v.setTotal1(v.getTotalPerson()+v.getFund1());
        v.setTotal2(v.getTotalDepartment()+v.getFund2());
    }

    JSONArray data = JSONArray.parseArray(JSON.toJSONString(details));
    String sheetName =vs.getName()+""+(vs.getMonth()==null?"":DateUtil.format(vs.getMonth(),"yyyy-MM")+"代缴社保汇款表");
    String title="汇款表";

    XlsUtil.write(response.getOutputStream(),sheetName,title,scheme, data);
    }

    //初始化个税专项扣除累计
    private String initDeducts(HttpServletRequest request)throws IOException, ServletException {
        String result = null;
        Part part = request.getPart("file");
        try {//获取part中的文件，读取数据
            InputStream is = part.getInputStream();
            JSONArray data = XlsUtil.read(is,SchemeDefined.SCHEME_DEDUCT_TOTAL,1);
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

    //读取每月专项扣除数据
    private String readDeducts(Connection conn,HttpServletRequest request)throws IOException, ServletException {
        DaoUpdateResult result = new DaoUpdateResult();
        Part part = request.getPart("file");
        long cid = Long.parseLong(request.getParameter("cid"));
        try {//获取part中的文件，读取数据
            InputStream is = part.getInputStream();
            Scheme[] schemes = {SchemeDefined.SCHEME_DEDUCT_SPOUSE,SchemeDefined.SCHEME_DEDUCT_CHILDREN,SchemeDefined.SCHEME_DEDUCT_EDUCATION,SchemeDefined.SCHEME_DEDUCT_LOAN,SchemeDefined.SCHEME_DEDUCT_RENT,SchemeDefined.SCHEME_DEDUCT_ELDER};
            int[] cows={1,1,2,2,1,2};
            JSONArray[] data =XlsUtil.read(is,schemes,cows);
            List<ViewDeduct> deductList = FileService.readDeduct(data);
            if(cid!=0){//代表是导入部分扣除
                 result=DeductService.updateDeducts(conn,deductList,cid);
             }else {
                 result=DeductService.updateDeducts(conn,deductList);
            }
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
                //FileService.exportFund(conn,response,file);
                break;
        }
    }

    //导出银行
    private void exportBank(Connection conn, HttpServletRequest request,HttpServletResponse response) throws IOException {
        byte category = Byte.parseByte(request.getParameter("category"));
        long sid = Long.parseLong(request.getParameter("id"));
        String fileName;
        String fullFileName;
        switch (category){
            case 0://招行
                fileName = "bank1.xls";
                fullFileName = getServletContext().getRealPath("/excelFile/" + fileName);
                FileService.exportBank1(conn,sid,response,fullFileName);
                break;
            case 1://农行
                fileName = "bank2.xls";
                fullFileName = getServletContext().getRealPath("/excelFile/" + fileName);
                FileService.exportBank2(conn,sid,response,fullFileName);
                break;
            case 2://浦发
                FileService.exportBank3(conn,sid,response);
                break;
            case 3://交通
                FileService.exportBank4(conn,sid,response);
                break;
        }
    }

}
