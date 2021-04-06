package service.settlement;

import bean.employee.Employee;
import bean.settlement.Detail4;
import bean.settlement.ViewDetail4;
import com.alibaba.fastjson.JSONObject;
import dao.employee.EmployeeDao;
import dao.settlement.Detail4Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryConditions;
import database.QueryParameter;
import utills.Salary.Tax;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DetailService4 {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param, long id){
        param.conditions.add("sid","=",id);
        return Detail4Dao.getList(conn,param);
    }

    public static DaoUpdateResult update(Connection conn, List<Detail4> details ){
        return  Detail4Dao.update(conn,details);
    }

    public static DaoUpdateResult importDetails(Connection conn, long sid, List<ViewDetail4> viewDetails, long did){
        DaoUpdateResult result = new DaoUpdateResult();
        List<Detail4> details =new ArrayList<>();

        for(ViewDetail4 v :viewDetails){
            if(v.getEid()!=0){//员工id存在
                Detail4 detail = new Detail4(sid,v.getEid(),v.getAmount(),v.getTax(),v.getPaid());
                details.add(detail);
            }else {//员工id不存在
                QueryConditions conditions = new QueryConditions();
                conditions.add("cardId","=",v.getCardId());
                conditions.add("did","=",did);
                if(!EmployeeDao.exist(conn,conditions).exist){
                    result.msg = "用户"+v.getName()+"不存在，或者身份证id不正确，请核对";
                    return  result;
                }
                Employee employee = (Employee) EmployeeDao.get(conn,conditions).data; //根据员工身份证获取员工id
                Detail4 detail = new Detail4(sid,employee.getId(),v.getAmount(),v.getTax(),v.getPaid());
                details.add(detail);
            }
        }
        result = Detail4Dao.importDetails(conn,details);
        return  result;
    }

    //计算结算单明细并修改
    public static String calcDetail(Connection conn, long sid) {
        List<Detail4> detailList  = new ArrayList<>();//新建一个集合用于存放计算好后的明细
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        List<ViewDetail4> details = (List<ViewDetail4>) Detail4Dao.getList(conn,parameter).rows;
        for(ViewDetail4 d:details){
            float tax = (float) Tax.tax2(d.getAmount()/12);
            float paid = d.getAmount()-tax;
            d.setTax(tax);
            d.setPaid(paid);
            detailList.add(d);
        }
        DaoUpdateResult res = Detail4Dao.update(conn,detailList);
        return JSONObject.toJSONString(res);
    }

}
