package service.settlement;

import bean.settlement.Detail3;
import bean.settlement.Settlement3;
import dao.settlement.Settlement3Dao;
import database.*;

import javax.sound.midi.Soundbank;
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
         * 2、根据cid查询出派遣到该单位的所有员工eids(不包括小时工)
         * 3、根据eids的个数 封装好商业保险单明细集合
         * 4、批量插入商业保险结算单明细
         **/
        DaoUpdateResult result = Settlement3Dao.insert(conn,settlement3);
        if(result.success){
            long sid = (long) result.extra;//返回插入后的主键
            System.out.println("sid:"+sid);
            long cid = settlement3.getCid();//合作单位id
            long pid = settlement3.getPid();//产品id
            Date month = settlement3.getMonth();//月份

            //根据cid获取所有外派员工eids，不包括小时工
            QueryParameter parameter = new QueryParameter();
            QueryConditions conditions = new QueryConditions();
            conditions.add("cid", "=", cid);
            conditions.add("type", "=", 1);
            conditions.add("category", "!=", 2);
            String sql = "id";
            parameter.conditions = conditions;
            //获取到员工的所有id
            List<Long> eids = DbUtil.getColumns(conn, sql,"employee",parameter);
            for(long eid:eids){
                System.out.println(eid);
                Detail3 detail3 = new Detail3();//生成明细对象
                detail3.setSid(sid);
                detail3.setEid(eid);
                detail3.setPid(pid);
                detail3.setMonth(month);
                System.out.println(detail3);
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
        /**流程
         * 1、查询出结算单，修改结算月份
         * 2、插入结算单，返回主键id
         * 3、获取结算单明细集合，修改月份和id
         * 4、批量插入数据库
         */
        Settlement3 settlement3 = (Settlement3) Settlement3Dao.get(conn, id).data;
        settlement3.setMonth(month);
        long sid = (long) Settlement3Dao.insert(conn, settlement3).extra;
        System.out.println("sid:"+sid);
        //获取结算单明细列表
        QueryParameter parameter = new QueryParameter();
        QueryConditions conditions = new QueryConditions();
        conditions.add("sid", "=", id);
        parameter.conditions = conditions;
        DaoQueryListResult result = DbUtil.getList(conn,"detail3",parameter,Detail3.class);

        return null;
    }

}
