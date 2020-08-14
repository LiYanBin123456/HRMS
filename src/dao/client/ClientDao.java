package dao.client;

import database.DaoUpdateResult;
import database.DbUtil;

import java.sql.Connection;

public class ClientDao {

    //分配管理员
    public DaoUpdateResult allocate(Connection conn, Long aid, String[] cids, byte category) {
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
               res = DbUtil.batch(conn,sql,params);
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


}
