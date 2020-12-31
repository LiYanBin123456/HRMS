package utills;

public class Bit {
    /**
     * 测试指定位是否为1
     * @param value 待测试的值
     * @param index 指定的位
     * @return 指定位是否为1
     */
    public static boolean test(int value,int index) {
        return (value&(1<<index))!=0;
    }

    /**
     * 测试指定范围位数的值
     * @param value 待测试的值
     * @param index1 高位
     * @param index2 低位
     * @return 指定范围位数的值
     */
    public static int testRange(int value,int index1,int index2) {
        return (value&((1<<(index1+1))-(1<<index2)))>>index2;
    }

    /**
     * 将某位置1
     * @param value 待置位的数
     * @param index 指定位
     * @return  置位之后的数
     */
    public static int set(int value, int index) {
        return value|(1<<index);
    }

    /**
     *
     * @param value
     * @param index1
     * @param index2
     * @param v
     * @return 置位之后的数
     */
    public static int setRange(int value,int index1,int index2,int v) {
        v = (v&((1<<(index1-index2+1))-1));//保证设置的值在合理范围内
        return value&(~((1<<(index1+1))-(1<<index2)))|(v<<index2);//指定位清0再置位
    }

    /**
     * 将某位置0
     * @param value 待置位的数
     * @param index 指定位
     * @return 置位之后的数
     */
    public static int reset(int value,int index) {
        return value&(~(1<<index));
    }
}
