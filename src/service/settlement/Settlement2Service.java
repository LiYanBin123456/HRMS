package service.settlement;

import bean.admin.Account;
import bean.employee.Employee;
import bean.employee.ViewEmployee;
import bean.log.Log;
import bean.settlement.Detail1;
import bean.settlement.Detail2;
import bean.settlement.Settlement1;
import bean.settlement.Settlement2;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.LogDao;
import dao.admin.AccountDao;
import dao.employee.EmployeeDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Detail2Dao;
import dao.settlement.Settlement1Dao;
import dao.settlement.Settlement2Dao;
import database.*;
import utills.Calculate;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Settlement2Service {
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    static String date = df.format(new Date(System.currentTimeMillis()));
    static  Date time = Date.valueOf(date);//获取当前时间

    //获取结算单列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement2Dao.getList(conn,param);
    }

    //插入结算单
    public static DaoUpdateResult insert(Connection conn, Settlement2 settlement2, byte type) {
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult result ;
        //需要获取合同中的小时工单价
        result = Settlement2Dao.insert(conn,settlement2);
        if(result.success&&type==1){
            long sid = (long) result.extra;//结算单id
            long cid = settlement2.getCid();//合作单位id
            long did = settlement2.getDid();//派遣方id
            //根据条件找到派遣到该单位的员工列表，条件有cid，did，类型为外派员工，用工性质是小时工，在职
            QueryParameter parameter = new QueryParameter();
            parameter.addCondition("cid","=",cid);
            parameter.addCondition("did","=",did);
            parameter.addCondition("type","=",1);
            parameter.addCondition("category","=",2);
            parameter.addCondition("status","=",0);
            List<ViewEmployee> employeeList = JSONArray.parseArray(JSONObject.toJSONString(EmployeeDao.getList(conn,parameter).rows),ViewEmployee.class);
            List<Detail2> detail2List = new ArrayList<>();
            for(int i = 0;i<employeeList.size();i++){//封装明细信息,添加进集合
                Detail2 detail2 = new Detail2();
                detail2.setSid(sid);
                detail2.setEid(employeeList.get(i).getId());
                //员工表中单价
                detail2.setPrice(employeeList.get(i).getPrice());
                detail2List.add(i,detail2);
            }
            //插入明细
            DaoUpdateResult result1 = Detail2Dao.importDetails(conn,detail2List);
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

    //另存为
    public static DaoUpdateResult saveAs(Connection conn, long id, Date month) {
        /**流程
         * 1、查询出结算单，修改结算月份
         * 2、插入结算单，返回主键id
         * 3、根据cid查询出派遣到该单位的所有员工(不包括小时工)
         * 4、根据员工的个数 封装好明细集合
         * 5、批量结算单明细
         */
        Settlement2 settlement2 = (Settlement2) Settlement2Dao.get(conn, id).data;
        settlement2.setMonth(month);
        long sid = (long) Settlement2Dao.insert(conn, settlement2).extra;
        long did = settlement2.getDid();
        long cid = settlement2.getCid();

        //根据条件找到派遣到该单位的员工列表，条件有cid，did，类型为外派员工，用工性质是小时工，在职
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("cid","=",cid);
        parameter.addCondition("did","=",did);
        parameter.addCondition("type","=",1);
        parameter.addCondition("category","=",2);
        parameter.addCondition("status","=",0);
        List<ViewEmployee> employeeList = JSONArray.parseArray(JSONObject.toJSONString(EmployeeDao.getList(conn,parameter).rows),ViewEmployee.class);
        List<Detail2> detail2List = new ArrayList<>();
        for(int i = 0;i<employeeList.size();i++){//封装明细信息,添加进集合
            Detail2 detail2 = new Detail2();
            detail2.setSid(sid);
            detail2.setEid(employeeList.get(i).getId());
            //员工表中单价
            detail2.setPrice(employeeList.get(i).getPrice());
            detail2List.add(i,detail2);
        }
        //插入明细
        DaoUpdateResult result = Detail2Dao.importDetails(conn,detail2List);

        return result;
    }
    //删除结算单
    public static DaoUpdateResult delete(Connection conn, Long id) {
        return Settlement2Dao.delete(conn,id);
    }


    //提交
    public static DaoUpdateResult commit(Connection conn, long id, long aid) {
        /**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement2Dao.commit(conn, id);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "提交";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 1);
                log.setOperator(operator);
                log.setTime(time);
                log.setContent(content);
                //插入log信息
                LogDao.insert(conn,log);
            }
        }
        return result;
    }

    //审核
    public static DaoUpdateResult check(Connection conn, long id, long aid, byte status) {
        /**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement2Dao.check(conn,id,status);
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
                log.setType((byte) 1);
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

    //重置
    public static DaoUpdateResult reset(Connection conn, long id,long aid) {
        /**流程
         *1、修改结算单状态为重置
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement2Dao.reset(conn, id);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "重置";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 1);
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

    //扣款
    public static DaoUpdateResult deduct(Connection conn, long id,long aid) {
        /**流程
         *1、修改结算单状态为扣款
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement2Dao.deduct(conn, id);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "扣款";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 1);
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

    //发放
    public static DaoUpdateResult confirm(Connection conn, long id, long aid) {
        /**流程
         *1、修改结算单状态为发放
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement2Dao.confirm(conn, id);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "发放";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 1);
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
        parameter.conditions.add("type","=",1);
        return LogDao.getList(conn,id,parameter);
    }

    public static String exportBank(Connection conn, long id,String bank) {
        return null;
    }



    public static String copy(Connection conn, long id,String month) {
        return null;
    }


    public static DaoUpdateResult saveSettlement(Connection conn, long sid) {
        Settlement2 settlement2 = (Settlement2) Settlement2Dao.get(conn,sid).data;
        QueryParameter param = new QueryParameter();
        param.addCondition("sid","=",sid);
        List<Detail2> detail2List = (List<Detail2>) Detail2Dao.getList(conn,param).rows;

        return Settlement2Dao.update(conn,Calculate.calculateSettlement2(settlement2,detail2List));
    }
}
