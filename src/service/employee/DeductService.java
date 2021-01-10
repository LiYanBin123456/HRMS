package service.employee;

import bean.employee.Deduct;
import bean.employee.Employee;
import bean.employee.ViewDeduct;
import bean.employee.ViewEmployee;
import dao.employee.DeductDao;
import dao.employee.EmployeeDao;
import database.*;
import utills.CollectionUtil;

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

    /**
     * 根据excel导入的数据更新数据库员工个税专项扣除的信息
     * @param conn
     * @param data  导入的数据
     * @return 返回更新结果，如果存在导入的员工不存在数据库的信息置于extra中
     */
    public static DaoUpdateResult updateDeducts(Connection conn, List<ViewDeduct> data){
        QueryParameter parameter = new QueryParameter();
        //获取数据库中所有员工个税专项扣除
        List<ViewDeduct> deducts_all = (List<ViewDeduct>) DeductDao.getList(conn,parameter).rows;

        //用来存储已存在员工个税专项扣除的信息
        List<Deduct> deducts_update=new ArrayList<>();

        //用来存储异常的员工，就是数据库中没有该员工的信息，需要返回给前台
        List<ViewDeduct> deducts_err=new ArrayList<>();

        DaoUpdateResult result = new DaoUpdateResult();
        result.success=true;

         for(ViewDeduct deduct:data){//遍历excel中的扣除信息
             //判断员工是否存在
            ViewDeduct d = CollectionUtil.getElement(deducts_all,"cardId",deduct.getCardId());
            if(d!=null){
                 d.setDeduct1(deduct.getDeduct1());
                 d.setDeduct2(deduct.getDeduct2());
                 d.setDeduct3(deduct.getDeduct3());
                 d.setDeduct4(deduct.getDeduct4());
                 d.setDeduct5(deduct.getDeduct5());
                 d.setDeduct6(deduct.getDeduct6());
                 deducts_update.add(d);
             }else {//异常
                 deducts_err.add(deduct);
            }
         }
        if(deducts_err.size()>0){//由异常的员工需要返回前台显示
            result.success = false;
            result.extra = deducts_err;
            return result;
        }
        result=DeductDao.updateDeducts(conn,deducts_update);
        return result;
    }



    /**
     *
     * @param conn
     * @param data  导入的数据
     * @param cid  客户id
     * @return
     */
    public static DaoUpdateResult updateDeducts(Connection conn, List<ViewDeduct> data, long cid) {
        /**
         * 流程
         * 1、先根据合作单位id 查询出所属该公司的员工列表
         * 2、遍历data数据，查询出所属的员工
         * 3、修改这一部分员工的信息
         */
        //获取该公司所有员工的信息
        QueryParameter parameter = new QueryParameter();
        parameter.addCondition("cid","=",cid);
        List<ViewEmployee> employees = (List<ViewEmployee>) EmployeeDao.getList(conn,parameter).rows;

        List<Deduct> deducts = new ArrayList<>();//用于保存修改的个税
        for(Employee e:employees){
            ViewDeduct d = CollectionUtil.getElement(data,"cardId",e.getCardId());
            if(d != null){
                d.setEid(e.getId());
                deducts.add(d);
            }
        }
        DaoUpdateResult result=DeductDao.updateDeducts(conn,deducts);
        return result;
    }
}
