package dao.settlement;

import bean.settlement.Detail2;
import bean.settlement.ViewDetail2;
import database.*;

import java.sql.Connection;
import java.util.List;

public class Detail2Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"view_detail2",param, ViewDetail2.class);
    }
    public static DaoUpdateResult update(Connection conn, List<Detail2> details){
        String sql = "update detail2 set month=?,hours=?,price=?,food=?,traffic=?,accommodation=?,utilities=?,insurance=?,tax=?,other1=?\n" +
                ",other2=?,payable=?,paid=? where id = ?";
        Object [][]params = new Object[details.size()][];
        for (int i = 0; i < details.size(); i++) {
            params[i] = new Object[]{details.get(i).getMonth(),details.get(i).getHours(),details.get(i).getPrice()
                    ,details.get(i).getFood(),details.get(i).getTraffic(),details.get(i).getAccommodation(),details.get(i).getUtilities()
                    ,details.get(i).getInsurance(),details.get(i).getTax(),details.get(i).getOther1(),details.get(i).getOther2(),details.get(i).getPayable()
                    ,details.get(i).getPaid(),details.get(i).getId()};
        }
        return DbUtil.batch(conn,sql,params);
    }
    public static DaoUpdateResult importDetails(Connection conn, List<Detail2> details){
        String sql = "insert detail2 (sid,eid,month,hours,price,food,traffic,accommodation,utilities,insurance,tax,other1\n" +
                ",other2,payable,paid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object [][]params = new Object[details.size()][];
        for (int i = 0; i < details.size(); i++) {
            params[i] = new Object[]{details.get(i).getSid(),details.get(i).getEid(),details.get(i).getMonth(),details.get(i).getHours(),details.get(i).getPrice()
                    ,details.get(i).getFood(),details.get(i).getTraffic(),details.get(i).getAccommodation(),details.get(i).getUtilities()
                    ,details.get(i).getInsurance(),details.get(i).getTax(),details.get(i).getOther1(),details.get(i).getOther2(),details.get(i).getPayable()
                    ,details.get(i).getPaid()};
        }
        return DbUtil.insertBatch(conn,sql,params);
    }
    public static String exportDetails(Connection conn,long id){
        return  null;
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"detail2",conditions);
    }
}
