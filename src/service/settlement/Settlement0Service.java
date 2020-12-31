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

//普通结算单
public class Settlement0Service {

    //获取普通结算单列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement0Dao.getList(conn,param);
    }

    /**
     * 插入结算单，插入后根据返回的id，自动生成明细
     * @param conn
     * @param settlement
     * @param needDetail 是否自动生成明细
     * @return
     */
    public static DaoUpdateResult insert(Connection conn, Settlement0 settlement, boolean needDetail) {
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult result = Settlement0Dao.insert(conn,settlement);

        if(result.success && needDetail){//自动生成结算单明细
            long sid = (Long) result.extra;
            long cid = settlement.getCid();
            long did = settlement.getDid();
            //根据条件找到派遣到该单位的员工列表，条件有cid，did，类型为外派员工，用工性质根据结算单类型而变，在职
            QueryParameter parameter = new QueryParameter();
            parameter.addCondition("cid","=",cid);
            parameter.addCondition("did","=",did);
            parameter.addCondition("type","=",1);
            //查询出与结算单类别（type)向对应的员工
            parameter.addCondition("category","=",settlement.getType()+1);
            parameter.addCondition("status","=",0);
            List<ViewEmployee> emmployees = (List<ViewEmployee>) EmployeeDao.getList(conn,parameter).rows;
            List<Detail0> details = new ArrayList<>();
            for(ViewEmployee e:emmployees){//封装明细信息,添加进集合
                Detail0 detail = new Detail0();
                detail.setSid(sid);
                detail.setEid(e.getId());
                details.add(detail);
            }
            DaoUpdateResult result1 = Detail0Dao.importDetails(conn,details);
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
        Settlement0 settlement = (Settlement0) Settlement0Dao.get(conn, id).data;
        settlement.setMonth(month);

        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);
        //插入结算单
        DaoUpdateResult res1 = Settlement0Dao.insert(conn, settlement);
        //返回结算单id
        long sid = (long)res1.extra;

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",id);
        //根据复制的结算单id查询出所有的结算单明细
        List<Detail0> details = (List<Detail0>) Detail0Dao.getList(conn,parameter).rows;
        for(Detail0 detail :details){
            //重新赋结算单id
           detail.setSid(sid);
        }
        //重新插入数据库
        DaoUpdateResult res2 = Detail0Dao.importDetails(conn,details);

        if(res1.success && res2.success){//事务
            ConnUtil.commit(conn);
            return res1;
        }else {//回滚
            ConnUtil.rollback(conn);
            res2.msg = "另存为失败";
            return res2;
        }
    }

    //保存结算单；实质是计算结算单并且修改
    public static DaoUpdateResult saveSettlement(Connection conn, long sid) {
        float amount=0;
        float tax=0;
        float paid;
        //结算单
        Settlement0 settlement = (Settlement0) Settlement0Dao.get(conn,sid).data;

        QueryParameter parm = new QueryParameter();
        parm.addCondition("sid","=",sid);
        //该结算单中的所有明细
        List<ViewDetail0> details = (List<ViewDetail0>) Detail0Dao.getList(conn,parm).rows;
        for(ViewDetail0 d:details){
            amount+=d.getAmount();
            tax+=d.getTax();
        }
        paid=amount-tax;
        settlement.setAmount(amount);
        settlement.setTax(tax);
        settlement.setPaid(paid);

        return Settlement0Dao.update(conn,settlement);
    }

}
