package bean.employee;
//员工个税专项扣除
public class EmployeeSetting {
    private long eid;//员工id
    private String city;//社保所在地市
    /**医保设置
     0_最低标准
     1_实际工资
     2_不交纳
     3_定义基数
     *
     */
    private byte settingM;
    /**自定义的医保基数（不能低于最低标准）
     默认为null
     只有选择了自定义才有用
     *
     */
    private float valueM;
    /**社保设置
     0_最低标准
     1_实际工资
     2_不交纳
     3自定义基数
     *
     */
    private byte settingS;
    /**自定义社保基数（不能低于最低标准）
     默认为null
     只有选择了自定义才有用
     *
     */
    private float valueS;
    /**公积金设置
     0_按比例
     1_自定义
     */
    private float settingF;
    private float fundPer;//公积金比例
    private float fundBase;//公积金基数
    private float valueF;//自定义公积金金额
    private float deduct1;//子女教育扣除额
    private float deduct2;//赡养老人
    private float deduct3;//继续教育
    private float deduct4;//大病医疗
    private float deduct5;//住房贷款利息
    private float deduct6;//住房租金

}
