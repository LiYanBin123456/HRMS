package bean.settlement;

import bean.contract.ViewContractCooperation;

import java.util.Date;
import java.util.List;

//结算单
public class Settlement1 extends Settlement {
    public static final byte TYPE_1 = 1;//派遣
    public static final byte TYPE_2 = 2;//外包
    public static final byte TYPE_3 = 3;//代发工资
    public static final byte TYPE_4 = 4;//代缴社保

    private byte type;
    private float salary;//应发工资总额
    private float social;//单位社保总额
    private float medicare;//单位医保总额
    private float fund;//单位公积金总额
    private float manage;//管理费
    private float tax;//税费
    private float extra;//单位补收核减
    private float free;//国家扣除项
    private float summary;//总额
    private String comments;//备注

    public Settlement1() {
    }

    public Settlement1(long id, long did, long cid, Date month, byte status, byte source, float salary, float social, float medicare, float fund, float manage, float tax, float summary) {
        super(id, did, cid, month, status, source);
        this.salary = salary;
        this.social = social;
        this.medicare = medicare;
        this.fund = fund;
        this.manage = manage;
        this.tax = tax;
        this.summary = summary;
    }

    public Settlement1(long id, long did, long cid, String ccid, Date month, byte status, byte source, byte type, float salary, float social, float medicare, float fund, float manage, float tax, float summary) {
        super(id, did, cid, ccid, month, status, source);
        this.type = type;
        this.salary = salary;
        this.social = social;
        this.medicare = medicare;
        this.fund = fund;
        this.manage = manage;
        this.tax = tax;
        this.summary = summary;
    }

    public Settlement1(long id, long did, long cid, String ccid, Date month, byte status, byte source, byte type, float salary, float social, float medicare, float fund, float manage, float tax, float extra, float free, float summary, String comments) {
        super(id, did, cid, ccid, month, status, source);
        this.type = type;
        this.salary = salary;
        this.social = social;
        this.medicare = medicare;
        this.fund = fund;
        this.manage = manage;
        this.tax = tax;
        this.extra = extra;
        this.free = free;
        this.summary = summary;
        this.comments = comments;
    }

    public Settlement1(byte type, float salary, float social, float medicare, float fund, float manage, float tax, float extra, float free, float summary, String comments) {
        this.type = type;
        this.salary = salary;
        this.social = social;
        this.medicare = medicare;
        this.fund = fund;
        this.manage = manage;
        this.tax = tax;
        this.extra = extra;
        this.free = free;
        this.summary = summary;
        this.comments = comments;
    }

    public float getFree() {
        return free;
    }

