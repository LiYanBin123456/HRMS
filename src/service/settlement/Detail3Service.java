package service.settlement;

import bean.employee.Employee;
import bean.insurance.Product;
import bean.settlement.*;
import dao.employee.EmployeeDao;
import dao.product.ProductDao;
import dao.settlement.Detail2Dao;
import dao.settlement.Detail3Dao;
import dao.settlement.Settlement3Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryConditions;
import database.QueryParameter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Detail3Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param,long id){
        param.conditions.add("sid","=",id);
        return Detail3Dao.getList(conn,param);
    }
    public static DaoUpdateResult update(Connection conn, List<Detail3> details){
        return  Detail3Dao.update(conn,details);
    }
    public static String exportDetails(Connection conn,long id){
        return  null;
    }

    public static DaoUpdateResult importDetails(Connection conn, long sid, List<ViewDetail3> ViewDetail3s, long did) {
        DaoUpdateResult result = new DaoUpdateResult();
        List<Detail3> detail3s =new ArrayList<>();

        Settlement3 settlement3 = (Settlement3) Settlement3Dao.get(conn,sid).data;
        long pid = settlement3.getPid();//商业保险id
        float price = settlement3.getPrice();//保险产品价格

        for(ViewDetail3 v3 :ViewDetail3s){
            QueryConditions conditions = new QueryConditions();
            conditions.add("cardId","=",v3.getCardId());
            conditions.add("did","=",did);

           if(!EmployeeDao.exist(conn,conditions).exist){
                result.msg = "用户"+v3.getCname()+"不存在，或者身份证id不正确，请核对";
                return  result;
            }

            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data; //根据员工身份证获取员工
            //封装detail3
            Detail3 detail3 = new Detail3(0,sid,employee.getId(),pid,v3.getPlace(),price);
            detail3s.add(detail3);

        }
        result = Detail3Dao.importDetails(conn,detail3s);
        return result;
    }
}
