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
        }
        result = Detail1Dao.importDetails(conn, details);
        return result;
    }

    //计算结算单明细并修改
    public static DaoUpdateResult saveDetail(Connection conn, long sid, long cid) {
        DaoUpdateResult result = new DaoUpdateResult();
        List<Detail1> detail1List = new ArrayList<>();//新建一个集合用于存放计算好后的明细

        //获取结算单
        Settlement1 settlement = (Settlement1) Settlement1Dao.get(conn, sid).data;
        Date month = settlement.getMonth();
        byte flag = settlement.getFlag();//判断是否计算社保

        //获取结算单明细
        QueryParameter param = new QueryParameter();
        param.conditions.add("sid", "=", sid);
        DaoQueryListResult result1 = Detail1Dao.getList(conn, param);
        List<Detail1> detail1s = (List<Detail1>) result1.rows;

        //用于暂时存放医保规则
        HashMap<String, RuleMedicare> mapMedicare = new HashMap<>();
        RuleMedicare medicare;

        //用于暂时存放社保规则
        HashMap<String, RuleSocial> mapSocial = new HashMap<>();
        RuleSocial social;

        //根据月份获取自定义工资项
        MapSalary mapSalary = (MapSalary) MapSalaryDao.selectByMonth(cid, conn, month).data;
        if ((flag & ((byte) 1)) == 0) {//不需要社保
            QueryParameter parameter = new QueryParameter();
            parameter.addCondition("month", "=", month);
            parameter.addCondition("cid", "=", settlement.getCid());
            parameter.addCondition("type", "=", settlement.getType());
            parameter.addCondition("id", "!=", settlement.getId());
            //获取当月出此结算单之外的其它结算单
            List<Settlement1> settlementList = (List<Settlement1>) Settlement1Dao.getList(conn, parameter).rows;
            for (Settlement1 s : settlementList) {
                QueryParameter param2 = new QueryParameter();
                param2.conditions.add("sid", "=", s.getId());
                DaoQueryListResult result2 = Detail1Dao.getList(conn, param2);
                List<Detail1> details = (List<Detail1>) result2.rows;
                for (Detail1 d : details) {
                    for (Detail1 d2 : detail1s) {
                        if (d.getEid() == d2.getEid()) {
                            float payable;
                            float tax;
                            payable = d2.getPayable() + d.getPayable();
                            tax = d2.getTax() + d.getTax();
                            d2.setPayable(payable);
                            d2.setTax(tax);
                        }
                    }
                }
            }
            for (Detail1 d3 : detail1s) {
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
                if (setting == null) {
                    result.msg = "请完善" + employee.getName() + "的社保设置";
                    return result;
                }
                String city = setting.getCity();//员工所处地市

                //获取该地市的医保规则
                medicare = mapMedicare.get(city);
                if (medicare == null) {
                    medicare = (RuleMedicare) RuleMedicareDao.get(conn, city, month).data;
                    mapMedicare.put(city, medicare);
                }
                //获取该地市的社保规则
                social = mapSocial.get(city);
                if (social == null) {
                    social = (RuleSocial) RuleSocialDao.get(conn, city, month).data;
                    mapSocial.put(city, social);
                }
                //仍然获取不到该地区的医保社保规则
                if (medicare == null || social == null) {
                    result.msg = "请确认系统中该员工" + employee.getName() + "的社保所在地是否存在";
                    return result;
                }

                //获取员工个税专项扣除
                Deduct deduct = (Deduct) DeductDao.get(conn, d.getEid()).data;
                if (deduct == null) {
                    result.msg = "请完善" + employee.getName() + "的个税专项扣除";
                    return result;
                }

                //计算结算单明细
                Detail1 detail1 = Calculate.calculateDetail1(settlement, d, medicare, social, setting, mapSalary, deduct, injuryPer);
                detail1List.add(detail1);
            }
        }
        return Detail1Dao.update(conn, detail1List);
    }

}
