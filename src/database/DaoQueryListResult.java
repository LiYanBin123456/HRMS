package database;

public class DaoQueryListResult extends DaoResult {
    public long total;//查询时表示符合条件的总记录数，可能大于返回的记录数，比如分页显示时；
    public Object rows;//返回的数据
}
