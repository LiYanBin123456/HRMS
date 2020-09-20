package service.settlement;

import bean.employee.Employee;
import bean.settlement.Detail1;
import bean.settlement.Detail2;
import bean.settlement.ViewDetail1;
import bean.settlement.ViewDetail2;
import dao.employee.EmployeeDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Detail2Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryConditions;
import database.QueryParameter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Detail1Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param, long id){
        param.conditions.add("sid","=",id);
        return Detail1Dao.getList(conn,param);
    }
    public static DaoUpdateResult update(Connection conn, List<Detail1> details ){
        return  Detail1Dao.update(conn,details);
    }
    public static DaoUpdateResult importDetails(Connection conn, long sid, List<ViewDetail1> viewDetail1s, long did){
        DaoUpdateResult result = new DaoUpdateResult();
        List<Detail1> detail1s =new ArrayList<>();

        for(ViewDetail1 v :viewDetail1s){
            QueryConditions conditions = new QueryConditions();
            conditions.add("cardId","=",v.getCardId());
            conditions.add("did","=",did);
            if(!EmployeeDao.exist(conn,conditions).exist){
                result.msg = "用户"+v.getName()+"不存在，或者身份证id不正确，请核对";
                return  result;
            }
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data; //根据员工身份证获取员工id
            Detail1 detail1 = new Detail1(0,sid,employee.getId(),v.getBase(),v.getPension1(),v.getMedicare1(),v.getUnemployment1(),v.getDisease1(),v.getFund1(),v.getPension2(),v.getMedicare2()
            ,v.getUnemployment2(),v.getInjury(),v.getDisease2(),v.getBirth(),v.getFund2(),v.getTax(),v.getPayable(),v.getPaid(),v.getF1(),v.getF2(),v.getF3(),v.getF4(),v.getF5(),v.getF6(),v.getF7()
            ,v.getF8(),v.getF9(),v.getF10(),v.getF11(),v.getF12(),v.getF13(),v.getF14(),v.getF15(),v.getF16(),v.getF17(),v.getF18(),v.getF19(),v.getF20(),v.getStatus());
            detail1s.add(detail1);
        }
        result = Detail1Dao.importDetails(conn,detail1s);
        return  result;
    }
    public static String backup(Connection conn,Long id,String month){
       return null;
    }
    public static String makeup(Connection conn,Long id,String month){
        return null;
    }
}
