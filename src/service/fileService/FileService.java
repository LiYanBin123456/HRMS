package service.fileService;

import bean.employee.Deduct;
import bean.employee.EnsureSetting;
import bean.employee.PayCard;
import bean.employee.ViewDeduct;
import bean.insurance.ViewInsurance;
import bean.settlement.ViewDetail1;
import com.alibaba.fastjson.JSON;
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
import utills.CollectionUtil;
import utills.DateUtil;
import utills.IDCardUtil;
import utills.excel.Field;
import utills.excel.Scheme;
import utills.excel.SchemeDefined;
import utills.excel.XlsUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileService {
    //导出新增社保单
    public static void exportSocial1(Connection conn, HttpServletResponse response) throws IOException {
        //文件名
        String fileName ="员工新增社保单";
        fileName = new String(fileName.getBytes(), "iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xls\"");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("status3","=",1);

        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);
        JSONArray data = JSONArray.parseArray(JSONObject.toJSONString(insurances));

        for(int i=0; i<data.size(); i++){
            JSONObject o = (JSONObject) data.get(i);
            String makeUp = o.getDate("entry")==null?"":compareDate(o.getDate("entry"));
            String house = o.getByte("household")==null?"":houseHold(o.getByte("household"));
            o.put("startDate",o.getDate("date3")==null?"":DateUtil.format(o.getDate("date3"),"yyyy-MM-dd"));
            o.put("changeReason","正常参保登记");
            o.put("form","合同制");
            o.put("makeUp",makeUp);
            o.put("entryDate",o.getDate("entry")==null?"":DateUtil.format(o.getDate("entry"),"yyyy-MM-dd"));
            o.put("house",house);
        }
        XlsUtil.write(response.getOutputStream(),"","员工新增社保单", SchemeDefined.SCHEME_exportSocial1, data);
    }

    //导出停保社保单
    public static void exportSocial2(Connection conn, HttpServletResponse response) throws IOException {
        //文件名
        String fileName ="员工停保社保单";
        fileName = new String(fileName.getBytes(), "iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xls\"");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("status3","=",4);

        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);
        //获取本月最后一天
        Date date = DateUtil.getLastDayofMonth(new Date());
        String lastDay = DateUtil.format(date,"yyyy-MM-dd");//本月最后一天

        JSONArray data = JSONArray.parseArray(JSONObject.toJSONString(insurances));
        for(int i=0; i<data.size(); i++){
            JSONObject o = (JSONObject) data.get(i);
            String changeReason = o.getByte("reason")==null?"":chageReason(o.getByte("reason"));
            o.put("changeDate",lastDay);
            o.put("changeReason",changeReason);
        }
        XlsUtil.write(response.getOutputStream(),"","员工停保社保单",SchemeDefined.SCHEME_exportSocial2, data);

    }

    //导出续保医保单
    public static void exportMedicare1(Connection conn, HttpServletResponse response) throws IOException {
        //文件名
        String fileName ="员工续保医保单";
        fileName = new String(fileName.getBytes(), "iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xls\"");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("status1","=",2);

        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);

        JSONArray data = JSONArray.parseArray(JSONObject.toJSONString(insurances));
        for(int i=0; i<data.size(); i++){
            JSONObject o = (JSONObject) data.get(i);
            o.put("insurance","医疗、大病、生育");
            o.put("startDate",o.getDate("date1")==null?"":DateUtil.format(o.getDate("date1"),"yyyy-MM-dd"));
        }
        XlsUtil.write(response.getOutputStream(),"","员工续保医保单",SchemeDefined.SCHEME_exportMedicare1, data);
    }

    //导出停保医保单
    public static void exportMedicare2(Connection conn, HttpServletResponse response) throws IOException {
        //文件名
        String fileName ="员工停保医保单";
        fileName = new String(fileName.getBytes(), "iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xls\"");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("status1","=",4);

        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);

        //获取本月最后一天
        Date date = DateUtil.getLastDayofMonth(new Date());
        String lastDay = DateUtil.format(date,"yyyy-MM-dd");

        JSONArray data = JSONArray.parseArray(JSONObject.toJSONString(insurances));
        for(int i=0; i<data.size(); i++){
            JSONObject o = (JSONObject) data.get(i);
            String changeReason = o.getByte("reason")==null?"":chageReason(o.getByte("reason"));
            o.put("changeDate",lastDay);
            o.put("changeReason",changeReason);
        }
        XlsUtil.write(response.getOutputStream(),"","员工停保医保单",SchemeDefined.SCHEME_exportMedicare2, data);

    }

    //导出公积金
