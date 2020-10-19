package service.settlement;

import bean.admin.Account;
import bean.contract.ViewContractCooperation;
import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.employee.ViewEmployee;
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
import dao.employee.SettingDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Settlement1Dao;
import database.*;
import utills.Calculate;

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
            parameter.addCondition("category","!=",2);
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
    public static String readBase(String[] eids, long sid, Connection conn) {
        float baseM=0;//医保基数
        float baseS=0;//社保基数
        List<JSONObject> data = new ArrayList<>();
        String result = null;

        for(int i = 0;i<eids.length;i++){
            JSONObject object = new JSONObject();
            //读取社保设置基数
            EnsureSetting setting = (EnsureSetting) SettingDao.get(conn, Long.parseLong(eids[i])).data;

            QueryConditions conditions = new QueryConditions();
            conditions.add("id","=",Long.parseLong(eids[i]));
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;

            if(setting==null){//员工社保设置异常
                JSONObject json = new JSONObject();
                json.put("success",false);
                json.put("msg","请完善员工"+employee.getName()+"的社保设置");
                result = json.toJSONString();
                return result;
            }

            byte settingM = setting.getSettingM();//医保设置
            byte settingS = setting.getSettingS();//社保设置
            switch (settingM){
                case 0://最低基数
                    RuleMedicare medicare = (RuleMedicare) RuleMedicareDao.getLast(conn,setting.getCity()).data;
                    if(medicare!=null){
                       baseM=medicare.getBase();
                    }else {
                        baseM = 0;
                    }
                    break;
                case 1://实际工资？当前月的实际工资
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
                case 0://最低基数?这里获取什么时候的社保规则呢？
                    RuleSocial social = (RuleSocial) RuleSocialDao.getLast(conn,setting.getCity()).data;
                    if(social!=null){
                        baseS=social.getBase();
                    }else {//社保规则不存在
                        baseS = 0;
                    }
                    break;
                case 1://实际工资？当前月的实际工资
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
        JSONObject json = new JSONObject();
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
            RuleMedicare medicare = (RuleMedicare) RuleMedicareDao.getLast(conn,setting.getCity()).data;
            //社保规则
            RuleSocial social = (RuleSocial) RuleSocialDao.getLast(conn,setting.getCity()).data;
            if(medicare==null||social==null){//医保规则空或者社保为空
                result.msg="员工"+employee.getName()+"社保或医保所在地的规则为空，请核对";
                return result;
            }
            //根据员工设置计算医保
            JSONObject object1 = calculateMedicare(setting,baseM,medicare);
            float medicare1 = Float.parseFloat(object1.getString("medicare1"));//个人医疗
            float medicare2 = Float.parseFloat(object1.getString("medicare2"));//单位医疗
            float birth = Float.parseFloat(object1.getString("birth"));//单位生育
            float  disease1 = Float.parseFloat(object1.getString("disease1"));//个人大病
            float disease2 = Float.parseFloat(object1.getString("disease2"));//单位大病

            //根据员工设置计算社保
            JSONObject object2 = calculateSocial(setting,baseS,social);
            float pension1 = Float.parseFloat(object2.getString("pension1"));//个人养老
            float unemployment1 = Float.parseFloat(object2.getString("unemployment1"));//个人失业
            float pension2 = Float.parseFloat(object2.getString("pension2"));//单位养老
            float unemployment2 = Float.parseFloat(object2.getString("unemployment2"));//单位失业
            float injury = Float.parseFloat(object2.getString("injury"));//单位工伤

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
                for (int i = 0;i<monthInterval;i++){
                    Detail1 detail1 = new Detail1();
                    detail1.setSid(sid);
                    detail1.setEid(eid);
                    detail1.setMedicare1(medicare1);
                    detail1.setMedicare2(medicare2);
                    detail1.setPension1(pension1);
                    detail1.setPension2(pension2);
                    detail1.setUnemployment1(unemployment1);
                    detail1.setUnemployment2(unemployment2);
                    detail1.setBirth(birth);
                    detail1.setDisease1(disease1);
                    detail1.setDisease2(disease2);
                    detail1.setInjury(injury);
                    detail1.setStatus((byte) 1);//补缴
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

        //初始化数据
        float pension1=0;//个人养老
        float medicare1=0;//个人医疗
        float unemployment1=0;//个人失业
        float disease1=0;//个人大病
        float pension2=0;//单位养老
        float medicare2=0;//单位医疗
        float unemployment2=0;//单位失业
        float injury=0;//单位工伤
        float disease2=0;//单位大病
        float birth=0;//单位生育
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
                    Detail1 detail1 = (Detail1) Detail1Dao.get(conn,conditions).data;
                    if(detail1==null){
                        result.msg = "该员工"+employee.getName()+","+month+"的结算单明细不存在,请核对";
                       return result;
                    }
                    //计算医保相关
                    int SettingM = setting.getSettingM();//员工医保设置
                    JSONObject object;
                    switch (SettingM){
                        case 0://最低标准
                            object = calculateMedicare(setting,medicare.getBase(),medicare);
                            medicare1 = Float.parseFloat(object.getString("medicare1"));//个人医疗
                            medicare2 = Float.parseFloat(object.getString("medicare2"));//单位医疗
                            birth = Float.parseFloat(object.getString("birth"));//单位生育
                            disease1 = Float.parseFloat(object.getString("disease1"));//个人大病
                            disease2 = Float.parseFloat(object.getString("disease2"));//单位大病
                            break;
                        case 1://实际工资
                            object = calculateMedicare(setting,detail1.getBase(),medicare);
                            medicare1 = Float.parseFloat(object.getString("medicare1"));//个人医疗
                            medicare2 = Float.parseFloat(object.getString("medicare2"));//单位医疗
                            birth = Float.parseFloat(object.getString("birth"));//单位生育
                            disease1 = Float.parseFloat(object.getString("disease1"));//个人大病
                            disease2 = Float.parseFloat(object.getString("disease2"));//单位大病

                            break;
                        case 2://不交纳，为0
                            medicare1 =0;//个人医疗
                            medicare2 = 0;//单位医疗
                            birth = 0;//单位生育
                            disease1 = 0;//个人大病
                            disease2 =0;//单位大病
                            break;
                        case 3://自定义基数
                            float ValM = setting.getValM();//自定义的基数
                            object = calculateMedicare(setting,ValM,medicare);
                            medicare1 = Float.parseFloat(object.getString("medicare1"));//个人医疗
                            medicare2 = Float.parseFloat(object.getString("medicare2"));//单位医疗
                            birth = Float.parseFloat(object.getString("birth"));//单位生育
                            disease1 = Float.parseFloat(object.getString("disease1"));//个人大病
                            disease2 = Float.parseFloat(object.getString("disease2"));//单位大病
                            break;
                    }
                    //计算社保相关
                    int SettingS = setting.getSettingS();//员工社保设置
                    JSONObject object2;
                    switch (SettingS){
                        case 0://最低标准
                            object2 = calculateSocial(setting,social.getBase(),social);
                            pension1 = Float.parseFloat(object2.getString("pension1"));
                            unemployment1 = Float.parseFloat(object2.getString("unemployment1"));
                            pension2 = Float.parseFloat(object2.getString("pension2"));
                            unemployment2 = Float.parseFloat(object2.getString("unemployment2"));
                            injury = Float.parseFloat(object2.getString("injury"));
                            break;
                        case 1://实际工资
                            object2 = calculateSocial(setting,detail1.getBase(),social);
                            pension1 = Float.parseFloat(object2.getString("pension1"));
                            unemployment1 = Float.parseFloat(object2.getString("unemployment1"));
                            pension2 = Float.parseFloat(object2.getString("pension2"));
                            unemployment2 = Float.parseFloat(object2.getString("unemployment2"));
                            injury = Float.parseFloat(object2.getString("injury"));
                            break;
                        case 2://不缴纳，为0
                            pension1=0;//个人养老
                            unemployment1=0;//个人失业
                            pension2=0;//单位养老
                            unemployment2=0;//单位失业
                            injury=0;//单位工伤
                            break;
                        case 3://自定义基数
                            float ValS=setting.getValS();//自定义的基数
                            object2 = calculateSocial(setting,ValS,social);
                            pension1 = Float.parseFloat(object2.getString("pension1"));
                            unemployment1 = Float.parseFloat(object2.getString("unemployment1"));
                            pension2 = Float.parseFloat(object2.getString("pension2"));
                            unemployment2 = Float.parseFloat(object2.getString("unemployment2"));
                            injury = Float.parseFloat(object2.getString("injury"));
                            break;
                    }
                    //计算补差的社保医保
                    medicare1 = medicare1 -detail1.getMedicare1();
                    medicare2 = medicare2 -detail1.getMedicare2();
                    birth = birth-detail1.getBirth();
                    disease1 = disease1 - detail1.getDisease1();
                    disease2 = disease2 - detail1.getDisease2();

                    pension1 = pension1 - detail1.getPension1();
                    pension2 = pension2 - detail1.getPension2();
                    unemployment1 = unemployment1 - detail1.getUnemployment1();
                    unemployment2 = unemployment2 - detail1.getUnemployment2();
                    injury = injury - detail1.getInjury();

                    //封装结算单明细
                    Detail1 detail = new Detail1();
                    detail.setEid(Long.parseLong(eid));
                    detail.setSid(sid);
                    detail.setMedicare1(medicare1);
                    detail.setMedicare2(medicare2);
                    detail.setPension1(pension1);
                    detail.setPension2(pension2);
                    detail.setUnemployment1(unemployment1);
                    detail.setUnemployment2(unemployment2);
                    detail.setBirth(birth);
                    detail.setInjury(injury);
                    detail.setStatus((byte) 2);

                    detail1List.add(detail);
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
