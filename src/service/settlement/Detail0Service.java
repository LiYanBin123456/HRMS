package service.settlement;

import bean.client.Cooperation;
import bean.client.MapSalary;
import bean.employee.Deduct;
import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.*;
import dao.client.CooperationDao;
import dao.client.MapSalaryDao;
import dao.employee.DeductDao;
import dao.employee.EmployeeDao;
import dao.employee.SettingDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import dao.settlement.Detail0Dao;
import dao.settlement.Detail1Dao;
import dao.settlement.Settlement1Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryConditions;
import database.QueryParameter;
import utills.Calculate;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Detail0Service {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param, long id){
        param.conditions.add("sid","=",id);
        return Detail0Dao.getList(conn,param);
    }

    public static DaoUpdateResult update(Connection conn, List<Detail0> details ){
        return  Detail0Dao.update(conn,details);
    }

    public static DaoUpdateResult importDetails(Connection conn, long sid, List<ViewDetail0> viewDetails, long did){
        DaoUpdateResult result = new DaoUpdateResult();
        List<Detail0> details =new ArrayList<>();

        for(ViewDetail0 v :viewDetails){
            if(v.getEid()!=0){//员工id存在
                Detail0 detail = new Detail0(sid,v.getEid(),v.getAmount(),v.getTax(),v.getPaid());
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
                Detail0 detail = new Detail0(sid,employee.getId(),v.getAmount(),v.getTax(),v.getPaid());
                details.add(detail);
            }
        }
        result = Detail0Dao.importDetails(conn,details);
        return  result;
    }

    //计算结算单明细并修改
    public static DaoUpdateResult saveDetail(Connection conn, long sid) {
        DaoUpdateResult result = new DaoUpdateResult();
        List<Detail0> detailList  = new ArrayList<>();//新建一个集合用于存放计算好后的明细
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("sid","=",sid);
        List<ViewDetail0> vs = (List<ViewDetail0>) Detail0Dao.getList(conn,parameter).rows;
        for(ViewDetail0 d:vs){
            float tax = (float) Calculate.calculateTax(d.getAmount()/12);
            float paid = d.getAmount()-tax;
            d.setTax(tax);
            d.setPaid(paid);
            detailList.add(d);
        }
        return  Detail0Dao.update(conn,detailList);
    }

}
