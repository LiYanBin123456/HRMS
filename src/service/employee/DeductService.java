package service.employee;

import bean.employee.Deduct;
import bean.employee.Employee;
import bean.employee.EnsureSetting;
import bean.employee.ViewDeduct;
import dao.employee.DeductDao;
import dao.employee.EmployeeDao;
import dao.employee.SettingDao;
import database.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

//个人专项扣除service层
public class DeductService {
    public static DaoQueryResult get(Connection conn, long id) {
        return DeductDao.get(conn,id);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, Deduct deduct) {
        float deducts = deduct.getDeduct1()+deduct.getDeduct2()+deduct.getDeduct3()+deduct.getDeduct4()+deduct.getDeduct5()+deduct.getDeduct6();
        deduct.setDeduct(deducts);
        return DeductDao.update(conn,deduct);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, Deduct deduct) {
        DaoUpdateResult result;
        //计算专项扣除总额
        float deducts = deduct.getDeduct1()+deduct.getDeduct2()+deduct.getDeduct3()+deduct.getDeduct4()+deduct.getDeduct5()+deduct.getDeduct6();
        deduct.setDeduct(deducts);
        if(!DeductDao.exist(conn,deduct.getEid()).exist){
           result = DeductDao.insert(conn,deduct);
        }else {//已存在则修改
            result = DeductDao.update(conn,deduct);
        }
        return result;
    }

    //列表查询
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return  DeductDao.getList(conn,param);
    }

    //删除
    public static DaoUpdateResult delete(Connection conn,long id){
        return DeductDao.delete(conn,id);
    }

    public static DaoUpdateResult importDeducts(Connection conn, List<ViewDeduct> viewDeducts) {
        DaoUpdateResult result = new DaoUpdateResult();
        for (ViewDeduct viewDeduct:viewDeducts){
            QueryConditions conditions = new QueryConditions();
            conditions.add("cardId","=",viewDeduct.getCardId());
            boolean flag = EmployeeDao.exist(conn,conditions).exist;
            if(!flag){
                result.success = false;
                result.msg="员工"+viewDeduct.getName()+"未找到，请核对";
               return result;
            }
            Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;
            long eid = employee.getId();
            //计算个人专项扣除总额
            float deducts = viewDeduct.getDeduct1()+viewDeduct.getDeduct2()+viewDeduct.getDeduct3()+viewDeduct.getDeduct4()+viewDeduct.getDeduct5()+viewDeduct.getDeduct6();
            Deduct deduct = viewDeduct;//将视图的数据赋值给员工各项扣除
            deduct.setEid(eid);
            deduct.setDeduct(deducts);
            if(!DeductDao.exist(conn,eid).exist){//如果不存在则插入
                result = DeductDao.insert(conn,deduct);
            }else {//存在则修改
                result = DeductDao.update(conn,deduct);
            }
        }
        return result;
    }
}
