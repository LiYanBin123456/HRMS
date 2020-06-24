package database;

public class QueryOrder {
    public boolean need;//是否需要排序
    public String field;//排序字段
    public boolean direction;//升序(true)/降序(false)

    /**
     * 设置排序参数
     * @param need 是否需要排序
     */
    public void set(boolean need){
        this.need = need;
    }

    /**
     * 设置排序参数
     * @param need 是否需要排序
     * @param field 排序字段
     * @param direction 排序方向，true-升序;false-降序
     */
    public void set(boolean need,String field,boolean direction){
        this.need = need;
        this.field = field;
        this.direction = direction;
    }
}
