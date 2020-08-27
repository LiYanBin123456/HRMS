package dao.settlement;

import bean.settlement.Detail1;
import bean.settlement.ViewDetail1;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryParameter;


import java.sql.Connection;
import java.util.List;

public class Detail1Dao {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return DbUtil.getList(conn,"view_detail1",param, ViewDetail1.class);
    }
    public static DaoUpdateResult update(Connection conn, List<Detail1> details){
        String sql = "update detail1 set month=?,base=?,pension1=?,medicare1=?,unemployment1=?,disease1=?,fund1=?,pension2=?,medicare2=?,unemployment2=?,injury=?,disease2=?,birth=?,fund2=?,tax=?,payable=?,paid=?,f1=?,f2=?,f3=?,f4=?,f5=?,f6=?,f7=?,f8=?,f9=?,f10=?,f11=?,f12=?,f13=?,f14=?,f15=?,f16=?,f17=?,f18=?,f19=?,f20=? where id = ?";
        Object [][]params = new Object[details.size()][];
        for (int i = 0; i < details.size(); i++) {
            params[i] = new Object[]{details.get(i).getMonth(),details.get(i).getBase(),details.get(i).getPension1(),details.get(i).getMedicare1(),
                    details.get(i).getUnemployment1(),details.get(i).getDisease1(),details.get(i).getFund1(),details.get(i).getPension2(),details.get(i).getMedicare2(),details.get(i).getUnemployment2(),
                    details.get(i).getInjury(),details.get(i).getDisease2(),details.get(i).getBirth(),details.get(i).getFund2(),details.get(i).getTax(),details.get(i).getPayable(),details.get(i).getPaid(),
                    details.get(i).getF1(),details.get(i).getF2(),details.get(i).getF3(),details.get(i).getF4(),details.get(i).getF5(),details.get(i).getF6(),details.get(i).getF7(),details.get(i).getF8(),
                    details.get(i).getF9(),details.get(i).getF10(),details.get(i).getF11(),details.get(i).getF12(),details.get(i).getF13(),details.get(i).getF14(),details.get(i).getF15(),details.get(i).getF16(),
                    details.get(i).getF17(),details.get(i).getF18(),details.get(i).getF19(),details.get(i).getF20(),details.get(i).getId()};
        }
        return DbUtil.batch(conn,sql,params);
    }

    public static DaoUpdateResult importDetails(Connection conn, List<Detail1> details){
        String sql = "insert detail1 (sid,eid,month,base,pension1,medicare1,unemployment1,disease1,fund1,pension2,medicare2,unemployment2,injury,disease2,birth,fund2,tax,payable,paid,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18,f19,f20,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object [][]params = new Object[details.size()][];
        for (int i = 0; i < details.size(); i++) {
            params[i] = new Object[]{details.get(i).getSid(),details.get(i).getEid(),details.get(i).getMonth(),details.get(i).getBase(),details.get(i).getPension1(),details.get(i).getMedicare1(),
                    details.get(i).getUnemployment1(),details.get(i).getDisease1(),details.get(i).getFund1(),details.get(i).getPension2(),details.get(i).getMedicare2(),details.get(i).getUnemployment2(),
                    details.get(i).getInjury(),details.get(i).getDisease2(),details.get(i).getBirth(),details.get(i).getFund2(),details.get(i).getTax(),details.get(i).getPayable(),details.get(i).getPaid(),
                    details.get(i).getF1(),details.get(i).getF2(),details.get(i).getF3(),details.get(i).getF4(),details.get(i).getF5(),details.get(i).getF6(),details.get(i).getF7(),details.get(i).getF8(),
                    details.get(i).getF9(),details.get(i).getF10(),details.get(i).getF11(),details.get(i).getF12(),details.get(i).getF13(),details.get(i).getF14(),details.get(i).getF15(),details.get(i).getF16(),
                    details.get(i).getF17(),details.get(i).getF18(),details.get(i).getF19(),details.get(i).getF20(),details.get(i).getStatus()};
        }
        return DbUtil.insertBatch(conn,sql,params);
    }
    public static String exportDetails(Connection conn,long id){
        return  null;
    }
    public static String backup(Connection conn,Long id,String month){
        return null;
    }
    public static String makeup(Connection conn,Long id,String month){
        return null;
    }
}
