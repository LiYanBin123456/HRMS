package service.settlement;

import bean.client.Cooperation;
import bean.client.MapSalary;
import bean.contract.ViewContractCooperation;
import bean.employee.Deduct;
import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.*;
import dao.client.CooperationDao;
import dao.client.MapSalaryDao;
import dao.contract.ContractDao;
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

import javax.print.attribute.standard.Chromaticity;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterInputStream;

import static utills.IDCardUtil.getLastday_Month;

public class Detail1Service {

    public static DaoQueryListResult getList(Connection conn, QueryParameter param, long id) {
        param.conditions.add("sid", "=", id);
        return Detail1Dao.getList(conn, param);
    }

    public static DaoUpdateResult update(Connection conn, List<Detail1> details) {
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

    //计算结算单明细并修改
    public static DaoUpdateResult saveDetail(Connection conn, long sid, long cid) {
        DaoUpdateResult result = new DaoUpdateResult();
        List<Detail1> detail1List = new ArrayList<>();//新建一个集合用于存放计算好后的明细

        //获取结算单
        Settlement1 settlement = (Settlement1) Settlement1Dao.get(conn, sid).data;
        Date month = settlement.getMonth();//该月份第一天

        Date month2 =getLastday_Month(month);//改为该月份最后一天
        byte flag = settlement.getFlag();//判断是否计算社保

        //获取除了补缴、补差和自定义工资的结算单明细
        QueryParameter param = new QueryParameter();
        param.conditions.add("sid", "=", sid);
        param.conditions.add("status", "!=",Detail1.STATUS_REPLENISH);
        param.conditions.add("status", "!=",Detail1.STATUS_BALANCE);
        param.conditions.add("status", "!=",Detail1.STATUS_CUSTOM);
        DaoQueryListResult result1 = Detail1Dao.getList(conn, param);
        List<Detail1> detail1s = (List<Detail1>) result1.rows;

        //用于暂时存放医保规则
        HashMap<String, RuleMedicare> mapMedicare = new HashMap<>();
        RuleMedicare medicare = new RuleMedicare();

        //用于暂时存放社保规则
        HashMap<String, RuleSocial> mapSocial = new HashMap<>();
        RuleSocial social = new RuleSocial();

        //根据月份获取自定义工资项
        MapSalary mapSalary = (MapSalary) MapSalaryDao.selectByMonth(cid, conn, month2).data;
        if ((flag & ((byte) 1)) == 0) {//不需要计算社保，也就是补发工资
            for (Detail1 d3 : detail1s) {
                //获取该员工在该公司当月其他结算单明细个税和应发总和
                QueryConditions condition = new QueryConditions();
                condition.add("month","=",month);
                condition.add("cid","=",cid);
                condition.add("type","=",settlement.getType());
                condition.add("eid","=",d3.getEid());
                ViewDetailTotal data = (ViewDetailTotal) Detail1Dao.getTotal(conn, condition).data;
                if(data==null){//如果不存在
                    d3.setTax(0);
                    d3.setPayable(0);
                }else {//存在则需要累加
                    d3.setTax(data.getTaxs());
                    d3.setPayable(data.getPayables());
                }

                //获取员工
                QueryConditions conditions = new QueryConditions();
                conditions.add("id", "=", d3.getEid());
                Employee employee = (Employee) EmployeeDao.get(conn, conditions).data;

                //获取员工个税专项扣除
                Deduct deduct = (Deduct) DeductDao.get(conn, d3.getEid()).data;
                if (deduct == null) {
                    result.msg = "请完善" + employee.getName() + "的个税专项扣除";
                    return result;
                }
                d3 = Calculate.calculateDetail1(d3, mapSalary, deduct);
                detail1List.add(d3);
            }
        } else {
            //获取合作客户
            QueryConditions conditions1 = new QueryConditions();
            conditions1.add("id", "=", settlement.getCid());
            Cooperation coop = (Cooperation) CooperationDao.get(conn, conditions1).data;
            float injuryPer = coop.getPer1();//单位工伤比例

            for (Detail1 d : detail1s) {
                //获取员工
                QueryConditions conditions = new QueryConditions();
                conditions.add("id", "=", d.getEid());
                Employee employee = (Employee) EmployeeDao.get(conn, conditions).data;

                //获取员工社保设置
                EnsureSetting setting = (EnsureSetting) SettingDao.get(conn, d.getEid()).data;
                if(settlement.getType()!=2) {//代发工资不需要检验社保
                    if (setting == null) {
                        result.msg = "请完善" + employee.getName() + "的社保设置";
                        return result;
                    }
                    String city = setting.getCity();//员工所处地市

                    //获取该地市的医保规则
                    medicare = mapMedicare.get(city);
                    if (medicare == null) {
                        medicare = (RuleMedicare) RuleMedicareDao.get(conn, city, month2).data;
                        mapMedicare.put(city, medicare);
                    }
                    //获取该地市的社保规则
                    social = mapSocial.get(city);
                    if (social == null) {
                        social = (RuleSocial) RuleSocialDao.get(conn, city, month2).data;
                        mapSocial.put(city, social);
                    }

                    //仍然获取不到该地区的医保社保规则
                    if (medicare == null || social == null) {
                        result.msg = "请确认系统中该员工" + employee.getName() + "的社保所在地是否存在";
                        return result;
                    }
                }

                //获取员工个税专项扣除
                Deduct deduct = (Deduct) DeductDao.get(conn, d.getEid()).data;

                if(settlement.getType()!=3) {//代发社保不需要计算个税所有不需要检验个税专项
                    if (deduct == null) {
                        result.msg = "请完善" + employee.getName() + "的个税专项扣除";
                        return result;
                    }
                }
                //计算结算单明细
                Detail1 detail1 = Calculate.calculateDetail1(settlement, d, medicare, social, setting, mapSalary, deduct, injuryPer);
                detail1List.add(detail1);
            }
        }
        return Detail1Dao.update(conn, detail1List);
    }

}
