package service.settlement;

import bean.client.MapSalary;
import bean.employee.Deduct;
import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.*;
import dao.client.MapSalaryDao;
import dao.employee.DeductDao;
import dao.employee.EmployeeDao;
import dao.employee.SettingDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Detail2Dao;
import dao.settlement.Settlement1Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryConditions;
import database.QueryParameter;
import utills.Calculate;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DeflaterInputStream;

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
            if(v.getEid()!=0){//员工id存在
                Detail1 detail1 = new Detail1(0,sid,v.getEid(),v.getBase(),v.getPension1(),v.getMedicare1(),v.getUnemployment1(),v.getDisease1(),v.getFund1(),v.getPension2(),v.getMedicare2()
                        ,v.getUnemployment2(),v.getInjury(),v.getDisease2(),v.getBirth(),v.getFund2(),v.getTax(),v.getPayable(),v.getPaid(),v.getF1(),v.getF2(),v.getF3(),v.getF4(),v.getF5(),v.getF6(),v.getF7()
                        ,v.getF8(),v.getF9(),v.getF10(),v.getF11(),v.getF12(),v.getF13(),v.getF14(),v.getF15(),v.getF16(),v.getF17(),v.getF18(),v.getF19(),v.getF20(),v.getStatus());
                detail1s.add(detail1);
            }else {//员工不存在
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

        }
        result = Detail1Dao.importDetails(conn,detail1s);
        return  result;
    }

    public static String makeup(Connection conn,Long id,String month){
        return null;
    }


    //计算结算单明细并修改
    public static DaoUpdateResult saveDetail(Connection conn,long sid, long cid) {

        DaoUpdateResult result = new DaoUpdateResult();
        //获取结算单
        Settlement1 settlement = (Settlement1) Settlement1Dao.get(conn,sid).data;
        Date month = settlement.getMonth();
        QueryParameter param = new QueryParameter();
        param.conditions.add("sid","=",sid);
        DaoQueryListResult result1 = Detail1Dao.getList(conn,param);//查询数据库中属于该结算单的所有明细
        List<Detail1> detail1s = (List<Detail1>) result1.rows;//所有明细

        List<Detail1> detail1List  = new ArrayList<>();//新建一个集合用于存放计算好后的明细
        for(Detail1 d:detail1s){
            QueryConditions conditions = new QueryConditions();
            conditions.add("id","=", d.getEid());
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;

            EnsureSetting setting = (EnsureSetting) SettingDao.get(conn,d.getEid()).data;//员工设置
            if(setting==null){
                result.msg = "请完善"+employee.getName()+"的社保设置";
                return result;
            }

            String city = setting.getCity();//员工所处地市
            //获取该地市的医保规则
            RuleMedicare medicare= (RuleMedicare)RuleMedicareDao.get(conn,city,month).data;
            //获取该地市的社保规则
            RuleSocial social = (RuleSocial)RuleSocialDao.get(conn,city,month).data;
            if(medicare==null||social==null){
                result.msg = "请确认系统中该员工"+employee.getName()+"的社保所在地是否存在";
                return result;
            }
            MapSalary mapSalary = (MapSalary) MapSalaryDao.getLast(cid,conn).data;//获取最新自定义的工资
            Deduct deduct = (Deduct) DeductDao.get(conn,d.getEid()).data;//获取员工个税专项扣除
            if(deduct==null){
                result.msg = "请完善"+employee.getName()+"的个税专项扣除";
                return result;
            }
            Detail1 detail1 = Calculate.calculateDetail1(d,medicare,social,setting,mapSalary,deduct);//计算结算单明细
            detail1List.add(detail1);
        }
        return  Detail1Dao.update(conn,detail1List);
    }

    //社保补差
    public static void backup(Connection conn, String[] eids, String start, String end, long sid) {
    for(int i = 0 ; i <eids.length; i++){
        long eid = Long.parseLong(eids[i]);//员工id
        EnsureSetting setting = (EnsureSetting) SettingDao.get(conn,eid).data;

    }

    }

    //社保补缴
    public static void makeup(Connection conn, String[] eids, String start, String end, long sid) {

    }
}
