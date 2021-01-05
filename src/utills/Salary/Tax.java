package utills.Salary;

public class Tax {

    /**
     * 计算普通个税
     * @param taxDue 应税额
     * @return 应缴个税
     */
    public static double tax1(float taxDue){
       float rate = 0;//税率
       float dudect = 0;//速算扣除数
        if(taxDue <= 36000){//根据个税比例报表计算个税
            rate = 0.03f;
            dudect = 0;
        }else if(taxDue<=144000){
            rate = 0.1f;
            dudect = 2520;
        }else if(taxDue<=300000){
            rate = 0.2f;
            dudect = 16920;
        }else if(taxDue<=420000){
            rate = 0.25f;
            dudect = 31920;
        }else if(taxDue<=660000){
            rate = 0.3f;
            dudect = 52920;
        }else if(taxDue<=960000){
            rate = 0.35f;
            dudect = 85920;
        }else if(taxDue>960000){
            rate = 0.45f;
            dudect = 181920;
        }
        float tax = taxDue*rate-dudect;
        return tax<=0?0:tax;
    }

    /**
     * 计算年终奖的个税
     * @param taxDue 应税额
     * @return v
     */
    public static double tax2(float taxDue){
        float rate = 0;//税率
        float d = 0;//速算扣除
        if(taxDue<=3000){//根据个税比例报表计算个税
            rate = 0.03f;
            d = 0;
        }else if(taxDue<=12000){
            rate = 0.1f;
            d = 210;
        }else if(taxDue<=25000){
            rate = 0.2f;
            d = 1410;
        }else if(taxDue<=35000){
            rate = 0.25f;
            d = 2660;
        }else if(taxDue<=55000){
            rate = 0.3f;
            d = 4410;
        }else if(taxDue<=80000){
            rate = 0.35f;
            d = 7160;
        }else if(taxDue>80000){
            rate = 0.45f;
            d = 15160;
        }
        double tax = taxDue*rate-d;
        return tax<=0?0:tax;
    }
}
