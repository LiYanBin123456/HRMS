package service.settlement;

import bean.settlement.*;
import dao.settlement.Detail3Dao;
import dao.settlement.Settlement3Dao;
import database.*;

import java.sql.Connection;
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
            d.setStatus(Detail3.STATUS_INSERT);
        }


        DaoUpdateResult result = Detail3Dao.importDetails(conn,details);
        return result;
    }

    /**
     * 替换参保
     * @param conn
     * @param member1 换下的
     * @param member2 换上的
     * @return
     */
    public static DaoUpdateResult replaceDetails(Connection conn, List<Detail3> member1, List<Detail3> member2) {
        long sid = member1.get(0).getSid();
        Date date = new Date();
        byte day = (byte) date.getDate();
        for(Detail3 d:member2){
            d.setSid(sid);
            d.setDay(day);
            d.setStatus(Detail3.STATUS_REPLACING_UP);
        }
        ConnUtil.closeAutoCommit(conn);
        DaoUpdateResult res1 = Detail3Dao.importDetails(conn,member2);

        long []ids = (long[]) res1.extra;
        for(int i=0; i<member1.size(); i++){
            Detail3 d = member1.get(i);
            d.setUid(ids[i]);
            d.setStatus(Detail3.STATUS_REPLACING_DOWN);
        }
        DaoUpdateResult res2 = Detail3Dao.update(conn,member1);

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

    /**
     * 确认新增
     * @param conn
     * @param sid
     * @param ids
     * @param day
     * @return
     */
    public static DaoUpdateResult confirmDetails(Connection conn, long sid,String[] ids, byte day) {
        /**
         * 思路：
         * （1）修改状态为参保
         * （2）修改结算单统计信息
         * （2）日志
         * （3）扣款
         * （4）资金明细
         */
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult res1 = Detail3Dao.confirm(conn,Detail3.STATUS_CONFIRMED,ids,day);
        DaoUpdateResult res2 = Settlement3Dao.statistic(conn,sid,ids.length);

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

    /**
     * 确认替换
     * @param conn
     * @param sid 结算单编号
     * @param ids1 换下的
     * @param ids2 换上的
     * @param day 生效日
     * @return
     */
    public static DaoUpdateResult confirmDetails(Connection conn,long sid, String[] ids1, String[] ids2,byte day) {
        /**
         * 思路：
         * （1）拟换下的改为替换
         * （2）拟换上的改为参保
         * （3）日志
         */
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult res1 = Detail3Dao.confirm(conn,Detail3.STATUS_REPLACED,ids1,day);
        DaoUpdateResult res2 = Detail3Dao.confirm(conn,Detail3.STATUS_CONFIRMED,ids2,day);

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
