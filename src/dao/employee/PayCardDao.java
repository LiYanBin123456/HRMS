package dao.employee;

import bean.employee.PayCard;
import database.*;

import java.sql.Connection;
import java.util.List;

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

    public static DaoUpdateResult insertBatch(Connection conn, List<PayCard> p) {
        String sql = "insert paycard (eid,bank1,bank2,bankNo,cardNo) values (?,?,?,?,?)";
        Object [][]params = new Object[p.size()][];
        for (int i = 0; i < p.size(); i++) {
            params[i] = new Object[]{
                    p.get(i).getEid(),p.get(i).getBank1(),p.get(i).getBank2(),
                    p.get(i).getBankNo(),p.get(i).getCardNo()
            };
        }
        return DbUtil.insertBatch(conn,sql,params);
    }
}
