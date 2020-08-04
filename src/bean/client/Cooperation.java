package bean.client;

//合作客户
public class Cooperation extends Dispatch{
    private Dispatch dispatch;
    /**客户性质
     0_政府部门
     1_事业单位
     2_人才市场、
     3_学校
     4_内资企业
     5_外资企业
     6_港澳台企业
     7_内资工厂
     8_外资工厂
     9_港澳台工厂
     10_其它。
     *
     */
    private byte category;
}
