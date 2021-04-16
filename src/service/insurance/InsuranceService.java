package service.insurance;

import bean.employee.EnsureSetting;
import bean.insurance.Insurance;
import bean.insurance.ViewInsurance;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.employee.SettingDao;
import dao.insurance.InsuranceDao;
import database.*;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import utills.CollectionUtil;

import javax.jnlp.ExtensionInstallerService;
import javax.lang.model.element.VariableElement;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InsuranceService {
    //插入
    public static String insertAndupdate(Connection conn, List<Insurance> insurances_insert, List<Insurance> insurances_update) {

        List<Insurance> insertAndupdate=new ArrayList<>();
        insertAndupdate.addAll(insurances_insert);
        insertAndupdate.addAll(insurances_update);
        if(insertAndupdate.size()<=0){//如果没有需要插入和修改的数据，直接返回
            return DaoResult.success();
        }
        List<Insurance> exists = (List<Insurance>)InsuranceDao.getList(conn,insertAndupdate.get(0).getEid()).rows;
        List<Insurance> insurances_delete=new ArrayList<>();
        List<Insurance> insurances_updateStatus=new ArrayList<>();
       //遍历数据库中的参保信息，如果不存在于传递过来的参保则停保或者删除
        if(exists.size()>0){
            String []keys = {"eid","category"};
            for(Insurance insurance:exists){
                Object []values = {insurance.getEid(),insurance.getCategory()};
                if(CollectionUtil.filter(insertAndupdate,keys,values).size() == 0){
                    if(insurance.getStatus()==Insurance.STATUS_APPENDING){//如果该数据是新增的，则删除，否则修改为拟停状态
                        insurances_delete.add(insurance);
                    }else if(insurance.getStatus()==Insurance.STATUS_NORMAL) {//如果数据是正常的，则改为拟停
                        insurance.setStatus(Insurance.STATUS_STOPING);
                        insurances_updateStatus.add(insurance);
                    }

                }
            }
        }
        ConnUtil.closeAutoCommit(conn);
        DaoUpdateResult res1 = InsuranceDao.insert(conn,insurances_insert);//插入数据
        DaoUpdateResult res2 = InsuranceDao.updateStatus(conn,insurances_updateStatus);//批量停保
        DaoUpdateResult res3 = InsuranceDao.delete(conn,insurances_delete);//删除停保
        DaoUpdateResult res4 = InsuranceDao.update(conn,insurances_update);//修改社保

        if(res1.success && res2.success && res3.success && res4.success ){
            ConnUtil.commit(conn);
            return JSONObject.toJSONString(res1);
        }else {
            ConnUtil.rollback(conn);
            return DaoResult.fail("数据库错误");
        }
    }

    //批量更新
    public static String insertBatch(Connection conn, String[] eids,List<Insurance> insurances) {
        /**
         * 1、封装所有员工的五险一金信息
         * 2、根据eids查询出所有的五险一金
         * 3、校对，如果存在同种数据则覆盖并且放入修改集合中否则放入新增集合
         * 4、如果数据库中存在，传递过来的数据中没有则需要停保
         * 5、插入数据库
         */
        List<Insurance> list = new ArrayList<>();
        List<Insurance> insurances_insert = new ArrayList<>();//新增集合
        List<Insurance> insurances_update= new ArrayList<>();//修改集合
        List<Insurance> insurances_delete= new ArrayList<>();//删除集合
        List<Insurance> insurances_updateStatus= new ArrayList<>();//修改集合

        for(int i = 0;i<eids.length;i++){
            for(Insurance in:insurances){
                Insurance insurance = new Insurance(Long.parseLong(eids[i]),in.getCity(),in.getCategory(),in.getCode(),in.getBase(),in.getBaseType()
                ,in.getV1(),in.getV2(),in.getDate(),in.getStatus(),in.getReason());
                list.add(insurance);
            }
        }

        //根据eids查询出员工已存在所拥有的五险一金
        QueryParameter p = new QueryParameter();
        String ids= "";
        for(int i =0 ;i<eids.length;i++){
            ids+= eids[i]+",";
        }
        if(ids.length()>1){
            ids = ids.substring(0,ids.length()-1);
        }
        p.addCondition("eid","in",ids);
        List<Insurance> exists = (List<Insurance>) InsuranceDao.getList(conn, p).rows;

        //遍历传递过来的参保信息，如果数据库中已存在则修改，否则添加
        String []keys = {"eid","category"};
        for(Insurance insurance:list){
            Object []values = {insurance.getEid(),insurance.getCategory()};
            if(CollectionUtil.filter(exists,keys,values).size() > 0){
                insurances_update.add(insurance);
            }else{
                insurances_insert.add(insurance);
            }
        }

        //遍历数据库中的参保信息，如果不存在于传递过来的参保则停保或者删除
        for(Insurance insurance:exists){
            Object []values = {insurance.getEid(),insurance.getCategory()};
            if(CollectionUtil.filter(list,keys,values).size() == 0){
                if(insurance.getStatus()==Insurance.STATUS_APPENDING){//如果该数据是新增的，则删除，否则修改为拟停状态
                    insurances_delete.add(insurance);
                }else if(insurance.getStatus()==Insurance.STATUS_NORMAL){//如果数据是正常的，则改为拟停
                    insurance.setStatus(Insurance.STATUS_STOPING);
                    insurances_updateStatus.add(insurance);
                }

            }
        }
        ConnUtil.closeAutoCommit(conn);

        DaoUpdateResult res1 = InsuranceDao.insert(conn,insurances_insert);//批量插入
        DaoUpdateResult res2 = InsuranceDao.update(conn,insurances_update);//批量修改
        DaoUpdateResult res3 = InsuranceDao.updateStatus(conn,insurances_updateStatus);//批量停保
        DaoUpdateResult res4 = InsuranceDao.delete(conn,insurances_delete);//删除停保

        if(res1.success && res2.success && res3.success && res4.success){
           ConnUtil.commit(conn);
            return JSONObject.toJSONString(res1);
        }else {
            ConnUtil.rollback(conn);
            return DaoResult.fail("数据库错误");
        }
    }

    //修改
    public static String update(Connection conn, List<Insurance> insurances) {
        DaoUpdateResult res = InsuranceDao.update(conn,insurances);//修改状态
        return JSONObject.toJSONString(res);
    }

    /**
     * 校对医保参保单
     * @param conn
     * @param data 校对的数据
     * @return
     */
    public static DaoUpdateResult checkMedicare(Connection conn, JSONArray data){
      /*
        * 校对流程
        * （1）获取导入的名册checks（参数传递）
        * （2）获取系统当前医疗，大病和工伤的名册
        * （3）分别校对，返回需要修改的数据
        * （4）修改数据库
        * */
        HashMap<String, String> checks = new HashMap<>();
        for(int i=0; i<data.size(); i++){//k:身份证号，v:个人代码
            JSONObject o = (JSONObject) data.get(i);
            checks.put(o.getString("cardId"),o.getString("code"));
        }
        List<Insurance> updates = new ArrayList<>();//校对后需要修改的数据

        QueryParameter p1 = new QueryParameter();//查询医疗保险参保单
        p1.addCondition("category","=",Insurance.CATEGORY3);
        List<ViewInsurance> medicare_exits = (List<ViewInsurance>) InsuranceDao.getList(conn,p1).rows;

        QueryParameter p2 = new QueryParameter();//查询大病保险参保单
        p2.addCondition("category","=",Insurance.CATEGORY4);
        List<ViewInsurance> disease_exits = (List<ViewInsurance>) InsuranceDao.getList(conn,p2).rows;

        QueryParameter p3 = new QueryParameter();//查询生育保险参保单
        p3.addCondition("category","=",Insurance.CATEGORY5);
        List<ViewInsurance> birth_exits = (List<ViewInsurance>) InsuranceDao.getList(conn,p3).rows;

        //校对医疗保险参保单
        if(medicare_exits.size()>0){
            updates.addAll(check(checks,medicare_exits));
        }
        //校对大病保险参保单
        if(medicare_exits.size()>0){
            updates.addAll(check(checks,disease_exits));
        }
        //校对生育保险参保单
        if(medicare_exits.size()>0){
            updates.addAll(check(checks,birth_exits));
        }
//        for(ViewInsurance insurance:medicare_exits){
//            byte status = insurance.getStatus();//该员工的医保状态
//
//            if(status == Insurance.STATUS_APPENDING){//新增状态
//                String code = checks.get(insurance.getCardId());
//                if(code != null){
//                    insurance.setCode(code);
//                    insurance.setStatus(Insurance.STATUS_NORMAL);//设置为在保
//                    updates.add(insurance);
//                }
//            }else if(status == Insurance.STATUS_STOPING){//拟停状态
//                String code = checks.get(insurance.getCardId());
//                if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
//                    insurance.setStatus(Insurance.STATUS_STOPED);//设置为停保
//                    updates.add(insurance);
//                }
//            }else if(status == Insurance.STATUS_NORMAL){//在保状态
//                String code = checks.get(insurance.getCardId());
//                if(code == null){//校对名单中不存在
//                    insurance.setStatus(Insurance.STATUS_ERROR);//设置为异常
//                    updates.add(insurance);
//                }
//            }
//        }
        //批量修改
        DaoUpdateResult result = InsuranceDao.update(conn,updates);
        return result;
    }

    /**
     * 校对社保参保单
     * @param conn
     * @param data 校对的数据
     * @param type 社保类型 0 养老  1 工伤  2失业
     * @return
     */
    public static DaoUpdateResult checkSocial(Connection conn, JSONArray data,byte type) {
        /*
        * 校对流程
        * （1）获取导入的名册data1（参数传递）
        * （2）获取系统当前的名册data2
        * （3）判断是校对养老，失业还是工伤
        * */
        HashMap<String, String> insurance_checks = new HashMap<>();
        for(int i=0; i<data.size(); i++){//k:身份证号，v:个人代码
            JSONObject o = (JSONObject) data.get(i);
            insurance_checks.put(o.getString("cardId"),o.getString("code"));
        }

        List<Insurance> insurance_updates = new ArrayList<>();//需要修改的数据
        switch (type){
            case 0://校对养老参保单
                QueryParameter p1 = new QueryParameter(); //获取数据库中所有的养老参保单
                p1.addCondition("category","=",Insurance.CATEGORY0);
                List<ViewInsurance> insurance_pensions = (List<ViewInsurance>) InsuranceDao.getList(conn,p1).rows;
                if(insurance_pensions.size()>0){
                    insurance_updates = check(insurance_checks,insurance_pensions);
                }
                 break;
            case 1://校对工伤参保单
                QueryParameter p2 = new QueryParameter(); //获取数据库中所有的失业参保单
                p2.addCondition("category","=",Insurance.CATEGORY1);
                List<ViewInsurance> insurance_injurys  = (List<ViewInsurance>) InsuranceDao.getList(conn,p2).rows;
                if(insurance_injurys.size()>0){
                    insurance_updates = check(insurance_checks,insurance_injurys);
                }
                break;
            case 2://校对工伤参保单
                QueryParameter p3 = new QueryParameter(); //获取数据库中所有的失业参保单
                p3.addCondition("category","=",Insurance.CATEGORY2);
                List<ViewInsurance> insurance_unemployments = (List<ViewInsurance>) InsuranceDao.getList(conn,p3).rows;
                if(insurance_unemployments.size()>0){
                    insurance_updates = check(insurance_checks,insurance_unemployments);
                }
                break;
        }
//        for(ViewInsurance insurance:data2){
//            switch (type){
//                case 0://校对养老参保单
//                    status = insurance.getStatus();
//                    if(status == Insurance.STATUS_APPENDING){//新增
//                        String code = insurance_checks.get(insurance.getCardId());
//                        if(code != null){
//                            insurance.setCode(code);
//                            insurance.setStatus(Insurance.STATUS_NORMAL);//设置为在保
//                            insurance_updates.add(insurance);
//                        }
//                    }else if(status == Insurance.STATUS_STOPING){//拟停
//                        String code = insurance_checks.get(insurance.getCardId());
//                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
//                            insurance.setStatus(Insurance.STATUS_STOPED);//设置为停保
//                            insurance_updates.add(insurance);
//                        }
//                    }else if(status == Insurance.STATUS_NORMAL){//在保
//                        String code = insurance_checks.get(insurance.getCardId());
//                        if(code == null){//校对名单中不存在
//                            insurance.setStatus(Insurance.STATUS_ERROR);//设置为异常
//                            insurance_updates.add(insurance);
//                        }
//                    }
//                    break;
//                case 1://校对失业参保单
//                    status = insurance.getStatus();
//                    if(status == Insurance.STATUS_APPENDING){//新增
//                        String code = insurance_checks.get(insurance.getCardId());
//                        if(code != null){
//                            insurance.setCode(code);
//                            insurance.setStatus(Insurance.STATUS_NORMAL);//设置为在保
//                            insurance_updates.add(insurance);
//                        }
//                    }else if(status == Insurance.STATUS_STOPING){//拟停
//                        String code = insurance_checks.get(insurance.getCardId());
//                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
//                            insurance.setStatus(Insurance.STATUS_STOPED);//设置为停保
//                            insurance_updates.add(insurance);
//                        }
//                    }else if(status == Insurance.STATUS_NORMAL){//在保
//                        String code = insurance_checks.get(insurance.getCardId());
//                        if(code == null){//校对名单中不存在
//                            insurance.setStatus(Insurance.STATUS_ERROR);//设置为异常
//                            insurance_updates.add(insurance);
//                        }
//                    }
//                    break;
//                case 2://校对工伤参保单
//                    status = insurance.getStatus();
//                    if(status == Insurance.STATUS_APPENDING){//新增
//                        String code = insurance_checks.get(insurance.getCardId());
//                        if(code != null){
//                            insurance.setCode(code);
//                            insurance.setStatus(Insurance.STATUS_NORMAL);//设置为在保
//                            insurance_updates.add(insurance);
//                        }
//                    }else if(status == Insurance.STATUS_STOPING){//拟停
//                        String code = insurance_checks.get(insurance.getCardId());
//                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
//                            insurance.setStatus(Insurance.STATUS_STOPED);//设置为停保
//                            insurance_updates.add(insurance);
//                        }
//                    }else if(status == Insurance.STATUS_NORMAL){//在保
//                        String code = insurance_checks.get(insurance.getCardId());
//                        if(code == null){//校对名单中不存在
//                            insurance.setStatus(Insurance.STATUS_ERROR);//设置为异常
//                            insurance_updates.add(insurance);
//                        }
//                    }
//                    break;
//            }
//        }

        //批量修改
        DaoUpdateResult result = InsuranceDao.update(conn,insurance_updates);
        return result;
    }

    /**
     * 校对公积金参保单
     * @param conn
     * @param data 校对的数据
     * @return
     */
    public static DaoUpdateResult checkFund(Connection conn, JSONArray data){
      /*
        * 校对流程
        * （1）获取导入的名册checks（参数传递）
        * （2）获取系统当前的公积金参保单
        * （3）校对
        * */
        HashMap<String, String> checks = new HashMap<>();
        for(int i=0; i<data.size(); i++){//k:身份证号，v:个人代码
            JSONObject o = (JSONObject) data.get(i);
            checks.put(o.getString("cardId"),o.getString("code"));
        }

        List<Insurance> updates = new ArrayList<>();
        //查询出公积金参保单
        QueryParameter param = new QueryParameter();
        param.addCondition("category","=",Insurance.CATEGORY6);
        List<ViewInsurance> fund_exits = (List<ViewInsurance>) InsuranceDao.getList(conn,param).rows;

        if(fund_exits.size()>0){
            updates=check(checks,fund_exits);
        }
//        List<Insurance> data3 = new ArrayList<>();//需要修改的数据
//        for(ViewInsurance insurance:data2){
//            byte status = insurance.getStatus();//该员工的公积金状态
//            if(status == Insurance.STATUS_APPENDING){//新增
//                String code = data1.get(insurance.getCardId());
//                if(code != null){
//                    insurance.setCode(code);
//                    insurance.setStatus(Insurance.STATUS_NORMAL);//设置为在保
//                    data3.add(insurance);
//                }
//            }else if(status == Insurance.STATUS_STOPING){//拟停
//                String code = data1.get(insurance.getCardId());
//                if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
//                    insurance.setStatus(Insurance.STATUS_STOPED);//设置为停保
//                    data3.add(insurance);
//                }
//            }else if(status == Insurance.STATUS_NORMAL){//在保
//                String code = data1.get(insurance.getCardId());
//                if(code == null){//校对名单中不存在
//                    insurance.setStatus(Insurance.STATUS_ERROR);//设置为异常
//                    data3.add(insurance);
//                }
//            }
//        }
        //批量修改
        DaoUpdateResult result = InsuranceDao.update(conn,updates);
        return result;
    }


    /**
     * 校对参保单
     * @param checks 需要校对的名单
     * @param exists 数据库已存在且需要校对的名单
     * @return updates 返回校对后需要修改的数据
     */
    private static List<Insurance> check(HashMap<String, String> checks,List<ViewInsurance> exists){
        /**
         * 校对流程
         * 1、遍历数据库中已经存在的数据
         * 2、判断数据的状态（新增、在保、拟停、停保）
         *   2.1 校对新增：如果校对数据checks中存在，则设置为在保否则新增异常
         *   2.2 校对拟停：如果校对数据checks中存在，则设置为拟停异常否则停保
         *   2.3 校对在保：如果校对数据checks中存在，则设置为正常否则在保异常
         *   2.4 校对停保：如果校对数据checks中存在，则设置为停保异常否则停保
         * 3、返回校对后需要修改的数据
         */
        List<Insurance> updates=new ArrayList<>();
        for(ViewInsurance insurance:exists){
            byte status = insurance.getStatus();
            if(status == Insurance.STATUS_APPENDING){//新增
                String code = checks.get(insurance.getCardId());
                if(code != null){//如果存在则设置为在保
                    insurance.setCode(code);
                    insurance.setStatus(Insurance.STATUS_NORMAL);
                    updates.add(insurance);
                }else {//不存在则设置为新增异常
                    insurance.setCode(code);
                    insurance.setStatus(Insurance.STATUS_APPENDING_ERROR);
                    updates.add(insurance);
                }
            }else if(status == Insurance.STATUS_STOPING){//拟停
                String code = checks.get(insurance.getCardId());
                if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                    insurance.setStatus(Insurance.STATUS_STOPED);//设置为停保
                    updates.add(insurance);
                }else {//存在则是拟停异常
                    insurance.setStatus(Insurance.STATUS_STOPING_ERROR);
                    updates.add(insurance);
                }
            }else if(status == Insurance.STATUS_NORMAL){//在保
                String code = checks.get(insurance.getCardId());
                if(code == null){//校对名单中不存在则是在保异常
                    insurance.setStatus(Insurance.STATUS_NORMAL_ERROR);//设置为异常
                    updates.add(insurance);
                }
            }else if(status == Insurance.STATUS_STOPED){
                String code = checks.get(insurance.getCardId());
                if(code != null){
                    insurance.setStatus(Insurance.STATUS_STOPED_ERROR);//设置为停保异常
                    updates.add(insurance);
                }
            }
        }
        return updates;
    }

}
