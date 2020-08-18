package dao;

import bean.log.Transaction;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryParameter;

import java.sql.Connection;

//资金往来明细Dao类
public class TransactionDao {
    /**
     * 查询该公司所有明细
     * @param conn 连接数据库
     * @param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public  static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"transaction", param, Transaction.class);
    }

    /**
     * 添加资金往来信息
     * @param conn 连接数据库
     * @param t 添加信息对象
     * @return 更新结果，格式："{success:true,msg:"",effects:1}"
     */
    public  static DaoUpdateResult insert(Connection conn, Transaction t){
        String sql = "insert into transaction(cid, time, money, comments) values (?,?,?,?)";
        Object[] params = {t.getCid(), t.getTime(), t.getMoney(), t.getComments()};
        return DbUtil.insert(conn, sql, params);
    }
}
