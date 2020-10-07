package dao.contract;

import bean.contract.*;
import database.*;

import javax.servlet.http.HttpSession;
import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.util.List;

public class ContractDao {
    //根据查询条件获取合同列表，用视图查找
    public static DaoQueryListResult getList(Connection conn, QueryParameter parameter, String type, long rid) {
        DaoQueryListResult res = null;
        if(parameter.conditions.extra!=null && !parameter.conditions.extra.isEmpty()) {
            parameter.addCondition("concat(name,comments)","like",parameter.conditions.extra);
        }
        switch(type){
            case "A":
                //获取平台和所有派遣方的合同
                res= DbUtil.getList(conn,"view_contract_dispatch",parameter, ViewContractDispatch.class);
                break;
            case "B":
                //获取该派遣方与之合作单位的合同
                parameter.addCondition("aid","=",rid);
                res= DbUtil.getList(conn,"view_contract_cooperation",parameter, ViewContractCooperation.class);
                break;
            case "C":
                //获取该派遣方与之员工的合同
                parameter.addCondition("aid","=",rid);
                res= DbUtil.getList(conn,"view_contract_employee",parameter, ViewContractEmployee.class);
                break;
        }
        return res;
    }

    public static DaoQueryResult get(Connection conn,String id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id", "=", id);
        return DbUtil.get(conn,"contract",conditions,Contract.class);
    }

    //获取合作客户的合同视图
    public static DaoQueryResult getViewContractCoop(Connection conn,String id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id", "=", id);
        return DbUtil.get(conn,"view_contract_cooperation",conditions,ViewContractCooperation.class);
    }

    //查询最新合同
    public  static DaoQueryResult getLast(Connection conn, long bid,String type) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("bid", "=", bid);
        conditions.add("type", "=", type);
        String order = " ORDER BY id DESC limit 1";
        return DbUtil.getLast(conn, "contract", conditions,Contract.class,order);

    }

    //插入合同
    public static DaoUpdateResult insert(Connection conn, Contract c) {
        String sql = "insert into contract (id,aid,bid,type,start,end,status,comments,invoice,project,per,times) values (?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {c.getId(),c.getAid(),c.getBid(),c.getType(),c.getStart(),c.getEnd(),c.getStatus(),c.getComments(),c.getInvoice(),c.getProject(),c.getPer(),c.getTimes()};
        return DbUtil.insert(conn,sql,params);
    }

    //修改合同
    public  static DaoUpdateResult update(Connection conn, Contract c){
        String sql = "update contract set type=?,start=?,end=?,status=?,comments=?,invoice=?,project=?,per=?,times=? where id =?";
        Object []params = {c.getType(),c.getStart(),c.getEnd(),c.getStatus(), c.getComments(), c.getInvoice(),c.getProject(),c.getPer(),c.getTimes(),c.getId()};
        //调用DbUtil封装的update方法
        return DbUtil.update(conn,sql,params);
    }

    //删除一个合同，还要删除对应的合同文件
    public static DaoUpdateResult delete(Connection conn, String id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id", "=", id);
        return DbUtil.delete(conn,"contract",conditions);
    }

    /**
     * 删除该客户的所有合同
     * @param conn
     * @param id 该客户的id
     * @param type 合同类型
     * @return
     */
    public static List<String> deleteContract(Connection conn, long id,String type) {
        QueryParameter parameter = new QueryParameter();
        QueryConditions conditions = new QueryConditions();
        conditions.add("bid", "=", id);
        conditions.add("type", "=", type);

        String sql = "id";
        parameter.conditions = conditions;
        //获取到该用户的所有合同名字
        List<String> column = DbUtil.getColumns(conn, sql,"contract",parameter);

        //查询完所有要删除的合同文件名字之后，在删除合同
        DbUtil.delete(conn,"contract",conditions);
        return column;
    }



}
