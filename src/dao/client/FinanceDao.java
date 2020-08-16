package dao.client;

import bean.client.Finance;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryConditions;

import java.sql.Connection;

public class FinanceDao {

    public static DaoQueryResult get(Connection conn, long cid,byte type) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid","=",cid);
        conditions.add("type","=",type);
        return DbUtil.get(conn,"finance",conditions, Finance.class);
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

    //充值 type 0_派遣方单位  1_合作客户单位 因为充值操作只有合作客户才有 所以type默认为1
    public static  DaoUpdateResult recharge(Connection conn, long id,float balance,byte type){
       return null;
    }
}
