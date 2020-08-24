package dao.insurance;

import bean.admin.Notice;
import bean.insurance.Insurance;
import bean.insurance.ViewInsurance;
import database.*;

import java.sql.Connection;

public class InsuranceDao {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param,byte category){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(status,name,cardId)","like",param.conditions.extra);
        }
        param.addCondition("type","=",category);
        return  DbUtil.getList(conn,"view_insurance",param, ViewInsurance.class);
    }

    public static DaoQueryResult get(Connection conn,QueryConditions conditions) {
        return DbUtil.get(conn,"view_insurance",conditions,ViewInsurance.class);
    }

    public static DaoUpdateResult update(Connection conn, Insurance i) {
        String sql = "update insurance set code=?,start=?,money=?,status=?,reason=? where eid=? and type = ?";
        Object []params = {i.getCode(),i.getStart(),i.getMoney(),i.getStatus(),i.getReason(),i.getEid(),i.getType()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    //删除
    public static DaoUpdateResult delete(Connection conn, long id,byte category) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        conditions.add("type","=",category);
        return DbUtil.delete(conn,"insurance",conditions);
    }

    //校对参保单
    public static String check(Connection conn,byte category){
       return null;
    }

    //导出参保单
    public static String export(Connection conn,byte category){
        return null;
    }

    //批量插入
    public static DaoUpdateResult insertBatch(Connection conn, Notice n,byte category) {
        return null;
    }
}
