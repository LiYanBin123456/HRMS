package service.settlement;

import bean.admin.Account;
import bean.contract.ViewContractCooperation;
import bean.employee.ViewEmployee;
import bean.log.Log;
import bean.settlement.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.LogDao;
import dao.admin.AccountDao;
import dao.contract.ContractDao;
import dao.employee.EmployeeDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Settlement1Dao;
import database.*;
import utills.Calculate;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


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
    public static DaoUpdateResult saveSettlement(Connection conn, long sid,long cid) {

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
}
