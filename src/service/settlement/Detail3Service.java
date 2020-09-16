package service.settlement;

import bean.employee.Employee;
import bean.insurance.Product;
import bean.settlement.Detail2;
import bean.settlement.Detail3;
import bean.settlement.ViewDetail2;
import bean.settlement.ViewDetail3;
import dao.employee.EmployeeDao;
import dao.product.ProductDao;
import dao.settlement.Detail2Dao;
import dao.settlement.Detail3Dao;
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
        DaoUpdateResult result = null;
        List<Detail3> detail3s =new ArrayList<>();

        for(ViewDetail3 v3 :ViewDetail3s){
            QueryConditions conditions = new QueryConditions();
            conditions.add("cardId","=",v3.getCardId());
            conditions.add("did","=",did);
           if(!EmployeeDao.exist(conn,conditions).exist){
                result.msg = "用户"+v3.getCname()+"不存在，或者身份证id不正确，请核对";
                return  result;
            }
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data; //根据员工身份证获取员工
            QueryConditions conditions2 = new QueryConditions();
            conditions.add("name","=",v3.getPname());
            conditions.add("did","=",did);
            if(!ProductDao.exist(conn,conditions).exist){
                result.msg = "保险产品"+v3.getPname()+"不存在，请核对";
                return  result;
            }
            Product product = (Product) ProductDao.get(conn,conditions2).data; //根据保险产品名称获取保险产品

            //封装detail3
            Detail3 detail3 = new Detail3(0,sid,employee.getId(),product.getId(),v3.getPlace(),v3.getPrice());
            detail3s.add(detail3);
        }
        result = Detail3Dao.importDetails(conn,detail3s);
        return result;
    }
}
