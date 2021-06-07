package service.contract;

import bean.admin.Account;
import bean.contract.Contract;
import dao.client.DispatchDao;
import dao.contract.ContractDao;
import database.*;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static utills.DateUtil.format;
import static utills.DateUtil.getExpireTime;

public class ContractService {
    public static DaoQueryListResult getList(Connection conn, QueryParameter parameter, String type, Account user) {
        if(user.getRole()==Account.ROLE_DISPATCH){
            if(user.isAdmin()) {
                parameter.addCondition("aid", "=", user.getRid());
            }else {
                parameter.addCondition("admin", "=", user.getId());
            }
        }else if(user.getRole()==Account.ROLE_COOPERATION ) {
            if(type.equals("B")){
                parameter.addCondition("bid", "=", user.getRid());
            }else if(type.equals("C")){
                parameter.addCondition("aid", "=", user.getRid());
            }
        }
        return ContractDao.getList(conn,parameter,type);
    }

    public static DaoQueryResult getLast(Connection conn, long id,String type) {
        return ContractDao.getLast(conn,id,type);
    }

    public static DaoUpdateResult insert(Connection conn, Contract contract) {
        return ContractDao.insert(conn,contract);
    }

    public static DaoUpdateResult update(Connection conn, Contract contract) {
        return  ContractDao.update(conn,contract);
    }


    public static DaoQueryResult get(Connection conn,String id) {
        return ContractDao.get(conn,id);
    }

    public static DaoUpdateResult delete(Connection conn, String id) {
        return ContractDao.delete(conn,id);
    }

    public static DaoQueryListResult getExpireContract(Connection conn, QueryParameter parameter, String type, Account user, int interval) {
        if(user.getRole()==Account.ROLE_DISPATCH){
            if(user.isAdmin()) {
                parameter.addCondition("aid", "=", user.getRid());
            }else {
                parameter.addCondition("admin", "=", user.getId());
            }
        }else if(user.getRole()==Account.ROLE_COOPERATION ) {
            if(type.equals("B")){
                parameter.addCondition("bid", "=", user.getRid());
            }else if(type.equals("C")){
                parameter.addCondition("aid", "=", user.getRid());
            }
        }


        Calendar calendar = Calendar.getInstance();
        Date startTime =calendar.getTime();//获取当前日期
        Date endTime = getExpireTime(startTime,interval);//结束时间
        return ContractDao.getExpireContract(conn,startTime,endTime,parameter,type);
    }
}
