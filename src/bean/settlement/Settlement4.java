package bean.settlement;

import bean.contract.ViewContractCooperation;

import java.util.Date;
import java.util.List;

//特殊结算单
public class Settlement4 {
    private long id;
    private long did;//派遣客户id
    private long cid;//合作客户id
    private String ccid;//合同id
    private Date month;
    private float amount;
    private byte type;//类型 1 派遣  2 外包  3代发工资 5小时工
    private byte status;//状态
    private float manage;//管理费
    private float tax;
    private float paid;//实发

    public Settlement4() {
    }

    public Settlement4(long id, long did, long cid, String ccid, Date month, float amount, byte type, byte status, float manage, float tax, float paid) {
        this.id = id;
        this.did = did;
        this.cid = cid;
        this.ccid = ccid;
        this.month = month;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.manage = manage;
        this.tax = tax;
        this.paid = paid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public String getCcid() {
        return ccid;
    }

    public void setCcid(String ccid) {
        this.ccid = ccid;
    }

    public float getManage() {
        return manage;
    }

    public void setManage(float manage) {
        this.manage = manage;
    }

    @Override
    public String toString() {
        return "Settlement4{" +
                "id=" + id +
                ", did=" + did +
                ", cid=" + cid +
                ", month=" + month +
                ", amount=" + amount +
                ", tax=" + tax +
                '}';
    }

    /**
     * 计算普通结算单
     * @param contract  合同
     * @param details   所有员工明细
     */
    public void calc(ViewContractCooperation contract, List<ViewDetail4> details) {
        //初始化，都要清零，防止客户多点了计算按钮出现累加的情况
        this.tax = 0;//税费和管理费
        this.amount=0;//总额
        this.paid=0;
        //计算社保、医保、公积金和核收补减总额
        for (ViewDetail4 d : details) {
            //应发总额+=明细中的工资总额
            this.amount+=d.getAmount();
        }
        //计算管理费和税费
        int type = contract.getStype();//合同服务项目中的类型
        int category = contract.getCategory();//合同服务项目中的结算方式
        int invoice = contract.getInvoice();//合同基础信息中的发票类型
        float per = contract.getPer()/100;//税费比例（选择增值税专用发票（全额）需要用到）
        float value = contract.getValue();//结算值，根据结算方式的不同，代表的意义不同
        switch (type){
            case 0://劳务派遣或者外包
                if(category==0){//按人数收取的结算方式
                    if(invoice==0){//增值税专用发票（全额）
                        this.tax=amount*per;
                        this.manage=0;
                    }
                }else if(category==1){//按比例收取的结算方式
                    //此时服务项目中value为比例所以需要转成小数
                    this.manage=amount*value/100;
                    this.tax = 0;
                }else {//按外包整体核算方式，暂时没有

                }
                break;
            case 1://人事代理
                this.manage=0;
                this.tax=0;
                break;
        }
        //计算总额
        this.paid=amount+tax+manage;
    }


    /**
     * 单独计算每个员工的管理费和个税,导出报表需要用到
     * @param contract 合同
     * @param detail 一个员工明细
     */
    public void calcManageAndTax(ViewContractCooperation contract, ViewDetail4 detail) {
        int type = contract.getStype();//合同服务项目中的类型
        int category = contract.getCategory();//合同服务项目中的结算方式
        int invoice = contract.getInvoice();//合同基础信息中的发票类型
        float per = contract.getPer()/100;//税费比例（选择增值税专用发票（全额）需要用到）
        float val = contract.getValue();//结算值，根据结算方式的不同，代表的意义不同
        this.manage=0;
        this.tax=0;
        this.paid=0;
        this.amount=detail.getAmount();//将员工的总额赋值
        //计算管理费
        switch (type){
            case 0://劳务派遣或者外包
                if(category==0){//按人数收取的结算方式
                    if(invoice==0){//增值税专用发票（全额）
                        this.tax=detail.getAmount()*per;
                        this.manage=0;
                    }
                }else if(category==1){//按比例收取的结算方式
                    //此时服务项目中value为比例所以需要转成小数
                    this.manage=detail.getAmount()*val/100;
                    this.tax = 0;
                }else {//按外包整体核算方式，暂时没有

                }
                break;
            case 1://人事代理
                this.manage=0;
                this.tax=0;
                break;
        }
        //计算总额
        this.paid=amount+tax+manage;
    }

}
