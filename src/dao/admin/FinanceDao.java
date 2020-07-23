package dao.admin;

import bean.admin.Finance;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryConditions;

import java.sql.Connection;

public class FinanceDao {

    public DaoQueryResult getFinance(Connection conn, long cid,String type) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid","=",cid);
        conditions.add("type","=",type);
        return DbUtil.get(conn,"finance",conditions, Finance.class);
    }

    public DaoUpdateResult updateFinance(Connection conn, Finance f) {
        String sql = "update finance set type=?,code=?,bank=?,cardNo=?,contact=?,phone=?,address=?,balance=? where cid=?";
        Object []params = {f.getType(),f.getCode(),f.getBank(),f.getCardNo(),f.getContact(),f.getPhone(),f.getAddress(),f.getBalance(),f.getCid()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    public DaoUpdateResult insertFinance(Connection conn, Finance f) {
        String sql = "insert into finance(cid,type,code,bank,cardNo,contact,phone,address,balance) values (?,?,?,?,?,?,?,?,?)";
        Object []params = {f.getCid(),f.getType(),f.getCode(),f.getBank(),f.getCardNo(),f.getContact(),f.getPhone(),f.getAddress(),f.getBalance()};
        return  DbUtil.insert(conn,sql,params);
    }

    public DaoUpdateResult deleteFinance(Connection conn, long id,int type){
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid","=",id);
        conditions.add("type","=",type);
        return DbUtil.delete(conn,"finance",conditions);
    }

}
