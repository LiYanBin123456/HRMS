package dao.client;

import bean.client.Cooperation;

import bean.client.Dispatch;
import database.*;

import java.sql.Connection;

public class CooperationDao {

    public DaoQueryResult get(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return  DbUtil.get(conn,"cooperation",conditions, Cooperation.class);
    }

    public DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(name,contact)","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"cooperation",param, Cooperation.class);
    }

    public  DaoUpdateResult update(Connection conn, Cooperation c){
        String sql = "update cooperation set aid=?,did=?, rid=?,name=?,nickname=?,address=?,contact=?,phone=?,wx=?,qq=?,mail=?,intro=?,type=?,category=? where id=?";
        Object []params = {c.getAid(),c.getDid(),c.getRid(),c.getName(),c.getNickname(),c.getAddress(), c.getContact(), c.getPhone(),c.getWx(),c.getQq(),c.getMail(),c.getIntro(),c.getType(),c.getCategory(),c.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }


    public DaoUpdateResult insert(Connection conn, Cooperation c) {
        String sql = "insert into cooperation (aid,did,rid,name,nickname,address,contact,phone,wx,qq,mail,intro,type,category) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {c.getAid(),c.getDid(),c.getRid(),c.getName(),c.getNickname(),c.getAddress(), c.getContact(), c.getPhone(),c.getWx(),c.getQq(),c.getMail(),c.getIntro(),c.getType()};
        return DbUtil.insert(conn,sql,params);
    }

    //删除客户
    public DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"cooperation",conditions);
    }


    //删除合作客户 实际是修改客户状态 合作或者潜在 0_潜在，1_合作
    public  DaoUpdateResult updateStatus(Connection conn, long id,int status){
        String sql = "update dispatch set status=? where id=?";
        Object []params = {status,id};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }
}
