package dao.finance;

import bean.log.Transaction;
import bean.settlement.ViewTax;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryParameter;

import java.sql.Connection;

public class FinanceDao {
    /**
     * 根据账户编号增减账户余额
     * @param conn 连接数据库
     * @param balance 修改的金额值
     * @param id 客户编号
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public static DaoUpdateResult arrive(Connection conn, float balance, long id) {
        String sql = "update finance set balance = balance + ? where cid = ?";
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

    public static  DaoQueryListResult getTaxs(Connection conn,QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"view_tax",param, ViewTax.class);
    }
}
