package service.settlement;

import bean.contract.Serve;
import bean.employee.Employee;
import bean.insurance.Product;
import bean.settlement.*;
import dao.contract.ServeDao;
import dao.employee.EmployeeDao;
import dao.product.ProductDao;
import dao.settlement.Detail2Dao;
import dao.settlement.Detail3Dao;
import dao.settlement.Settlement3Dao;
import database.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Detail3Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param,long id){
        param.conditions.add("sid","=",id);
        return Detail3Dao.getList(conn,param);
    }
    public static DaoUpdateResult update(Connection conn, List<Detail3> details){
        return  Detail3Dao.update(conn,details);
    }
    public static String exportDetails(Connection conn,long id){
        return  null;
    }

    public static DaoUpdateResult importDetails(Connection conn, long sid, List<Detail3> details) {
        for(Detail3 d:details){
            d.setSid(sid);
        }


        DaoUpdateResult result = Detail3Dao.importDetails(conn,details);
        return result;
    }

    public static DaoUpdateResult replaceDetails(Connection conn, List<Detail3> member1, List<Detail3> member2) {
        for(Detail3 d:member1){
            d.setStatus(Detail3.STATUS_REPLACED);
        }
        long sid = member1.get(0).getSid();
        Date date = new Date();
        byte day = (byte) date.getDate();
        for(Detail3 d:member2){
            d.setSid(sid);
            d.setDay(day);
            d.setStatus(Detail3.STATUS_INSERT);
        }
        ConnUtil.closeAutoCommit(conn);
        DaoUpdateResult res1 = Detail3Dao.update(conn,member1);
        DaoUpdateResult res2 = Detail3Dao.importDetails(conn,member2);
        if(res1.success && res2.success){
            ConnUtil.commit(conn);
            return res1;
        }else{
            ConnUtil.rollback(conn);
            DaoUpdateResult res = new DaoUpdateResult();
            res.success = false;
            res.msg = "数据库错误";
            return res;
        }
    }
}
