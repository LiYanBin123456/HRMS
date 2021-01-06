package service.settlement;

import bean.admin.Account;
import bean.contract.ViewContractCooperation;
import bean.employee.Deduct;
import bean.employee.EnsureSetting;
import bean.employee.ViewEmployee;
import bean.log.Log;
import bean.log.Transaction;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.Detail1;
import bean.settlement.Settlement1;
import bean.settlement.ViewDetail1;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.LogDao;
import dao.client.FinanceDao;
import dao.contract.ContractDao;
import dao.employee.DeductDao;
import dao.employee.EmployeeDao;
import dao.employee.SettingDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Settlement1Dao;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;
import utills.DateUtil;
import utills.Salary.Salary;
import utills.CollectionUtil;

import java.sql.Connection;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class Settlement1Service {

    //获取普通结算单列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement1Dao.getList(conn,param);
    }
    /**
     * 插入结算单，插入后根据返回的id，自动生成明细
     * @param conn
     * @param settlement
     * @param needDetail 是否自动生成明细
     * @return
     */
    public static DaoUpdateResult insert(Connection conn, Settlement1 settlement, boolean needDetail) {
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult result = Settlement1Dao.insert(conn,settlement);

        if(result.success && needDetail){//自动生成结算单明细
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

            byte status = (settlement.isNeedCalculateSocial())?Detail1.STATUS_NORMAL:Detail1.STATUS_MAKEUP;
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
            transaction.setMoney(balance);
            transaction.setComments(content);
            //插入资金明细
            DaoUpdateResult result2 = FinanceDao.insertTransactions(conn,transaction);

            //封装log信息
            Log log = new Log();
            log.setSid(id);
            log.setType((byte) 0);
            log.setOperator(operator);
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
         * 1、修改结算单状态为发放
         * 2、判断是否为补发
         * 2、获取结算单明细并且修改员工个税专项扣除中的累计收入，累计已预缴税额，累计减免
         * 3、插入日志
         */
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult result = Settlement1Dao.confirm(conn, sid);
        //查询出该结算单除了补缴，补差外的所有结算单明细
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        parameter.addCondition("status","!=",Detail1.STATUS_REPLENISH);
        parameter.addCondition("status","!=",Detail1.STATUS_BALANCE);
        List<Detail1> details = (List<Detail1>) Detail1Dao.getList(conn,parameter).rows;
        List<Deduct> deductList = new ArrayList<>();

        //判断是否是补发
        Settlement1 settlement = (Settlement1) Settlement1Dao.get(conn,sid).data;
        for (Detail1 detail:details){
            float deducts=0;
            Deduct deduct1 = getDeduct(deductList,detail.getEid());
            if(deduct1!=null){//个税专项扣除存在于集合中，只需要加累计收入和累计已预缴税额
                //累计收入
                deduct1.setIncome(deduct1.getIncome()+detail.getPayable());
                //累计已预缴税额=累计已预缴税额+个税
                deduct1.setPrepaid(deduct1.getPrepaid()+detail.getTax());

            }else{//不存在于该集合中

                //去数据库中找到该员工的个税专项扣除
                Deduct deduct = (Deduct) DeductDao.get(conn,detail.getEid()).data;

                //累计收入=累计收入+当月应发；
                deduct.setIncome(deduct.getIncome()+detail.getPayable());

                //累计已预缴税额=累计已预缴税额+个税
                deduct.setPrepaid(deduct.getPrepaid()+detail.getTax());

                if(settlement.isNeedCalculateSocial()) {//如果需要计算社保，就需要累加累计减免和累加个税专项扣除
                    //累计减免=累计减免+5000；
                    deduct.setFree(deduct.getFree()+5000);

                    //将该月的个税专项扣除总额累加
                    deducts=deduct.getDeduct()+deduct.getDeduct1()+deduct.getDeduct2()+deduct.getDeduct3()+deduct.getDeduct4()+deduct.getDeduct5()+deduct.getDeduct6();
                    deduct.setDeduct(deducts);
                }
                deductList.add(deduct);
            }
        }

        //批量修改个税信息
        DaoUpdateResult result1 = DeductDao.updateDeducts(conn,deductList);

        //封装log信息
        String operator = account.getNickname()+"("+account.getId()+")";
        String content = "发放";
        Log log = new Log();
        log.setSid(sid);
        log.setType((byte) 0);
        log.setOperator(operator);
        log.setContent(content);

        //插入log信息
        DaoUpdateResult result2 = LogDao.insert(conn,log);
        if(!result.success&&!result1.success&&!result2.success){//有一个不成功就回滚
            ConnUtil.rollback(conn);
            result.success=false;
            result.msg="数据库操作错误";
        }else {//成功则提交
            ConnUtil.commit(conn);
        }
        return result;
    }

    private static Deduct getDeduct(List<Deduct> deducts,long id) {
        for(Deduct d:deducts){
            if(d.getEid() == id){
                return d;
            }
        }
        return null;
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
        Settlement1 settlement = (Settlement1) Settlement1Dao.get(conn,sid).data;
        //获取合作客户的合同视图
        ViewContractCooperation viewContractCooperation = (ViewContractCooperation) ContractDao.getViewContractCoop(conn,settlement.getCcid()).data;
        QueryParameter parm = new QueryParameter();
        parm.addCondition("sid","=",sid);
        //该结算单中的所有明细
        List<ViewDetail1> viewDetail1s = (List<ViewDetail1>) Detail1Dao.getList(conn,parm).rows;

        //计算结算单
        Salary.calculateSettlement1(settlement,viewContractCooperation,viewDetail1s);

        return Settlement1Dao.update(conn,settlement);
    }

    //社保补缴
    public static DaoUpdateResult backup(String start, String end, long sid,List<JSONObject> employees, Connection conn) {
        /**
         * 1、获取前台传过来的员工数据（包含员工id、医保基数、社保基数）
         * 2、获取所有员工结算单正常明细
         * 3、获取所有员工的社保设置
         * 4、获取所有员工的医保和社保规则
         * 5、根据月份算出员工的医保和社保
         * 6、算出个人和单位社保合计置于正常结算单明细
         * 7、批量插入补缴明细
         * 8、批量修改正常结算单明细
         */
        DaoUpdateResult res1 = new DaoUpdateResult();
        ConnUtil.closeAutoCommit(conn);

        String ids = "";
        for(JSONObject object:employees){
            ids += (object.getLong("eid")+",");
        }
        ids = ids.substring(0,ids.length()-1);

        //获取所有员工正常的结算单明细，后面计算完补差的数值后放到核收补减中
        QueryParameter p1 = new QueryParameter();
        p1.addCondition("eid","in",ids);
        p1.addCondition("sid","=",sid);
        p1.addCondition("status","=",0);
        List<ViewDetail1> details11 = (List<ViewDetail1>) Detail1Dao.getList(conn,p1).rows;

        //获取所有员工的社保设置
        QueryParameter p2 = new QueryParameter();
        p2.addCondition("eid","in",ids);
        List<EnsureSetting> settings = (List<EnsureSetting>) SettingDao.getList(conn,p2).rows;

        //获取所有地市的医保和社保规则,使用缓冲机制保存相关地市的医保规则和社保规则
        HashMap<String, RuleMedicare> medicares = new HashMap<>();
        HashMap<String, RuleSocial> socials = new HashMap<>();

        int year = Integer.parseInt(start.split("-")[0]);//年
        int month1 = Integer.parseInt(start.split("-")[1]);//起始月
        int month2 = Integer.parseInt(end.split("-")[1]);//结束月
        Calendar c = Calendar.getInstance();
        c.set(year,month1,1);

        for(EnsureSetting s:settings){
            String city = s.getCity();//员工所处地市
            Date date = DateUtil.parse(year+"-12-31","yyyy-MM-dd");
            //获取该地市的医保规则
            RuleMedicare medicare = medicares.get(city);
            if (medicare == null) {
                medicare = (RuleMedicare) RuleMedicareDao.get(conn, city, date).data;
                if (medicare == null) {
                    res1.success = false;
                    res1.msg = String.format("请确认%s的医保规则是否存在", city);
                    return res1;
                }
                medicares.put(city, medicare);
            }
            RuleSocial social = socials.get(city);
            if (social == null) {
                social = (RuleSocial) RuleSocialDao.get(conn, city, date).data;
                if (social == null) {
                    res1.success = false;
                    res1.msg = String.format("请确认%s的社保规则是否存在", city);
                    return res1;
                }
                socials.put(city, social);
            }
        }

        List<Detail1> detail_append = new ArrayList<>();//添加补缴明细的集合
        List<ViewDetail1> detail_update = new ArrayList<>();//添加正常明细的集合

        for(JSONObject object:employees){
            long eid = object.getLong("eid");
            float baseM = object.getFloat("baseM");
            float baseS = object.getFloat("baseS");

            //获取该员工正常的结算单明细
            ViewDetail1 detail = CollectionUtil.getElement(details11,"eid",eid);

            EnsureSetting setting = CollectionUtil.getElement(settings,"eid",eid);//获取员工社保设置
            if(setting==null){//员工设置为空
                res1.msg = String.format("请先完善%s的社保设置",object.getString("name"));
               return res1;
            }
            //设置医保社保为自定义基数
            setting.setSettingM((byte) 2);
            setting.setValM(baseM);
            setting.setSettingS((byte) 2);
            setting.setValS(baseS);

            RuleMedicare medicare = medicares.get(setting.getCity());
            RuleSocial social = socials.get(setting.getCity());

            float sum1=0;//个人累计社保合计
            float sum2=0;//单位累计社保合计
            for(int i=0;i<=month2-month1;i++) {//有多少个月生成多少条明细
                //新建明细,并且初始化
                Detail1 d = new Detail1();
                d.setSid(sid);
                d.setEid(eid);
                d.setStatus(Detail1.STATUS_REPLENISH);//补缴
                int month = c.get(Calendar.MONTH)+i;
                d.setComments1(month+"月份个人社保补缴");
                d.setComments2(month+"月份单位社保补缴");

                Salary.calculateMedicare(d,setting,medicare);//根据员工设置计算医保
                Salary.calculateSocial(d,setting,social);//根据员工设置计算社保

                detail_append.add(d);

                sum1 += d.getTotalPerson();//个人社保合计
                sum2 +=d.getTotalDepartment();//单位社保合计
            }
            detail.setExtra1(detail.getExtra1()-sum1);  //将个人社保合计赋值给该员工正常结算单明细的单位核收补减，正数
            detail.setComments1("个人社保补缴合计");
            detail.setExtra2(detail.getExtra2()+sum2); //将单位社保合计赋值给该员工正常结算单明细的单位核收补减，正数
            detail.setComments2("单位社保补缴合计");
            detail_update.add(detail);//添加进正常明细集合

        }

        //批量插入补缴明细
        res1 = Detail1Dao.importDetails(conn,detail_append);

        //批量修改正常明细
        DaoUpdateResult res2= Detail1Dao.update(conn,detail_update);
        if(!res1.success || !res2.success){
            ConnUtil.rollback(conn);
            res1.msg ="数据库操作错误";
            return res1;
        }
        ConnUtil.commit(conn);
        return res1;
    }

    //社保补差
    public static DaoUpdateResult makeup(Connection conn, String[] eids, String start, String end, long sid) {
        /**
         * 思路
         * 1、获取所有员工的社保设置
         * 2、获取最新社保和医保规则，并且放在缓存中
         * 逐月完成步骤345
         * 3、算出当月应缴的社保和医保
         * 4、获取当月已交的社保和医保
         * 5、计算差额（以及累计差额）
         * 6、将累计差额置于当前结算单明细中（先获取）的核收补减少，目的是计算完补差的数值后放到正常明细的核收补减中。
         * 7、批量插入差额明细
         * 8、批量修改当前结算单明细
         */
        DaoUpdateResult res1 = new DaoUpdateResult();
        ConnUtil.closeAutoCommit(conn);
        List<Detail1> details_append = new ArrayList<>();
        List<ViewDetail1> details_update = new ArrayList<>();

        String ids = "";
        for(String s:eids){
            ids += (s+",");
        }
        ids = ids.substring(0,ids.length()-1);

        //获取所有员工正常的结算单明细，后面计算完补差的数值后放到核收补减中
        QueryParameter p1 = new QueryParameter();
        p1.addCondition("eid","in",ids);
        p1.addCondition("sid","=",sid);
        p1.addCondition("status","=",0);
        List<ViewDetail1> details11 = (List<ViewDetail1>) Detail1Dao.getList(conn,p1).rows;

        //获取所有员工的社保设置
        QueryParameter p2 = new QueryParameter();
        p2.addCondition("eid","in",ids);
        List<EnsureSetting> settings = (List<EnsureSetting>) SettingDao.getList(conn,p2).rows;

        //获取所有地市的医保和社保规则,使用缓冲机制保存相关地市的医保规则和社保规则
        HashMap<String, RuleMedicare> medicares = new HashMap<>();
        HashMap<String, RuleSocial> socials = new HashMap<>();
        for(EnsureSetting s:settings){
            String city = s.getCity();//员工所处地市
            //获取该地市的医保规则
            RuleMedicare medicare = medicares.get(city);
            if (medicare == null) {
                medicare = (RuleMedicare) RuleMedicareDao.getLast(conn, city).data;
                if(medicare == null){
                    res1.success = false;
                    res1.msg = String.format("请确认%s的医保规则是否存在",city);
                    return res1;
                }
                medicares.put(city, medicare);
            }
            RuleSocial social = socials.get(city);
            if (social == null) {
                social = (RuleSocial) RuleSocialDao.getLast(conn, city).data;
                if(social == null){
                    res1.success = false;
                    res1.msg = String.format("请确认%s的社保规则是否存在",city);
                    return res1;
                }
                socials.put(city, social);
            }
        }

        //获取所有员工所有月份正常的的结算单明细
        QueryParameter p3 = new QueryParameter();
        p3.addCondition("month",">=",start);
        p3.addCondition("month","<=",end);
        p3.addCondition("status","=",Detail1.STATUS_NORMAL);
        p3.addCondition("eid","in",ids);
        List<ViewDetail1> detail1s2 = (List<ViewDetail1>) Detail1Dao.getList(conn,p3).rows;

        int year = Integer.parseInt(start.split("-")[0]);//年
        int month1 = Integer.parseInt(start.split("-")[1]);//起始月
        int month2 = Integer.parseInt(end.split("-")[1]);//结束月
        Calendar c = Calendar.getInstance();
        c.set(year,month1-1,1);
        for(String id:eids){
            long eid = Long.parseLong(id);
            //个人社保合计
            float sum1=0;
            //单位社保合计
            float sum2=0;
            for(int i=0;i<=month2-month1;i++) {
                c.set(Calendar.MONTH,c.get(Calendar.MONTH)+i);
                //获取该员工当月分的结算单明细
                ViewDetail1 detail1 = getDetail(detail1s2,eid, c.getTime());
                if (detail1 != null) {
                    //重新生成一个明细用于计算医保和社保
                    Detail1 detail2 = new Detail1();
                    //计算医保相关
                    EnsureSetting setting = CollectionUtil.getElement(settings,"eid",eid);
                    if(setting == null){
                        res1.success = false;
                        res1.msg = String.format("请确认%s的社保设置是否存在",detail1.getName());
                        return res1;
                    }
                    RuleMedicare medicare = medicares.get(setting.getCity());
                    RuleSocial social = socials.get(setting.getCity());
                    Salary.calculateMedicare(detail2, setting, medicare);
                    Salary.calculateSocial(detail2, setting, social);//计算社保相关

                    Detail1 d = Detail1.subtract(detail2,detail1);
                    d.setEid(eid);
                    d.setSid(sid);
                    d.setStatus(Detail1.STATUS_BALANCE);
                    int month = c.get(Calendar.MONTH)+1;
                    d.setComments1(month+"月个人补差");
                    d.setComments2(month+"月单位补差");
                    details_append.add(d);

                   sum1 += d.getTotalPerson();//个人社保合计
                   sum2 +=d.getTotalDepartment();//单位社保合计
                }
            }

            ViewDetail1 detail1 = CollectionUtil.getElement(details11,"eid",eid);
            detail1.setExtra1(detail1.getExtra1()-sum1);
            detail1.setExtra2(detail1.getExtra2()+sum2);
            detail1.setComments1("补差");
            details_update.add(detail1);
        }

        res1 = Detail1Dao.importDetails(conn,details_append);//批量插入补缴明细
        DaoUpdateResult res2 = Detail1Dao.update(conn,details_update);//批量修改正常明细

        if(!res1.success || !res2 .success){
            ConnUtil.rollback(conn);
            res1.msg ="数据库操作错误";
            return res1;
        }

        ConnUtil.commit(conn);
        return res1;
    }

    private static ViewDetail1 getDetail(List<ViewDetail1> details, long eid, Date date) {
        for(ViewDetail1 d:details){
            if(DateUtil.equal(date,d.getMonth()) && d.getEid()==eid){
                return d;
            }
        }
        return null;
    }


}
