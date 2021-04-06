package utills.excel;

//Excel表结构
public class SchemeDefined {
    public static Scheme SCHEME_DEDUCT_SPOUSE;//个税专项扣除-配偶信息
    public static Scheme SCHEME_DEDUCT_CHILDREN;//个税专项扣除-子女教育支出
    public static Scheme SCHEME_DEDUCT_EDUCATION;//个税专项扣除-继续教育
    public static Scheme SCHEME_DEDUCT_LOAN;//个税专项扣除-房贷
    public static Scheme SCHEME_DEDUCT_RENT;//个税专项扣除-房租
    public static Scheme SCHEME_DEDUCT_ELDER;//个税专项扣除-赡养老人
    public static Scheme SCHEME_DEDUCT_TOTAL;//个税专项扣除-累计
    public static Scheme SCHEME_SOCIAL;//社保核对
    public static Scheme SCHEME_MEDICAL;//医保核对
    public static Scheme SCHEME_FUND;//公积金核对
    public static Scheme SCHEME_DETAIL_EXPORT;//导出员工模板
    public static Scheme SCHEME_DETAIL0_1;//特殊结算单汇款表
    public static Scheme SCHEME_DETAIL0_2;//特殊结算单明细表
    public static Scheme SCHEME_DETAIL2_PAID;//小时工汇款表
    public static Scheme SCHEME_DETAIL2_DETAIL;//小时工明细表
    public static Scheme SCHEME_TAX1;//个税申报名单表
    public static Scheme SCHEME_TAX2;//个税申报表
    public static Scheme SCHEME_exportSocial1;//员工新增社保单
    public static Scheme SCHEME_exportSocial2;//员工停保社保单
    public static Scheme SCHEME_exportMedicare1;//员工续保医保单
    public static Scheme SCHEME_exportMedicare2;//员工停保医保单
    public static Scheme SCHEME_BANK_CMCC1;//招行转本行
    public static Scheme SCHEME_BANK_CMCC2;//招行转外行
    public static Scheme SCHEME_BANK_AG1;//农行转本行
    public static Scheme SCHEME_BANK_AG2;//农行转外行
    public static Scheme SCHEME_BANK_SPDB;//浦发
    public static Scheme SCHEME_BANK_BOCOM;//交通

