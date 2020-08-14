package dao;

import bean.log.Transaction;
import database.DaoQueryListResult;
import database.QueryParameter;

import java.sql.Connection;

//资金往来明细Dao类
public class TransactionDao {
    //查询该公司所有明细
    public  static DaoQueryListResult getList(Connection conn, QueryParameter param,long id){
        return null;
    }

    //插入资金往来信息
    public  static DaoQueryListResult insert(Connection conn, Transaction t){
        return null;
    }
}
