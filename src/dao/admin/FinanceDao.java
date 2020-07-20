package dao.admin;

import bean.admin.Finance;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryConditions;

import java.sql.Connection;

public class FinanceDao {

    public DaoQueryResult getFinance(Connection conn, long cid) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid","=",cid);
        return DbUtil.get(conn,"finance",conditions, Finance.class);
    }

    public DaoUpdateResult updateFinance(Connection conn, Finance f) {
        String sql = "update finance set credit_code=?,bank=?,cardNo=?,contact=?,phone=?,address=? where cid=?";
        Object []params = { f.getCredit_code(),f.getBank(),f.getCardNo(),f.getContact(),f.getPhone(),f.getAddress(),f.getCid()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    public DaoUpdateResult insertFinance(Connection conn, Finance f) {
        String sql = "insert into finance(cid,credit_code,bank,cardNo,contact,phone,address) values (?,?,?,?,?,?,?)";
        Object []params = {f.getCid(),f.getCredit_code(),f.getBank(),f.getCardNo(),f.getContact(),f.getPhone(),f.getAddress()};
        return  DbUtil.insert(conn,sql,params);
    }

    public DaoUpdateResult deleteFinance(Connection conn, long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("cid","=",id);
        return DbUtil.delete(conn,"finance",conditions);
    }

}