    public void setFree(float free) {
        this.free = free;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public float getSocial() {
        return social;
    }

    public void setSocial(float social) {
        this.social = social;
    }

    public float getMedicare() {
        return medicare;
    }

    public void setMedicare(float medicare) {
        this.medicare = medicare;
    }

    public float getFund() {
        return fund;
    }

    public void setFund(float fund) {
        this.fund = fund;
    }

    public float getManage() {
        return manage;
    }

    public void setManage(float manage) {
        this.manage = manage;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getSummary() {
        return summary;
    }

    public void setSummary(float summary) {
        this.summary = summary;
    }

    public float getExtra() {
        return extra;
    }

    public void setExtra(float extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "Settlement1{" +
                "salary=" + salary +
                ", social=" + social +
                ", fund=" + fund +
                ", manage=" + manage +
                ", tax=" + tax +
                ", summary=" + summary +
                '}';
    }

    /**
     * 计算普通结算单
     * @param contract  合同
     * @param details   员工明细
     */
    public void calc(ViewContractCooperation contract, List<ViewDetail1> details) {
        this.salary=0;//应发总额
        this.social=0;//单位社保
        this.medicare=0;//单位医保
        this.fund = 0;//单位公积金
        this.summary=0;//实发总额
        this.manage =0;//管理费
        this.extra = 0;//额外
        this.free = 0;//国家减免项
        this.tax = 0;//税费

        //计算社保、医保、公积金和核收补减总额
        for (ViewDetail1 detail : details) {
            //应发总额+=明细中的应发总额
            this.salary += detail.getPayable();
            this.social += detail.getSocialDepartment();//单位社保总额
            this.medicare += detail.getMedicaleDepartment();//单位医保总额
            this.fund += detail.getFund2();//单位公积金总额
            if (this.type == Settlement1.TYPE_4) {//代缴社保还需加上个人部分
                this.social += detail.getSocialPerson();
                this.medicare += detail.getMedicalePerson();
                this.fund += detail.getFund1();
            }
            //补收核减
            this.extra += detail.getExtra2();
        }

        //计算管理费和税费
        int type = contract.getStype();//合同服务项目中的类型
        int category = contract.getCategory();//合同服务项目中的结算方式
        int invoice = contract.getInvoice();//合同基础信息中的发票类型
        float per = contract.getPer()/100;//税费比例（选择增值税专用发票（全额）需要用到）
        float value = contract.getValue();//结算值，根据结算方式的不同，代表的意义不同
        float num = details.size();//总人数
        switch (type){
            case 0://劳务派遣
                if(category==0){//按人数收取的结算方式
                    //管理费总额=人数*管理费,如果不需要计算社保（即补发），不需要重复计算管理费
                    this.manage = this.isNeedCalculateSocial()?num*value:0;
                    if(invoice==0){//增值税专用发票（全额）
                        this.tax=(salary+social+medicare+fund+manage)*per;//税费 = （应发总额+单位五险一金+管理费）*税率（基准6.72，但可以浮动）
                    }
                }else if(category==1){//按比例收取的结算方式
                    //此时服务项目中value为比例所以需要转成小数
                    this.manage = (salary+social+medicare+fund)*(value/100);//管理费 = （应发总额+单位五险一金）*比例（从服务项目中的比例）
                    this.tax = 0;
                }else {//按外包整体核算方式

                }
                break;
            case 1://人事代理
                this.tax=0;
                this.manage = num*value;//管理费=人数*单价
                break;
        }

        //计算总额
        switch (this.getType()){
            case Settlement1.TYPE_1://派遣结算单
            case Settlement1.TYPE_2://外包结算单
                this.summary=salary+social+medicare+fund+manage+tax+extra-free;//总额
                break;
            case Settlement1.TYPE_3://代发工资
                this.summary=salary+manage+tax+extra-free;
                break;
            case Settlement1.TYPE_4://代缴社保
                this.summary=social+medicare+fund+manage+extra-free;
                this.salary=0;
                this.tax = 0;
                break;

        }
    }

    /**
     * 计算管理费和个税
     * @param contract 合同
     * @param detail 员工明细
     */
    public void calcManageAndTax(ViewContractCooperation contract, ViewDetail1 detail) {
        int type = contract.getStype();//合同服务项目中的类型
        int category = contract.getCategory();//合同服务项目中的结算方式
        int invoice = contract.getInvoice();//合同基础信息中的发票类型
        float per = contract.getPer()/100;//税费比例（选择增值税专用发票（全额）需要用到）
        float val = contract.getValue();//结算值，根据结算方式的不同，代表的意义不同
        manage=0;
        tax=0;
        //计算管理费
        switch (type){
            case 0://劳务派遣
                if(category==0){//按人数收取的结算方式
                    manage=val;//管理费=管理费
                    if(invoice==0){//增值税专用发票（全额）
                        //税费=（基本工资+自定义工资项+单位五险一金+管理费-国家减免+单位核收补减）
                        tax +=  (detail.getTaxDueOfDepartment(detail.getPayable())+manage)*per;
                    }
                }else if(category==1){//按比例收取的结算方式
                    //此时服务项目中value为比例所以需要转成小数
                    //管理费 = （基本工资+自定义工资项+单位五险一金-国家减免+单位核收补减）*比例（从服务项目中的比例）
                    manage +=  (detail.getTaxDueOfDepartment(detail.getPayable()))*(val/100);
                    tax=0;
                }else {//按外包整体核算方式

                }
                break;
            case 1://人事代理
                tax=0;
                manage = val;//管理费=人数*单价
                break;
        }
    }
}