//    public static void exportFund(Connection conn, HttpServletResponse response, File file) {
//        response.setContentType("APPLICATION/OCTET-STREAM");
//        response.setHeader("Content-Disposition", "attachment; filename=exportFund.xls");
//
//        Workbook book = null;
//        //公积金的变更是什么状态
//        QueryParameter parameter = new QueryParameter();
//        parameter.addCondition("status2","=",4);
//        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
//        String rows = JSONObject.toJSONString(result.rows);
//        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);
//        try {
//            //获取模板
//            book = Workbook.getWorkbook(file);
//            // jxl.Workbook 对象是只读的，所以如果要修改Excel，需要创建一个可读的副本，副本指向原Excel文件（即下面的new File(excelpath)）
//            WritableWorkbook workbook = Workbook.createWorkbook(response.getOutputStream(),book);
//            WritableSheet sheet = workbook.getSheet(0);//获取第一个sheet
//
//            int index = 6;
//            float per=0;
//            for(ViewInsurance v:insurances){
//                EnsureSetting setting = (EnsureSetting) SettingDao.get(conn,v.getEid()).data;
//                if(setting!=null){
//                    per = setting.getFundPer()/100;
//                }
//                sheet.addCell(new jxl.write.Number(0, index, index-5));//序号
//                sheet.addCell(new Label(1, index, v.getName()));//职工姓名
//                sheet.addCell(new Label(2, index, v.getCardId()));//证件号
//                sheet.addCell(new Label(3, index, v.getDate2()==null?"":DateUtil.format(v.getDate2(),"yyyy-MM-dd")));//起缴时间
//                sheet.addCell(new jxl.write.Number(4, index, v.getBase2()));//工资基数
//                sheet.addCell(new jxl.write.Number(5, index, per));//单位比例
//                sheet.addCell(new jxl.write.Number(6, index, per));//个人比例
//                sheet.addCell(new jxl.write.Number(7, index, v.getBase2()*per));//个人缴存标准
//                sheet.addCell(new jxl.write.Number(8, index, (v.getBase2()*per)*2));//缴存合计
//                sheet.addCell(new Label(10, index, v.getPhone()));//手机号码
//                index++;
//            }
//            workbook.write();
//            workbook.close();
//            book.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (BiffException e) {
//            e.printStackTrace();
//        } catch (RowsExceededException e) {
//            e.printStackTrace();
//        } catch (WriteException e) {
//            e.printStackTrace();
//        }
//    }

    //导出招行
    public static void exportBank1(Connection conn, long sid, HttpServletResponse response, String file) throws UnsupportedEncodingException {
        //文件名
        String fileName ="招商银行工资报表";
        fileName = new String(fileName.getBytes(), "iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xls\"");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid", "=", sid);

        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewDetail1> details = JSONArray.parseArray(rows, ViewDetail1.class);
        String month = details.get(0).getMonth()==null?"":(DateUtil.format(details.get(0).getMonth(),"yyyy-MM-dd").split("-"))[1];
        String eids = "";
        for(ViewDetail1 d:details){
            eids+=(d.getEid()+",");
        }
        eids = eids.substring(0,eids.length()-1);
        //批量获取员工的工资卡
        QueryParameter p1 =new QueryParameter();
        p1.addCondition("eid","in",eids);
        List<PayCard> payCards = (List<PayCard>) PayCardDao.getList(conn,p1).rows;
        JSONArray data = JSONArray.parseArray(JSONObject.toJSONString(details));

        for(int i=0; i<data.size(); i++){
            JSONObject o = (JSONObject) data.get(i);
            o.put("comments",month+"月工资");
            PayCard p = CollectionUtil.getElement(payCards,"eid",o.getLong("eid"));
            if(p!=null){
                o.put("cardNo",p.getCardNo());
                o.put("bank1",p.getBank1());
                o.put("bankNo",p.getBankNo());
            }else {
                o.put("cardNo","");
                o.put("bank1","");
                o.put("bankNo","");
            }
        }
        Scheme[] schemes = {SchemeDefined.SCHEME_BANK_CMCC1, SchemeDefined.SCHEME_BANK_CMCC2};
        JSONArray[] datas = {data, data};
        try {
           utills.excel.XlsUtil.write(response.getOutputStream(),file,schemes, datas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //导出农行
    public static void exportBank2(Connection conn, long sid, HttpServletResponse response, String file) throws UnsupportedEncodingException {
        //文件名
        String fileName ="农业银行工资报表";
        fileName = new String(fileName.getBytes(), "iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xls\"");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid", "=", sid);

        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewDetail1> viewDetail1s = JSONArray.parseArray(rows, ViewDetail1.class);
        String eids = "";
        for(ViewDetail1 d:viewDetail1s){
            eids+=(d.getEid()+",");
        }
        eids = eids.substring(0,eids.length()-1);
        //批量获取员工的工资卡
        QueryParameter p1 =new QueryParameter();
        p1.addCondition("eid","in",eids);
        List<PayCard> payCards = (List<PayCard>) PayCardDao.getList(conn,p1).rows;
        JSONArray data = JSONArray.parseArray(JSONObject.toJSONString(viewDetail1s));
        String month = viewDetail1s.get(0).getMonth()==null?"":DateUtil.format(viewDetail1s.get(0).getMonth(),"yyyy.MM");

        for(int i=0; i<data.size(); i++){
            JSONObject o = (JSONObject) data.get(i);
            o.put("comments",month+"工资");
            PayCard p = CollectionUtil.getElement(payCards,"eid",o.getLong("eid"));
            if(p!=null){
                o.put("cardNo",p.getCardNo());
                o.put("bank1",p.getBank1());
                o.put("bankNo",p.getBankNo());
                o.put("bank2",p.getBank2());
                }else {
                o.put("cardNo","");
                o.put("bank1","");
                o.put("bankNo","");
                o.put("bank2","");
            }
        }
        Scheme[] schemes = {SchemeDefined.SCHEME_BANK_AG1, SchemeDefined.SCHEME_BANK_AG1};
        JSONArray[] datas = {data, data};
        try {
            utills.excel.XlsUtil.write(response.getOutputStream(),file,schemes, datas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //导出浦发
    public static void exportBank3(Connection conn, long sid, HttpServletResponse response) throws IOException {
        //文件名
        String fileName ="浦发银行工资报表";
        fileName = new String(fileName.getBytes(), "iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xls\"");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);

        List<ViewDetail1> details = JSONArray.parseArray(rows, ViewDetail1.class);
        String month = details.get(0).getMonth()==null?"":(DateUtil.format(details.get(0).getMonth(),"yyyy-MM-dd").split("-"))[1];
        String eids = "";
        for(ViewDetail1 d:details){
            eids+=(d.getEid()+",");
        }
        eids = eids.substring(0,eids.length()-1);
        //批量获取员工的工资卡
        QueryParameter p1 =new QueryParameter();
        p1.addCondition("eid","in",eids);
        List<PayCard> payCards = (List<PayCard>) PayCardDao.getList(conn,p1).rows;
        JSONArray data = JSONArray.parseArray(JSONObject.toJSONString(details));

        for(int i=0; i<data.size(); i++){
            JSONObject o = (JSONObject) data.get(i);
            o.put("comments",month+"月工资");
            o.put("code","");
            PayCard p = CollectionUtil.getElement(payCards,"eid",o.getLong("eid"));
            if(p!=null){
                o.put("cardNo",p.getCardNo());
            }else {
                o.put("cardNo","");
            }
        }
        XlsUtil.write(response.getOutputStream(),"","浦发银行",SchemeDefined.SCHEME_BANK_SPDB, data);

    }
    //导出交通
    public static void exportBank4(Connection conn, long sid, HttpServletResponse response) throws IOException {
        //文件名
        String fileName ="交通银行工资报表";
        fileName = new String(fileName.getBytes(), "iso-8859-1");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xls\"");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewDetail1> details = JSONArray.parseArray(rows, ViewDetail1.class);

        String month = details.get(0).getMonth()==null?"":(DateUtil.format(details.get(0).getMonth(),"yyyy-MM-dd").split("-"))[1];
        String eids = "";
        for(ViewDetail1 d:details){
            eids+=(d.getEid()+",");
        }
        eids = eids.substring(0,eids.length()-1);
        //批量获取员工的工资卡
        QueryParameter p1 =new QueryParameter();
        p1.addCondition("eid","in",eids);
        List<PayCard> payCards = (List<PayCard>) PayCardDao.getList(conn,p1).rows;
        JSONArray data = JSONArray.parseArray(JSONObject.toJSONString(details));

        for(int i=0; i<data.size(); i++){
            JSONObject o = (JSONObject) data.get(i);
            o.put("comments",month+"月工资");
            PayCard p = CollectionUtil.getElement(payCards,"eid",o.getLong("eid"));
            if(p!=null){
                o.put("cardNo",p.getCardNo());
            }else {
                o.put("cardNo","");
            }
        }
        XlsUtil.write(response.getOutputStream(),"","交通银行",SchemeDefined.SCHEME_BANK_BOCOM, data);
    }

    //读累计扣除数
    public static List<ViewDeduct> readDeduct(JSONArray[] data) {
        List<ViewDeduct> deductList = new ArrayList<>();
        JSONArray a1=data[1];//子女教育支出数据
        JSONArray a2=data[2];//继续教育数据
        JSONArray a3=data[3];//住房贷款数据
        JSONArray a4=data[4];//住房租金数据
        JSONArray a5=data[5];//赡养老人数据

        for(int i =0;i<a1.size();i++){//获取子女教育累计扣除
            JSONObject o = (JSONObject) a1.get(i);
            String per = o.getString("per");
            Number num = null;
            try {
                num = (NumberFormat.getInstance().parse(per));
            } catch (ParseException e) {
                e.printStackTrace();
            }
          float deduct = Deduct.DEDUCT1*(num.intValue()/100);//换算成扣除金额
          ViewDeduct d = CollectionUtil.getElement(deductList,"cardId",o.getString("cardId"));
          if(d == null){//不在
              d = new ViewDeduct();
              d.setCardId(o.getString("cardId"));
              d.setName(o.getString("name"));
              d.setDeduct1(deduct);
              deductList.add(d);
          }else{
              d.setDeduct1(d.getDeduct1()+deduct);
          }
        }
        for(int i =0;i<a2.size();i++){//获取继续教育累计扣除
            JSONObject o = (JSONObject) a2.get(i);
            ViewDeduct d = CollectionUtil.getElement(deductList,"cardId",o.getString("cardId"));
            if(d == null){//不在
                d = new ViewDeduct();
                d.setCardId(o.getString("cardId"));
                d.setName(o.getString("name"));
                d.setDeduct3(Deduct.DEDUCT3);
                deductList.add(d);
            }else{
                d.setDeduct3(d.getDeduct3()+Deduct.DEDUCT3);
            }
        }
        for(int i =0;i<a3.size();i++){//读住房贷款利息
            JSONObject o = (JSONObject) a3.get(i);
            ViewDeduct d = CollectionUtil.getElement(deductList,"cardId",o.getString("cardId"));
            if(d == null){//不在
                d = new ViewDeduct();
                d.setCardId(o.getString("cardId"));
                d.setName(o.getString("name"));
                d.setDeduct5(Deduct.DEDUCT5);
                deductList.add(d);
            }else{
                d.setDeduct5(d.getDeduct5()+Deduct.DEDUCT5);
            }
        }
        for(int i =0;i<a4.size();i++){//读租房租金
            JSONObject o = (JSONObject) a4.get(i);
            ViewDeduct d = CollectionUtil.getElement(deductList,"cardId",o.getString("cardId"));
            if(d == null){//不在
                d = new ViewDeduct();
                d.setCardId(o.getString("cardId"));
                d.setName(o.getString("name"));
                d.setDeduct6(Deduct.DEDUCT6);
                deductList.add(d);
            }else{
                d.setDeduct6(d.getDeduct6()+Deduct.DEDUCT6);
            }
        }
        for(int i =0;i<a5.size();i++){//赡养老人
            JSONObject o = (JSONObject) a5.get(i);
            float deduct= o.getFloat("per");
            ViewDeduct d = CollectionUtil.getElement(deductList,"cardId",o.getString("cardId"));
            if(d == null){//不在
                d = new ViewDeduct();
                d.setCardId(o.getString("cardId"));
                d.setName(o.getString("name"));
                d.setDeduct2(deduct);
                deductList.add(d);
            }else{
                d.setDeduct2(d.getDeduct2()+deduct);
            }
        }
        return deductList;
    }

    //转换户口性质
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

}
