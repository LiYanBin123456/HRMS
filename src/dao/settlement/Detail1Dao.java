package dao.settlement;

import bean.settlement.Detail1;
import bean.settlement.ViewDetail1;
import database.*;


import java.sql.Connection;
import java.util.List;

public class Detail1Dao {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"view_detail1",param, ViewDetail1.class);
    }

    public static DaoUpdateResult update(Connection conn, List<Detail1> ds){
        String sql = "update detail1 set base=?,pension1=?,medicare1=?,unemployment1=?,disease1=?,fund1=?,pension2=?,medicare2=?,unemployment2=?,injury=?,disease2=?,birth=?,fund2=?,tax=?,tax2=?,free=?,extra1=?,extra2=?,manage=?,sum=?,payable=?,paid=?,f1=?,f2=?,f3=?,f4=?,f5=?,f6=?,f7=?,f8=?,f9=?,f10=?,f11=?,f12=?,f13=?,f14=?,f15=?,f16=?,f17=?,f18=?,f19=?,f20=?,comments1=?,comments2=? where id = ?";
        Object [][]params = new Object[ds.size()][];
        for (int i = 0; i < ds.size(); i++) {
            params[i] = new Object[]{ds.get(i).getBase(),ds.get(i).getPension1(),ds.get(i).getMedicare1(),
                    ds.get(i).getUnemployment1(),ds.get(i).getDisease1(),ds.get(i).getFund1(),ds.get(i).getPension2(),ds.get(i).getMedicare2(),ds.get(i).getUnemployment2(),
                    ds.get(i).getInjury(),ds.get(i).getDisease2(),ds.get(i).getBirth(),ds.get(i).getFund2(),ds.get(i).getTax(),ds.get(i).getTax2(),ds.get(i).getFree(),ds.get(i).getExtra1(),
                    ds.get(i).getExtra2(),ds.get(i).getManage(),ds.get(i).getSum(),ds.get(i).getPayable(),ds.get(i).getPaid(),
                    ds.get(i).getF1(),ds.get(i).getF2(),ds.get(i).getF3(),ds.get(i).getF4(),ds.get(i).getF5(),ds.get(i).getF6(),ds.get(i).getF7(),ds.get(i).getF8(),
                    ds.get(i).getF9(),ds.get(i).getF10(),ds.get(i).getF11(),ds.get(i).getF12(),ds.get(i).getF13(),ds.get(i).getF14(),ds.get(i).getF15(),ds.get(i).getF16(),
                    ds.get(i).getF17(),ds.get(i).getF18(),ds.get(i).getF19(),ds.get(i).getF20(),ds.get(i).getComments1(),ds.get(i).getComments2(),ds.get(i).getId()};
        }
        return DbUtil.batch(conn,sql,params);
    }
    //获取明细
    public static DaoQueryResult get(Connection conn, QueryConditions conditions){
        return DbUtil.get(conn,"view_detail1",conditions, ViewDetail1.class);
    }

    public static DaoUpdateResult importDetails(Connection conn, List<Detail1> ds){
        String sql = "insert detail1 (sid,eid,base,pension1,medicare1,unemployment1,disease1,fund1,pension2,medicare2,unemployment2,injury,disease2,birth,fund2,tax,tax2,free,extra1,extra2,manage,sum,payable,paid,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18,f19,f20,status,comments1,comments2) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object [][]params = new Object[ds.size()][];
        for (int i = 0; i < ds.size(); i++) {
            params[i] = new Object[]{ds.get(i).getSid(),ds.get(i).getEid(),ds.get(i).getBase(),ds.get(i).getPension1(),ds.get(i).getMedicare1(),
                    ds.get(i).getUnemployment1(),ds.get(i).getDisease1(),ds.get(i).getFund1(),ds.get(i).getPension2(),ds.get(i).getMedicare2(),ds.get(i).getUnemployment2(),
                    ds.get(i).getInjury(),ds.get(i).getDisease2(),ds.get(i).getBirth(),ds.get(i).getFund2(),ds.get(i).getTax(),ds.get(i).getTax2(),ds.get(i).getFree(),ds.get(i).getExtra1(),ds.get(i).getExtra2(),
                    ds.get(i).getManage(),ds.get(i).getSum(),ds.get(i).getPayable(),ds.get(i).getPaid(),
                    ds.get(i).getF1(),ds.get(i).getF2(),ds.get(i).getF3(),ds.get(i).getF4(),ds.get(i).getF5(),ds.get(i).getF6(),ds.get(i).getF7(),ds.get(i).getF8(),
                    ds.get(i).getF9(),ds.get(i).getF10(),ds.get(i).getF11(),ds.get(i).getF12(),ds.get(i).getF13(),ds.get(i).getF14(),ds.get(i).getF15(),ds.get(i).getF16(),
                    ds.get(i).getF17(),ds.get(i).getF18(),ds.get(i).getF19(),ds.get(i).getF20(),ds.get(i).getStatus(),ds.get(i).getComments1(),ds.get(i).getComments2()};
        }
        return DbUtil.insertBatch(conn,sql,params);
    }

    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"detail1",conditions);
    }

}
