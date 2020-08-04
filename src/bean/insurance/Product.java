package bean.insurance;
//保险产品
public class Product {
    private long id;//产品id
    private long did;//派遣单位id
    private String name;//产品名称
    private float fin1;//产品保额
    private float fin2;//医疗保额
    private float allowance;//住院津贴
    /**保险时间
     0_上班时间
     1_24小时
     */
    private byte period;
    /**可参保人员类型（采用位运算）
     第0位第一类
     第1位第二类
     第2位第三类
     第3位第四类
     第4位第五类
     第5位第六类
     */
    private byte allow;
    private byte min;//年龄下限
    private byte max;//年龄上限
    private String intro;//产品介绍

}
