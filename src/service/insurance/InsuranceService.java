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
    public static String insert(Connection conn, List<Insurance> insurances) {
       DaoUpdateResult res = InsuranceDao.insert(conn,insurances);
       return JSONObject.toJSONString(res);
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

        //遍历数据库中的参保信息，如果不存在于传递过来的参保则停保
        for(Insurance insurance:exists){
            Object []values = {insurance.getEid(),insurance.getCategory()};
            if(CollectionUtil.filter(list,keys,values).size() == 0){
                if(insurance.getStatus()==Insurance.STATUS_APPENDING){//如果该数据是新增的，则删除，否则修改为拟停状态
                    insurances_delete.add(insurance);
                }else {
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
        }else {
            return DaoResult.fail("数据库错误");
        }
        return JSONObject.toJSONString(res1);
    }

    //修改
    public static String update(Connection conn, List<Insurance> insurances) {
        DaoUpdateResult res = InsuranceDao.update(conn,insurances);
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
        * （1）获取导入的名册data1（参数传递）
        * （2）获取系统当前的名册data2
        * （3）判断data2中“新增”状态的是否也存在于data1中，若存在则将其状态置为“在保”
        * （4）判断data2中“拟停”状态的是否也存在于data1中，若不存在则将其状态置为“停保”
        * （5）判断data2中“在保”状态的是否也存在于data1中，若不存在则将其状态置为“异常”
        * */
        HashMap<String, String> data1 = new HashMap<>();
        for(int i=0; i<data.size(); i++){//k:身份证号，v:个人代码
            JSONObject o = (JSONObject) data.get(i);
            data1.put(o.getString("cardId"),o.getString("code"));
        }

        QueryParameter param = new QueryParameter();
        List<ViewInsurance> data2 = (List<ViewInsurance>) InsuranceDao.getList(conn,param).rows;

        List<Insurance> data3 = new ArrayList<>();//需要修改的数据
        for(ViewInsurance insurance:data2){
            byte status = insurance.getStatus();//该员工的医保状态
            if(status == Insurance.STATUS_APPENDING){//新增
                String code = data1.get(insurance.getCardId());
                if(code != null){
                    insurance.setCode(code);
                    insurance.setStatus(Insurance.STATUS_NORMAL);//设置为在保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_STOPING){//拟停
                String code = data1.get(insurance.getCardId());
                if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                    insurance.setStatus(Insurance.STATUS_STOPED);//设置为停保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_NORMAL){//在保
                String code = data1.get(insurance.getCardId());
                if(code == null){//校对名单中不存在
                    insurance.setStatus(Insurance.STATUS_ERROR);//设置为异常
                    data3.add(insurance);
                }
            }
        }
        //批量修改
        DaoUpdateResult result = InsuranceDao.update(conn,data3);
        return result;
    }

    /**
     * 校对社保参保单
     * @param conn
     * @param data 校对的数据
     * @param type 社保类型 0 养老 1 失业 2 工伤
     * @return
     */
    public static DaoUpdateResult checkSocial(Connection conn, JSONArray data,byte type) {
        /*
        * 校对流程
        * （1）获取导入的名册data1（参数传递）
        * （2）获取系统当前的名册data2
        * （3）判断是校对养老，失业还是工伤
        * （4）判断data2中“新增”状态的是否也存在于data1中，若存在则将其状态置为“在保”
        * （5）判断data2中“拟停”状态的是否也存在于data1中，若不存在则将其状态置为“停保”
        * （6）判断data2中“在保”状态的是否也存在于data1中，若不存在则将其状态置为“异常”
        * */
        HashMap<String, String> data1 = new HashMap<>();
        for(int i=0; i<data.size(); i++){//k:身份证号，v:个人代码
            JSONObject o = (JSONObject) data.get(i);
            data1.put(o.getString("cardId"),o.getString("code"));
        }

        QueryParameter param = new QueryParameter();
        List<ViewInsurance> data2 = (List<ViewInsurance>) InsuranceDao.getList(conn,param).rows;

        List<Insurance> data3 = new ArrayList<>();//需要修改的数据
        byte status;//医保状态
        for(ViewInsurance insurance:data2){
            switch (type){
                case 0://校对养老参保单
                    status = insurance.getStatus();
                    if(status == Insurance.STATUS_APPENDING){//新增
                        String code = data1.get(insurance.getCardId());
                        if(code != null){
                            insurance.setCode(code);
                            insurance.setStatus(Insurance.STATUS_NORMAL);//设置为在保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_STOPING){//拟停
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                            insurance.setStatus(Insurance.STATUS_STOPED);//设置为停保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_NORMAL){//在保
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//校对名单中不存在
                            insurance.setStatus(Insurance.STATUS_ERROR);//设置为异常
                            data3.add(insurance);
                        }
                    }
                    break;
                case 1://校对失业参保单
                    status = insurance.getStatus();
                    if(status == Insurance.STATUS_APPENDING){//新增
                        String code = data1.get(insurance.getCardId());
                        if(code != null){
                            insurance.setCode(code);
                            insurance.setStatus(Insurance.STATUS_NORMAL);//设置为在保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_STOPING){//拟停
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                            insurance.setStatus(Insurance.STATUS_STOPED);//设置为停保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_NORMAL){//在保
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//校对名单中不存在
                            insurance.setStatus(Insurance.STATUS_ERROR);//设置为异常
                            data3.add(insurance);
                        }
                    }
                    break;
                case 2://校对工伤参保单
                    status = insurance.getStatus();
                    if(status == Insurance.STATUS_APPENDING){//新增
                        String code = data1.get(insurance.getCardId());
                        if(code != null){
                            insurance.setCode(code);
                            insurance.setStatus(Insurance.STATUS_NORMAL);//设置为在保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_STOPING){//拟停
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                            insurance.setStatus(Insurance.STATUS_STOPED);//设置为停保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_NORMAL){//在保
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//校对名单中不存在
                            insurance.setStatus(Insurance.STATUS_ERROR);//设置为异常
                            data3.add(insurance);
                        }
                    }
                    break;
            }
        }
        //批量修改
        DaoUpdateResult result = InsuranceDao.update(conn,data3);
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
        * （1）获取导入的名册data1（参数传递）
        * （2）获取系统当前的名册data2
        * （3）判断data2中“新增”状态的是否也存在于data1中，若存在则将其状态置为“在保”
        * （4）判断data2中“拟停”状态的是否也存在于data1中，若不存在则将其状态置为“停保”
        * （5）判断data2中“在保”状态的是否也存在于data1中，若不存在则将其状态置为“异常”
        * */
        HashMap<String, String> data1 = new HashMap<>();
        for(int i=0; i<data.size(); i++){//k:身份证号，v:个人代码
            JSONObject o = (JSONObject) data.get(i);
            data1.put(o.getString("cardId"),o.getString("code"));
        }

        QueryParameter param = new QueryParameter();
        List<ViewInsurance> data2 = (List<ViewInsurance>) InsuranceDao.getList(conn,param).rows;

        List<Insurance> data3 = new ArrayList<>();//需要修改的数据
        for(ViewInsurance insurance:data2){
            byte status = insurance.getStatus();//该员工的公积金状态
            if(status == Insurance.STATUS_APPENDING){//新增
                String code = data1.get(insurance.getCardId());
                if(code != null){
                    insurance.setCode(code);
                    insurance.setStatus(Insurance.STATUS_NORMAL);//设置为在保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_STOPING){//拟停
                String code = data1.get(insurance.getCardId());
                if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                    insurance.setStatus(Insurance.STATUS_STOPED);//设置为停保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_NORMAL){//在保
                String code = data1.get(insurance.getCardId());
                if(code == null){//校对名单中不存在
                    insurance.setStatus(Insurance.STATUS_ERROR);//设置为异常
                    data3.add(insurance);
                }
            }
        }
        //批量修改
        DaoUpdateResult result = InsuranceDao.update(conn,data3);
        return result;
    }

}
