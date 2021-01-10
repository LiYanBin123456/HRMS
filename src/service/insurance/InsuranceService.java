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

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class InsuranceService {
    //批量插入
    public static DaoUpdateResult insertBatch(Connection conn, List<Insurance> insurances) {
       return InsuranceDao.insertBatch(conn,insurances);
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

        List<ViewInsurance> data3 = new ArrayList<>();//需要修改的数据
        for(ViewInsurance insurance:data2){
            byte status = insurance.getStatus1();//该员工的医保状态
            if(status == Insurance.STATUS_APPENDING){//新增
                String code = data1.get(insurance.getCardId());
                if(code != null){
                    insurance.setCode1(code);
                    insurance.setStatus1(Insurance.STATUS_NORMAL);//设置为在保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_STOPING){//拟停
                String code = data1.get(insurance.getCardId());
                if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                    insurance.setStatus1(Insurance.STATUS_STOPED);//设置为停保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_NORMAL){//在保
                String code = data1.get(insurance.getCardId());
                if(code == null){//校对名单中不存在
                    insurance.setStatus1(Insurance.STATUS_ERROR);//设置为异常
                    data3.add(insurance);
                }
            }
        }
        //批量修改
        DaoUpdateResult result = InsuranceDao.updateBatch(conn,data3);
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

        List<ViewInsurance> data3 = new ArrayList<>();//需要修改的数据
        byte status;//医保状态
        for(ViewInsurance insurance:data2){
            switch (type){
                case 0://校对养老参保单
                    status = insurance.getStatus3();
                    if(status == Insurance.STATUS_APPENDING){//新增
                        String code = data1.get(insurance.getCardId());
                        if(code != null){
                            insurance.setCode3(code);
                            insurance.setStatus3(Insurance.STATUS_NORMAL);//设置为在保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_STOPING){//拟停
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                            insurance.setStatus3(Insurance.STATUS_STOPED);//设置为停保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_NORMAL){//在保
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//校对名单中不存在
                            insurance.setStatus3(Insurance.STATUS_ERROR);//设置为异常
                            data3.add(insurance);
                        }
                    }
                    break;
                case 1://校对失业参保单
                    status = insurance.getStatus4();
                    if(status == Insurance.STATUS_APPENDING){//新增
                        String code = data1.get(insurance.getCardId());
                        if(code != null){
                            insurance.setCode3(code);
                            insurance.setStatus4(Insurance.STATUS_NORMAL);//设置为在保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_STOPING){//拟停
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                            insurance.setStatus4(Insurance.STATUS_STOPED);//设置为停保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_NORMAL){//在保
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//校对名单中不存在
                            insurance.setStatus3(Insurance.STATUS_ERROR);//设置为异常
                            data3.add(insurance);
                        }
                    }
                    break;
                case 2://校对工伤参保单
                    status = insurance.getStatus5();
                    if(status == Insurance.STATUS_APPENDING){//新增
                        String code = data1.get(insurance.getCardId());
                        if(code != null){
                            insurance.setCode3(code);
                            insurance.setStatus5(Insurance.STATUS_NORMAL);//设置为在保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_STOPING){//拟停
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                            insurance.setStatus5(Insurance.STATUS_STOPED);//设置为停保
                            data3.add(insurance);
                        }
                    }else if(status == Insurance.STATUS_NORMAL){//在保
                        String code = data1.get(insurance.getCardId());
                        if(code == null){//校对名单中不存在
                            insurance.setStatus3(Insurance.STATUS_ERROR);//设置为异常
                            data3.add(insurance);
                        }
                    }
                    break;
            }
        }
        //批量修改
        DaoUpdateResult result = InsuranceDao.updateBatch(conn,data3);
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

        List<ViewInsurance> data3 = new ArrayList<>();//需要修改的数据
        for(ViewInsurance insurance:data2){
            byte status = insurance.getStatus2();//该员工的公积金状态
            if(status == Insurance.STATUS_APPENDING){//新增
                String code = data1.get(insurance.getCardId());
                if(code != null){
                    insurance.setCode1(code);
                    insurance.setStatus2(Insurance.STATUS_NORMAL);//设置为在保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_STOPING){//拟停
                String code = data1.get(insurance.getCardId());
                if(code == null){//如果code不存在，也就是说明校对数据中不存在该员工
                    insurance.setStatus2(Insurance.STATUS_STOPED);//设置为停保
                    data3.add(insurance);
                }
            }else if(status == Insurance.STATUS_NORMAL){//在保
                String code = data1.get(insurance.getCardId());
                if(code == null){//校对名单中不存在
                    insurance.setStatus2(Insurance.STATUS_ERROR);//设置为异常
                    data3.add(insurance);
                }
            }
        }
        //批量修改
        DaoUpdateResult result = InsuranceDao.updateBatch(conn,data3);
        return result;
    }

}