    static {
        //个税专项扣除-配偶信息
        SCHEME_DEDUCT_SPOUSE = new Scheme();
        SCHEME_DEDUCT_SPOUSE.addField(new Field(1,"name",Field.STRING,false));
        SCHEME_DEDUCT_SPOUSE.addField(new Field(3,"cardId",Field.STRING,false));

        //个税专项扣除-子女教育支出
        SCHEME_DEDUCT_CHILDREN = new Scheme();
        SCHEME_DEDUCT_CHILDREN.addField(new Field(1,"name",Field.STRING,false));
        SCHEME_DEDUCT_CHILDREN.addField(new Field(3,"cardId",Field.STRING,false));
        SCHEME_DEDUCT_CHILDREN.addField(new Field(17,"per",Field.STRING,false));

        //个税专项扣除-继续教育
        SCHEME_DEDUCT_EDUCATION = SCHEME_DEDUCT_SPOUSE;

        //个税专项扣除-房贷
        SCHEME_DEDUCT_LOAN = SCHEME_DEDUCT_SPOUSE;

        //个税专项扣除-房租
        SCHEME_DEDUCT_RENT = SCHEME_DEDUCT_SPOUSE;

        //个税专项扣除-赡养老人
        SCHEME_DEDUCT_ELDER = new Scheme();
        SCHEME_DEDUCT_ELDER.addField(new Field(1,"name",Field.STRING,false));
        SCHEME_DEDUCT_ELDER.addField(new Field(3,"cardId",Field.STRING,false));
        SCHEME_DEDUCT_ELDER.addField(new Field(6,"per",Field.FLOAT,false));

        //个税专项扣除-累计
        SCHEME_DEDUCT_TOTAL = new Scheme();
        SCHEME_DEDUCT_TOTAL.addField(new Field(1,"name","姓名",Field.STRING,100));
        SCHEME_DEDUCT_TOTAL.addField(new Field(3,"cardId","身份证号",Field.STRING,100));
        SCHEME_DEDUCT_TOTAL.addField(new Field(18,"income","累计收入",Field.FLOAT,100));
        SCHEME_DEDUCT_TOTAL.addField(new Field(20,"free","累计减免费用",Field.FLOAT,100));
        SCHEME_DEDUCT_TOTAL.addField(new Field(22,"deduct1","累计子女教育扣除",Field.FLOAT,100));
        SCHEME_DEDUCT_TOTAL.addField(new Field(23,"deduct3","累计继续教育扣除额",Field.FLOAT,100));
        SCHEME_DEDUCT_TOTAL.addField(new Field(24,"deduct5","累计住房贷款利息",Field.FLOAT,100));
        SCHEME_DEDUCT_TOTAL.addField(new Field(25,"deduct6","累计住房租金",Field.FLOAT,100));
        SCHEME_DEDUCT_TOTAL.addField(new Field(26,"deduct2","累计赡养老人",Field.FLOAT,100));
        SCHEME_DEDUCT_TOTAL.addField(new Field(35,"prepaid","累计已预缴税额",Field.FLOAT,100));

        //社保核对
        SCHEME_SOCIAL = new Scheme();
        SCHEME_SOCIAL.addField(new Field(0,"code",Field.STRING,false));
        SCHEME_SOCIAL.addField(new Field(2,"cardId",Field.STRING,false));

        //医保核对
        SCHEME_MEDICAL = new Scheme();
        SCHEME_MEDICAL.addField(new Field(2,"code",Field.STRING,false));
        SCHEME_MEDICAL.addField(new Field(4,"cardId",Field.STRING,false));

        //公积金核对
        SCHEME_FUND = new Scheme();
        SCHEME_FUND.addField(new Field(1,"code",Field.STRING,false));
        SCHEME_FUND.addField(new Field(3,"cardId",Field.STRING,false));

        //导出员工模板明细
        SCHEME_DETAIL_EXPORT = new Scheme();
        SCHEME_DETAIL_EXPORT.addField(new Field(0, "name", "员工姓名*", Field.STRING, 100));
        SCHEME_DETAIL_EXPORT.addField(new Field(1, "cardId", "身份证号码*", Field.STRING, 300));

        //小时工汇款
        SCHEME_DETAIL2_PAID = new Scheme();
        SCHEME_DETAIL2_PAID.addField(new Field(0, "name", "员工姓名", Field.STRING, 100));
        SCHEME_DETAIL2_PAID.addField(new Field(1, "cardId", "身份证号码", Field.STRING, 300));
        SCHEME_DETAIL2_PAID.addField(new Field(2, "hours", "工时", Field.INT, 100));
        SCHEME_DETAIL2_PAID.addField(new Field(3, "price", "公司单价", Field.FLOAT, 100));
        SCHEME_DETAIL2_PAID.addField(new Field(4, "food", "餐费", Field.FLOAT, 100));
        SCHEME_DETAIL2_PAID.addField(new Field(5, "traffic", "交通费", Field.FLOAT, 100));
        SCHEME_DETAIL2_PAID.addField(new Field(6, "accommodation", "住宿费", Field.FLOAT, 100));
        SCHEME_DETAIL2_PAID.addField(new Field(7, "utilities", "水电费", Field.FLOAT, 100));
        SCHEME_DETAIL2_PAID.addField(new Field(8, "insurance", "保险费", Field.FLOAT, 100));
        SCHEME_DETAIL2_PAID.addField(new Field(9, "other1", "其他1", Field.FLOAT, 100));
        SCHEME_DETAIL2_PAID.addField(new Field(10, "other2", "其他2", Field.FLOAT, 100));
        SCHEME_DETAIL2_PAID.addField(new Field(11, "sum", "汇款总额", Field.FLOAT, 100));

        //小时工明细表
        SCHEME_DETAIL2_DETAIL = new Scheme();
        SCHEME_DETAIL2_DETAIL.addField(new Field(0, "name", "员工姓名", Field.STRING, 100));
        SCHEME_DETAIL2_DETAIL.addField(new Field(1, "cardId", "身份证号码", Field.STRING, 300));
        SCHEME_DETAIL2_DETAIL.addField(new Field(2, "hours", "工时", Field.INT, 100));
        SCHEME_DETAIL2_DETAIL.addField(new Field(3, "price", "公司单价", Field.FLOAT, 100));
        SCHEME_DETAIL2_DETAIL.addField(new Field(4, "food", "餐费", Field.FLOAT, 100));
        SCHEME_DETAIL2_DETAIL.addField(new Field(5, "traffic", "交通费", Field.FLOAT, 100));
        SCHEME_DETAIL2_DETAIL.addField(new Field(6, "accommodation", "住宿费", Field.FLOAT, 100));
        SCHEME_DETAIL2_DETAIL.addField(new Field(7, "utilities", "水电费", Field.FLOAT, 100));
        SCHEME_DETAIL2_DETAIL.addField(new Field(8, "insurance", "保险费", Field.FLOAT, 100));
        SCHEME_DETAIL2_DETAIL.addField(new Field(9, "other1", "其他1", Field.FLOAT, 100));
        SCHEME_DETAIL2_DETAIL.addField(new Field(10, "other2", "其他2", Field.FLOAT, 100));
        SCHEME_DETAIL2_DETAIL.addField(new Field(11, "payable", "实付", Field.FLOAT, 100));
        SCHEME_DETAIL2_DETAIL.addField(new Field(12, "paid", "应付", Field.FLOAT, 100));

        //特殊结算单回款表
        SCHEME_DETAIL0_1 = new Scheme();
        SCHEME_DETAIL0_1.addField(new Field(0, "name", "员工姓名", Field.STRING, 100));
        SCHEME_DETAIL0_1.addField(new Field(1, "cardId", "身份证号码", Field.STRING, 300));
        SCHEME_DETAIL0_1.addField(new Field(2, "amount", "金额", Field.INT, 100));
        SCHEME_DETAIL0_1.addField(new Field(3, "manage", "管理费", Field.FLOAT, 100));
        SCHEME_DETAIL0_1.addField(new Field(3, "tax", "税费", Field.FLOAT, 100));
        SCHEME_DETAIL0_1.addField(new Field(4, "paid", "总额", Field.FLOAT, 100));

        //特殊结算单明细
        SCHEME_DETAIL0_2 = new Scheme();
        SCHEME_DETAIL0_2.addField(new Field(1, "cardId", "身份证号码", Field.STRING, 300));
        SCHEME_DETAIL0_2.addField(new Field(2, "amount", "金额", Field.INT, 100));
        SCHEME_DETAIL0_2.addField(new Field(3, "tax", "税费", Field.FLOAT, 100));
        SCHEME_DETAIL0_2.addField(new Field(4, "paid", "实发", Field.FLOAT, 100));
        SCHEME_DETAIL0_2.addField(new Field(0, "name", "员工姓名", Field.STRING, 100));

        //个税申报名单表
        SCHEME_TAX1 = new Scheme();
        SCHEME_TAX1.addField(new Field(0, "", "工号", Field.NULL, 100));
        SCHEME_TAX1.addField(new Field(1, "name", "姓名", Field.STRING, 100));
        SCHEME_TAX1.addField(new Field(2, "cardType", "证件类型", Field.STRING, 100));
        SCHEME_TAX1.addField(new Field(3, "name", "证件号码", Field.STRING, 100));
        SCHEME_TAX1.addField(new Field(4, "nation", "国籍", Field.STRING, 100));
        SCHEME_TAX1.addField(new Field(5, "sex", "性别", Field.STRING, 100));
        SCHEME_TAX1.addField(new Field(6, "birthday", "出生日期", Field.STRING, 100));
        SCHEME_TAX1.addField(new Field(7, "", "人员状态", Field.NULL, 100));
        SCHEME_TAX1.addField(new Field(8, "type", "任职受雇从业类型", Field.STRING, 100));
        SCHEME_TAX1.addField(new Field(9, "phone", "手机号码", Field.STRING, 160));

        //员工新增社保单
        SCHEME_exportSocial1 = new Scheme();
        SCHEME_exportSocial1.addField(new Field(0, "name", "员工姓名", Field.STRING, 100));
        SCHEME_exportSocial1.addField(new Field(1, "code3", "个人代码", Field.STRING, 300));
        SCHEME_exportSocial1.addField(new Field(2, "cardId", "证件号码", Field.STRING, 100));
        SCHEME_exportSocial1.addField(new Field(3, "startDate", "参保开始年月", Field.STRING, 100));
        SCHEME_exportSocial1.addField(new Field(4, "base3", "月缴费工资", Field.FLOAT, 100));
        SCHEME_exportSocial1.addField(new Field(5, "changeReason", "变更原因", Field.STRING, 100));
        SCHEME_exportSocial1.addField(new Field(6, "form", "用工形式", Field.STRING, 100));
        SCHEME_exportSocial1.addField(new Field(7, "makeUp", "是否补收（不填表示否）", Field.STRING, 100));
        SCHEME_exportSocial1.addField(new Field(8, "entryDate", "参加工作时间", Field.STRING, 100));
        SCHEME_exportSocial1.addField(new Field(9, "phone", "联系电话", Field.STRING, 100));
        SCHEME_exportSocial1.addField(new Field(10, "house", "户口性质", Field.STRING, 100));
        SCHEME_exportSocial1.addField(new Field(11, "address", "户籍地址", Field.STRING, 100));

        //员工停保社保单
        SCHEME_exportSocial2=new Scheme();
        SCHEME_exportSocial2.addField(new Field(0, "code3", "个人代码", Field.STRING, 100));
        SCHEME_exportSocial2.addField(new Field(1, "name", "姓名", Field.STRING, 300));
        SCHEME_exportSocial2.addField(new Field(2, "cardId", "证件号码", Field.STRING, 100));
        SCHEME_exportSocial2.addField(new Field(3, "changeDate", "变更日期", Field.STRING, 100));
        SCHEME_exportSocial2.addField(new Field(4, "changeReason", "变更原因", Field.STRING, 100));

        //员工续保医保单
        SCHEME_exportMedicare1 = new Scheme();
        SCHEME_exportMedicare1.addField(new Field(1, "code1", "个人编号", Field.STRING, 100));
        SCHEME_exportMedicare1.addField(new Field(0, "name", "姓名", Field.STRING, 300));
        SCHEME_exportMedicare1.addField(new Field(2, "cardId", "证件号码", Field.STRING, 100));
        SCHEME_exportMedicare1.addField(new Field(3, "base1", "参保基数", Field.FLOAT, 100));
        SCHEME_exportMedicare1.addField(new Field(4, "insurance", "参保险种", Field.STRING, 100));
        SCHEME_exportMedicare1.addField(new Field(5, "startDate", "参保时间", Field.STRING, 100));

        //员工停保医保单
        SCHEME_exportMedicare2 = new Scheme();
        SCHEME_exportMedicare2.addField(new Field(1, "code3", "个人代码", Field.STRING, 100));
        SCHEME_exportMedicare2.addField(new Field(0, "name", "姓名", Field.STRING, 300));
        SCHEME_exportMedicare2.addField(new Field(2, "cardId", "证件号码", Field.STRING, 100));
        SCHEME_exportMedicare2.addField(new Field(3, "changeDate", "变更日期", Field.STRING, 100));
        SCHEME_exportMedicare2.addField(new Field(4, "changeReason", "变更原因", Field.STRING, 100));

        //招行转本行
        SCHEME_BANK_CMCC1 = new Scheme();
        SCHEME_BANK_CMCC1.addField(new Field(1, "paid", "金额上限", Field.FLOAT, 100));
        SCHEME_BANK_CMCC1.addField(new Field(7, "cardNo", "收方账号", Field.STRING, 300));
        SCHEME_BANK_CMCC1.addField(new Field(8, "name", "收方户名", Field.STRING, 300));
        SCHEME_BANK_CMCC1.addField(new Field(10, "bank1", "收方行名称", Field.STRING, 100));
        SCHEME_BANK_CMCC1.addField(new Field(11, "bankNo", "收方行行号", Field.STRING, 100));
        SCHEME_BANK_CMCC1.addField(new Field(13, "comments", "附言", Field.STRING, 100));

        //招行转外行行
        SCHEME_BANK_CMCC2 = new Scheme();
        SCHEME_BANK_CMCC2.addField(new Field(0, "paid", "金额", Field.FLOAT, 100));
        SCHEME_BANK_CMCC2.addField(new Field(1, "cardNo", "卡号", Field.STRING, 300));
        SCHEME_BANK_CMCC2.addField(new Field(2, "name", "姓名", Field.STRING, 300));
        SCHEME_BANK_CMCC2.addField(new Field(3, "comments", "备注", Field.STRING, 300));

        //农行转本行
        SCHEME_BANK_AG1 = new Scheme();
        SCHEME_BANK_AG1.addField(new Field(1, "cardNo", "卡号", Field.STRING, 100));
        SCHEME_BANK_AG1.addField(new Field(2, "name", "姓名", Field.STRING, 100));
        SCHEME_BANK_AG1.addField(new Field(3, "bank1", "开户银行(行别)", Field.STRING, 300));
        SCHEME_BANK_AG1.addField(new Field(4, "bankNo", "大额行号", Field.STRING, 300));
        SCHEME_BANK_AG1.addField(new Field(5, "bank2", "开户行支行名称",Field.STRING, 100));
        SCHEME_BANK_AG1.addField(new Field(6, "paid", "实发", Field.FLOAT, 100));
        SCHEME_BANK_AG1.addField(new Field(7, "comments", "项目", Field.STRING, 100));

        //农行转外行
        SCHEME_BANK_AG2 = new Scheme();
        SCHEME_BANK_AG2.addField(new Field(1, "cardNo", "卡号", Field.STRING, 100));
        SCHEME_BANK_AG2.addField(new Field(2, "name", "姓名", Field.STRING, 300));
        SCHEME_BANK_AG2.addField(new Field(3, "paid", "金额", Field.FLOAT, 300));
        SCHEME_BANK_AG2.addField(new Field(4, "comments", "备注", Field.STRING, 300));

        //浦发银行
        SCHEME_BANK_SPDB = new Scheme();
        SCHEME_BANK_SPDB.addField(new Field(0, "cardNo", "卡号（或账号）", Field.STRING, 300));
        SCHEME_BANK_SPDB.addField(new Field(1, "paid", "金额", Field.FLOAT, 100));
        SCHEME_BANK_SPDB.addField(new Field(2, "name", "客户姓名", Field.STRING, 300));
        SCHEME_BANK_SPDB.addField(new Field(3, "code", "第三方编号", Field.STRING, 300));
        SCHEME_BANK_SPDB.addField(new Field(4, "comments", "摘要", Field.STRING, 100));

        //交通银行
        SCHEME_BANK_BOCOM = new Scheme();
        SCHEME_BANK_BOCOM.addField(new Field(0, "cardNo", "卡号", Field.STRING, 300));
        SCHEME_BANK_BOCOM.addField(new Field(1, "name", "姓名", Field.STRING, 100));
        SCHEME_BANK_BOCOM.addField(new Field(2, "paid", "实发", Field.FLOAT, 300));
        SCHEME_BANK_BOCOM.addField(new Field(3, "comments", "项目", Field.STRING, 300));

    }
}
