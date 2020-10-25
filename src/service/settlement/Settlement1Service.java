package service.settlement;

import bean.admin.Account;
import bean.contract.ViewContractCooperation;
import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.employee.PayCard;
import bean.employee.ViewEmployee;
import bean.insurance.ViewInsurance;
import bean.log.Log;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.LogDao;
import dao.admin.AccountDao;
import dao.contract.ContractDao;
import dao.employee.EmployeeDao;
import dao.employee.PayCardDao;
import dao.employee.SettingDao;
import dao.insurance.InsuranceDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Detail3Dao;
import dao.settlement.Settlement1Dao;
import database.*;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import utills.Calculate;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static utills.Calculate.calculateMedicare;
import static utills.Calculate.calculateSocial;


public class Settlement1Service {
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    static String date = df.format(new Date(System.currentTimeMillis()));
    static  Date time = Date.valueOf(date);//获取当前时间

    //获取普通结算单列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement1Dao.getList(conn,param);
    }

    /**
     * 插入结算单，插入后根据返回的id，自动生成明细
     * @param conn
     * @param settlement
     * @param type 0 不自动生成明细  1 自动生成明细
     * @return
     */
    public static DaoUpdateResult insert(Connection conn, Settlement1 settlement, byte type) {
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult result = Settlement1Dao.insert(conn,settlement);

        if(result.success&&type == 1){//自动生成结算单明细
            long sid = (Long) result.extra;
            long cid = settlement.getCid();
            long did = settlement.getDid();
            //根据条件找到派遣到该单位的员工列表，条件有cid，did，类型为外派员工，用工性质不是小时工，在职
            QueryParameter parameter = new QueryParameter();
            parameter.addCondition("cid","=",cid);
            parameter.addCondition("did","=",did);
            parameter.addCondition("type","=",1);
            parameter.addCondition("category","!=",3);
            parameter.addCondition("status","=",0);
            List<ViewEmployee> employeeList = JSONArray.parseArray(JSONObject.toJSONString(EmployeeDao.getList(conn,parameter).rows),ViewEmployee.class);
            List<Detail1> detail1List = new ArrayList<Detail1>();
            for(int i = 0;i<employeeList.size();i++){//封装明细信息,添加进集合
                Detail1 detail1 = new Detail1();
                detail1.setSid(sid);
                detail1.setEid(employeeList.get(i).getId());
                detail1List.add(i,detail1);
            }
            DaoUpdateResult result1 = Detail1Dao.importDetails(conn,detail1List);
            //事务处理
            if(result1.success){
                ConnUtil.commit(conn);
                return result;
            }else {//回滚
                ConnUtil.rollback(conn);
                result1.msg = "明细插入失败";
                return result1;
            }
        }else {//不自动生成明细，提交事务
            ConnUtil.commit(conn);
            return result;
        }
    }

    //删除结算单
    public static DaoUpdateResult delete(Connection conn, Long id) {
        return Settlement1Dao.delete(conn,id);
    }


    public static DaoUpdateResult commit(Connection conn, long id, long aid) {
        /**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement1Dao.commit(conn, id);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "提交";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 0);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                System.out.println(log);
                //插入log信息
                LogDao.insert(conn,log);
            }
        }
        return result;
    }

    public static DaoUpdateResult check(Connection conn, long id, long aid, byte status) {
        /**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement1Dao.check(conn,id,status);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = null;
                if(status == 2){//判断是几审
                    content = "一审";
                }else if(status == 3){
                    content = "二审";
                }else {
                    content = "终审";
                }
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 0);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                System.out.println(log);
                //插入log信息
                LogDao.insert(conn,log);
            }
        }
        return result;
    }

    public static DaoUpdateResult reset(Connection conn, long id,long aid) {
        /**流程
         *1、修改结算单状态为重置
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement1Dao.reset(conn, id);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "重置";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 0);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                System.out.println(log);
                //插入log信息
                LogDao.insert(conn,log);
            }
        }
        return result;
    }

    public static DaoUpdateResult deduct(Connection conn, long id,long aid) {
        /**流程
         *1、修改结算单状态为扣款
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement1Dao.deduct(conn, id);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "扣款";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 0);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                System.out.println(log);
                //插入log信息
                LogDao.insert(conn,log);
            }
        }
        return result;
    }

    public static DaoUpdateResult confirm(Connection conn, long id, long aid) {
        /**流程
         *1、修改结算单状态为发放
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement1Dao.confirm(conn, id);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "发放";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 0);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                System.out.println(log);
                //插入log信息
                LogDao.insert(conn,log);
            }
        }
        return result;
    }
    //获取该结算单的所有日志
    public static DaoQueryListResult getLogs(Connection conn, long id, QueryParameter parameter) {
        parameter.conditions.add("type","=",0);
        return LogDao.getList(conn,id,parameter);
    }

    public static String exportBank(Connection conn, long id,String bank) {
        return null;
    }

    public static DaoUpdateResult saveAs(Connection conn, long id, Date month) {
        /**流程
         * 1、查询出结算单，修改结算月份
         * 2、插入结算单，返回主键id
         * 3、根据原来结算单id查询出所有的结算单明细
         * 4、修改结算单明细中的结算单id
         * 5、批量结算单明细
         */
        Settlement1 settlement1 = (Settlement1) Settlement1Dao.get(conn, id).data;
        settlement1.setMonth(month);

        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);
        //插入结算单
        DaoUpdateResult result = Settlement1Dao.insert(conn, settlement1);
        //返回结算单id
        long sid = (long)result.extra;

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",id);
        //根据复制的结算单id查询出所有的结算单明细
        List<Detail1> detail1List = (List<Detail1>) Detail1Dao.getList(conn,parameter).rows;
        for(Detail1 detail1 :detail1List){
            //重新赋结算单id
           detail1.setSid(sid);
        }
        //重新插入数据库
        DaoUpdateResult result1 = Detail1Dao.importDetails(conn,detail1List);

        if(result.success&&result1.success){//事务
            ConnUtil.commit(conn);
            return result;
        }else {//回滚
            ConnUtil.rollback(conn);
            result1.msg = "另存为失败";
            return result1;
        }
    }


    //保存结算单；实质是计算结算单并且修改
    public static DaoUpdateResult saveSettlement(Connection conn, long sid) {

        //结算单
        Settlement1 settlement1 = (Settlement1) Settlement1Dao.get(conn,sid).data;
        //获取合作客户的合同视图
        ViewContractCooperation viewContractCooperation = (ViewContractCooperation) ContractDao.getViewContractCoop(conn,settlement1.getCcid()).data;
        QueryParameter parm = new QueryParameter();
        parm.addCondition("sid","=",sid);
        //该结算单中的所有明细
        List<ViewDetail1> viewDetail1s = (List<ViewDetail1>) Detail1Dao.getList(conn,parm).rows;

        //计算结算单
        Settlement1 settlement11 = Calculate.calculateSettlement1(settlement1,viewContractCooperation,viewDetail1s);

        return Settlement1Dao.update(conn,settlement11);
    }

    //读取基数
    public static String readBase(String start, String end, String[] eids, long sid, Connection conn) {
        float baseM=0;//医保基数
        float baseS=0;//社保基数
        List<JSONObject> data = new ArrayList<>();
        String result ;
        JSONObject json = new JSONObject();
        for(int i = 0;i<eids.length;i++){
            //获取员工信息
            QueryConditions conditions = new QueryConditions();
            conditions.add("id","=",Long.parseLong(eids[i]));
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;

            JSONObject object = new JSONObject();
            //读取员工社保设置基数
            EnsureSetting setting = (EnsureSetting) SettingDao.get(conn, Long.parseLong(eids[i])).data;
            if(setting==null){//员工社保设置异常
                json.put("success",false);
                json.put("msg","请完善员工"+employee.getName()+"的社保设置");
                result = json.toJSONString();
                return result;
            }
            byte settingM = setting.getSettingM();//医保设置
            byte settingS = setting.getSettingS();//社保设置
            switch (settingM){
                case 0://最低基数
                    DaoQueryResult result1 = RuleMedicareDao.get(conn,setting.getCity(), Date.valueOf(start));
                    DaoQueryResult result2 = RuleMedicareDao.get(conn,setting.getCity(), Date.valueOf(end));
                    if(result2.success){//结束月份存在规则
                        RuleMedicare medicare1 = (RuleMedicare) result1.data;
                        RuleMedicare medicare2 = (RuleMedicare) result2.data;
                        if(!result1.success){//起始月份不存在规则
                           baseM =medicare2.getBase();
                        }else {//存在两个规则，则判断规则是否是同一个
                            if(medicare1.getId()!=medicare2.getId()) {//两个规则不相同
                                json.put("success", false);
                                json.put("msg", "请重新确认月份区间");
                                result = json.toJSONString();
                                return result;
                            }
                            baseM = medicare1.getBase();
                        }
                    }else {//两个都不在规则
                        json.put("success",false);
                        //这里应该是今年的医保规则不存在
                        String year = end.split("-")[0];
                        json.put("msg","员工"+employee.getName()+year+"年，所处地市的医保规则不存在,请核对");
                        result = json.toJSONString();
                        return result;
                    }
                    break;
                case 1://实际工资,当前月的实际工资
                    QueryConditions conditions1 = new QueryConditions();
                    conditions1.add("sid","=",sid);
                    conditions1.add("eid","=",Long.parseLong(eids[i]));
                    Detail1 detail1 = (Detail1) Detail1Dao.get(conn,conditions1).data;
                    if(detail1!=null){
                        baseM = detail1.getPayable();
                    }else {
                        baseM = 0;
                    }
                    break;
                case 2://不缴纳
                    baseM = 0;
                    break;
                case 3://自定义基数
                    baseM = setting.getValM();
                    break;
            }
            switch (settingS){
                case 0://最低基数
                    DaoQueryResult result1 = RuleSocialDao.get(conn,setting.getCity(), Date.valueOf(start));
                    DaoQueryResult result2 = RuleSocialDao.get(conn,setting.getCity(), Date.valueOf(end));
                    if(result2.success){//结束月份存在规则
                        RuleSocial social1 = (RuleSocial) result1.data;
                        RuleSocial social2 = (RuleSocial) result2.data;
                        if(!result1.success){//起始月份不存在规则
                            baseS =social2.getBase();
                        }else {//存在两个规则，则判断规则是否是同一个
                            if(social1.getId()!=social2.getId()) {//两个规则不相同
                                json.put("success", false);
                                json.put("msg", "请重新确认月份区间");
                                result = json.toJSONString();
                                return result;
                            }
                            baseS = social1.getBase();
                        }
                    }else {//两个都不在规则
                        json.put("success",false);
                        //这里应该是今年的医保规则不存在
                        String year = end.split("-")[0];
                        json.put("msg","员工"+employee.getName()+year+"年，所处地市的社保规则不存在,请核对");
                        result = json.toJSONString();
                        return result;
                    }
                    break;
                case 1://实际工资，当前月的实际工资
                    QueryConditions conditions2 = new QueryConditions();
                    conditions2.add("sid","=",sid);
                    conditions2.add("eid","=",Long.parseLong(eids[i]));
                    Detail1 detail1 = (Detail1) Detail1Dao.get(conn,conditions2).data;
                    if(detail1!=null){
                        baseS = detail1.getPayable();
                    }else {
                        baseS = 0;
                    }
                    break;
                case 2://不缴纳
                    baseS = 0;
                    break;
                case 3://自定义基数
                    baseS = setting.getValS();
                    break;
            }
            object.put("id",eids[i]);
            object.put("cardId",employee.getCardId());
            object.put("name",employee.getName());
            object.put("baseM",baseM);
            object.put("baseS",baseS);
            data.add(object);
        }
        json.put("success",true);
        json.put("data",data);
        result = json.toJSONString();
        return result;
    }

    //社保补缴
    public static DaoUpdateResult backup(String start, String end, long sid,List<JSONObject> employees, Connection conn) {
        DaoUpdateResult result = new DaoUpdateResult();
        List<Detail1> detail1List = new ArrayList<>();
        for(JSONObject object:employees){
            long eid = object.getLong("eid");
            float baseM = object.getFloat("baseM");
            float baseS = object.getFloat("baseS");

            //该员工的信息
            QueryConditions conditions = new QueryConditions();
            conditions.add("id","=",eid);
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;

            EnsureSetting setting = (EnsureSetting) SettingDao.get(conn,eid).data;//获取员工社保设置
            if(setting==null){//员工设置为空
                result.msg="请先完善"+employee.getName()+"员工的社保设置";
               return result;
            }
            //医保规则
            RuleMedicare medicare = (RuleMedicare)RuleMedicareDao.get(conn,setting.getCity(),Date.valueOf(end)).data;
            //社保规则
            RuleSocial social = (RuleSocial)RuleSocialDao.get(conn,setting.getCity(),Date.valueOf(end)).data;
            if(medicare==null||social==null){//医保规则空或者社保为空
                result.msg="员工"+employee.getName()+"社保或医保所在地的规则为空，请核对";
                return result;
            }

            //新建明细
            Detail1 detail1 = new Detail1();
            detail1.setSid(sid);
            detail1.setEid(eid);
            detail1.setStatus((byte) 1);//补缴

            //根据员工设置计算医保
            detail1 = calculateMedicare(detail1,setting,baseM,medicare);
            //根据员工设置计算社保
            detail1 = calculateSocial(detail1,setting,baseS,social);

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            try {
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                java.util.Date date1 = sdf.parse(start);
                java.util.Date date2 = sdf.parse(end);
                c1.setTime(date1);
                c2.setTime(date2);
                int month1 = c1.get(Calendar.MONTH);
                int month2 = c2.get(Calendar.MONTH);
                int monthInterval = month2 - month1 ;
                for (int i = 0;i<monthInterval;i++){//有多少个月 生成多少个明细
                    detail1List.add(detail1);//添加至集合
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //批量插入补缴明细
        result = Detail1Dao.importDetails(conn,detail1List);
        return result;
    }

    //社保补差
    public static DaoUpdateResult makeup(Connection conn, String[] eids, String start, String end, long sid) {
        DaoUpdateResult result = new DaoUpdateResult();
        List<Detail1> detail1List = new ArrayList<>();
        for(String eid:eids){
            //该员工的信息
            QueryConditions conditions = new QueryConditions();
            conditions.add("id","=",Long.parseLong(eid));
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;
            //获取员工社保设置
            EnsureSetting setting = (EnsureSetting) SettingDao.get(conn,Long.parseLong(eid)).data;
            if(setting==null){//员工设置为空
                result.msg="请先完善"+employee.getName()+"员工的社保设置";
                return result;
            }
            //获取目前最新医保规则
            RuleMedicare medicare = (RuleMedicare) RuleMedicareDao.getLast(conn,setting.getCity()).data;
            //获取目前最新社保规则
            RuleSocial social = (RuleSocial) RuleSocialDao.getLast(conn,setting.getCity()).data;
            if(medicare==null||social==null){//医保规则空或者社保为空
                result.msg="员工"+employee.getName()+"社保或医保所在地的规则为空，请核对";
                return result;
            }

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            try {
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                java.util.Date date1 = sdf.parse(start);
                java.util.Date date2 = sdf.parse(end);
                c1.setTime(date1);
                c2.setTime(date2);
                String year = start.split("-")[0];//年
                int month1 = c1.get(Calendar.MONTH);
                int month2 = c2.get(Calendar.MONTH);
                for(int i=month1;i<=month2;i++){
                    String month =year+"-"+(month1+1)+"-"+"01";
                    //获取该员工当月分的结算单明细
                    QueryConditions conditions1 = new QueryConditions();
                    conditions1.add("eid","=",eid);
                    conditions1.add("month","=",month);
                    ViewDetail1 detail1 = (ViewDetail1) Detail1Dao.get(conn,conditions).data;
                    if(detail1==null){
                        result.msg = "该员工"+employee.getName()+","+month+"的结算单明细不存在,请核对";
                        return result;
                    }

                    //重新生成一个明细用于计算医保和社保
                    Detail1 detail2 = new Detail1();
                    //计算医保相关
                    int SettingM = setting.getSettingM();//员工医保设置
                    float baseM = 0;
                    switch (SettingM){
                        case 0://最低标准
                            baseM = medicare.getBase();
                             break;
                        case 1://不缴纳
                            baseM = 0;
                             break;
                        case 2://自定义基数
                            baseM = setting.getValM();
                           break;
                    }
                    detail2 = calculateMedicare(detail2,setting,baseM,medicare);

                    //计算社保相关
                    int SettingS = setting.getSettingS();//员工社保设置
                    float baseS = 0;
                    switch (SettingS){
                        case 0://最低标准
                            baseS =social.getBase();
                            break;
                        case 1://不缴纳
                            baseS =social.getBase();
                            break;
                        case 2://自定义工资
                            baseS=setting.getValS();
                            break;
                    }
                    detail2 = calculateSocial(detail2,setting,baseS,social);

                    //计算补差的社保医保,并重新赋值
                    detail2.setMedicare2(detail2.getMedicare1()-detail1.getMedicare1());
                    detail2.setMedicare2(detail2.getMedicare2() -detail1.getMedicare2());
                    detail2.setBirth(detail2.getBirth()-detail1.getBirth());
                    detail2.setDisease1(detail2.getDisease1() - detail1.getDisease1());
                    detail2.setDisease2(detail2.getDisease2() - detail1.getDisease2());
                    detail2.setPension1(detail2.getPension1() - detail1.getPension1());
                    detail2.setPension2(detail2.getPension2() - detail1.getPension2());
                    detail2.setUnemployment1(detail2.getUnemployment1() - detail1.getUnemployment1());
                    detail2.setUnemployment2(detail2.getUnemployment2() - detail1.getUnemployment2());
                    detail2.setInjury(detail2.getInjury() - detail1.getInjury());
                    detail2.setEid(Long.parseLong(eid));
                    detail2.setSid(sid);
                    detail2.setStatus((byte) 2);

                    detail1List.add(detail2);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //批量导入
        result = Detail1Dao.importDetails(conn,detail1List);
        return result;
    }

    //导出招行
    public static void exportBank1(Connection conn, long sid, HttpServletResponse response, File file) {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=bank1.xls");

        Workbook book = null;
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid", "=", sid);

        DaoQueryListResult result = Detail1Dao.getList(conn,parameter);
        String rows = JSONObject.toJSONString(result.rows);
        List<ViewDetail1> viewDetail1s = JSONArray.parseArray(rows, ViewDetail1.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
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
                sheet2.addCell(new Label(3, index, "大正月"+sdf.format(v.getMonth())+"工资"));//附言
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
        ConnUtil.closeConnection(conn);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
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
                    sheet1.addCell(new Label(7, index, sdf.format(v.getMonth()) + "工资"));

                    //序号	卡号	姓名	金额	备注
                    sheet2.addCell(new jxl.write.Number(0, index, index ));
                    sheet2.addCell(new Label(1, index, card.getCardNo()));
                    sheet2.addCell(new jxl.write.Number(3, index, v.getPaid()));
                    sheet2.addCell(new Label(4, index, sdf.format(v.getMonth()) + "工资"));
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
        ConnUtil.closeConnection(conn);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String month = (sdf.format(details.get(0).getMonth()).split("-"))[1];
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
        ConnUtil.closeConnection(conn);
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
        String month = sdf.format(details.get(0).getMonth());

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
        ConnUtil.closeConnection(conn);
    }
}
