package dao.insurance;

import bean.employee.EnsureSetting;
import bean.insurance.Insurance;
import bean.insurance.ViewInsurance;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import database.*;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class InsuranceDao {
    //判断是否已经存在
    public static DaoExistResult exist(Connection conn,long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.exist(conn,"insurance",conditions);
    }

    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        if (param.conditions.extra != null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(cname,name)", "like", param.conditions.extra);
        }
        return DbUtil.getList(conn, "view_insurance", param, ViewInsurance.class);
    }

    //获取
    public static DaoQueryListResult getList(Connection conn, long eid) {
        QueryParameter p = new QueryParameter();
        p.addCondition("eid","=",eid);
        return DbUtil.getList(conn, "insurance", p, Insurance.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, Insurance insurance) {
        String sql = "update insurance set code=?,base=?,baseType=?,v1=?,v2=?,date=?,status=? where eid=? and category=?";
        Object[] params = {insurance.getCode(),insurance.getBase(),insurance.getBaseType(),insurance.getV1(),insurance.getV2(),insurance.getDate(),insurance.getStatus(),insurance.getEid(),insurance.getCategory()};
        return DbUtil.update(conn, sql, params);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, List<Insurance> insurances) {
        String sql = "update insurance set city=?, code=?,base=?,baseType=?,v1=?,v2=?,date=?,status=? where eid=? and category=?";
        Object [][]params = new Object[insurances.size()][];
        for (int i = 0; i < insurances.size(); i++) {
            Insurance s = insurances.get(i);
            params[i] = new Object[]{s.getCity(),s.getCode(),s.getBase(),s.getBaseType(),s.getV1(),s.getV2(),s.getDate(),s.getStatus(),s.getEid(),s.getCategory()};
        }
        return DbUtil.batch(conn,sql,params);
    }

    //修改
    public static DaoUpdateResult updateStatus(Connection conn, long eid, byte status, String reason) {
        String sql = "update insurance set status=?,reason=? where eid=?";
        Object[] params = {status,reason,eid};
        return DbUtil.update(conn, sql, params);
    }

    //修改
    public static DaoUpdateResult updateStatus(Connection conn, long eid, byte category,byte status, String reason) {
        String sql = "update insurance set status=?,reason=? where eid=? and category=?";
        Object[] params = {status,reason,eid,category};
        return DbUtil.update(conn, sql, params);
    }

    //批量修改状态
    public static DaoUpdateResult updateStatus(Connection conn, List<Insurance> insurances) {
        String sql = "update insurance set status=?,reason=? where eid=? and category=?";
        Object [][]params = new Object[insurances.size()][];
        for (int i = 0; i < insurances.size(); i++) {
            Insurance s = insurances.get(i);
            params[i] = new Object[]{s.getStatus(),s.getReason(),s.getEid(),s.getCategory()};
        }
        return DbUtil.batch(conn,sql,params);
    }

    //插入
    public static DaoUpdateResult insert(Connection conn, Insurance in) {
        String sql = "insert insurance (eid,city,category,code,base,baseType,v1,v2,date,status,reason) values(?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params ={in.getEid(), in.getCity(),in.getCategory(), in.getCode(),in.getBase(),in.getBaseType(),in.getV1(),in.getV2(), in.getCode(),in.getDate(),in.getStatus(),in.getReason()};
        return DbUtil.insert(conn, sql, params);
    }

    //批量插入
    public static DaoUpdateResult insert(Connection conn, List<Insurance> insurances) {
        String sql = "insert insurance (eid,city,category,code,base,baseType,v1,v2,date,status,reason) values(?,?,?,?,?,?,?,?,?,?,?)";
        Object[][] params = new Object[insurances.size()][];
        for (int i = 0; i < insurances.size(); i++) {
            Insurance in = insurances.get(i);
            params[i] = new Object[]{in.getEid(), in.getCity(),in.getCategory(), in.getCode(),in.getBase(),in.getBaseType(),in.getV1(),in.getV2(),in.getDate(),in.getStatus(),in.getReason()};
        }
        return DbUtil.insertBatch(conn, sql, params);
    }

    //批量删除
    public static DaoUpdateResult delete(Connection conn, List<Insurance> insurances) {
        String sql = "delete from insurance where eid = ? and category = ? ";
        Object[][] params = new Object[insurances.size()][];
        for (int i = 0; i < insurances.size(); i++) {
            Insurance in = insurances.get(i);
            params[i] = new Object[]{in.getEid(), in.getCategory()};
        }
        return DbUtil.batch(conn, sql, params);
    }

    //自动生成员工参保单
    public static Insurance autoCreateInsurance(EnsureSetting setting, Date date, RuleMedicare ruleMedicare, RuleSocial ruleSocial, Connection conn){
        Insurance insurance = new Insurance();//参保单
        insurance.setEid(setting.getEid());//设置员工id

        //设置医保相关
        byte medicare = setting.getMedicare();//要计算的医保类别
        byte settingM=setting.getSettingM();//医保设置
        if(medicare!=0) {//勾选了医保类别
            insurance.setStatus(Insurance.STATUS_APPENDING);//设置医保参保单状态
            insurance.setDate(date);//设置医保参保单时间

            float base1 = 0;//医保基数
            switch (settingM) {//根据医保设置获取医保基数
                case 0://最低基数
                    //base1 = ruleMedicare.getBase();
                    break;
                case 1://不缴纳
                    base1 = 0;
                    break;
                case 2://自定义基数
                    base1 = setting.getBaseM();
                    break;
            }
        }

        //设置公积金相关
        if(setting.getBaseFund()!=0){
            insurance.setStatus((byte) 1);
            insurance.setDate(date);
        }

        //设置社保相关
        byte social = setting.getSocial();//要计算的社保类别
        if(social!=0){//勾选了社保
            if((social&((byte)1)) != 0){
                //养老状态设置为新增
                insurance.setStatus((byte) 1);
                insurance.setDate(date);
            }
            if((social&((byte)2)) != 0){
                //失业状态设置为新增
                insurance.setStatus((byte) 1);
                insurance.setDate(date);
            }
            if((social&((byte)4)) != 0) {
                //工伤状态设置为新增
                insurance.setStatus((byte) 1);
                insurance.setDate(date);
            }
            float base3 = 0;//社保基数
            byte settingS=setting.getSettingS();//社保设置
            switch (settingS){
                case 0://最低基数
                    //base3 = ruleSocial.getBase();
                    break;
                case 1://不缴纳
                    base3 = 0;
                    break;
                case 2://不缴纳
                    base3 = setting.getBaseS();
                    break;
            }
        }
        return insurance;
    }

}

