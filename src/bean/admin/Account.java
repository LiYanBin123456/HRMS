package bean.admin;

//账号表
public class Account {
    private long id;//编号
    private String nickname;//昵称
    private String username;//账号
    private String password;//密码
    private byte role;//账号角色 0_平台管理员 1_派遣单位 2_合作单位 3_员工
    private long rid;//所属公司id 平台默认为0 用role和rid可以确定账户属于那个派遣单位或者合作单位
    private  int permisson;//权限使用位运算 第0位账号管理 第1位客户管理 第2位工资审核 第3位财务管理


}
