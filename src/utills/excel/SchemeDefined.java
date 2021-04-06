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
    public static Scheme SCHEME_DETAIL4_1;//特殊结算单汇款表
    public static Scheme SCHEME_DETAIL4_2;//特殊结算单明细表
    public static Scheme SCHEME_DETAIL2_PAID;//小时工汇款表
    public static Scheme SCHEME_DETAIL2_DETAIL;//小时工明细表
    public static Scheme SCHEME_TAX1;//个税申报名单表
    public static Scheme SCHEME_TAX2;//个税申报表

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
        SCHEME_DETAIL4_1 = new Scheme();
        SCHEME_DETAIL4_1.addField(new Field(0, "name", "员工姓名", Field.STRING, 100));
        SCHEME_DETAIL4_1.addField(new Field(1, "cardId", "身份证号码", Field.STRING, 300));
        SCHEME_DETAIL4_1.addField(new Field(2, "amount", "金额", Field.INT, 100));
        SCHEME_DETAIL4_1.addField(new Field(3, "manage", "管理费", Field.FLOAT, 100));
        SCHEME_DETAIL4_1.addField(new Field(3, "tax", "税费", Field.FLOAT, 100));
        SCHEME_DETAIL4_1.addField(new Field(4, "paid", "总额", Field.FLOAT, 100));

        //特殊结算单明细
        SCHEME_DETAIL4_2 = new Scheme();
        SCHEME_DETAIL4_2.addField(new Field(1, "cardId", "身份证号码", Field.STRING, 300));
        SCHEME_DETAIL4_2.addField(new Field(2, "amount", "金额", Field.INT, 100));
        SCHEME_DETAIL4_2.addField(new Field(3, "tax", "税费", Field.FLOAT, 100));
        SCHEME_DETAIL4_2.addField(new Field(4, "paid", "实发", Field.FLOAT, 100));
        SCHEME_DETAIL4_2.addField(new Field(0, "name", "员工姓名", Field.STRING, 100));

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










    }
}
