package utills.excel;

import java.util.ArrayList;
import java.util.List;

//Excel表结构
public class Scheme {
    public static final byte SCHEME_DEDUCT_SPOUSE = 0;//个税专项扣除-配偶信息
    public static final byte SCHEME_DEDUCT_CHILDREN = 1;//个税专项扣除-子女教育支出
    public static final byte SCHEME_DEDUCT_EDUCATION = 2;//个税专项扣除-继续教育
    public static final byte SCHEME_DEDUCT_LOAN = 3;//个税专项扣除-房贷
    public static final byte SCHEME_DEDUCT_RENT = 4;//个税专项扣除-房租
    public static final byte SCHEME_DEDUCT_ELDER = 5;//个税专项扣除-赡养老人
    public static final byte SCHEME_DEDUCT_TOTAL = 6;//个税专项扣除-累计
    public static final byte SCHEME_SOCIAL = 7;//社保核对
    public static final byte SCHEME_MEDICAL = 8;//医保核对
    public static final byte SCHEME_FUND = 9;//公积金核对

    private List<Field> fields;

    public Scheme() {
        fields = new ArrayList<>();
    }

    public Scheme(byte category){
        fields = new ArrayList<>();
        switch (category){
            case SCHEME_DEDUCT_CHILDREN:
                fields.add(new Field(1,"name",Field.STRING,false));
                fields.add(new Field(3,"cardId",Field.STRING,false));
                fields.add(new Field(17,"per",Field.STRING,false));
                break;
            case SCHEME_DEDUCT_SPOUSE:
            case SCHEME_DEDUCT_EDUCATION:
            case SCHEME_DEDUCT_LOAN:
            case SCHEME_DEDUCT_RENT:
                fields.add(new Field(1,"name",Field.STRING,false));
                fields.add(new Field(3,"cardId",Field.STRING,false));
                break;
            case SCHEME_DEDUCT_ELDER:
                fields.add(new Field(1,"name",Field.STRING,false));
                fields.add(new Field(3,"cardId",Field.STRING,false));
                fields.add(new Field(6,"per",Field.FLOAT,false));
                break;
            case SCHEME_DEDUCT_TOTAL:
                fields.add(new Field(1,"name","姓名",Field.STRING,100));
                fields.add(new Field(3,"cardId","身份证号",Field.STRING,100));
                fields.add(new Field(18,"income","累计收入",Field.FLOAT,100));
                fields.add(new Field(20,"free","累计减免费用",Field.FLOAT,100));
                fields.add(new Field(22,"deduct1","累计子女教育扣除",Field.FLOAT,100));
                fields.add(new Field(23,"deduct3","累计继续教育扣除额",Field.FLOAT,100));
                fields.add(new Field(24,"deduct5","累计住房贷款利息",Field.FLOAT,100));
                fields.add(new Field(25,"deduct6","累计住房租金",Field.FLOAT,100));
                fields.add(new Field(26,"deduct2","累计赡养老人",Field.FLOAT,100));
                fields.add(new Field(35,"prepaid","累计已预缴税额",Field.FLOAT,100));
                break;
            case SCHEME_SOCIAL:
                fields.add(new Field(0,"code",Field.STRING,false));
                fields.add(new Field(2,"cardId",Field.STRING,false));
                break;
            case SCHEME_MEDICAL:
                fields.add(new Field(2,"code",Field.STRING,false));
                fields.add(new Field(4,"cardId",Field.STRING,false));
                break;
            case SCHEME_FUND:
                fields.add(new Field(1,"code",Field.STRING,false));
                fields.add(new Field(3,"cardId",Field.STRING,false));
                break;
        }
    }

    public void addField(Field field){
        fields.add(field);
    }

    public List<Field> getFields() {
        return fields;
    }
}
