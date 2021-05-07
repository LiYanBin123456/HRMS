package dao.insurance;

import bean.insurance.Insured;
import bean.insurance.ViewInsured;
import database.*;

import java.sql.Connection;
import java.util.List;

public class InsuredDao {

    //增加
    public static DaoUpdateResult insert(Connection conn, Insured e) {
        //需要判断外键是否为0，为0就需要转换成null
        String cid = e.getCid()==0?null:String.valueOf(e.getCid());
        String did = e.getDid()==0?null:String.valueOf(e.getDid());
        String sql = "insert insured (cid,did,cardId,name,place,post,category) values (?,?,?,?,?,?,?)";
        Object []params = {cid,did,e.getCardId(),e.getName(),e.getPlace(),e.getPost(),e.getCategory()};
        return  DbUtil.insert(conn,sql,params);
    }

    //删除
    public static DaoUpdateResult delete(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"insured",conditions);
    }

    //修改
    public static DaoUpdateResult update(Connection conn, Insured e) {
        String sql = "update insured set cid =?,cardId=?,name=?,place=?,post=?,category=? where id=?";
        Object []params = {e.getCid(),e.getCardId(),e.getName(),e.getPlace(),e.getPost(),e.getCategory(),e.getId()};
        return DbUtil.update(conn,sql,params);
    }

    //获取列表
    public static DaoQueryListResult getList(Connection conn, QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("name","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"view_insured",param,ViewInsured.class);
    }

    public static DaoQueryResult get(Connection conn, QueryConditions conditions) {
        return DbUtil.get(conn,"insured",conditions,Insured.class);
    }

    public static DaoUpdateResult insertBatch(Connection conn, List<Insured> is) {
        String sql = "insert insured (cid,did,cardId,name,place,post,category) values (?,?,?,?,?,?,?)";
        Object [][]params = new Object[is.size()][];
        for (int i = 0; i < is.size(); i++) {
            //需要判断外键是否为0，为0就需要转换成null
            String cid = is.get(i).getCid()==0?null:String.valueOf(is.get(i).getCid());
            String did = is.get(i).getDid()==0?null:String.valueOf(is.get(i).getDid());
            params[i] = new Object[]{cid,did,is.get(i).getCardId(),is.get(i).getName(),is.get(i).getPlace(),
                    is.get(i).getPost(),is.get(i).getCategory()
            };
        }
        return DbUtil.insertBatch(conn,sql,params);
    }
}
