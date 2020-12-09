package service.settlement;

import bean.admin.Account;
import bean.contract.ViewContractCooperation;
import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.employee.ViewEmployee;
import bean.log.Log;
import bean.log.Transaction;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.LogDao;
import dao.client.FinanceDao;
import dao.contract.ContractDao;
import dao.employee.EmployeeDao;
import dao.employee.SettingDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import dao.settlement.Detail0Dao;
import dao.settlement.Detail1Dao;
import dao.settlement.Settlement0Dao;
import dao.settlement.Settlement1Dao;
import database.*;
import utills.Calculate;

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


public class Settlement0Service {
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    static String date = df.format(new Date(System.currentTimeMillis()));
    static  Date time = Date.valueOf(date);//获取当前时间

    //获取普通结算单列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement0Dao.getList(conn,param);
    }

    /**
     * 插入结算单，插入后根据返回的id，自动生成明细
     * @param conn
     * @param settlement
     * @param type 0 不自动生成明细  1 自动生成明细
     * @return
     */
    public static DaoUpdateResult insert(Connection conn, Settlement0 settlement, byte type) {
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult result = Settlement0Dao.insert(conn,settlement);

        if(result.success&&type == 1){//自动生成结算单明细
            long sid = (Long) result.extra;
            long cid = settlement.getCid();
            long did = settlement.getDid();
            //根据条件找到派遣到该单位的员工列表，条件有cid，did，类型为外派员工，用工性质根据结算单类型而变，在职
            QueryParameter parameter = new QueryParameter();
            parameter.addCondition("cid","=",cid);
            parameter.addCondition("did","=",did);
            parameter.addCondition("type","=",1);
            //查询出类别为派遣（1）或者外包（2）的员工
            parameter.addCondition("category","=",settlement.getType()+1);
            parameter.addCondition("status","=",0);
            List<ViewEmployee> employeeList = JSONArray.parseArray(JSONObject.toJSONString(EmployeeDao.getList(conn,parameter).rows),ViewEmployee.class);
            List<Detail0> arrayList = new ArrayList<Detail0>();
            for(int i = 0;i<employeeList.size();i++){//封装明细信息,添加进集合
                Detail0 detail = new Detail0();
                detail.setSid(sid);
                detail.setEid(employeeList.get(i).getId());
                arrayList.add(i,detail);
            }
            DaoUpdateResult result1 = Detail0Dao.importDetails(conn,arrayList);
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
        return Settlement0Dao.delete(conn,id);
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

    public static DaoUpdateResult confirm(Connection conn, long id, Account account) {
        /**流程
         *1、修改结算单状态为发放
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement1Dao.confirm(conn, id);
        if(result.success){//修改成功，插入日志
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "发放";
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
        List<ViewDetail1> detail1List = (List<ViewDetail1>) Detail1Dao.getList(conn,parameter).rows;
        for(ViewDetail1 detail1 :detail1List){
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
        float amount=0;
        float tax=0;
        float paid;
        //结算单
        Settlement0 settlement0 = (Settlement0) Settlement0Dao.get(conn,sid).data;

        QueryParameter parm = new QueryParameter();
        parm.addCondition("sid","=",sid);
        //该结算单中的所有明细
        List<ViewDetail0> vs = (List<ViewDetail0>) Detail0Dao.getList(conn,parm).rows;
        for(ViewDetail0 d:vs){
            amount+=d.getAmount();
            tax+=d.getTax();
        }
        paid=amount-tax;
        settlement0.setAmount(amount);
        settlement0.setTax(tax);
        settlement0.setPaid(paid);

        return Settlement0Dao.update(conn,settlement0);
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
        List<ViewDetail1> detail1List = new ArrayList<>();
        HashMap<String,RuleMedicare> mapMedicare;
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
            ViewDetail1 detail1 = new ViewDetail1();
            detail1.setSid(sid);
            detail1.setEid(eid);
            detail1.setStatus(Detail1.STATUS_REPLENISH);//补缴

            //根据员工设置计算医保
            detail1 = (ViewDetail1) calculateMedicare(detail1,setting,baseM,medicare);
            //根据员工设置计算社保
            detail1 = (ViewDetail1) calculateSocial(detail1,setting,baseS,social);

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
        List<ViewDetail1> detail1List = new ArrayList<>();
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
                    ViewDetail1 d = new ViewDetail1();
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
                    d = (ViewDetail1) calculateMedicare(d,setting,baseM,medicare);

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
                    d = (ViewDetail1) calculateSocial(d,setting,baseS,social);

                    //计算补差的社保医保,并重新赋值
                    d.setMedicare2(d.getMedicare1()-detail1.getMedicare1());
                    d.setMedicare2(d.getMedicare2() -detail1.getMedicare2());
                    d.setBirth(d.getBirth()-detail1.getBirth());
                    d.setDisease1(d.getDisease1() - detail1.getDisease1());
                    d.setDisease2(d.getDisease2() - detail1.getDisease2());
                    d.setPension1(d.getPension1() - detail1.getPension1());
                    d.setPension2(d.getPension2() - detail1.getPension2());
                    d.setUnemployment1(d.getUnemployment1() - detail1.getUnemployment1());
                    d.setUnemployment2(d.getUnemployment2() - detail1.getUnemployment2());
                    d.setInjury(d.getInjury() - detail1.getInjury());
                    d.setEid(Long.parseLong(eid));
                    d.setSid(sid);
                    d.setStatus(Detail1.STATUS_BALANCE);

                    detail1List.add(d);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //批量导入
        result = Detail1Dao.importDetails(conn,detail1List);
        return result;
    }


}
