package dao.contract;

import bean.contract.Contract;
import bean.contract.Serve;
import bean.contract.ViewContractCooperation;
import bean.contract.ViewContractEmployee;
import database.*;

import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.util.List;

public class ContractDao {
    //根据查询条件获取合同列表，用视图查找
    public DaoQueryListResult getList(Connection conn, QueryParameter parameter,String type) {
        DaoQueryListResult res = null;
        String A="A";
        String B="B";
        String C="C";
        String D="D";
        if(type.equals(A)){
            //查询type为A即平台和派遣单位的合同
            parameter.addCondition("type","=",type);
            res= DbUtil.getList(conn,"contract",parameter, Contract.class);
        }else if(type.equals(B)){
            //查询平台和合作单位的合同,现在还没有这方面的合同
            res = null;
        } else if(type.equals(C)){
            //查询type为C即派遣单位和合作单位的合同
            if(parameter.conditions.extra!=null && !parameter.conditions.extra.isEmpty()) {
                parameter.addCondition("name","like",parameter.conditions.extra);
            }
            parameter.addCondition("type","=",type);
            res= DbUtil.getList(conn,"view_contract_cooperation",parameter, ViewContractCooperation.class);
        }else if(type.equals(D)){
            //查询type为D即派遣单位和员工的合同
            if(parameter.conditions.extra!=null && !parameter.conditions.extra.isEmpty()) {
                parameter.addCondition("name","like",parameter.conditions.extra);
                System.out.println(parameter.conditions.extra);
            }
            parameter.addCondition("type","=",type);
            res= DbUtil.getList(conn,"view_contract_employee",parameter, ViewContractEmployee.class);
        }
        return res;
    }

    //查询最新合同
    public  DaoQueryResult getLast(Connection conn, String bid,String type) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("bid", "=", bid);
        conditions.add("type", "=", type);
        return DbUtil.getLast(conn, "contract", conditions,Contract.class);

    }

    //插入合同
    public DaoUpdateResult insertContract(Connection conn, Contract c) {
        String sql = "insert into contract (id,aid,bid,type,start,end,status,intro,comments,project,times) values (?,?,?,?,?,?,?,?,?,?,?)";
        Object []params = {c.getId(),c.getAid(),c.getBid(),c.getType(),c.getStart(),c.getEnd(),c.getStatus(),c.getComments(),c.getInvoice(),c.getProject(),c.getTimes()};
        return DbUtil.insert(conn,sql,params);
    }

    //修改合同
    public  DaoUpdateResult update(Connection conn, Contract c){
        return null;
    }

    //删除一个合同，还要删除对应的合同文件
    public DaoUpdateResult delete(Connection conn, String id) {
        QueryConditions conditions = new QueryConditions();
        conditions.add("id", "=", id);
        return DbUtil.delete(conn,"contract",conditions);
    }

    //删除该客户的所有合同,还要删除对应合同的文件
    public List<String> deleteContract(Connection conn, long id) {
        QueryParameter parameter = new QueryParameter();
        QueryConditions conditions = new QueryConditions();
        conditions.add("bid", "=", id);

        String sql = "id";
        parameter.conditions = conditions;
        //获取到该用户的所有合同名字
        List<String> column = DbUtil.getColumns(conn, sql,"contract",parameter);

        //查询完所有要删除的合同文件名字之后，在删除合同
        DbUtil.delete(conn,"contract",conditions);
        return column;
    }

    //添加合同服务项目
    public String insertService(Connection conn, Serve serve){
        return null;
    }

    //获取合同服务项目
    public String getService(Connection conn,Long cid){
        return null;
    }

    //获取合作客户的所有合同服务项目
    public String getServiceList(Connection conn,QueryParameter parameter){
        return null;
    }

    //修改合同服务项目
    public String updateService(Connection conn,Serve serve){
        return null;
    }
}
