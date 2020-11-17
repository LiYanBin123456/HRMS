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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Settlement3Service {
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    static String date = df.format(new Date(System.currentTimeMillis()));
    static  Date time = Date.valueOf(date);//获取当前时间

    //获取结算列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement3Dao.getList(conn,param);
    }

    //添加
    public static DaoUpdateResult insert(Connection conn, Settlement3 settlement3, byte type) {
        /**
         * 获取合同服务项目中的商业保险id
         * 1、插入结算单 返回id，判断是否需要自动生成明细
         * 2、根据cid查询出派遣到该单位的所有员工(不包括小时工)
         * 3、根据员工的个数 封装好商业保险单明细集合
         * 4、批量插入商业保险结算单明细
         **/
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        //获取合同服务项目
        Serve serve = (Serve) ServeDao.get(conn,settlement3.getCcid()).data;
        settlement3.setPid(serve.getPid());//设置商业保险id
        settlement3.setPrice(serve.getValue());//设置商业保险的价格

        DaoUpdateResult result = Settlement3Dao.insert(conn,settlement3);
        if(result.success&&type==1) {
            long sid = (long) result.extra;//返回插入后的主键
            long did = settlement3.getDid();//合作单位id
            long cid = settlement3.getCid();//合作单位id
            long pid = settlement3.getPid();//产品id

            //根据条件找到派遣到该单位的员工列表，条件有cid，did，类型为外派员工，用工性质不是小时工，在职
            QueryParameter parameter = new QueryParameter();
            QueryConditions conditions = new QueryConditions();
            conditions.add("did", "=", did);
            conditions.add("cid", "=", cid);
            conditions.add("type", "=", 1);
            conditions.add("category", "!=", 3);
            conditions.add("status", "=", 0);
            parameter.conditions = conditions;
            List<ViewEmployee> employeeList = JSONArray.parseArray(JSONObject.toJSONString(EmployeeDao.getList(conn,parameter).rows),ViewEmployee.class);
            List<Detail3> detail3List = new ArrayList<>();
            for(int i = 0;i<employeeList.size();i++){//封装明细信息,添加进集合
                Detail3 detail3 = new Detail3();
                detail3.setSid(sid);//结算单id
                detail3.setEid(employeeList.get(i).getId());//员工id
                detail3.setPid(pid);//商业保险id
                detail3.setPrice(serve.getValue());//商业保险价格
                detail3List.add(i,detail3);
            }
            //插入明细
           DaoUpdateResult result1 =  Detail3Dao.importDetails(conn,detail3List);

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

    //提交
    public static DaoUpdateResult commit(Connection conn, long id, long aid) {
        /**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement3Dao.commit(conn, id);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
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
        }
        return result;
    }

    //审核
    public static DaoUpdateResult check(Connection conn, long id, byte type,boolean result,String reason,Account user) {
        /**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
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
    public static DaoUpdateResult reset(Connection conn, long id,long aid) {
        /**流程
         *1、修改结算单状态为重置
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement3Dao.reset(conn, id);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
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
        DaoUpdateResult result = Settlement3Dao.deduct(conn, id);
        if(result.success){//修改成功，插入日志
            DaoQueryResult result1 = AccountDao.get(conn, aid);
            if(result1.success){
                Account account = (Account) result1.data;
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
        }
        return result;
    }

    //获取该结算单的所有日志
    public static DaoQueryListResult getLogs(Connection conn, long id, QueryParameter parameter) {
        parameter.conditions.add("type","=",2);
        return LogDao.getList(conn,id,parameter);
    }

    public static DaoUpdateResult saveSettlement(Connection conn, long sid) {
        Settlement3 settlement3 = (Settlement3) Settlement3Dao.get(conn,sid).data;
        QueryParameter param = new QueryParameter();
        param.addCondition("sid","=",sid);
        List<Detail3> detail3List = (List<Detail3>) Detail3Dao.getList(conn,param).rows;
        float price = 0;
        for(Detail3 detail3:detail3List){
          price+=detail3.getPrice();
        }
        settlement3.setPrice(price);
        return Settlement3Dao.updatePrice(conn,settlement3);
    }
}
