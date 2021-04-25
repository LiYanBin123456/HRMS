package service.employee;


import bean.client.Cooperation;
import bean.employee.*;
import bean.insurance.Insurance;
import bean.rule.RuleMedicare;
import bean.rule.RuleSocial;
import com.alibaba.fastjson.JSONObject;
import dao.client.CooperationDao;
import dao.employee.*;
import dao.insurance.InsuranceDao;
import dao.rule.RuleMedicareDao;
import dao.rule.RuleSocialDao;
import database.*;
import utills.DateUtil;

import java.sql.Connection;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


public class EmployeeService {
    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return EmployeeDao.getList(conn,param);
    }

    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return EmployeeDao.get(conn,conditions);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, Employee employee) {
        return EmployeeDao.update(conn,employee);
    }

    //增加
    public static String insert(Connection conn, Employee employee) {
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        QueryConditions conditions = new QueryConditions();
        conditions.add("cardId","=",employee.getCardId());
        if(EmployeeDao.exist(conn,conditions).exist){
            return DaoResult.fail("该员工已经存在");
        }
        DaoUpdateResult res = EmployeeDao.insert(conn,employee);
        if(!res.success){
            return JSONObject.toJSONString(res);
        }
        long eid = (long) res.extra;
        Deduct deduct = new Deduct();
        deduct.setEid(eid);
        DaoUpdateResult res2= DeductDao.insert(conn,deduct);

        if(res.success&&res2.success){
           ConnUtil.commit(conn);
        }else {
            ConnUtil.rollback(conn);
            res.success=false;
            res.msg="数据库操作错误";
        }
        return JSONObject.toJSONString(res);
    }

    //删除
    public static DaoUpdateResult delete(Connection conn, long id, byte category) {
        DaoUpdateResult result = null;
        switch (category){
            case 0://移入人才库
                result = EmployeeDao.remove(conn,id);
                break;
            case 1://删除
                result = EmployeeDao.delete(conn,id);
                break;
        }
        return result;
    }

    //批量插入
    public static String insertBatch(Connection conn, List<Employee> employees, List<EmployeeExtra> extras, List<PayCard> cards) {
        /**
         * 流程
         * 1、先批量插入员工信息
         * 2、返回插入后的员工id集合  eids[]
         * 3、员工补充信息添加对应eid
         * 4、批量插入员工补充信息
         */
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

//        List<Employee> employees =new ArrayList<>();
//        List<EmployeeExtra> extras =new ArrayList<>();
//        List<PayCard> payCards =new ArrayList<>();

//        for(JSONObject v : viewEmployees) {
//            long cid = v.getLong("cid");
//            //无外派单位
//           //封装员工信息
//            Employee employee = new Employee(0, did, cid, v.getString("cardId"), v.getString("name"), v.getString("phone"), v.getByte("degree"), v.getByte("type"),  v.getSqlDate("entry")
//                    , v.getByte("status"), v.getString("department"), v.getString("post"), v.getByte("category"),  v.getFloat("price"));
//            employees.add(employee);
//
//            //封装员工补充信息
//            EmployeeExtra extra = new EmployeeExtra(0, v.getString("rid"), v.getString("school"), v.getString("major"), v.getByte("household"), v.getString("address"));
//            extras.add(extra);
//
//            PayCard payCard = new PayCard(0,v.getString("bank1"),v.getString("bank2"),v.getString("bankNo"),v.getString("cardNo"));
//            payCards.add(payCard);
//
//        }

        DaoUpdateResult  result = EmployeeDao.insertBatch(conn,employees);//批量插入员工数据
        if(!result.success){
            ConnUtil.rollback(conn);
            return DaoResult.fail("员工批量插入失败，请仔细核对员工信息");
        }
        List<Deduct> deducts = new ArrayList<>();
        long[] eids = (long[]) result.extra;
        for(int i = 0;i<eids.length;i++){//员工补充信息添加对应eid
            extras.get(i).setEid(eids[i]);
            cards.get(i).setEid(eids[i]);
            Deduct deduct=new Deduct();
            deduct.setEid(eids[i]);
            deducts.add(deduct);
        }
        DaoUpdateResult result1 = ExtraDao.insertBatch(conn,extras);//批量插入员工补充数据
        DaoUpdateResult result2 =PayCardDao.insertBatch(conn,cards);
        DaoUpdateResult result3 = DeductDao.insertBatch(conn,deducts);

        if(result1.success&&result2.success&&result3.success){//事务处理
            ConnUtil.commit(conn);
            return  JSONObject.toJSONString(result);
        }else {
            ConnUtil.rollback(conn);
            return DaoResult.fail("员工批量插入失败，请仔细核对员工信息");
        }
    }

    //批量派遣
    public static String dispatch(Connection conn, String[] eids,long cid) {
//        QueryConditions condition = new QueryConditions();
//        condition.add("id","=",cid);
//        DaoQueryResult res1 = CooperationDao.get(conn,condition);
//        Cooperation c = (Cooperation) res1.data;
//
//        ConnUtil.closeAutoCommit(conn);

        //为什么要修改工伤？
        //DaoUpdateResult res2 = SettingDao.updateInjuryPer(conn,eids,c.getPer1());
        DaoUpdateResult res3 = EmployeeDao.dispatch(conn,eids,cid);
        return JSONObject.toJSONString(res3);

//        if(res2.success && res3.success){
//            ConnUtil.commit(conn);
//            return DaoResult.success();
//        }else{
//            ConnUtil.rollback(conn);
//            return DaoResult.fail("数据库出错误");
//        }
    }

    //雇用
    public static DaoUpdateResult employ(Connection conn, long id,byte category) {
        return  EmployeeDao.employ(conn,id,category);
    }

    //离职或者退休
    public static String leave(Connection conn, long id, byte category, byte reason, Date date) {
        ConnUtil.closeAutoCommit(conn);
        DaoUpdateResult res1 = EmployeeDao.updateStatus(conn,id,category==0? Employee.LEAVED :Employee.RETIRE);
        DaoUpdateResult res2 = ExtraDao.leave(conn,id,category,reason,date);
        DaoUpdateResult res3 = InsuranceDao.updateStatus(conn,id,Insurance.STATUS_STOPING,"离职/退休");//离职或者退休后将医社保状态设置为拟停
        if(!res1.success || !res2.success || !res3.success){
            ConnUtil.rollback(conn);
            return DaoResult.fail("操作失败");
        }
        ConnUtil.commit(conn);
        return JSONObject.toJSONString(res1);
    }
    //读取基数
