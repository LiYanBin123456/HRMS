package service.settlement;

import bean.admin.Account;
import bean.contract.Serve;
import bean.employee.Deduct;
import bean.employee.Employee;
import bean.employee.ViewEmployee;
import bean.log.Log;
import bean.settlement.*;
import dao.LogDao;
import dao.contract.ServeDao;
import dao.employee.DeductDao;
import dao.employee.EmployeeDao;
import dao.settlement.Detail2Dao;
import dao.settlement.Settlement2Dao;
import database.ConnUtil;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryParameter;
import utills.CollectionUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettlementService2 {

    //获取结算单列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement2Dao.getList(conn,param);
    }

    //插入结算单
    public static DaoUpdateResult insert(Connection conn, Settlement2 settlement2, boolean needDetail) {
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult result ;
        //获取合同中的服务项目
        Serve serve = (Serve) ServeDao.get(conn,settlement2.getCcid()).data;
        float price = serve.getValue();//获取合同中的小时工价格
        settlement2.setPrice(price);
        result = Settlement2Dao.insert(conn,settlement2);

        if(result.success && needDetail){
            long sid = (long) result.extra;//结算单id
            long cid = settlement2.getCid();//合作单位id
            long did = settlement2.getDid();//派遣方id
            //根据条件找到派遣到该单位的员工列表，条件有cid，did，类型为外派员工，用工性质是小时工，在职
            QueryParameter parameter = new QueryParameter();
            parameter.addCondition("cid","=",cid);
            parameter.addCondition("did","=",did);
            parameter.addCondition("type","=",1);
            parameter.addCondition("category","=", Employee.CATEGORY_5);
            parameter.addCondition("status","=",0);
            List<ViewEmployee> employeeList = (List<ViewEmployee>) EmployeeDao.getList(conn,parameter).rows;
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

        Settlement2 settlement2 = (Settlement2) Settlement2Dao.get(conn, id).data;
        settlement2.setMonth(month);

        DaoUpdateResult result = Settlement2Dao.insert(conn, settlement2);
        long sid = (long) result.extra;

        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",id);
        //根据复制的结算单id查询出所有的结算单明细
        List<Detail2> detail2List = (List<Detail2>) Detail2Dao.getList(conn,parameter).rows;
        for(Detail2 detail2 :detail2List){
            //重新赋结算单id
            detail2.setSid(sid);
        }
        //重新插入数据库
        DaoUpdateResult result1 =Detail2Dao.importDetails(conn,detail2List);

        if(result.success&&result1.success){//事务
            ConnUtil.commit(conn);
            return result;
        }else {//回滚
            ConnUtil.rollback(conn);
            result1.msg = "另存为失败";
            return result1;
        }
    }
    //删除结算单
    public static DaoUpdateResult delete(Connection conn, Long id) {
        return Settlement2Dao.delete(conn,id);
    }


    //提交
    public static DaoUpdateResult commit(Connection conn, long id, Account account) {
        /**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement2Dao.updateStatus(conn, id, Settlement.STATUS_COMMITED);
        if(result.success){//修改成功，插入日志
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "提交";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 1);
                log.setOperator(operator);
                log.setContent(content);
                //插入log信息
                LogDao.insert(conn,log);
        }
        return result;
    }

    //审核
    public static DaoUpdateResult check(Connection conn, long id, byte level,boolean result,String reason,Account user) {
        /**流程
         *1、修改结算单状态为提交
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        byte status;
        if(level==0){
            status = result?Settlement.STATUS_CHECKED1:Settlement.STATUS_EDITING;
        }else{
            status = result?Settlement.STATUS_CHECKED2:Settlement.STATUS_COMMITED;
        }
        DaoUpdateResult res = Settlement2Dao.updateStatus(conn,id,status);
        if(res.success){//修改成功，插入日志
                //封装log信息
            String operator = user.getNickname()+"("+user.getId()+")";
            String content = (level==0?"初审":"终审")+(result?"通过":("不通过:"+reason));
            Log log = new Log();
            log.setSid(id);
            log.setType((byte) 1);
            log.setOperator(operator);
            log.setContent(content);
            //插入log信息
            LogDao.insert(conn,log);
        }
        return res;
    }

    //重置
    public static DaoUpdateResult reset(Connection conn, long id, Account account) {
        /**流程
         *1、修改结算单状态为重置
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement2Dao.updateStatus(conn, id, Settlement3.STATUS_EDITING);
        if(result.success){//修改成功，插入日志
            //封装log信息
            String operator = account.getNickname()+"("+account.getId()+")";
            String content = "重置";
            Log log = new Log();
            log.setSid(id);
            log.setType((byte) 1);
            log.setOperator(operator);
            log.setContent(content);
            //插入log信息
            LogDao.insert(conn,log);
        }
        return result;
    }

    //扣款
    public static DaoUpdateResult charge(Connection conn, long id, Account account) {
        /**流程
         *1、修改结算单状态为扣款
         * 2、根据aid查询出管理员
         * 2、插入日志
         */
        DaoUpdateResult result = Settlement2Dao.updateStatus(conn, id, Settlement.STATUS_PAYED1);
        if(result.success){//修改成功，插入日志
                //封装log信息
                String operator = account.getNickname()+"("+account.getId()+")";
                String content = "扣款";
                Log log = new Log();
                log.setSid(id);
                log.setType((byte) 1);
                log.setOperator(operator);
                log.setContent(content);
                //插入log信息
                LogDao.insert(conn,log);
        }
        return result;
    }

    //发放
    public static DaoUpdateResult payroll(Connection conn, long id, Account account) {
        /**流程
         * 1、修改结算单状态为发放
         * 2、获取结算单明细并且修改员工个税专项扣除中的累计收入，累计已预缴税额，累计减免
         * 3、插入日志
         */
        ConnUtil.closeAutoCommit(conn);
        //确认发放
        DaoUpdateResult res1 = Settlement2Dao.updateStatus(conn, id, Settlement.STATUS_PAYED2);

        //获取所有小时工明细
        QueryParameter p1 = new QueryParameter();
        p1.addCondition("sid","=",id);
        List<ViewDetail2> details= (List<ViewDetail2>) Detail2Dao.getList(conn,p1).rows;

        //获取所有员工的个税专项扣除
        QueryParameter p2 = new QueryParameter();
        p2.addCondition("eid","in",CollectionUtil.getKeySerial(details,"eid"));
        List<Deduct> deducts = (List<Deduct>) DeductDao.getList(conn, p2).rows;

        //修改员工的个税专项扣除
        for (ViewDetail2 d:details){
            Deduct deduct = CollectionUtil.getElement(deducts,"eid",d.getEid());
            if(deduct!=null){//个税专项扣除存在于集合中，只需要加累计收入和累计已预缴税额
                //累计收入
                deduct.setIncome(deduct.getIncome()+d.getPayable());
                //累计已预缴税额=累计已预缴税额+个税
                deduct.setPrepaid(deduct.getPrepaid()+d.getTax());
            }
        }

        DaoUpdateResult res2 = DeductDao.updateDeducts(conn,deducts);


        //日志
        String operator = account.getNickname()+"("+account.getId()+")";
        String content = "发放";
        Log log = new Log();
        log.setSid(id);
        log.setType((byte) 1);
        log.setOperator(operator);
        log.setContent(content);

        DaoUpdateResult res3 = LogDao.insert(conn,log);
        if(res1.success&&res2.success&&res3.success){
           ConnUtil.commit(conn);
        }else {
            ConnUtil.rollback(conn);
            res1.success=false;
            res1.msg="数据库操作错误";
        }

        return res1;
    }
    //获取该结算单的所有日志
    public static DaoQueryListResult getLogs(Connection conn, long id, QueryParameter parameter) {
        parameter.conditions.add("type","=",1);
        return LogDao.getList(conn,id,parameter);
    }

    public static DaoUpdateResult saveSettlement(Connection conn, long sid) {
        Settlement2 settlement = (Settlement2) Settlement2Dao.get(conn,sid).data;
        QueryParameter param = new QueryParameter();
        param.addCondition("sid","=",sid);
        List<Detail2> details = (List<Detail2>) Detail2Dao.getList(conn,param).rows;
        Serve serve = (Serve) ServeDao.get(conn,settlement.getCcid()).data;
        settlement.calc(details,serve.getPayer());
        return Settlement2Dao.update(conn, settlement);
    }
}
