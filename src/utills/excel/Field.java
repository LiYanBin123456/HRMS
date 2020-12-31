package utills.excel;

//Excel字段定义
public class Field {
    public static final String INT = "int";
    public static final String LONG = "long";
    public static final String FLOAT = "float";
    public static final String DOUBLE = "double";
    public static final String STRING = "string";
    public static final String ENUM = "enum";

    public int col;//对应xls中的列号
    public String name;//字段名
    public String title;//字段名对应的显示标题
    public String type;//类型，取值int,long,float,double,string
    public int width;//显示宽度
    public byte decimal;//如果是数值型，允许的小数位数
    public boolean isNull;//是否允许为空

    public Field(int col,String name, String title, String type, int width, byte decimal, boolean isNull) {
        this.col = col;
        this.name = name;
        this.title = title;
        this.type = type;
        this.width = width;
        this.decimal = decimal;
        this.isNull = isNull;
    }

    public Field(int col,String name, String title, String type, int width) {
        this.col = col;
        this.name = name;
        this.title = title;
        this.type = type;
        this.width = width;
        this.decimal = 0;
        this.isNull = true;
    }

    public Field(int col,String name, String type) {
        this.col = col;
        this.name = name;
        this.title = "";
        this.type = type;
        this.width = 0;
        this.decimal = 0;
        this.isNull = true;
    }
}
