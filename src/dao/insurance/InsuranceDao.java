package dao.insurance;

import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.insurance.Insurance;
import bean.insurance.ViewInsurance;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import database.*;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class InsuranceDao {
    //判断是否已经存在
    public static DaoExistResult exist(Connection conn,long id){
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        return DbUtil.exist(conn,"insurance",conditions);
    }

    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param, byte category) {
        if (param.conditions.extra != null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(status1,status2,status3,status4,status5,cname,name)", "like", param.conditions.extra);
        }
        param.addCondition("type", "=", category);
        return DbUtil.getList(conn, "view_insurance", param, ViewInsurance.class);
    }

    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        if (param.conditions.extra != null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(status1,status2,status3,status4,status5,cname,name)", "like", param.conditions.extra);
        }
        return DbUtil.getList(conn, "view_insurance", param, ViewInsurance.class);
    }

    //获取
    public static DaoQueryResult get(Connection conn, QueryConditions conditions) {
        return DbUtil.get(conn, "view_insurance", conditions, ViewInsurance.class);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, Insurance i) {
        String sql = "update insurance set code1=?,code2=?,code3=?,date1=?,date2=?,date3=?,date4=?,date5=?,status1=?,status2=?,status3=?,status4=?,status5=?,base1=?,base2=?,base3=? where eid=?";
        Object[] params = {i.getCode1(),i.getCode2(),i.getCode3(),i.getDate1(),i.getDate2(),i.getDate3(),i.getDate4(),i.getDate5(),i.getStatus1(),i.getStatus2(),
          i.getStatus3(),i.getStatus4(),i.getStatus5(),i.getBase1(),i.getBase2(),i.getBase3(),i.getEid()
        };
        //调用DbUtil封装的update方法
        return DbUtil.update(conn, sql, params);
    }

    //删除
    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid", "=", id);
        return DbUtil.delete(conn, "insurance", conditions);
    }

    //插入
    public static DaoUpdateResult insert(Connection conn, Insurance in) {
        String sql = "insert insurance (eid,code1,code2,code3,date1,date2,date3,date4,date5,status1,status2,status3,status4,status5,base1,base2,base3) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params ={in.getEid(), in.getCode1(),in.getCode2(),in.getCode3(), in.getDate1(), in.getDate2(), in.getDate3(), in.getDate4(),in.getDate5(),
                in.getStatus1(),in.getStatus2(),in.getStatus3(),in.getStatus4(),in.getStatus5(),
                in.getBase1(),in.getBase2(),in.getBase3()};
        return DbUtil.insert(conn, sql, params);
    }

    //批量插入
    public static DaoUpdateResult insertBatch(Connection conn, List<Insurance> in) {
        String sql = "insert insurance (eid,code1,code2,code3,date1,date2,date3,date4,date5,status1,status2,status3,status4,status5,base1,base2,base3) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[][] params = new Object[in.size()][];
        for (int i = 0; i < in.size(); i++) {
            params[i] = new Object[]{in.get(i).getEid(), in.get(i).getCode1(),in.get(i).getCode2(),in.get(i).getCode3(), in.get(i).getDate1(), in.get(i).getDate2(), in.get(i).getDate3(), in.get(i).getDate4(),in.get(i).getDate5(),
                    in.get(i).getStatus1(),in.get(i).getStatus2(),in.get(i).getStatus3(),in.get(i).getStatus4(),in.get(i).getStatus5(),in.get(i).getBase1(),in.get(i).getBase2(),in.get(i).getBase3()
            };
        }
        return DbUtil.insertBatch(conn, sql, params);
    }

    //批量修改
    public static DaoUpdateResult updateBatch(Connection conn, List<ViewInsurance> in) {
        String sql = "update insurance set code1=?,code2=?,code3=?,date1=?,date2=?,date3=?,date4=?,date5=?,status1=?,status2=?,status3=?,status4=?,status5=?,base1=?,base2=?,base3=? where eid=?";
        Object[][] params = new Object[in.size()][];
        for (int i = 0; i < in.size(); i++) {
            params[i] = new Object[]{ in.get(i).getCode1(),in.get(i).getCode2(),in.get(i).getCode3(), in.get(i).getDate1(), in.get(i).getDate2(), in.get(i).getDate3(), in.get(i).getDate4(),in.get(i).getDate5(),
                    in.get(i).getStatus1(),in.get(i).getStatus2(),in.get(i).getStatus3(),in.get(i).getStatus4(),in.get(i).getStatus5(),in.get(i).getBase1(),in.get(i).getBase2(),in.get(i).getBase3()
                    ,in.get(i).getEid()
            };
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
            insurance.setStatus1((byte) 1);//设置医保参保单状态
            insurance.setDate1(date);//设置医保参保单时间

            float base1 = 0;//医保基数
            switch (settingM) {//根据医保设置获取医保基数
                case 0://最低基数
                    base1 = ruleMedicare.getBase();
                    break;
                case 1://不缴纳
                    base1 = 0;
                    break;
                case 2://自定义基数
                    base1 = setting.getValM();
                    break;
            }
            insurance.setBase1(base1);
        }

        //设置公积金相关
        if(setting.getFundBase()!=0){
            insurance.setBase2(setting.getFundBase());
            insurance.setStatus2((byte) 1);
            insurance.setDate2(date);
        }

        //设置社保相关
        byte social = setting.getSocial();//要计算的社保类别
        if(social!=0){//勾选了社保
            if((social&((byte)1)) != 0){
                //养老状态设置为新增
                insurance.setStatus3((byte) 1);
                insurance.setDate3(date);
            }
            if((social&((byte)2)) != 0){
                //失业状态设置为新增
                insurance.setStatus4((byte) 1);
                insurance.setDate4(date);
            }
            if((social&((byte)4)) != 0) {
                //工伤状态设置为新增
                insurance.setStatus5((byte) 1);
                insurance.setDate5(date);
            }
            float base3 = 0;//社保基数
            byte settingS=setting.getSettingS();//社保设置
            switch (settingS){
                case 0://最低基数
                    base3 = ruleSocial.getBase();
                    break;
                case 1://不缴纳
                    base3 = 0;
                    break;
                case 2://不缴纳
                    base3 = setting.getValS();
                    break;
            }
            insurance.setBase3(base3);
        }
        return insurance;
    }

}

