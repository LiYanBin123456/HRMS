package dao.settlement;

import bean.rule.RuleFund;
import bean.settlement.Settlement1;
import bean.settlement.Settlement3;
import bean.settlement.ViewSettlement3;
import database.*;

import java.sql.Connection;

public class Settlement3Dao {
    //获取结算单列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            //根据地市模糊查询
            param.addCondition("concat(cname,month,status)","like",param.conditions.extra);
        }
        return DbUtil.getList(conn, "ViewSettlement3", param, ViewSettlement3.class);

    }
    //获取结算单详情
    public static DaoQueryResult get(Connection conn,long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"settlement3",conditions,Settlement3.class);
    }
    //添加结算单
    public static DaoUpdateResult insert(Connection conn, Settlement3 s) {
        String sql = "insert into settlement3 (did,cid,pid,month,price,status,source) values (?,?,?,?,?,?,?)";
        Object []params = {s.getDid(),s.getCid(),s.getPid(),s.getMonth(),s.getPrice(),s.getStatus(),s.getSource()};
        return DbUtil.insert(conn,sql,params);
    }

    //删除结算单
    public static DaoUpdateResult delete(Connection conn, Long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"settlement3",conditions);
    }

    public static DaoUpdateResult copy(Connection conn, long id,String month) {
        return null;
    }

}
