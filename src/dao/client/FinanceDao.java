package dao.client;

import bean.client.Finance;
import bean.client.ViewFinance;
import bean.log.Transaction;
import bean.settlement.ViewTax;
import database.*;

import java.sql.Connection;

public class FinanceDao {

    public static DaoQueryResult get(Connection conn, long cid,byte type) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid","=",cid);
        conditions.add("type","=",type);
        return DbUtil.get(conn,"finance",conditions, Finance.class);
    }

    //判断客户财务信息是否存在
    public static DaoExistResult existFinance(Connection conn, long cid){
        QueryConditions conditions = new QueryConditions();
        conditions.add("type","=",1);
        conditions.add("cid","=",cid);
        return DbUtil.exist(conn,"finance",conditions);
    }

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"view_finance",param,ViewFinance.class);
    }

    public static DaoUpdateResult update(Connection conn, Finance f) {
        String sql = "update finance set type=?,code=?,bank=?,cardNo=?,contact=?,phone=?,address=?,bankNo=?,balance=?,comments=? where cid=?";
        Object []params = {f.getType(),f.getCode(),f.getBank(),f.getCardNo(),f.getContact(),f.getPhone(),f.getAddress(),f.getBankNo(),f.getBalance(),f.getComments(),f.getCid()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult insert(Connection conn, Finance f) {
        String sql = "insert into finance(cid,type,code,bank,cardNo,contact,phone,address,bankNo,balance,comments) values (?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {f.getCid(),f.getType(),f.getCode(),f.getBank(),f.getCardNo(),f.getContact(),f.getPhone(),f.getAddress(),f.getBankNo(),f.getBalance(),f.getComments()};
        return  DbUtil.insert(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, long id,int type){
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid","=",id);
        conditions.add("type","=",type);
        return DbUtil.delete(conn,"finance",conditions);
    }

    /**
     * 根据账户编号增减账户余额
     * @param conn 连接数据库
     * @param balance 修改的金额值
     * @param id 客户编号
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoUpdateResult arrive(Connection conn, float balance, long id) {
        String sql = "update finance set balance = balance + ? where type = 1 and cid = ?";
        Object []params = {balance, id};
        return DbUtil.update(conn, sql, params);
    }

    /**
     * 获取资金往来明细
     * @param conn 连接数据库
     * @param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoQueryListResult getTransactions(Connection conn, QueryParameter param) {
        return DbUtil.getList(conn, "transaction", param, Transaction.class);
    }

    /**
     * 添加资金明细
     * @param conn 连接数据库
     * @param t 添加资金明细对象
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoUpdateResult insertTransactions(Connection conn, Transaction t){
        String sql = "insert into transaction (cid, time, money, comments) values (?,?,?,?)";
        Object[] params = {t.getCid(), t.getTime(), t.getMoney(), t.getComments()};
        return DbUtil.insert(conn, sql, params);
    }

    /**
     * 判断是否存在资金明细
     * @param conn
     * @param cid
     * @return
     */
    public static DaoExistResult existTransactions(Connection conn, long cid){
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",cid);
        return DbUtil.exist(conn,"transactions",conditions);
    }
    /**
     * 修改资金明细
     * @param conn 连接数据库
     * @param t 添加资金明细对象
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoUpdateResult updateTransactions(Connection conn, Transaction t){
        String sql = "update  transaction set  time=?, money=?, comments=? where cid=?";
        Object[] params = {t.getTime(), t.getMoney(), t.getComments(),t.getCid()};
        return DbUtil.update(conn, sql, params);
    }


    public static  DaoQueryListResult getTaxs(Connection conn,QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"view_tax",param, ViewTax.class);
    }
}
