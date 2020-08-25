package service.employee;

import bean.employee.PayCard;
import dao.employee.ExtraDao;
import dao.employee.PayCardDao;
import database.DaoQueryResult;
import database.DaoUpdateResult;

import java.sql.Connection;

public class PayCardService {
    //获取详情
    public static DaoQueryResult get(Connection conn, long id) {
        return PayCardDao.get(conn,id);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, PayCard payCard) {
        return PayCardDao.update(conn,payCard);
    }

    //增加
    public static DaoUpdateResult insert(Connection conn, PayCard payCard) {
        DaoUpdateResult result = new DaoUpdateResult();
        if(!PayCardDao.exist(conn,payCard.getEid()).exist){
            result = PayCardDao.insert(conn,payCard);
        }else {
            result.msg = "该员工的工资卡已存在，请勿重复添加";
        }
        return result;
    }

}
