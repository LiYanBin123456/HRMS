package bean.client;

//分解MapSalary中的items
public class Items {
    private short type;//类型
    private String field;//字段值

    public Items() {
    }

    public Items(short type, String field) {
        this.type = type;
        this.field = field;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "Items{" +
                "type=" + type +
                ", field='" + field + '\'' +
                '}';
    }
}
