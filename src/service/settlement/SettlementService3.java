package service.settlement;

import bean.admin.Account;
import bean.contract.Contract;
import bean.contract.Serve;
import bean.employee.Employee;
import bean.employee.ViewEmployee;
import bean.log.Log;
import bean.settlement.Detail1;
import bean.settlement.Detail3;
import bean.settlement.Settlement3;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.LogDao;
import dao.admin.AccountDao;
import dao.contract.ContractDao;
import dao.contract.ServeDao;
import dao.employee.EmployeeDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Detail3Dao;
import dao.settlement.Settlement2Dao;
import dao.settlement.Settlement3Dao;
import database.*;

import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SettlementService3 {

    //获取结算列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement3Dao.getList(conn,param);
    }

    //添加
    public static DaoUpdateResult insert(Connection conn, Settlement3 settlement, boolean needDetail) {
        /**
         * 获取合同服务项目中的商业保险id
         * 1、插入结算单 返回id，判断是否需要自动生成明细
         * 2、根据cid查询出派遣到该单位的所有员工(不包括小时工)
         * 3、根据员工的个数 封装好商业保险单明细集合
         * 4、批量插入商业保险结算单明细
         **/
        //关闭自动提交

        return Settlement3Dao.insert(conn,settlement);
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        return Settlement3Dao.delete(conn,id);
    }

    public static DaoUpdateResult saveAs(Connection conn, long id,Date month) {

        Settlement3 settlement3 = (Settlement3) Settlement3Dao.get(conn, id).data;
        settlement3.setMonth(month);

        DaoUpdateResult result = Settlement3Dao.insert(conn, settlement3);
        long sid = (long) result.extra;

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",id);
        //根据复制的结算单id查询出所有的结算单明细
        List<Detail3> detail3List = (List<Detail3>) Detail3Dao.getList(conn,parameter).rows;
        for(Detail3 detail3 :detail3List){
            //重新赋结算单id
            detail3.setSid(sid);
        }

        //重新插入数据库
        DaoUpdateResult result1 = Detail3Dao.importDetails(conn,detail3List);

        if(result.success&&result1.success){//事务
            ConnUtil.commit(conn);
            return result;
        }else {//回滚
            ConnUtil.rollback(conn);
            result1.msg = "另存为失败";
            return result1;
        }
    }

    /*//提交
    public static DaoUpdateResult commit(Connection conn, long id, Account account) {
        *//**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         *//*
        DaoUpdateResult result = Settlement3Dao.commit(conn, id);
        if(result.success){//修改成功，插入日志
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "提交";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 2);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                //插入log信息
                LogDao.insert(conn,log);
        }
        return result;
    }

    //审核
    public static DaoUpdateResult check(Connection conn, long id, byte type,boolean result,String reason,Account user) {
        *//**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         *//*
        DaoUpdateResult res = Settlement3Dao.check(conn,id,type,result);
        if(res.success){//修改成功，插入日志
            String operator = user.getNickname()+"("+user.getId()+")";
            String content = (type==0?"初审":"终审")+(result?"通过":("不通过:"+reason));
            Log log = new Log();
            log.setSid(id);
            log.setType((byte) 2);
            log.setOperator(operator);
            log.setTime(time);
            log.setContent(content);
            //插入log信息
            LogDao.insert(conn,log);
        }
        return res;
    }

    //重置
    public static DaoUpdateResult reset(Connection conn, long id, Account account) {
        *//**流程
         *1、修改结算单状态为重置
         * 2、根据aid查询出管理员
         * 2、插入日志
         *//*
        DaoUpdateResult result = Settlement3Dao.reset(conn, id);
        if(result.success){//修改成功，插入日志
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "重置";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 2);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                //插入log信息
                LogDao.insert(conn,log);
        }
        return result;
    }*/

    //扣款
    /*public static DaoUpdateResult charge(Connection conn, long id, Account account) {
        *//**流程
         *1、修改结算单状态为扣款
         * 2、根据aid查询出管理员
         * 2、插入日志
         *//*
        DaoUpdateResult result = Settlement3Dao.deduct(conn, id);
        if(result.success){//修改成功，插入日志
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "扣款";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 2);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                //插入log信息
                LogDao.insert(conn,log);

        }
        return result;
    }*/

    //获取该结算单的所有日志
    public static DaoQueryListResult getLogs(Connection conn, long id, QueryParameter parameter) {
        parameter.conditions.add("type","=",2);
        return LogDao.getList(conn,id,parameter);
    }

    /*public static DaoUpdateResult calculate(Connection conn, long sid) {
        Settlement3 settlement = (Settlement3) Settlement3Dao.get(conn,sid).data;
        QueryParameter param = new QueryParameter();
        param.addCondition("sid","=",sid);
        param.addCondition("status","=",1);//
        List<Detail3> details = (List<Detail3>) Detail3Dao.getList(conn,param).rows;
        settlement.setAmount(details.size());
        settlement.setSummary(settlement.getPrice()*settlement.getAmount());
        return Settlement3Dao.update(conn,settlement);
    }*/
}
