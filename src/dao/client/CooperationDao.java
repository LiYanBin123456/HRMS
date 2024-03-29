package dao.client;

import bean.client.Cooperation;

import database.*;

import java.sql.Connection;

public class CooperationDao {

    public static DaoQueryResult get(Connection conn,  QueryConditions conditions) {
        return  DbUtil.get(conn,"cooperation",conditions, Cooperation.class);
    }

    public static DaoExistResult exist(Connection conn, QueryConditions conditions) {
        return  DbUtil.exist(conn,"cooperation",conditions);
    }

    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(name,contact)","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"cooperation",param, Cooperation.class);
    }

    public  static DaoUpdateResult update(Connection conn, Cooperation c){
        String sql = "update cooperation set rid=?,name=?,nickname=?,address=?,contact=?,phone=?,wx=?,qq=?,mail=?,intro=?,type=?,category=?,per1=?,per2=? where id=?";
        Object []params = {c.getRid(),c.getName(),c.getNickname(),c.getAddress(), c.getContact(), c.getPhone(),c.getWx(),c.getQq(),c.getMail(),c.getIntro(),c.getType(),c.getCategory(),c.getPer1(),c.getPer2(),c.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }


    public static DaoUpdateResult insert(Connection conn, Cooperation c) {
        String sql = "insert into cooperation (aid,did,rid,name,nickname,address,contact,phone,wx,qq,mail,intro,type,category,per1,per2) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {c.getAid(),c.getDid(),c.getRid(),c.getName(),c.getNickname(),c.getAddress(), c.getContact(), c.getPhone(),c.getWx(),c.getQq(),c.getMail(),c.getIntro(),c.getType(),c.getCategory(),c.getPer1(),c.getPer2()};
        return DbUtil.insert(conn,sql,params);
    }

    //删除客户
    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"cooperation",conditions);
    }


    //修改客户状态 0_潜在，1_合作,2_流失
    public static DaoUpdateResult updateType(Connection conn, long id,byte type){
        String sql = "update cooperation set type=? where id=?";
        Object []params = {type,id};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    public static DaoUpdateResult allocateAdmin(Connection conn, String[] cids, long aid) {
        String sql = "update cooperation set aid=? where id=?";
        Object [][]params = new Object[cids.length][];
        for (int i = 0; i < cids.length; i++) {
            params[i] = new Object[]{aid,cids[i]};
        }
        return DbUtil.batch(conn,sql,params);
    }
}
