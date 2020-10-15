package service.settlement;

import bean.employee.Deduct;
import bean.employee.Employee;
import bean.settlement.Detail2;
import bean.settlement.ViewDetail2;
import dao.employee.DeductDao;
import dao.employee.EmployeeDao;
import dao.settlement.Detail2Dao;
import dao.settlement.Detail3Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryConditions;
import database.QueryParameter;
import utills.Calculate;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Detail2Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param, long id){
        param.conditions.add("sid","=",id);
        return Detail2Dao.getList(conn,param);
    }
    public static DaoUpdateResult update(Connection conn, List<Detail2> details){
        return Detail2Dao.update(conn,details);
    }
    public static DaoUpdateResult importDetails(Connection conn, long sid, List<ViewDetail2> ViewDetail2s, long did){
        DaoUpdateResult result = new DaoUpdateResult();
        List<Detail2> detail2s =new ArrayList<>();

        for(ViewDetail2 v2 :ViewDetail2s){
            QueryConditions conditions = new QueryConditions();
            conditions.add("cardId","=",v2.getCardId());
            conditions.add("did","=",did);
            if(!EmployeeDao.exist(conn,conditions).exist){
                 result.msg = "用户"+v2.getName()+"不存在，或者身份证id不正确，请核对";
                 return  result;
            }

            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data; //根据员工身份证获取员工id
            Detail2 detail2 = new Detail2(0,sid,employee.getId(),v2.getHours(),employee.getPrice(),v2.getFood()
            ,v2.getTraffic(),v2.getAccommodation(),v2.getUtilities(),v2.getInsurance(),v2.getTax(),v2.getOther1()
            ,v2.getOther2(),v2.getPayable(),v2.getPaid());
            detail2s.add(detail2);//封装detail2
            System.out.println(detail2);
        }
        result =Detail2Dao.importDetails(conn,detail2s);
        return  result;
    }

    public static DaoUpdateResult saveDetail(Connection conn, long sid) {
        QueryParameter param = new QueryParameter();
        param.addCondition("sid","=",sid);
        List<Detail2> detail2List = (List<Detail2>) Detail2Dao.getList(conn,param).rows;
        List<Detail2> detail2s = new ArrayList<>();
        for(Detail2 detail2:detail2List){
            Deduct deduct = (Deduct) DeductDao.get(conn,detail2.getEid()).data;
            detail2s.add(Calculate.calculatteDetail2(detail2,deduct));
        }

        return Detail2Dao.update(conn,detail2s);
    }
}
