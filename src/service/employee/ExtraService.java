package service.employee;

import bean.employee.Employee;
import bean.employee.EmployeeExtra;
import com.alibaba.fastjson.JSONObject;
import dao.employee.EmployeeDao;
import dao.employee.ExtraDao;
import database.*;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class ExtraService {
    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        return ExtraDao.getList(conn,param);
    }

    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        return ExtraDao.get(conn,id);
    }

    //修改
    public static DaoUpdateResult update(Connection conn,  EmployeeExtra extra) {
        return ExtraDao.update(conn,extra);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, EmployeeExtra extra) {
        DaoUpdateResult result = new DaoUpdateResult();
        if(!ExtraDao.exist(conn,extra.getEid()).exist){
           result = ExtraDao.insert(conn,extra);
        }else {
            result.msg = "该员工的补充信息已存在，请勿重复添加";
        }
        return result;
    }

    //批量插入
    public static DaoUpdateResult insertBatch(Connection conn, List<EmployeeExtra> extras) {
        return  ExtraDao.insertBatch(conn,extras);
    }
}
