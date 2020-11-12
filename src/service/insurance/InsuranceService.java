package service.insurance;

import bean.employee.EnsureSetting;
import bean.insurance.Insurance;
import bean.insurance.ViewInsurance;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.employee.SettingDao;
import dao.insurance.InsuranceDao;
import database.*;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class InsuranceService {
    //批量插入
    public static DaoUpdateResult insertBatch(Connection conn, List<Insurance> insurances) {
       return InsuranceDao.insertBatch(conn,insurances);
    }

    //导出新增社保单
    public static void exportSocial1(Connection conn, HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=exportSocial1.xls");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("status3","=",1);

        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                sheet1.addCell(new Label(3, index, v.getDate3()==null?"":sdf.format(v.getDate3())));
                sheet1.addCell(new jxl.write.Number(4, index, v.getBase3()));
                sheet1.addCell(new Label(5, index, "正常参保登记"));
                sheet1.addCell(new Label(6, index, "合同制"));
                sheet1.addCell(new Label(7, index, msg));
                sheet1.addCell(new Label(8, index, v.getEntry()==null?"":sdf.format(v.getEntry())));
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
        ConnUtil.closeConnection(conn);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        String lastDay = sdf.format(cale.getTime());//本月最后一天

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
        ConnUtil.closeConnection(conn);
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

    //导出续保医保单
    public static void exportMedicare1(Connection conn, HttpServletResponse response) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=exportMedicare1.xls");

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("status1","=",2);

        DaoQueryListResult result = InsuranceDao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewInsurance> insurances = JSONArray.parseArray(rows, ViewInsurance.class);

        //获取本月最后一天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
                sheet1.addCell(new Label(5, index, insurance.getDate1()==null?"":sdf.format(insurance.getDate1())));
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        String lastDay = sdf.format(cale.getTime());//本月最后一天

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
        ConnUtil.closeConnection(conn);
    }

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                sheet.addCell(new Label(3, index, v.getDate2()==null?"":sdf.format(v.getDate2())));//起缴时间
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
        ConnUtil.closeConnection(conn);
    }

    /**
     * 校对医保参保单
     * @param conn
     * @param data 校对的数据
     * @return
     */
    public static DaoUpdateResult checkMedicare(Connection conn, List<JSONObject> data){
      /*
        * 校对流程
        * （1）获取导入的名册data1（参数传递）
        * （2）获取系统当前的名册data2
        * （3）判断data2中“新增”状态的是否也存在于data1中，若存在则将其状态置为“在保”
        * （4）判断data2中“拟停”状态的是否也存在于data1中，若不存在则将其状态置为“停保”
        * （5）判断data2中“在保”状态的是否也存在于data1中，若不存在则将其状态置为“异常”
        * */
        HashMap<String, String> data1 = new HashMap<>();
        for(JSONObject o:data){//k:身份证号，v:个人代码
            data1.put(o.getString("cardId"),o.getString("code"));
        }

        QueryParameter param = new QueryParameter();
        List<ViewInsurance> data2 = (List<ViewInsurance>) InsuranceDao.getList(conn,param).rows;

        List<ViewInsurance> data3 = new ArrayList<>();//需要修改的数据
        for(ViewInsurance insurance:data2){
            byte status = insurance.getStatus1();//该员工的医保状态
            if(status == Insurance.STATUS_APPENDING){//新增
                String code = data1.get(insurance.getCardId());
                if(code != null){
                    insurance.setCode1(code);
                    insurance.setStatus1(Insurance.STATUS_NORMAL);//设置为在保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_STOPING){//拟停
                String code = data1.get(insurance.getCardId());
                if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                    insurance.setStatus1(Insurance.STATUS_STOPED);//设置为停保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_NORMAL){//在保
                String code = data1.get(insurance.getCardId());
                if(code == null){//校对名单中不存在
                    insurance.setStatus1(Insurance.STATUS_ERROR);//设置为异常
                    data3.add(insurance);
                }
            }
        }
        //批量修改
        DaoUpdateResult result = InsuranceDao.updateBatch(conn,data3);
        return result;
    }


    /**
     * 校对社保参保单
     * @param conn
     * @param data 校对的数据
     * @param type 社保类型 0 养老 1 失业 2 工伤
     * @return
     */
    public static DaoUpdateResult checkSocial(Connection conn, List<JSONObject> data,byte type) {
        /*
        * 校对流程
        * （1）获取导入的名册data1（参数传递）
        * （2）获取系统当前的名册data2
        * （3）判断是校对养老，失业还是工伤
        * （4）判断data2中“新增”状态的是否也存在于data1中，若存在则将其状态置为“在保”
        * （5）判断data2中“拟停”状态的是否也存在于data1中，若不存在则将其状态置为“停保”
        * （6）判断data2中“在保”状态的是否也存在于data1中，若不存在则将其状态置为“异常”
        * */
        HashMap<String, String> data1 = new HashMap<>();
        for(JSONObject o:data){//k:身份证号，v:个人代码
            data1.put(o.getString("cardId"),o.getString("code"));
        }

        QueryParameter param = new QueryParameter();
        List<ViewInsurance> data2 = (List<ViewInsurance>) InsuranceDao.getList(conn,param).rows;

        List<ViewInsurance> data3 = new ArrayList<>();//需要修改的数据
        byte status;//医保状态
        for(ViewInsurance insurance:data2){
            switch (type){
                case 0://校对养老参保单
                    status = insurance.getStatus3();
                    if(status == Insurance.STATUS_APPENDING){//新增
                        String code = data1.get(insurance.getCardId());
                        if(code != null){
                            insurance.setCode3(code);
                            insurance.setStatus3(Insurance.STATUS_NORMAL);//设置为在保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_STOPING){//拟停
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                            insurance.setStatus3(Insurance.STATUS_STOPED);//设置为停保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_NORMAL){//在保
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//校对名单中不存在
                            insurance.setStatus3(Insurance.STATUS_ERROR);//设置为异常
                            data3.add(insurance);
                        }
                    }
                    break;
                case 1://校对失业参保单
                    status = insurance.getStatus4();
                    if(status == Insurance.STATUS_APPENDING){//新增
                        String code = data1.get(insurance.getCardId());
                        if(code != null){
                            insurance.setCode3(code);
                            insurance.setStatus4(Insurance.STATUS_NORMAL);//设置为在保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_STOPING){//拟停
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                            insurance.setStatus4(Insurance.STATUS_STOPED);//设置为停保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_NORMAL){//在保
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//校对名单中不存在
                            insurance.setStatus3(Insurance.STATUS_ERROR);//设置为异常
                            data3.add(insurance);
                        }
                    }
                    break;
                case 2://校对工伤参保单
                    status = insurance.getStatus5();
                    if(status == Insurance.STATUS_APPENDING){//新增
                        String code = data1.get(insurance.getCardId());
                        if(code != null){
                            insurance.setCode3(code);
                            insurance.setStatus5(Insurance.STATUS_NORMAL);//设置为在保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_STOPING){//拟停
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                            insurance.setStatus5(Insurance.STATUS_STOPED);//设置为停保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_NORMAL){//在保
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//校对名单中不存在
                            insurance.setStatus3(Insurance.STATUS_ERROR);//设置为异常
                            data3.add(insurance);
                        }
                    }
                    break;
            }
        }
        //批量修改
        DaoUpdateResult result = InsuranceDao.updateBatch(conn,data3);
        return result;
    }

    /**
     * 校对公积金参保单
     * @param conn
     * @param data 校对的数据
     * @return
     */
    public static DaoUpdateResult checkFund(Connection conn, List<JSONObject> data){
      /*
        * 校对流程
        * （1）获取导入的名册data1（参数传递）
        * （2）获取系统当前的名册data2
        * （3）判断data2中“新增”状态的是否也存在于data1中，若存在则将其状态置为“在保”
        * （4）判断data2中“拟停”状态的是否也存在于data1中，若不存在则将其状态置为“停保”
        * （5）判断data2中“在保”状态的是否也存在于data1中，若不存在则将其状态置为“异常”
        * */
        HashMap<String, String> data1 = new HashMap<>();
        for(JSONObject o:data){//k:身份证号，v:个人代码
            data1.put(o.getString("cardId"),o.getString("code"));
        }

        QueryParameter param = new QueryParameter();
        List<ViewInsurance> data2 = (List<ViewInsurance>) InsuranceDao.getList(conn,param).rows;

        List<ViewInsurance> data3 = new ArrayList<>();//需要修改的数据
        for(ViewInsurance insurance:data2){
            byte status = insurance.getStatus2();//该员工的公积金状态
            if(status == Insurance.STATUS_APPENDING){//新增
                String code = data1.get(insurance.getCardId());
                if(code != null){
                    insurance.setCode1(code);
                    insurance.setStatus2(Insurance.STATUS_NORMAL);//设置为在保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_STOPING){//拟停
                String code = data1.get(insurance.getCardId());
                if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                    insurance.setStatus2(Insurance.STATUS_STOPED);//设置为停保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_NORMAL){//在保
                String code = data1.get(insurance.getCardId());
                if(code == null){//校对名单中不存在
                    insurance.setStatus2(Insurance.STATUS_ERROR);//设置为异常
                    data3.add(insurance);
                }
            }
        }
        //批量修改
        DaoUpdateResult result = InsuranceDao.updateBatch(conn,data3);
        return result;
    }

}
