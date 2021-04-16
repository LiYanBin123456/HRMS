package service.settlement;

import bean.client.Cooperation;
import bean.client.MapSalary;
import bean.employee.Deduct;
import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.employee.ViewDeduct;
import bean.insurance.Insurance;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import bean.settlement.Detail1;
import bean.settlement.Settlement1;
import bean.settlement.ViewDetail1;
import com.alibaba.fastjson.JSONObject;
import dao.client.CooperationDao;
import dao.client.MapSalaryDao;
import dao.employee.DeductDao;
import dao.employee.EmployeeDao;
import dao.employee.SettingDao;
import dao.insurance.InsuranceDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import dao.settlement.Detail1Dao;
import dao.settlement.Settlement1Dao;
import database.*;
import utills.CollectionUtil;
import utills.DateUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DetailService1 {

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
     * 计算派遣和外包结算单明细并修改
     * @param conn
     * @param sid 结算单编号
     * @param cid 合作客户编号
     * @return
     */
    public static String calcDetail(Connection conn, long sid, long cid) {
        /**
         * 思路：
         * （1）获取需要计算的明细以及准备计算所需的员工的个税专项扣除和五险一金参保单信息（只有正常或补发的才需要计算）
         * （2）逐个计算每条明细，注意顺序不能错，应为应发依赖于五险一金，个税依赖于应发，实发依赖于应发和个税
         * （2.1）计算五险一金，只有需计算社保的（settlement.isNeedCalcInsurance）才需要计算
         * （2.2）计算应发（需自定义工资项）
         * （2.3）计算个税（需个税扣除，代缴社保的不需要计算，如果是补发应考虑专项扣除的回撤，否则就会重复扣除和减免）
         * （2.4）计算实发
         *
         */
        //获取结算单和明细（只有正常或补发的才需要计算，补缴和补差的已经并入正常的核收补减中去了。同时一个结算单不可能正常和补发并存）
        Settlement1 settlement = (Settlement1) Settlement1Dao.get(conn, sid).data;//获取结算单
        QueryParameter param = new QueryParameter();
        param.conditions.add("sid", "=", sid);
        param.conditions.add("status", "=",settlement.isNeedCalcInsurance()|| settlement.getType()==Settlement1.TYPE_3?Detail1.STATUS_NORMAL:Detail1.STATUS_MAKEUP);
        List<ViewDetail1> details = (List<ViewDetail1>) Detail1Dao.getList(conn, param).rows;

        //准备计算明细所需的自定义工资项、个税专项扣除、个人医保、社保设置和相应城市医社保规则
        String eids = CollectionUtil.getKeySerial(details,"eid");
        Date month2 = DateUtil.getLastDayofMonth(settlement.getMonth());//获取结算月最后一天作为获取医社保规则条件
        MapSalary mapSalary = (MapSalary) MapSalaryDao.selectByMonth(cid, conn, month2).data;//获取工资定义

        List<ViewDeduct> deducts = new ArrayList<>();//用于存放员工的个税专项扣除
        List<Insurance> insurances  = new ArrayList<>();//用于存放员工的社保设置

        /*List<EnsureSetting> settings = new ArrayList<>();//用于存放员工的社保设置
        HashMap<String, RuleMedicare> medicares = new HashMap<>();//用于暂时存放医保规则
        HashMap<String, RuleSocial> socials = new HashMap<>();//用于暂时存放社保规则*/

        //准备参保单中所有人的个税扣除信息
        if(settlement.getType() != Settlement1.TYPE_4){//代缴社保不需要计算个税
            QueryParameter p = new QueryParameter();
            p.addCondition("eid","in",eids);
            deducts = (List<ViewDeduct>) DeductDao.getList(conn, p).rows;
        }

        //准备结算单中所有人的五险一金参保信息
        if(settlement.isNeedCalcInsurance()){
            QueryParameter p3 = new QueryParameter();
            p3.addCondition("eid","in",eids);//只查询出新增、在保的参保单
           //p3.addCondition("(status","=",Insurance.STATUS_APPENDING +" or status =" +Insurance.STATUS_NORMAL+")");//只查询出新增、在保的参保单
            insurances = (List<Insurance>) InsuranceDao.getList(conn, p3).rows;
         /*//获取医社保规则
            QueryConditions conditions1 = new QueryConditions();
            conditions1.add("id", "=", settlement.getCid());
            Cooperation coop = (Cooperation) CooperationDao.get(conn, conditions1).data;
            float perInjury = coop.getPer1();
            for(EnsureSetting s:settings){
                s.setPerInjury(perInjury);//设置工伤比例
                String city = s.getCity();//员工所处地市
                RuleMedicare medicare = medicares.get(city);//获取该地市的医保规则
                if (medicare == null) {
                    medicare = (RuleMedicare) RuleMedicareDao.get(conn, city, month2).data;
                    if(medicare == null){
                        return DaoResult.fail(String.format("请确认%s的医保规则是否存在",city));
                    }
                    medicares.put(city, medicare);
                }
                RuleSocial social = socials.get(city);
                if (social == null) {
                    social = (RuleSocial) RuleSocialDao.get(conn, city, month2).data;
                    if(social == null){
                        return DaoResult.fail(String.format("请确认%s的社保规则是否存在",city));
                    }
                    socials.put(city, social);
                }
            }*/
        }
        for(ViewDetail1 d:details){
            //2.1计算五险一金
            if(settlement.isNeedCalcInsurance()){
                /*EnsureSetting setting = CollectionUtil.getElement(settings,"eid",d.getEid());
                RuleMedicare ruleMedicare = medicares.get(setting.getCity());
                RuleSocial ruleSocial = socials.get(setting.getCity());
                d.calculateMedicare(setting,ruleMedicare);
                d.calculateSocial(setting,ruleSocial);
                d.calcFund(setting);*/
                //根据员工过滤五险一金的参保单
                List<Insurance> insurances_one = CollectionUtil.filter(insurances,"eid",d.getEid());
                //计算员工的五险一金
                if(insurances_one.size()>0){
                    d.calcInsurance(insurances_one);
                }
            }

            //2.2计算应发
            d.calcPayable(mapSalary);

            //2.3计算个税
            if(settlement.getType() != Settlement1.TYPE_4) {//代缴社保不需要计算个税
                Deduct deduct = CollectionUtil.getElement(deducts, "eid", d.getEid());
                if(deduct == null){
                    return DaoResult.fail(String.format("请完善员工[%d]的个税专项扣除",d.getEid()));
                }
                //如果是补发，且存在当月的已正常发放的工资，个人专项扣除应回撤，否则的逻辑如下，是否正确？
                if(!settlement.isNeedCalcInsurance()&&settlement.getType() != Settlement1.TYPE_3){
                    QueryParameter p4 = new QueryParameter();
                    p4.addCondition("eid", "=", d.getEid());
                    p4.addCondition("month", "=",month2);
                    p4.addCondition("status", "=",Detail1.STATUS_NORMAL);
                    p4.addCondition("statusSettlement", "=",Settlement1.STATUS_PAYED2);
                    List<ViewDetail1> ss = (List<ViewDetail1>) Detail1Dao.getList(conn, param).rows;
                    if(ss.size()>0){
                        deduct.rollback();
                    }
                }
                d.calculateTax(deduct);
            }

            //2.4计算实发
            d.calcPayed();
            if(settlement.getType() == Settlement1.TYPE_4){//代缴社保不需要应发和实发
                d.setPaid(0);
                d.setPayable(0);
            }
        }


        DaoUpdateResult res = Detail1Dao.update(conn, details);
        return JSONObject.toJSONString(res);
    }

    /**
     * 计算代发工资和代缴社保明细并修改
     * @param conn
     * @param sid
     * @param cid
     * @param type
     * @return
     */
    public static String calcSupDetail(Connection conn, long sid, long cid, int type) {

        return null;
    }
}