//    public static String readBase(String start, String end, String[] eids, long sid, Connection conn) {
//        float baseM=0;//医保基数
//        float baseS=0;//社保基数
//        List<JSONObject> data = new ArrayList<>();
//        String result ;
//        JSONObject json = new JSONObject();
//        for(int i = 0;i<eids.length;i++){
//            //获取员工信息
//            QueryConditions conditions = new QueryConditions();
//            conditions.add("id","=",Long.parseLong(eids[i]));
//            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;
//
//            JSONObject object = new JSONObject();
//            //读取员工社保设置基数
//            EnsureSetting setting = (EnsureSetting) SettingDao.get(conn, Long.parseLong(eids[i])).data;
//            if(setting==null){//员工社保设置异常
//                json.put("success",false);
//                json.put("msg","请完善员工"+employee.getName()+"的社保设置");
//                result = json.toJSONString();
//                return result;
//            }
//            byte settingM = setting.getSettingM();//医保设置
//            byte settingS = setting.getSettingS();//社保设置
//            switch (settingM){
//                case 0://最低基数
//                    DaoQueryResult result1 = RuleMedicareDao.get(conn,setting.getCity(), DateUtil.parse(start,"yyyy-MM-dd"));
//                    DaoQueryResult result2 = RuleMedicareDao.get(conn,setting.getCity(), DateUtil.parse(end,"yyyy-MM-dd"));
//                    if(result2.success){//结束月份存在规则
//                        RuleMedicare medicare1 = (RuleMedicare) result1.data;
//                        RuleMedicare medicare2 = (RuleMedicare) result2.data;
//                        if(!result1.success){//起始月份不存在规则
//                            baseM =medicare2.getBase();
//                        }else {//存在两个规则，则判断规则是否是同一个
//                            if(medicare1.getId()!=medicare2.getId()) {//两个规则不相同
//                                json.put("success", false);
//                                json.put("msg", "请重新确认月份区间");
//                                result = json.toJSONString();
//                                return result;
//                            }
//                            baseM = medicare1.getBase();
//                        }
//                    }else {//两个都不在规则
//                        json.put("success",false);
//                        //这里应该是今年的医保规则不存在
//                        String year = end.split("-")[0];
//                        json.put("msg","员工"+employee.getName()+year+"年，所处地市的医保规则不存在,请核对");
//                        result = json.toJSONString();
//                        return result;
//                    }
//                    break;
//                case 1://不缴纳
//                    baseM = 0;
//                    break;
//                case 2://自定义工资
//                    baseM = setting.getBaseM();
//                    break;
//            }
//            switch (settingS){
//                case 0://最低基数
//                    DaoQueryResult result1 = RuleSocialDao.get(conn,setting.getCity(), DateUtil.parse(start,"yyyy-MM-dd"));
//                    DaoQueryResult result2 = RuleSocialDao.get(conn,setting.getCity(), DateUtil.parse(end,"yyyy-MM-dd"));
//                    if(result2.success){//结束月份存在规则
//                        RuleSocial social1 = (RuleSocial) result1.data;
//                        RuleSocial social2 = (RuleSocial) result2.data;
//                        if(!result1.success){//起始月份不存在规则
//                            baseS =social2.getBase();
//                        }else {//存在两个规则，则判断规则是否是同一个
//                            if(social1.getId()!=social2.getId()) {//两个规则不相同
//                                json.put("success", false);
//                                json.put("msg", "请重新确认月份区间");
//                                result = json.toJSONString();
//                                return result;
//                            }
//                            baseS = social1.getBase();
//                        }
//                    }else {//两个都不在规则
//                        json.put("success",false);
//                        //这里应该是今年的医保规则不存在
//                        String year = end.split("-")[0];
//                        json.put("msg","员工"+employee.getName()+year+"年，所处地市的社保规则不存在,请核对");
//                        result = json.toJSONString();
//                        return result;
//                    }
//                    break;
//                case 1://不缴纳
//                    baseS = 0;
//                    break;
//                case 2://自定义基数
//                    baseS = setting.getBaseS();
//                    break;
//            }
//            object.put("id",eids[i]);
//            object.put("cardId",employee.getCardId());
//            object.put("name",employee.getName());
//            object.put("baseM",baseM);
//            object.put("baseS",baseS);
//            data.add(object);
//        }
//        json.put("success",true);
//        json.put("data",data);
//        result = json.toJSONString();
//        return result;
//    }

}
