package service.settlement;

import bean.admin.Account;
import bean.contract.ViewContractCooperation;
import bean.employee.*;
import bean.insurance.ViewInsurance;
import bean.log.Log;
import bean.log.Transaction;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.LogDao;
import dao.admin.AccountDao;
import dao.client.FinanceDao;
import dao.contract.ContractDao;
import dao.employee.DeductDao;
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
import java.util.HashMap;
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
            //根据条件找到派遣到该单位的员工列表，条件有cid，did，类型为外派员工，用工性质根据结算单类型而变，在职
            QueryParameter parameter = new QueryParameter();
            parameter.addCondition("cid","=",cid);
            parameter.addCondition("did","=",did);
            parameter.addCondition("type","=",1);
            if(settlement.getType()==0||settlement.getType()==1){//如果结算单类型为派遣或者外包
                //查询出类别为派遣（1）或者外包（2）的员工
                parameter.addCondition("category","=",settlement.getType()+1);
            }else if (settlement.getType()==2||settlement.getType()==3){////如果结算单类型为代发工资或者代缴社保
                //查询出类别为代发工资（4）或者代缴社保（5）的员工
                parameter.addCondition("category","=",settlement.getType()+2);
            }
            parameter.addCondition("status","=",0);
            List<ViewEmployee> employeeList = JSONArray.parseArray(JSONObject.toJSONString(EmployeeDao.getList(conn,parameter).rows),ViewEmployee.class);
            List<Detail1> details = new ArrayList<>();
            byte status = ((settlement.getFlag()&((byte)1)) == 0)?Detail1.STATUS_MAKEUP:Detail1.STATUS_NORMAL;
            for(int i = 0;i<employeeList.size();i++){//封装明细信息,添加进集合
                Detail1 detail1 = new Detail1();
                detail1.setSid(sid);
                detail1.setEid(employeeList.get(i).getId());
                detail1.setStatus(status);
                details.add(i,detail1);
            }
            DaoUpdateResult result1 = Detail1Dao.importDetails(conn,details);
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

    public static DaoUpdateResult commit(Connection conn, long id, Account account) {
        /**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement1Dao.commit(conn, id);
        if(result.success){//修改成功，插入日志
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "提交";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 0);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                //插入log信息
                LogDao.insert(conn,log);
        }
        return result;
    }

    public static DaoUpdateResult check(Connection conn, long id, byte type,boolean result,String reason,Account user) {
        /**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult res1 = Settlement1Dao.check(conn,id,type,result);
        if(res1.success){//修改成功，插入日志
            //封装log信息
            String operator = user.getNickname()+"("+user.getId()+")";
            String content = (type==0?"初审":"终审")+(result?"通过":("不通过:"+reason));
            Log log = new Log();
            log.setSid(id);
            log.setType((byte) 0);
            log.setOperator(operator);
            log.setTime(time);
            log.setContent(content);
            //插入log信息
            LogDao.insert(conn,log);
        }
        return res1;
    }

    public static DaoUpdateResult reset(Connection conn, long id, Account account) {
        /**流程
         *1、修改结算单状态为重置
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement1Dao.reset(conn, id);
        if(result.success){//修改成功，插入日志
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "重置";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 0);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                //插入log信息
                LogDao.insert(conn,log);

        }
        return result;
    }

    public static DaoUpdateResult deduct(Connection conn, long id, Account account) {
        /**流程
         * 1、修改结算单状态为扣款
         * 2、修改合作客户账户余额
         * 3、增加资金明细
         * 4、插入日志
         */
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        float balance;//资金
        long cid;//合作客户id
        DaoUpdateResult result = Settlement1Dao.deduct(conn, id);//修改结算单为扣款
        DaoUpdateResult result1 = new DaoUpdateResult();//用于存放异常信息

        if(result.success){//修改成功
            //获取结算单
            Settlement1 settlement = (Settlement1) Settlement1Dao.get(conn,id).data;
            balance = -(settlement.getSummary());//获取结算单总额,此步是扣款 所以金额应该是负的
            cid = settlement.getCid();//合作客户id

            if(FinanceDao.existFinance(conn,cid).exist){//判断该合作客户的财务信息是否存在
                //修改合作客户账户余额
                result1 = FinanceDao.arrive(conn,balance,cid);
            }else {
                result1.success = false;
                result1.msg="该合作客户的财务信息不存在，请确认";
                return result1;
            }

            String operator = account.getNickname()+"("+account.getId()+")";
            String content = "扣款";
            //封装资金明细信息
            Transaction transaction = new Transaction();
            transaction.setCid(cid);
            transaction.setTime(time);
            transaction.setMoney(balance);
            transaction.setComments(content);
            //插入资金明细
            DaoUpdateResult result2 = FinanceDao.insertTransactions(conn,transaction);

            //封装log信息
            Log log = new Log();
            log.setSid(id);
            log.setType((byte) 0);
            log.setOperator(operator);
            log.setTime(time);
            log.setContent(content);
            //插入log信息
            DaoUpdateResult result3 =LogDao.insert(conn,log);
            if(result1.success&&result2.success&&result3.success){//事务处理
                ConnUtil.commit(conn);
                return  result;
            }else {
                ConnUtil.rollback(conn);
                result.success = false;
                result.msg = "扣款失败";
                return  result;
            }
        }
        return result;
    }
    //确认发放
    public static DaoUpdateResult confirm(Connection conn, long sid, Account account) {
        /**流程
         *1、修改结算单状态为发放
         * 2、获取结算单明细并且修改员工个税专项扣除中的累计收入，累计已预缴税额，累计减免
         * 3、插入日志
         */
        DaoUpdateResult result = Settlement1Dao.confirm(conn, sid);
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        parameter.addCondition("status","=",0);
        List<Detail1> detailList = (List<Detail1>) Detail1Dao.getList(conn,parameter).rows;
        List<Deduct> deductList = new ArrayList<>();
        for (Detail1 detail1:detailList){
            Deduct deduct = (Deduct) DeductDao.get(conn,detail1.getEid()).data;
            //累计收入=累计收入+当月应发；
            deduct.setIncome(deduct.getIncome()+detail1.getPayable());
            //累计减免=累计减免+5000；
            deduct.setFree(deduct.getFree()+5000);
            //累计已预缴税额=累计已预缴税额+个税
            deduct.setPrepaid(deduct.getPrepaid()+detail1.getTax());
            deductList.add(deduct);
        }
        if(result.success){//修改成功，插入日志
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "发放";
                Log log = new Log();
                log.setSid(sid);
                log.setType((byte) 0);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                //插入log信息
                LogDao.insert(conn,log);
        }
        return result;
    }
    //获取该结算单的所有日志
    public static DaoQueryListResult getLogs(Connection conn, long id, QueryParameter parameter) {
        parameter.conditions.add("type","=",0);
        return LogDao.getList(conn,id,parameter);
    }
    //另存为
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
        List<Detail1> details = (List<Detail1>) Detail1Dao.getList(conn,parameter).rows;
        for(Detail1 detail1 :details){
            //重新赋结算单id
           detail1.setSid(sid);
        }
        //重新插入数据库
        DaoUpdateResult result1 = Detail1Dao.importDetails(conn,details);

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
        DaoUpdateResult result1;
        ConnUtil.closeAutoCommit(conn);
        List<Detail1> detail1List = new ArrayList<>();//添加补缴明细的集合
        List<Detail1> detail2List = new ArrayList<>();//添加正常明细的集合
        for(JSONObject object:employees){
            long eid = object.getLong("eid");
            float baseM = object.getFloat("baseM");
            float baseS = object.getFloat("baseS");

            //该员工的信息
            QueryConditions conditions = new QueryConditions();
            conditions.add("id","=",eid);
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;
            //获取该员工正常的结算单明细
            QueryConditions conditions1 = new QueryConditions();
            conditions1.add("eid","=",eid);
            conditions1.add("sid","=",sid);
            conditions1.add("status","=",0);
            ViewDetail1 detail =(ViewDetail1) Detail1Dao.get(conn,conditions1).data;

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
            detail1.setStatus(Detail1.STATUS_REPLENISH);//补缴

            //根据员工设置计算医保
            detail1 = calculateMedicare(detail1,setting,baseM,medicare);
            //根据员工设置计算社保
            detail1 = calculateSocial(detail1,setting,baseS,social);
            detail1 = calculateSocial(detail1,setting,baseS,social);
            //个人社保合计
            float sum1 = detail1.getPension1()+detail1.getDisease1()+detail1.getUnemployment1()
                    +detail1.getMedicare1();
            //单位社保合计
            float sum2 =detail1.getPension2()+detail1.getMedicare2()+detail1.getDisease2()+detail1.getBirth()+detail1.getUnemployment2()
                    +detail1.getInjury();

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            try {
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                java.util.Date date1 = sdf.parse(start);
                java.util.Date date2 = sdf.parse(end);
                c1.setTime(date1);
                c2.setTime(date2);
                int month1 = c1.get(Calendar.MONTH)+1;
                int month2 = c2.get(Calendar.MONTH)+1;
                int monthInterval = (month2 - month1)+1;
                //将个人社保合计赋值给该员工正常结算单明细的个人核收补减*月数，且是负数
                detail.setExtra1(detail1.getExtra1()-(sum1*monthInterval));
                detail.setComments1("个人社保补缴合计");
                //将单位社保合计赋值给该员工正常结算单明细的单位核收补减*月数，正数
                detail.setExtra2(detail.getExtra2()+sum2*monthInterval);
                detail.setComments2("单位社保补缴合计");
                detail2List.add(detail);//添加进正常明细集合
                for (int i = 0;i<monthInterval;i++){//有多少个月 生成多少个明细
                    ViewDetail1 detail2 = new ViewDetail1();
                    detail2.setSid(sid);
                    detail2.setEid(eid);
                    detail2.setComments1((month1+i)+"月补缴");
                    detail2.setDisease1(detail1.getDisease1());
                    detail2.setDisease2(detail1.getDisease2());
                    detail2.setMedicare1(detail1.getMedicare1());
                    detail2.setMedicare2(detail1.getMedicare2());
                    detail2.setPension1(detail1.getPension1());
                    detail2.setPension2(detail1.getPension2());
                    detail2.setUnemployment1(detail1.getUnemployment1());
                    detail2.setUnemployment2(detail1.getUnemployment2());
                    detail2.setInjury(detail1.getInjury());
                    detail2.setBirth(detail1.getBirth());
                    detail2.setStatus((byte) 1);//补缴
                    detail1List.add(detail2);//添加至集合
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //批量插入补缴明细
        result = Detail1Dao.importDetails(conn,detail1List);

        //批量修改正常明细
        result1= Detail1Dao.update(conn,detail2List);
        if(!result.success&&!result1.success){
            ConnUtil.rollback(conn);
            result.msg ="数据库操作错误";
            return result;
        }
        ConnUtil.commit(conn);
        return result;
    }

    //社保补差
    public static DaoUpdateResult makeup(Connection conn, String[] eids, String start, String end, long sid) {
        DaoUpdateResult result = new DaoUpdateResult();
        DaoUpdateResult result1;
        ConnUtil.closeAutoCommit(conn);
        List<Detail1> detail1List = new ArrayList<>();
        List<Detail1> detail2List = new ArrayList<>();
        for(String eid:eids){
            //获取该员工正常的结算单明细
            QueryConditions conditions1 = new QueryConditions();
            conditions1.add("eid","=",eid);
            conditions1.add("sid","=",sid);
            conditions1.add("status","=",0);
            ViewDetail1 detail =(ViewDetail1) Detail1Dao.get(conn,conditions1).data;

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
                //个人社保合计
                float sum1=0;
                //单位社保合计
                float sum2=0;
                for(int i=month1;i<=month2;i++) {
                    String month = year + "-" + (month1 + 1) + "-" + "01";
                    //获取该员工当月分的结算单明细
                    QueryConditions conditions2 = new QueryConditions();
                    conditions2.add("eid", "=", eid);
                    conditions2.add("month", "=", month);
                    ViewDetail1 detail1 = (ViewDetail1) Detail1Dao.get(conn, conditions2).data;
                    if (detail1 != null) {
                        //重新生成一个明细用于计算医保和社保
                        Detail1 detail2 = new Detail1();
                        //计算医保相关
                        int SettingM = setting.getSettingM();//员工医保设置
                        float baseM = 0;
                        switch (SettingM) {
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
                        detail2 = calculateMedicare(detail2, setting, baseM, medicare);

                        //计算社保相关
                        int SettingS = setting.getSettingS();//员工社保设置
                        float baseS = 0;
                        switch (SettingS) {
                            case 0://最低标准
                                baseS = social.getBase();
                                break;
                            case 1://不缴纳
                                baseS = social.getBase();
                                break;
                            case 2://自定义工资
                                baseS = setting.getValS();
                                break;
                        }
                        detail2 = calculateSocial(detail2, setting, baseS, social);

                        //计算补差的社保医保,并重新赋值
                        detail2.setMedicare2(detail2.getMedicare1() - detail1.getMedicare1());
                        detail2.setMedicare2(detail2.getMedicare2() - detail1.getMedicare2());
                        detail2.setBirth(detail2.getBirth() - detail1.getBirth());
                        detail2.setDisease1(detail2.getDisease1() - detail1.getDisease1());
                        detail2.setDisease2(detail2.getDisease2() - detail1.getDisease2());
                        detail2.setPension1(detail2.getPension1() - detail1.getPension1());
                        detail2.setPension2(detail2.getPension2() - detail1.getPension2());
                        detail2.setUnemployment1(detail2.getUnemployment1() - detail1.getUnemployment1());
                        detail2.setUnemployment2(detail2.getUnemployment2() - detail1.getUnemployment2());
                        detail2.setInjury(detail2.getInjury() - detail1.getInjury());
                        detail2.setEid(Long.parseLong(eid));
                        detail2.setSid(sid);
                        detail2.setStatus(Detail1.STATUS_BALANCE);
                        detail2.setComments1(month+"个人补差");
                        detail2.setComments2(month+"单位补差");
                        detail1List.add(detail2);

                        //个人社保合计
                       sum1 += detail2.getPension1()+detail2.getDisease1()+detail2.getUnemployment1()
                                +detail2.getMedicare1();
                        //单位社保合计
                       sum2 +=detail2.getPension2()+detail2.getMedicare2()+detail2.getDisease2()+detail2.getBirth()+detail2.getUnemployment2()
                                +detail2.getInjury();
                    }
                }
                detail.setExtra1(detail.getExtra1()-sum1);
                detail.setExtra2(detail.getExtra2()+sum2);
                detail.setComments1("补差");
                detail2List.add(detail);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //批量插入补缴明细
        result = Detail1Dao.importDetails(conn,detail1List);

        //批量修改正常明细
        result1= Detail1Dao.update(conn,detail2List);
        if(!result.success&&!result1.success){
            ConnUtil.rollback(conn);
            result.msg ="数据库操作错误";
            return result;
        }

        ConnUtil.commit(conn);
        return result;
    }


}
