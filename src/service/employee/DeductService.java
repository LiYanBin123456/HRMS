package service.employee;

import bean.employee.Deduct;
import bean.employee.Employee;
import bean.employee.ViewDeduct;
import bean.employee.ViewEmployee;
import dao.employee.DeductDao;
import dao.employee.EmployeeDao;
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
         return DeductDao.update(conn,deduct);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, Deduct deduct) {
        DaoUpdateResult result;
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

    //增加个人专项扣除数
    public static DaoQueryListResult addDeducts(Connection conn, List<ViewDeduct> data){
        ConnUtil.closeAutoCommit(conn);

        QueryParameter parameter = new QueryParameter();
        //获取数据库中所有员工个税专项扣除
        List<ViewDeduct> deductList = (List<ViewDeduct>) DeductDao.getList(conn,parameter).rows;

        //用来存储已存在员工个税专项扣除的信息
        List<Deduct> deductList1=new ArrayList<>();

        //用来存储不存在员工个税专项扣除的信息
        List<Deduct> deductList2=new ArrayList<>();

        //用来存储异常的员工，就是数据库中没有该员工的信息，需要返回给前台
        List<ViewDeduct> deductList3=new ArrayList<>();

        DaoUpdateResult result1 = new DaoUpdateResult();
        result1.success=true;

        DaoUpdateResult result2 = new DaoUpdateResult();
        result2.success=true;

        DaoQueryListResult result = new DaoQueryListResult();
        result.success=true;
         for(ViewDeduct deduct:data){//遍历excel中的扣除信息
             //判断员工是否存在
            ViewDeduct d = getDeduct(deductList,deduct.getCardId());
            if(d!=null){
                 d.setDeduct1(deduct.getDeduct1());
                 d.setDeduct2(deduct.getDeduct2());
                 d.setDeduct3(deduct.getDeduct3());
                 d.setDeduct4(deduct.getDeduct4());
                 d.setDeduct5(deduct.getDeduct5());
                 d.setDeduct6(deduct.getDeduct6());
                 deductList1.add(d);
             }else {
                QueryConditions conditions = new QueryConditions();
                conditions.add("cardId","=",deduct.getCardId());
                if(EmployeeDao.exist(conn,conditions).exist){//该员工存在数据库中，但是该员工的专项扣除还不存在
                    Employee employee = (Employee) EmployeeDao.get(conn,conditions).data;
                    deduct.setEid(employee.getId());
                    deductList2.add(deduct);
                }else {//这种员工属于数据库中不存在,属于异常员工
                    deductList3.add(deduct);
                }
            }
         }
         if(deductList1.size()>0){
             result1=DeductDao.updateDeducts(conn,deductList1);
         }
         if(deductList2.size()>0){//需要插入员工专项扣除
             result2=DeductDao.importDeducts(conn,deductList2);
         }
         if(deductList3.size()>0){//由异常的员工需要返回前台显示
             result.rows = deductList3;
         }else {
             result.rows=null;
         }

         if(!result1.success&&!result2.success){//失败回滚
             ConnUtil.rollback(conn);
             result.success=false;
             return result;
         }
         //提交
         ConnUtil.commit(conn);

         return result;
    }

    private static ViewDeduct getDeduct(List<ViewDeduct> deductList, String cardId) {
        for(ViewDeduct deduct:deductList){
            if(deduct.getCardId().equals(cardId)){
                return deduct;
            }
        }
        return null;
    }

    /**
     * @param conn
     * @param data
     * @param cid
     */
    public static DaoQueryListResult addPartDeducts(Connection conn, List<ViewDeduct> data, long cid) {
        /**
         * 流程
         * 1、先根据合作单位id 查询出所属该公司的员工列表
         * 2、遍历data数据，查询出所属的员工
         * 3、
         */
        ConnUtil.closeAutoCommit(conn);
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("cid","=",cid);
        List<ViewEmployee> employeeList = (List<ViewEmployee>) EmployeeDao.getList(conn,parameter).rows;

        DaoUpdateResult result1 = new DaoUpdateResult();
        result1.success=true;
        DaoUpdateResult result2 = new DaoUpdateResult();
        result2.success=true;
        DaoQueryListResult result = new DaoQueryListResult();
        result.success=true;
        List<Deduct> deductList = new ArrayList<>();//用于保存修改的个税
        List<Deduct> deductList2 = new ArrayList<>();//用于保存添加的个税
        for(Employee employee:employeeList){
            ViewDeduct d =getDeduct(data,employee.getCardId());
            if(d!=null){
               d.setEid(employee.getId());
               if(DeductDao.exist(conn,employee.getId()).exist){//如果存在则添加到要修改的个税集合中
                   deductList.add(d);
               }else {//不存在则添加到导入个税集合中
                   deductList2.add(d);
               }
            }
        }
        if(deductList.size()>0){
            result1=DeductDao.updateDeducts(conn,deductList);
        }
        if(deductList2.size()>0){
            result2=DeductDao.importDeducts(conn,deductList2);
        }
        if(!result1.success&&!result2.success){//失败回滚
            ConnUtil.rollback(conn);
            result.success=false;
            return result;
        }
        //提交
        ConnUtil.commit(conn);
        return result;
    }
}
