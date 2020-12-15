package utills.excel;

//Excel字段定义
public class Field {
    public String name;//字段名
    public String title;//字段名对应的显示标题
    public String type;//类型
    public byte width;//显示宽度
    public byte decimal;//如果是数值型，允许的小数位数
    public boolean isNull;//是否允许为空

    public Field(String name, String title, String type, byte width, byte decimal, boolean isNull) {
        this.name = name;
        this.title = title;
        this.type = type;
        this.width = width;
        this.decimal = decimal;
        this.isNull = isNull;
    }

    public Field(String name, String title, String type, byte width) {
        this.name = name;
        this.title = title;
        this.type = type;
        this.width = width;
        this.decimal = 0;
        this.isNull = true;
    }
}
