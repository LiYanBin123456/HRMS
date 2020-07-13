package dao.admin;

import bean.admin.Rule_medical;
import database.*;

import java.sql.Connection;

public class Rule_medicalDao {

    /**
     * 获取医保列表
     * @param conn 数据库连接
     *@param param 查询参数
     * @return 检索结果，格式："{success:true,msg:"",effects:1}"
     */
    public DaoQueryListResult getRule_medicalList(Connection conn, QueryParameter param){
        //sql 自定义要查询的字段
        //String sql = "id,city,`start`,base,concat(round(per1 *100,2),'%')AS per1,concat(round(per2 *100,2),'%')AS per2,concat(round(per3 *100,2),'%')AS per3,concat(round(per4 *100,2),'%')AS per4";
        return DbUtil.getList(conn,"rule_medical",param, Rule_medical.class);
    }

    public DaoQueryResult getMedical(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.get(conn,"rule_medical",conditions,Rule_medical.class);
    }

    public DaoUpdateResult updateMedical(Connection conn, Rule_medical m) {
        String sql = "update rule_medical set city = ?,start = ?,base=?,per1=?,per2=?,per3=?,per4=? where id=?";
        Object []param ={m.getCity(),m.getStart(),m.getBase(),m.getPer1(),m.getPer2(),m.getPer3(),m.getPer4(),m.getId()} ;
        return  DbUtil.update(conn,sql,param);
    }

    public DaoUpdateResult insertMedical(Connection conn, Rule_medical m) {
        String sql = "insert rule_medical(city ,start ,base,per1,per2,per3,per4) values (?,?,?,?,?,?,?)";
        Object []param ={m.getCity(),m.getStart(),m.getBase(),m.getPer1(),m.getPer2(),m.getPer3(),m.getPer4()};
        return  DbUtil.insert(conn,sql,param);
    }

    public DaoUpdateResult deleteMedical(Connection conn, long id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id","=",id);
        return DbUtil.delete(conn,"rule_medical",conditions);
    }
}
