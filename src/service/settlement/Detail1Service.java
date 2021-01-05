package service.settlement;

import bean.client.Cooperation;
import bean.client.MapSalary;
import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.employee.ViewDeduct;
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
import dao.settlement.Detail1Dao;
import dao.settlement.Settlement1Dao;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.QueryConditions;
import database.QueryParameter;
import utills.Salary.Salary;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static utills.IDCardUtil.getLastday_Month;

public class Detail1Service {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param, long id) {
        param.conditions.add("sid", "=", id);
        return Detail1Dao.getList(conn, param);
    }

    public static DaoUpdateResult update(Connection conn, List<ViewDetail1> details) {
        return Detail1Dao.update(conn, details);
    }

    public static DaoUpdateResult importDetails(Connection conn, long sid, List<ViewDetail1> details, long did) {
        DaoUpdateResult result = new DaoUpdateResult();
        List<Detail1> ds = new ArrayList<>();
        Settlement1 settlement = (Settlement1) Settlement1Dao.get(conn,sid).data;
        for (ViewDetail1 v : details) {
            v.setSid(sid);
            if (v.getEid() == 0) {//通过导入方式，需要确认员工是否存在
                QueryConditions conditions = new QueryConditions();
                conditions.add("cardId", "=", v.getCardId());
                conditions.add("did", "=", did);
                if (!EmployeeDao.exist(conn, conditions).exist) {
                    result.msg = "用户" + v.getName() + "不存在，或者身份证id不正确，请核对";
                    return result;
                }
                Employee employee = (Employee) EmployeeDao.get(conn, conditions).data; //根据员工身份证获取员工id
                v.setEid(employee.getId());
            }
            byte status = ((settlement.getFlag()&((byte)1)) == 0)?Detail1.STATUS_MAKEUP:Detail1.STATUS_NORMAL;

            if(v.getStatus()!=0){//如果是自定义工资条
               v.setStatus(Detail1.STATUS_CUSTOM);
            }else {
                v.setStatus(status);
            }

            ds.add(v);
        }
        result = Detail1Dao.importDetails(conn, ds);
        return result;
    }


    /**
     * 计算结算单明细并修改
     * @param conn
     * @param sid 结算单编号
     * @param cid 合作客户编号
     * @return
     */
    public static DaoUpdateResult saveDetail(Connection conn, long sid, long cid) {
        DaoUpdateResult result = new DaoUpdateResult();

        //获取结算单
        Settlement1 settlement = (Settlement1) Settlement1Dao.get(conn, sid).data;
        Date month = settlement.getMonth();//该月份第一天

        //获取工资定义
        Date month2 =getLastday_Month(month);//改为该月份最后一天
        MapSalary mapSalary = (MapSalary) MapSalaryDao.selectByMonth(cid, conn, month2).data;

        //获取除了补缴、补差和自定义工资的结算单明细
        QueryParameter param = new QueryParameter();
        param.conditions.add("sid", "=", sid);
        param.conditions.add("status", "!=",Detail1.STATUS_REPLENISH);
        param.conditions.add("status", "!=",Detail1.STATUS_BALANCE);
        param.conditions.add("status", "!=",Detail1.STATUS_CUSTOM);
        DaoQueryListResult result1 = Detail1Dao.getList(conn, param);
        List<ViewDetail1> details = (List<ViewDetail1>) result1.rows;

        //获取明细中员工的当月其它结算单中应发总额和个税预缴总额
        String eids="";
        for(Detail d:details){
            eids += (d.getEid()+",");
        }
        eids = eids.substring(0,eids.length()-1);

        //获取明细中员工的个税专项扣除
        QueryParameter p2 = new QueryParameter();
        p2.addCondition("eid","in",eids);
        List<ViewDeduct> deducts = (List<ViewDeduct>) DeductDao.getList(conn, p2).rows;

        //根据月份获取自定义工资项
        if (!settlement.isNeedCalculateSocial()) {//不需要计算社保，也就是补发工资
            QueryParameter p1 = new QueryParameter();
            p1.addCondition("month","=",month);
            p1.addCondition("cid","=",cid);
            p1.addCondition("type","=",settlement.getType());
            p1.addCondition("eid","in",eids);
            List<ViewDetailTotal> totals = (List<ViewDetailTotal>) Detail1Dao.getTotals(conn, p1).rows;

            String res = Salary.calculateDetail1(details, mapSalary,totals,deducts);
            if(res != null){
                result.success = false;
                result.msg = res;
                return result;
            }
        } else {
            //用于暂时存放医保规则和社保规则
            HashMap<String, RuleMedicare> medicares = new HashMap<>();
            HashMap<String, RuleSocial> socials = new HashMap<>();

            //获取明细中员工的社保设置
            QueryConditions conditions1 = new QueryConditions();
            conditions1.add("id", "=", settlement.getCid());
            Cooperation coop = (Cooperation) CooperationDao.get(conn, conditions1).data;
            float injuryPer = coop.getPer1();
            List<EnsureSetting> settings = (List<EnsureSetting>) SettingDao.getList(conn, p2).rows;
            for(EnsureSetting s:settings){
                s.setInjuryPer(injuryPer);
                String city = s.getCity();//员工所处地市
                //获取该地市的医保规则
                RuleMedicare medicare = medicares.get(city);
                if (medicare == null) {
                    medicare = (RuleMedicare) RuleMedicareDao.get(conn, city, month2).data;
                    if(medicare == null){
                        result.success = false;
                        result.msg = String.format("请确认%s的医保规则是否存在",city);
                        return result;
                    }
                    medicares.put(city, medicare);
                }
                RuleSocial social = socials.get(city);
                if (social == null) {
                    social = (RuleSocial) RuleSocialDao.get(conn, city, month2).data;
                    if(social == null){
                        result.success = false;
                        result.msg = String.format("请确认%s的社保规则是否存在",city);
                        return result;
                    }
                    socials.put(city, social);
                }
            }

            Salary.calculateDetail1(settlement, details, medicares, socials, settings, mapSalary, deducts);

        }
        return Detail1Dao.update(conn, details);
    }

}
