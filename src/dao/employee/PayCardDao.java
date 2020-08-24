package dao.employee;

import bean.employee.PayCard;
import database.*;

import java.sql.Connection;

public class PayCardDao {
    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.get(conn,"payCard",conditions,PayCard.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, PayCard p) {
        String sql = "update payCard set bank1=?,bank2=?,bankNo=?,cardNo=? where eid = ?";
        Object []params = {p.getBank1(),p.getBank2(),p.getBankNo(),p.getCardNo(),p.getEid()};
        return  DbUtil.update(conn,sql,params);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, PayCard p) {
        String sql = "insert into payCard (eid,bank1,bank2,bankNo,cardNo) values (?,?,?,?,?)";
        Object []params = {p.getEid(),p.getBank1(),p.getBank2(),p.getBankNo(),p.getCardNo()};
        return  DbUtil.update(conn,sql,params);
    }

    //判断是否已经存在
    public static DaoExistResult exist(Connection conn, long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.exist(conn,"payCard",conditions);
    }

}
