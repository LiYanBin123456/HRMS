package service.fileService;

import bean.employee.EnsureSetting;
import bean.employee.PayCard;
import bean.insurance.ViewInsurance;
import bean.settlement.ViewDetail1;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.employee.PayCardDao;
import dao.employee.SettingDao;
import dao.insurance.InsuranceDao;
import dao.settlement.Detail1Dao;
import database.DaoQueryListResult;
import database.QueryParameter;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import utills.DateUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FileService {
    //导出新增社保单
    public static void exportSocial1(Connection conn, HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=exportSocial1.xls");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("status3","=",1);

        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);
        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("新增社保单", 0);
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
            for(ViewInsurance v:insurances){
                //转化户口性质
                String houseHold =  houseHold(v.getHousehold());
                //比较时间
                String msg = compareDate(v.getEntry());
                sheet1.addCell(new Label(0, index, v.getName()));
                sheet1.addCell(new Label(1, index, v.getCode3()));
                sheet1.addCell(new Label(2, index, v.getCardId()));
                sheet1.addCell(new Label(3, index, v.getDate3()==null?"":DateUtil.format(v.getDate3(),"yyyy-MM-dd")));
                sheet1.addCell(new jxl.write.Number(4, index, v.getBase3()));
                sheet1.addCell(new Label(5, index, "正常参保登记"));
                sheet1.addCell(new Label(6, index, "合同制"));
                sheet1.addCell(new Label(7, index, msg));
                sheet1.addCell(new Label(8, index, v.getEntry()==null?"":DateUtil.format(v.getEntry(),"yyyy-MM-dd")));
                sheet1.addCell(new Label(9, index, v.getPhone()));
                sheet1.addCell(new Label(10, index, houseHold));
                sheet1.addCell(new Label(11, index, v.getAddress()));
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
    }

    //导出停保社保单
    public static void exportSocial2(Connection conn, HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=exportSocial2.xls");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("status3","=",4);

        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);
        //获取本月最后一天
        Date date = DateUtil.getLastDayofMonth(new Date());
        String lastDay = DateUtil.format(date,"yyyy-MM-dd");//本月最后一天

        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("停保社保单", 0);
        try {
            sheet1.addCell(new Label(0, 0, "个人代码"));
            sheet1.addCell(new Label(1, 0, "姓名"));
            sheet1.addCell(new Label(2, 0, "证件号码"));
            sheet1.addCell(new Label(3, 0, "变更日期"));
            sheet1.addCell(new Label(4, 0, "变更原因"));
            int index = 1;
            for(ViewInsurance insurance:insurances){
                sheet1.addCell(new Label(1, index, insurance.getCode3()));
                sheet1.addCell(new Label(0, index, insurance.getName()));
                sheet1.addCell(new Label(2, index, insurance.getCardId()));
                sheet1.addCell(new Label(3, index, lastDay));
                sheet1.addCell(new Label(4, index, insurance==null?"":chageReason(insurance.getReason())));
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
    }

    //导出续保医保单
    public static void exportMedicare1(Connection conn, HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=exportMedicare1.xls");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("status1","=",2);

        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);

        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("续保医保单", 0);
        try {
            sheet1.addCell(new Label(1, 0, "个人编号"));
            sheet1.addCell(new Label(0, 0, "姓名"));
            sheet1.addCell(new Label(2, 0, "证件号码"));
            sheet1.addCell(new Label(3, 0, "参保基数"));
            sheet1.addCell(new Label(4, 0, "参保险种"));
            sheet1.addCell(new Label(4, 0, "参保时间"));
            int index = 1;
            for(ViewInsurance insurance:insurances){
                sheet1.addCell(new Label(1, index, insurance.getCode1()));
                sheet1.addCell(new Label(0, index, insurance.getName()));
                sheet1.addCell(new Label(2, index, insurance.getCardId()));
                sheet1.addCell(new jxl.write.Number(3, index, insurance.getBase1()));
                sheet1.addCell(new Label(4, index, "医疗、大病、生育"));
                sheet1.addCell(new Label(5, index, insurance.getDate1()==null?"":DateUtil.format(insurance.getDate1(),"yyyy-MM-dd")));
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
    }

    //导出停保医保单
    public static void exportMedicare2(Connection conn, HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=exportMedicare2.xls");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("status1","=",4);

        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);

        //获取本月最后一天
        Date date = DateUtil.getLastDayofMonth(new Date());
        String lastDay = DateUtil.format(date,"yyyy-MM-dd");
        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("停保医保单", 0);
        try {
            sheet1.addCell(new Label(1, 0, "个人代码"));
            sheet1.addCell(new Label(0, 0, "姓名"));
            sheet1.addCell(new Label(2, 0, "证件号码"));
            sheet1.addCell(new Label(3, 0, "变更日期"));
            sheet1.addCell(new Label(4, 0, "变更原因"));
            int index = 1;
            for(ViewInsurance Insurance:insurances){
                sheet1.addCell(new Label(1, index, Insurance.getCode1()));
                sheet1.addCell(new Label(0, index, Insurance.getName()));
                sheet1.addCell(new Label(2, index, Insurance.getCardId()));
                sheet1.addCell(new Label(3, index, lastDay));
                sheet1.addCell(new Label(4, index, chageReason(Insurance.getReason())));
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
    }

    //导出公积金
    public static void exportFund(Connection conn, HttpServletResponse response, File file) {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=exportFund.xls");

        Workbook book = null;
        //公积金的变更是什么状态
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("status2","=",4);
        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);
        try {
            //获取模板
            book = Workbook.getWorkbook(file);
            // jxl.Workbook 对象是只读的，所以如果要修改Excel，需要创建一个可读的副本，副本指向原Excel文件（即下面的new File(excelpath)）
            WritableWorkbook workbook = Workbook.createWorkbook(response.getOutputStream(),book);
            WritableSheet sheet = workbook.getSheet(0);//获取第一个sheet

            int index = 6;
            float per=0;
            for(ViewInsurance v:insurances){
                EnsureSetting setting = (EnsureSetting) SettingDao.get(conn,v.getEid()).data;
                if(setting!=null){
                    per = setting.getFundPer()/100;
                }
                sheet.addCell(new jxl.write.Number(0, index, index-5));//序号
                sheet.addCell(new Label(1, index, v.getName()));//职工姓名
                sheet.addCell(new Label(2, index, v.getCardId()));//证件号
                sheet.addCell(new Label(3, index, v.getDate2()==null?"":DateUtil.format(v.getDate2(),"yyyy-MM-dd")));//起缴时间
                sheet.addCell(new jxl.write.Number(4, index, v.getBase2()));//工资基数
                sheet.addCell(new jxl.write.Number(5, index, per));//单位比例
                sheet.addCell(new jxl.write.Number(6, index, per));//个人比例
                sheet.addCell(new jxl.write.Number(7, index, v.getBase2()*per));//个人缴存标准
                sheet.addCell(new jxl.write.Number(8, index, (v.getBase2()*per)*2));//缴存合计
                sheet.addCell(new Label(10, index, v.getPhone()));//手机号码
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
        String msg = null;
        if(date == null){
            msg = "否";
        }else {
            //获取当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
            String now = df.format(new Date(System.currentTimeMillis()));
            String time =df.format(date);
            try {
                java.util.Date date1 = df.parse(now);
                java.util.Date date2 = df.parse(time);
                if(date1.compareTo(date2)!=0){
                    msg = "是";
                }else{
                    msg = "否";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    //转换离职原因
    public static String chageReason(byte r){
        String reason = null;
        switch (r){
            case 0:
                reason = "合同到期";
                break;
            case 1:
                reason = "被用人单位解除劳动合同";
                break;
            case 2:
                reason = "被用人单位开除";
                break;
            case 3:
                reason = "被用人单位除名";
                break;
            case 4:
                reason = "被用人单位辞退";
                break;
            case 5:
                reason = "公司倒闭";
                break;
            case 6:
                reason = "公司破产";
                break;
            case 7:
                reason = "单位人员减少";
                break;
            case 8:
                reason = "养老在职转退休";
                break;
            case 9:
                reason = "参军";
                break;
            case 10:
                reason = "入学";
                break;
            case 11:
                reason = "劳改劳教";
                break;
            case 12:
                reason = "出国定居";
                break;
            case 13:
                reason = "异地转移";
                break;
            case 14:
                reason = "不足缴费年限";
                break;
            case 15:
                reason = "人员失踪";
                break;
            case 16:
                reason = "错误申报";
                break;
            case 17:
                reason = "其他原因减少";
                break;
        }
        return reason;
    }

    //导出招行
    public static void exportBank1(Connection conn, long sid, HttpServletResponse response, File file) {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=bank1.xls");

        Workbook book ;
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid", "=", sid);

        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewDetail1> viewDetail1s = JSONArray.parseArray(rows, ViewDetail1.class);
        try {
            //获取模板
            book = Workbook.getWorkbook(file);
            // jxl.Workbook 对象是只读的，所以如果要修改Excel，需要创建一个可读的副本，副本指向原Excel文件（即下面的new File(excelpath)）
            WritableWorkbook workbook = Workbook.createWorkbook(response.getOutputStream(),book);
            WritableSheet sheet1 = workbook.getSheet(0);//获取第一个sheet
            WritableSheet sheet2 = workbook.getSheet(1);//获取第二个sheet

            int index = 1;
            for(ViewDetail1 v:viewDetail1s){
                //金额上限（发放额）、收方账号、收方户名、收方行名称、收方行行号、附言
                PayCard card = (PayCard) PayCardDao.get(conn,v.getEid()).data;
                if(card!=null){
                    sheet1.addCell(new jxl.write.Number(1, index, v.getPaid()));//金额上限,实发
                    sheet1.addCell(new Label(7, index, card.getCardNo()));//收方账号
                    sheet1.addCell(new Label(10, index,card.getBank1()));//收方行名称
                    sheet1.addCell(new Label(11, index, card.getBankNo()));//收方行行号
                    sheet1.addCell(new Label(13, index, "融金2月工资"));//附言

                    sheet2.addCell(new jxl.write.Number(0, index, v.getPaid()));//金额上限,实发
                    sheet2.addCell(new Label(1, index, card.getCardNo()));//收方账号
                    //sheet2.addCell(new Label(3, index, "大正月"+v.getMonth()==null?"":sdf.format(v.getMonth())+"工资"));//附言
                }
                sheet1.addCell(new Label(8, index, v.getName()));//收方户名
                sheet2.addCell(new Label(2, index, v.getName()));//收方户名
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
    //导出农行
    public static void exportBank2(Connection conn, long sid, HttpServletResponse response, File file) {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=bank2.xls");

        Workbook book = null;
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid", "=", sid);

        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewDetail1> viewDetail1s = JSONArray.parseArray(rows, ViewDetail1.class);
        try {
            //获取模板
            book = Workbook.getWorkbook(file);
            // jxl.Workbook 对象是只读的，所以如果要修改Excel，需要创建一个可读的副本，副本指向原Excel文件（即下面的new File(excelpath)）
            WritableWorkbook workbook = Workbook.createWorkbook(response.getOutputStream(),book);
            WritableSheet sheet1 = workbook.getSheet(0);//获取第一个sheet
            WritableSheet sheet2 = workbook.getSheet(1);//获取第二个sheet

            int index = 1;
            for(ViewDetail1 v:viewDetail1s){
                //序号	卡号	姓名	开户银行（行别）	大额行号	开户行支行名称	实发	项目
                PayCard card = (PayCard) PayCardDao.get(conn,v.getEid()).data;
                if(card!=null) {
                    sheet1.addCell(new jxl.write.Number(0, index, index ));
                    sheet1.addCell(new Label(1, index, card.getCardNo()));
                    sheet1.addCell(new Label(3, index, card.getBank1()));
                    sheet1.addCell(new Label(4, index, card.getBankNo()));
                    sheet1.addCell(new Label(5, index, card.getBank2()));
                    sheet1.addCell(new jxl.write.Number(6, index, v.getPaid()));
                    sheet1.addCell(new Label(7, index, v.getMonth()==null?"": DateUtil.format(v.getMonth(),"yyyy.MM") + "工资"));

                    //序号	卡号	姓名	金额	备注
                    sheet2.addCell(new jxl.write.Number(0, index, index ));
                    sheet2.addCell(new Label(1, index, card.getCardNo()));
                    sheet2.addCell(new jxl.write.Number(3, index, v.getPaid()));
                    sheet2.addCell(new Label(4, index, v.getMonth()==null?"":DateUtil.format(v.getMonth(),"yyyy.MM") + "工资"));
                }
                sheet1.addCell(new Label(2, index, v.getName()));
                sheet2.addCell(new Label(2, index, v.getName()));
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
    //导出浦发
    public static void exportBank3(Connection conn, long sid, HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=bank3.xls");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);

        List<ViewDetail1> details = JSONArray.parseArray(rows, ViewDetail1.class);
        String month = details.get(0).getMonth()==null?"":(DateUtil.format(details.get(0).getMonth(),"yyyy-MM-dd").split("-"))[1];
        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("浦发银行", 0);
        try {
            //卡号（或账号）	金额	客户姓名	第三方编号	摘要
            sheet1.addCell(new Label(0, 0, "卡号（或账号）"));
            sheet1.addCell(new Label(1, 0, "金额"));
            sheet1.addCell(new Label(2, 0, "客户姓名"));
            sheet1.addCell(new Label(3, 0, "第三方编号"));
            sheet1.addCell(new Label(4, 0, "摘要"));

            int index = 1;
            for(ViewDetail1 detail1:details){
                PayCard card = (PayCard) PayCardDao.get(conn,detail1.getEid()).data;
                if(card!=null){
                    sheet1.addCell(new Label(0, index, card.getCardNo()));
                    sheet1.addCell(new jxl.write.Number(1, index, detail1.getPaid()));
                    sheet1.addCell(new Label(3, index, ""));
                    sheet1.addCell(new Label(4, index, month+"月工资"));
                }
                sheet1.addCell(new Label(2, index, detail1.getName()));
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
    //导出交通
    public static void exportBank4(Connection conn, long sid, HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=bank4.xls");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewDetail1> details = JSONArray.parseArray(rows, ViewDetail1.class);

        String month = details.get(0).getMonth()==null?"":DateUtil.format(details.get(0).getMonth(),"yyyy.MM");

        WritableWorkbook book = Workbook.createWorkbook(response.getOutputStream());
        WritableSheet sheet1 = book.createSheet("浦发银行", 0);
        try {
            //序号	卡号	姓名	实发	项目
            sheet1.addCell(new Label(0, 0, "序号"));
            sheet1.addCell(new Label(1, 0, "卡号"));
            sheet1.addCell(new Label(2, 0, "姓名"));
            sheet1.addCell(new Label(3, 0, "实发"));
            sheet1.addCell(new Label(4, 0, "项目"));

            int index = 1;
            for(ViewDetail1 detail1:details){
                PayCard card = (PayCard) PayCardDao.get(conn,detail1.getEid()).data;
                if(card!=null){
                    sheet1.addCell(new jxl.write.Number(0, index, index));
                    sheet1.addCell(new Label(1, index, card.getCardNo()));
                    sheet1.addCell(new jxl.write.Number(3, index, detail1.getPaid()));
                    sheet1.addCell(new Label(4, index, month+"月工资"));
                }
                sheet1.addCell(new Label(2, index, detail1.getName()));
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

    public static void exportDetail3(Connection conn, HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        String fileName = URLEncoder.encode("商业保险参保单.xls","utf-8");
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);

    }

}
