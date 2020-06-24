package database;

public class DaoResult {
    public boolean success;//操作是否成功
    public String msg;//操作结果补充信息，尤其是不成功时返回更多信息
    public Object extra;//附加信息
}
