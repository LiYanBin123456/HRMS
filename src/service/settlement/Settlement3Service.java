package service.settlement;

import bean.admin.Account;
import bean.log.Log;
import bean.settlement.Detail3;
import bean.settlement.Settlement3;
import dao.LogDao;
import dao.admin.AccountDao;
import dao.settlement.Settlement2Dao;
import dao.settlement.Settlement3Dao;
import database.*;

import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
    public static DaoUpdateResult insert(Connection conn, Settlement3 settlement3) {
        /**
         * 1、插入结算单 返回id
         * 2、根据cid查询出派遣到该单位的所有员工eids(不包括小时工)
         * 3、根据eids的个数 封装好商业保险单明细集合
         * 4、批量插入商业保险结算单明细
         **/
        DaoUpdateResult result = Settlement3Dao.insert(conn,settlement3);
        if(result.success){
            long sid = (long) result.extra;//返回插入后的主键
            System.out.println("sid:"+sid);
            long cid = settlement3.getCid();//合作单位id
            long pid = settlement3.getPid();//产品id
            Date month = settlement3.getMonth();//月份

            //根据cid获取所有外派员工eids，不包括小时工
            QueryParameter parameter = new QueryParameter();
            QueryConditions conditions = new QueryConditions();
            conditions.add("cid", "=", cid);
            conditions.add("type", "=", 1);
            conditions.add("category", "!=", 2);
            String sql = "id";
            parameter.conditions = conditions;
            //获取到员工的所有id
            List<Long> eids = DbUtil.getColumns(conn, sql,"employee",parameter);
            for(long eid:eids){
                System.out.println(eid);
                Detail3 detail3 = new Detail3();//生成明细对象
                detail3.setSid(sid);
                detail3.setEid(eid);
                detail3.setPid(pid);
                detail3.setMonth(month);
                System.out.println(detail3);
            }
            //批量插入明细
        }else {

        }
        return null;
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        return Settlement3Dao.delete(conn,id);
    }

    public static DaoUpdateResult saveAs(Connection conn, long id,Date month) {
        /**流程
         * 1、查询出结算单，修改结算月份
         * 2、插入结算单，返回主键id
         * 3、获取结算单明细集合，修改月份和id
         * 4、批量插入数据库
         */
        Settlement3 settlement3 = (Settlement3) Settlement3Dao.get(conn, id).data;
        settlement3.setMonth(month);
        long sid = (long) Settlement3Dao.insert(conn, settlement3).extra;
        System.out.println("sid:"+sid);
        //获取结算单明细列表
        QueryParameter parameter = new QueryParameter();
        QueryConditions conditions = new QueryConditions();
        conditions.add("sid", "=", id);
        parameter.conditions = conditions;
        DaoQueryListResult result = DbUtil.getList(conn,"detail3",parameter,Detail3.class);

        return null;
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
                System.out.println(log);
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
        DaoUpdateResult result = Settlement3Dao.check(conn,id,status);
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
                log.setType((byte) 2);
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
                System.out.println(log);
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

}
