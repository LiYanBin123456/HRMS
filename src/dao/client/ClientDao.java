package dao.client;

import bean.client.Cooperation;
import bean.client.ViewFinanceCooperation;
import database.DaoQueryListResult;
import database.DaoUpdateResult;
import database.DbUtil;
import database.QueryParameter;

import java.sql.Connection;

public class ClientDao {

    //分配管理员
    public static DaoUpdateResult allocate(Connection conn, Long aid, String[] cids, byte category) {
       DaoUpdateResult res =null;
        String sql;
        Object [][]params ;
       switch (category){
           case 0://修改派遣单位的管理员
               sql = String.format("update dispatch set aid = %S where id = ?",aid);
               params = new Object[cids.length][];
               for (int i = 0; i < cids.length; i++) {
                   params[i] = new Object[]{cids[i]};
               }
               res = DbUtil.insertBatch(conn,sql,params);
               break;
           case 1://修改合作单位的管理员
              sql = String.format("update cooperation set aid = %S where id = ?",aid);
               params = new Object[cids.length][];
               for (int i = 0; i < cids.length; i++) {
                   params[i] = new Object[]{cids[i]};
               }
               res= DbUtil.batch(conn,sql,params);
               break;
       }
       return  res;

    }

    /**
     * 获取公司和余额列表
     * @return
     */
    public static DaoQueryListResult getBalances(Connection conn,QueryParameter param){
        if(param.conditions.extra!=null && !param.conditions.extra.isEmpty()) {
            param.addCondition("concat(name)","like",param.conditions.extra);
        }
        return DbUtil.getList(conn,"view_finance_cooperation",param, ViewFinanceCooperation.class);

    }


}
