package dao.employee;

import bean.employee.Employee;
import bean.employee.PayCard;
import database.DaoQueryResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryConditions;

import java.sql.Connection;

public class PayCardDao {
    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"payCard",conditions,PayCard.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, PayCard p) {
        String sql = "update payCard set bank1=?,bank2=?,bankNo=?,cardNo=? where eid = ?";
        Object []params = {p.getBank1(),p.getBank2(),p.getBankNo(),p.getEid()};
        return  DbUtil.update(conn,sql,params);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, PayCard p) {
        String sql = "insert into payCard (eid,bank1,bank2,bankNo,cardNo) values (?,?,?,?,?)";
        Object []params = {p.getEid(),p.getBank1(),p.getBank2(),p.getBankNo()};
        return  DbUtil.update(conn,sql,params);
    }

}
