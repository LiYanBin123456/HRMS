package service.settlement;

import bean.contract.Serve;
import bean.employee.Deduct;
import bean.employee.Employee;
import bean.settlement.Detail2;
import bean.settlement.Settlement2;
import bean.settlement.ViewDetail2;
import bean.settlement.ViewSettlement2;
import com.alibaba.fastjson.JSONObject;
import dao.contract.ServeDao;
import dao.employee.DeductDao;
import dao.employee.EmployeeDao;
import dao.settlement.Detail2Dao;
import dao.settlement.Settlement2Dao;
import database.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DetailService2 {
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
        try {
            for(ViewDetail2 v2 :ViewDetail2s){
                if(v2.getEid()!=0){//员工id存在
                    QueryConditions conditions = new QueryConditions();
                    conditions.add("id","=",v2.getEid());
                    Employee employee = (Employee) EmployeeDao.get(conn,conditions).data; //根据员工身份证获取员工id
                    Detail2 detail2 = new Detail2(0,sid,employee.getId(),v2.getHours(),employee.getPrice(),v2.getFood()
                            ,v2.getTraffic(),v2.getAccommodation(),v2.getUtilities(),v2.getInsurance(),v2.getTax(),v2.getOther1()
                            ,v2.getOther2(),v2.getPayable(),v2.getPaid());
                    detail2s.add(detail2);//封装detail2
                }else {//员工id不存在
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
                }

            }
            result =Detail2Dao.importDetails(conn,detail2s);
        } catch (Exception e) {
            result.msg="批量导入数据失败，请核对数据是否正确，或者改为手动添加";
            result.success=false;
            return result;
        }
        return  result;
    }

    public static String calcDetail(Connection conn, long sid) {
        QueryParameter param = new QueryParameter();
        param.addCondition("sid","=",sid);
        List<Detail2> details = (List<Detail2>) Detail2Dao.getList(conn,param).rows;
        //获取小时工结算单视图
        ViewSettlement2 vs = (ViewSettlement2) Settlement2Dao.get(conn, sid).data;
        Serve serve = (Serve) ServeDao.get(conn, vs.getCcid()).data;
        byte payer = serve.getPayer();//0 派遣单位发放工资  1 合作客户发放工资
//        if (payer == 1) {//合作客户发放工资  单价=公司单价-员工单价
//            vs.setPrice(vs.getPrice()-details.get(0).getPrice());
//            Settlement2 settlement2 = vs;
//            Settlement2Dao.update(conn,settlement2);
//        }

        for(Detail2 detail:details){
            QueryConditions conditions = new QueryConditions();
            conditions.add("id","=", detail.getEid());
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;
            Deduct deduct = (Deduct) DeductDao.get(conn,detail.getEid()).data;
            if(deduct==null){
                return DaoResult.fail("请完善该员工"+employee.getName()+"的个税专项扣除");
            }
            if (payer == 1) {//合作客户发放工资  不计算个税
                detail.calc();
            }else {//派遣方发工资计算个税
                detail.calc(deduct);
            }
        }
        DaoUpdateResult res = Detail2Dao.update(conn,details);
        return JSONObject.toJSONString(res);
    }
}
