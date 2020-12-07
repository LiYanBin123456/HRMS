package service.employee;


import bean.client.Cooperation;
import bean.employee.Employee;
import bean.employee.EmployeeExtra;
import bean.employee.EnsureSetting;
import bean.employee.PayCard;
import bean.insurance.Insurance;
import com.alibaba.fastjson.JSONObject;
import dao.client.CooperationDao;
import dao.employee.EmployeeDao;
import dao.employee.ExtraDao;
import dao.employee.PayCardDao;
import dao.employee.SettingDao;
import dao.insurance.InsuranceDao;
import database.*;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class EmployeeService {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        QueryConditions conditions = new QueryConditions();
        conditions.add("cardId","=",employee.getCardId());
        if(EmployeeDao.exist(conn,conditions).exist){
            return DaoResult.fail("该员工已经存在");
        }
        DaoUpdateResult res = EmployeeDao.insert(conn,employee);
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
    public static DaoUpdateResult insertBatch(Connection conn, List<JSONObject> viewEmployees, long did) {
        /**
         * 流程
         * 1、先批量插入员工信息
         * 2、返回插入后的员工id集合  eids[]
         * 3、员工补充信息添加对应eid
         * 4、批量插入员工补充信息
         */
        //关闭自动提交
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult result;
        List<Employee> employees =new ArrayList<>();
        List<EmployeeExtra> extras =new ArrayList<>();
        List<PayCard> payCards =new ArrayList<>();
        for(JSONObject v : viewEmployees) {
            long cid = v.getLong("cid");
            //无外派单位
           //封装员工信息
            Employee employee = new Employee(0, did, cid, v.getString("cardId"), v.getString("name"), v.getString("phone"), v.getByte("degree"), v.getByte("type"),  v.getSqlDate("entry")
                    , v.getByte("status"), v.getString("department"), v.getString("post"), v.getByte("category"),  v.getFloat("price"));
            employees.add(employee);

            //封装员工补充信息
            EmployeeExtra extra = new EmployeeExtra(0, v.getString("rid"), v.getString("school"), v.getString("major"), v.getByte("household"), v.getString("address"));
            extras.add(extra);

            PayCard payCard = new PayCard(0,v.getString("bank1"),v.getString("bank2"),v.getString("bankNo"),v.getString("cardNo"));
            payCards.add(payCard);
        }

        result = EmployeeDao.insertBatch(conn,employees);//批量插入员工数据
        int[] eids = (int[])result.extra;
        for(int i = 0;i<eids.length;i++){//员工补充信息添加对应eid
            extras.get(i).setEid(eids[i]);
            payCards.get(i).setEid(eids[i]);
        }

        DaoUpdateResult result1 = ExtraDao.insertBatch(conn,extras);//批量插入员工补充数据
        DaoUpdateResult result2 =PayCardDao.insertBatch(conn,payCards);

        if(result.success&&result1.success&&result2.success){//事务处理
            ConnUtil.commit(conn);
            return  result;
        }else {
            ConnUtil.rollback(conn);
            result.success = false;
            result.msg = "员工批量插入失败，请仔细核对员工信息";
            return  result;
        }
    }

    //批量派遣
    public static String dispatch(Connection conn, String[] eids,long cid) {
        QueryConditions condition = new QueryConditions();
        condition.add("id","=",cid);
        DaoQueryResult res1 = CooperationDao.get(conn,condition);
        Cooperation c = (Cooperation) res1.data;

        ConnUtil.closeAutoCommit(conn);
        DaoUpdateResult res2 = SettingDao.updateInjuryPer(conn,eids,c.getPer1());
        DaoUpdateResult res3 = EmployeeDao.dispatch(conn,eids,cid);


        if(res2.success && res3.success){
            ConnUtil.commit(conn);
            return DaoResult.success();
        }else{
            ConnUtil.rollback(conn);
            return DaoResult.fail("数据库出错误");
        }
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
        if(!res1.success || !res2.success){
            ConnUtil.rollback(conn);
            JSONObject json = new JSONObject();
            json.put("success",false);
            json.put("msg","操作失败");
            return json.toJSONString();
        }
        //离职或者退休后将医社保状态设置为拟停
        QueryConditions conditions = new QueryConditions();
        conditions.add("eid","=",id);
        Insurance insurance = (Insurance) InsuranceDao.get(conn,conditions).data;
        if(insurance!=null){//如果存在,将医社保参保状态全部设置为拟停
            insurance.setStatus1(Insurance.STATUS_STOPING);
            insurance.setStatus2(Insurance.STATUS_STOPING);
            insurance.setStatus3(Insurance.STATUS_STOPING);
            insurance.setStatus4(Insurance.STATUS_STOPING);
            insurance.setStatus5(Insurance.STATUS_STOPING);
            //修改参保单
            InsuranceDao.update(conn,insurance);
        }
        ConnUtil.commit(conn);
        return JSONObject.toJSONString(res1);
    }

    public static String settingBatch(Connection conn, String[] eids, EnsureSetting setting) {
        ConnUtil.closeAutoCommit(conn);
        List<EnsureSetting> settingList=new ArrayList<>();
        DaoUpdateResult result=new DaoUpdateResult();
        for(int i=0;i<eids.length;i++){
            EnsureSetting setting1=new EnsureSetting();
            setting1.setEid(Long.parseLong(eids[i]));
            setting1.setCity(setting.getCity());
            setting1.setFundBase(setting.getFundBase());
            setting1.setFundPer(setting.getFundPer());
            setting1.setInjuryPer(setting.getInjuryPer());
            setting1.setMedicare(setting.getMedicare());
            setting1.setProduct(setting.getProduct());
            setting1.setSettingM(setting.getSettingM());
            setting1.setSettingS(setting.getSettingS());
            setting1.setSocial(setting.getSocial());
            setting1.setValM(setting.getValM());
            setting1.setValS(setting.getValS());
            settingList.add(setting1);
        }
        for (EnsureSetting s:settingList){
            if(SettingDao.exist(conn,s.getEid()).exist){
                result=SettingDao.update(conn,s);
            }else {
                result=SettingDao.insert(conn,s);
            }
            if(result.success=false){//事务处理
                ConnUtil.rollback(conn);
                return JSONObject.toJSONString(result);
            }
        }
        ConnUtil.commit(conn);
        return JSONObject.toJSONString(result);
    }
}
