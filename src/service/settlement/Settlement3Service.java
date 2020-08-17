package service.settlement;

import bean.settlement.Detail3;
import bean.settlement.Settlement1;
import bean.settlement.Settlement3;
import dao.employee.EmployeeDao;
import dao.settlement.Settlement3Dao;
import database.*;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class Settlement3Service {
    public static DaoQueryListResult getList(Connection conn, QueryParameter param) {
        return Settlement3Dao.getList(conn,param);
    }

    public static DaoUpdateResult insert(Connection conn, Settlement3 settlement3) {
        /**
         * 1、插入结算单 返回id
         * 2、根据cid查询出派遣到该单位的所有员工eids
         * 3、根据eids的个数 封装好商业保险单明细集合
         * 4、批量插入商业保险结算单明细
         **/
        DaoUpdateResult result = Settlement3Dao.insert(conn,settlement3);
        if(result.success){
            long sid = (long) result.extra;//返回插入后的主键
            long cid = settlement3.getCid();//合作单位id
            long pid = settlement3.getPid();//产品id
            Date month = settlement3.getMonth();//月份

            //根据cid获取所有员工eids
            QueryParameter parameter = new QueryParameter();
            QueryConditions conditions = new QueryConditions();
            conditions.add("cid", "=", cid);
            String sql = "id";
            parameter.conditions = conditions;
            //获取到员工的所有id
            List<Long> eids = DbUtil.getColumns(conn, sql,"employee",parameter);
            for(long eid:eids){
                Detail3 detail3 = new Detail3();//生成明细对象
                detail3.setSid(sid);
                detail3.setEid(eid);
                detail3.setPid(pid);
                detail3.setMonth(month);
            }
            //批量插入明细
        }else {

        }
        return null;
    }

    public static DaoUpdateResult delete(Connection conn, Long id) {
        return Settlement3Dao.delete(conn,id);
    }

    public static DaoUpdateResult saveAs(Connection conn, long id,Date month) {
        DaoQueryResult result = null;
        return null;
    }

}
